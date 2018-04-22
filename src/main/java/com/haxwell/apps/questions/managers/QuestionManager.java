package com.haxwell.apps.questions.managers;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import java.util.ArrayList;	
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyEnums;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.QuestionType;
import com.haxwell.apps.questions.entities.Reference;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.filters.DifficultyFilter;
import com.haxwell.apps.questions.filters.QuestionFilter;
import com.haxwell.apps.questions.filters.QuestionTopicFilter;
import com.haxwell.apps.questions.filters.QuestionTypeFilter;
import com.haxwell.apps.questions.utils.CollectionUtil;
import com.haxwell.apps.questions.utils.FilterCollection;
import com.haxwell.apps.questions.utils.ListFilterer;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.StringUtil;

public class QuestionManager extends Manager {
	
	private static Logger log = LogManager.getLogger();
	
	protected static QuestionManager instance = null;
	
	public static Manager getInstance() {
		if (instance == null)
			instance = new QuestionManager();
		
		return instance;
	}
	
	@Override
	public AbstractEntity getEntity(String entityId) {
		return getQuestionById(entityId);
	}

	public static long persistQuestion(Question question) 
	{
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		log.debug("\n\n" + question.toJSON());
		
		em.persist(question);
		
		em.getTransaction().commit();
		
		em.close();
		
		return question.getId(); //rtn.getId();
	}
	
	public static long mergeQuestion(Question question) 
	{
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		log.debug("\n\n" + question.toJSON());
		
		Question rtn = em.merge(question);
		
		em.getTransaction().commit();
		
		em.close();
		
		return rtn.getId();
	}
	
	public static String deleteQuestion(long userId, String questionId)
	{
		return deleteQuestion(userId, getQuestionById(questionId));
	}
	
	public static String deleteQuestion(long userId, Question question)
	{
		EntityManager em = emf.createEntityManager();
		
		long questionId = question.getId();		
		String rtn  = QuestionUtil.getDisplayString(question, Constants.MAX_QUESTION_TEXT_LENGTH);
		
		em.getTransaction().begin();
		
		// Delete related REFERENCEs
		Query query = em.createNativeQuery("SELECT qr.reference_id FROM question_reference qr WHERE qr.question_id = ?1");
		query.setParameter(1, questionId);
		
		List<Long> referenceIds = (List<Long>)query.getResultList();
		
		if (!referenceIds.isEmpty()) {
			query = em.createNativeQuery("DELETE FROM question_reference WHERE question_id = ?1");
			query.setParameter(1, questionId);
			
			query.executeUpdate();
			
			String str = "DELETE FROM reference WHERE ";
			
			for (int i = 0; i < referenceIds.size(); i++)
			{
				str += "id= " + referenceIds.get(i);
				
				if (i+1 < referenceIds.size())
					str += " OR ";
			}
				
			query = em.createNativeQuery(str);
			query.executeUpdate();
		}
		
		// Delete related TOPICs
		query = em.createNativeQuery("SELECT qt.topic_id FROM question_topic qt WHERE qt.question_id = ?1");
		query.setParameter(1, questionId);
		
		List<Long> topicIds = (List<Long>)query.getResultList();

		if (!topicIds.isEmpty()) {
			query = em.createNativeQuery("DELETE FROM question_topic WHERE question_id = ?1");
			query.setParameter(1, questionId);
			query.executeUpdate();
			
			for (Long l : topicIds)
			{
				// Are there any more questions using this topic?
				query = em.createNativeQuery("SELECT qt.question_id FROM question_topic qt WHERE qt.topic_id = ?1");
				query.setParameter(1, l);
				
				List<Long> list = (List<Long>)query.getResultList();
				
				// If not, delete this topic, too..
				if (list.size() == 0) {
					query = em.createNativeQuery("DELETE FROM topic WHERE id = ?1");
					query.setParameter(1, l);
					query.executeUpdate();
				}
			}
		}
		
		// Delete related CHOICEs
		query = em.createNativeQuery("SELECT qc.choice_id FROM question_choice qc WHERE qc.question_id = ?1");
		query.setParameter(1, questionId);
		
		List<Long> choiceIds = (List<Long>)query.getResultList();
		
		if (!choiceIds.isEmpty()) {
			query = em.createNativeQuery("DELETE FROM question_choice WHERE question_id = ?1");
			query.setParameter(1, questionId);
			query.executeUpdate();
			
			String str = "DELETE FROM choice WHERE ";
			
			for (int i = 0; i < choiceIds.size(); i++)
			{
				str += "id= " + choiceIds.get(i);
				
				if (i+1 < choiceIds.size())
					str += " OR ";
			}
				
			query = em.createNativeQuery(str);
			query.executeUpdate();
		}

		// Delete related EXAMs (if necessary)
		query = em.createNativeQuery("SELECT eq.exam_id FROM exam_question eq WHERE eq.question_id = ?1");
		query.setParameter(1, questionId);

		List<Long> examIds = (List<Long>)query.getResultList();
		
		if (!examIds.isEmpty()) {
			query = em.createNativeQuery("DELETE FROM exam_question WHERE question_id = ?1");
			query.setParameter(1, questionId);
			query.executeUpdate();
			
			for (Long examId : examIds)
			{
				// Who is the owner of this exam?
				query = em.createNativeQuery("SELECT e.user_id FROM exam e where e.id = ?1");
				query.setParameter(1, examId);
				
				Long IDofTheOwnerOfThisExam = (Long)query.getSingleResult();
				
				// if its not the given user id
				if (IDofTheOwnerOfThisExam != userId) {
					// 	notify them that the question was removed from their exam
					NotificationManager.issueNotification_questionDeletedAndRemovedFromExam(IDofTheOwnerOfThisExam, questionId, examId);
				}
				
				// For the given exam ID, does it have any more questions?
				query = em.createNativeQuery("SELECT eq.question_id FROM exam_question eq WHERE eq.exam_id = ?1");
				query.setParameter(1, examId);
				
				List<Long> list = (List<Long>)query.getResultList();
				
				// If not, this exam is empty.. delete this exam ID..
				if (list.size() == 0) {
					NotificationManager.issueNotification_emptyExamWasDeleted(examId);
					
					query = em.createNativeQuery("DELETE FROM exam WHERE id = ?1");
					query.setParameter(1, examId);
					query.executeUpdate();
					
				}
			}
		}
		
		// Delete the question itself..
		query = em.createNativeQuery("DELETE FROM question WHERE id = ?1");
		query.setParameter(1, questionId);
		query.executeUpdate();
		
		em.getTransaction().commit();
		
		em.close();
		
		return rtn;
	}
	
	public static Question newQuestion()
	{
		return new Question();
	}
	
	public static Collection<Question> getQuestionsByTopic(long topicId)
	{
		EntityManager em = emf.createEntityManager();

		Query query = em.createNativeQuery("SELECT qt.question_id FROM question_topic qt WHERE qt.topic_id = ?1");
		
		query.setParameter(1, topicId);
				
		List<Long> list = (List<Long>)query.getResultList();
		
		Collection<Question> coll = getQuestionsById(StringUtil.getCSVString(list));
		
		em.close();
		
		return coll;
	}
	
	public static Collection<Question> getAllQuestions() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT q FROM Question q");
		
		Collection<Question> rtn = query.getResultList();
		
		em.close();
		
		return rtn;
	}
	
	/**
	 * Takes comma delimited list of IDs, and returns a collection of the Question 
	 * object represented by those IDs.
	 * 
	 * @param questionIDs
	 * @return
	 */
	public static List<Question> getQuestionsById(String questionIDs) {
		List<Question> rtn = new ArrayList<Question>();
		
		StringTokenizer tokenizer = new StringTokenizer(questionIDs, ",");
		
		if (tokenizer.hasMoreTokens())
		{
			EntityManager em = emf.createEntityManager();

			int tokenCount = tokenizer.countTokens();
			
			String queryStr = "SELECT q FROM Question q";
			String whereClause = " WHERE ";
			
			while (tokenizer.hasMoreTokens())
			{
				String token = tokenizer.nextToken();
	
				whereClause += " q.id=" + token;
				
				if (tokenizer.hasMoreTokens())
				{
					whereClause += " OR ";
				}
			}
			
			queryStr += whereClause;

			Query query = em.createQuery(queryStr);
			
			rtn = (List<Question>)query.getResultList();
			
			em.close();
		}
		
		return rtn;
	}
	
	public static Question getQuestionById(String aSingleId)
	{
		return getQuestionById(Long.parseLong(aSingleId));
	}
	
	public static Question getQuestionById(long aSingleId)
	{
		Question rtn = null;
		
		EntityManager em = emf.createEntityManager();
		
		String queryStr = "SELECT q FROM Question q WHERE q.id=?1";

		Query query = em.createQuery(queryStr);
		
		query.setParameter(1, aSingleId);
		
		try {
			rtn = (Question)query.getSingleResult();
		}
		catch (Exception e) 
		{
			String strr = e.getMessage();
		}
		finally {
			em.close();
		}

		return rtn;
	}
	
//	public static boolean isAnsweredCorrectly(Question question, Map<String, String> answers)
//	{
//		AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(question);
//		return checker.questionIsCorrect(answers);
//	}

	public static long getNumberOfQuestionsInTotal() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT count(*) FROM question");
		
		Long rtn = (Long)query.getSingleResult();
		
		em.close();
		
		return rtn;
	}
	
	public static long getNumberOfQuestionsCreatedByUser(long id) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT count(*) FROM question WHERE user_id = ?1");
		
		query.setParameter(1, id);
		
		Long rtn = (Long)query.getSingleResult();
		
		em.close();
		
		return rtn;
	}

	public static Collection<Question> getAllQuestionsForUser(long id) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT q FROM Question q, User u WHERE q.user.id = u.id AND u.id = ?1", Question.class);
		
		query.setParameter(1, id);
		
		Collection<Question> rtn = query.getResultList();
		
		em.close();
		
		return rtn;
	}

	private static List<Question> getFilteredListOfQuestions(FilterCollection fc) {
		EntityManager em = emf.createEntityManager();
		
		String filterText = fc.get(FilterConstants.QUESTION_CONTAINS_FILTER).toString();
		String topicFilterText = fc.get(FilterConstants.TOPIC_CONTAINS_FILTER).toString();
		int maxDifficulty = Integer.parseInt(fc.get(FilterConstants.DIFFICULTY_FILTER).toString());
		int questionType = Integer.parseInt(fc.get(FilterConstants.QUESTION_TYPE_FILTER).toString());
		boolean includeOnlyUserCreatedEntities = fc.get(FilterConstants.RANGE_OF_ENTITIES_FILTER).equals(Constants.MY_ITEMS.toString());
		User user = (User)fc.get(FilterConstants.USER_ID_ENTITY);
		
		String queryString = "SELECT q FROM Question q"; 

		/**
		 * Need a piece of code which will take a condition, and then the string to be added to the query if that condition is true.
		 * It should be able to tell that it previously added something to the query, and then add ' AND ' before it adds the next
		 * string from the next condition that is true.
		 * 
		 * The conditions and the Strings should be returned from functions. So, we have a class which pairs a condition, and a string.
		 * We have a list of those. For each item in the list, was the previous condition added? If so, add and 'AND '. now, is this 
		 * condition true? if so add the string to the stringbuffer.. 
		 */
		
		boolean filterTextIsNullOrEmpty = StringUtil.isNullOrEmpty(filterText);
		if (!filterTextIsNullOrEmpty || maxDifficulty > 0 || includeOnlyUserCreatedEntities)
			queryString += " WHERE "; 
		
		if (includeOnlyUserCreatedEntities)
			queryString += "q.user.id = ?3";

		if (includeOnlyUserCreatedEntities && (maxDifficulty > 0 || !filterTextIsNullOrEmpty))
			queryString += " AND ";
		
		if (maxDifficulty > 0) 
			queryString += "q.difficulty.id = ?1 ";
		
		if (maxDifficulty > 0 && !filterTextIsNullOrEmpty)
			queryString += " AND ";
		
		if (!filterTextIsNullOrEmpty)
			queryString += " UPPER(q.text) LIKE ?2 OR UPPER(q.description) LIKE ?2"; 
		
		Query query = em.createQuery(queryString, Question.class);
		
		if (maxDifficulty > 0) 
			query.setParameter(1, maxDifficulty);
		
		if (!filterTextIsNullOrEmpty)
			query.setParameter(2, "%" + filterText.toUpperCase() + "%");
		
		if (includeOnlyUserCreatedEntities)
			query.setParameter(3, user.getId());
		
		List<Question> rtn = (List<Question>)query.getResultList();

		// TODO: figure out a way to get the query to do this...
		FilterCollection fc2 = new FilterCollection();
		fc2.add(FilterConstants.TOPIC_CONTAINS_FILTER, topicFilterText);
		fc2.add(FilterConstants.QUESTION_TYPE_FILTER, questionType+"");
		
		//rtn = (List<Question>)filterQuestionListByTopicAndQuestionType(topicFilterText, questionType, rtn);
		rtn = (List<Question>)filterList(fc2, rtn);
		
		return rtn;
	}
	
	public static AJAXReturnData getAJAXReturnObject(FilterCollection fc, Set<Question> selectedQuestions) {
		int maxEntityCount = Integer.parseInt(fc.get(FilterConstants.MAX_ENTITY_COUNT_FILTER).toString());
		int offset = Integer.parseInt(fc.get(FilterConstants.OFFSET_FILTER).toString());

		AJAXReturnData rtn = new AJAXReturnData();

		if (fc.get(FilterConstants.RANGE_OF_ENTITIES_FILTER).equals(Constants.SELECTED_ITEMS+"")) {
			rtn = handleTheSelectedQuestionsCase(fc, selectedQuestions, maxEntityCount, offset);
		}
		else {  // this is a request for questions from the db
			String entityIdFilter = (String)fc.get(FilterConstants.ENTITY_ID_FILTER);
			
			if (!StringUtil.isNullOrEmpty(entityIdFilter))
				rtn = handleTheGetEntityByIdCase(fc, selectedQuestions);
			else
				rtn = handleTheOtherCases(fc, selectedQuestions, maxEntityCount, offset);
		}
		
		return rtn;
	}
	
	/**
	 * 
	 * @param fc
	 * @param selectedQuestions a Set of questions, representing those selected on an exam. Now that I think about it, don't really
	 * 			like this here. Another, separate method should be doing what the selectedQuestions section is. Other than that, why
	 * 			should this method care about which questions are selected?
	 * @return
	 */
	private static AJAXReturnData handleTheGetEntityByIdCase(FilterCollection fc, Set<Question> selectedQuestions) {
		AJAXReturnData rtn = new AJAXReturnData();
		
		long questionId = Long.parseLong(fc.get(FilterConstants.ENTITY_ID_FILTER)+"");
		
		Question q = getQuestionById(questionId);
		
		if (q == null)
			rtn.additionalInfoCode = Manager.ADDL_INFO_NO_ENTITIES_MATCHING_GIVEN_FILTER;
		else {
			ArrayList<Question> arr = new ArrayList<Question>();
			arr.add(q);
			
			rtn.entities = arr;
			
			// a more robust solution will be necessary when a new question type is added.. but for the sake of expediency
			if (isDynamificationNeeded(q))
				rtn.setDynamificationStatus(AJAXReturnData.DynamificationStatus.NEEDED);
			
			if (selectedQuestions != null) {
				Iterator<Question> iterator = selectedQuestions.iterator();
				List<Question> listOfSelectedEntitiesMatchingFilter = new ArrayList<Question>();
				
				while (iterator.hasNext())
				{
					Question selectedQuestion = iterator.next();
					if (selectedQuestion.getId() == q.getId())
						listOfSelectedEntitiesMatchingFilter.add(q);
				}
				
				rtn.addKeyValuePairToJSON("selectedEntityIDsAsCSV", CollectionUtil.getCSVofIDsFromListofEntities(listOfSelectedEntitiesMatchingFilter));				
			}
		}
		
		return rtn;
	}
	
	private static boolean isDynamificationNeeded(Question q) {
		if (q.getQuestionType().getId() == TypeConstants.SET)
			return true;
		
		if (q.getQuestionType().getId() == TypeConstants.PHRASE && StringUtil.getCountOfDynamicFields(q.getText()) > 0)
			return true;
		
		return false;
	}
	
	/* Helper method for ::getAJAXReturnObject() */
	private static AJAXReturnData handleTheSelectedQuestionsCase(FilterCollection fc, Set<Question> selectedQuestions, int maxEntityCount, int offset) {
		AJAXReturnData rtn = new AJAXReturnData();
		List<Question> list = null;

		list = new ArrayList<Question>();
		
		if (selectedQuestions != null && selectedQuestions.size() > 0)
		{
			Iterator<Question> iterator = selectedQuestions.iterator();
			
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
			
			list = filterList(fc, list);

			rtn.addKeyValuePairToJSON("selectedEntityIDsAsCSV", CollectionUtil.getCSVofIDsFromListofEntities(list));
			
			rtn.additionalItemCount = Math.max((list.size() - offset - maxEntityCount), 0);			
		}
		else
			rtn.additionalInfoCode = Manager.ADDL_INFO_NO_SELECTED_ITEMS;
		
		rtn.entities = CollectionUtil.pareListDownToSize(list, offset, maxEntityCount);		
		
		return rtn;
	}
	
	/* Helper method for ::getAJAXReturnObject() */
	private static AJAXReturnData handleTheOtherCases(FilterCollection fc, Set<Question> selectedQuestions, int maxEntityCount, int offset) {
		AJAXReturnData rtn = new AJAXReturnData();
		List<Question> list = null;

		list = getFilteredListOfQuestions(fc);
		
		if (list.size() == 0) {
			if (getNumberOfQuestionsCreatedByUser( Long.parseLong((String)fc.get(FilterConstants.USER_ID_FILTER))) == 0)
				rtn.additionalInfoCode = Manager.ADDL_INFO_USER_HAS_CREATED_NO_ENTITIES;
			else
				rtn.additionalInfoCode = Manager.ADDL_INFO_NO_ENTITIES_MATCHING_GIVEN_FILTER;
		}
		else {
			rtn.additionalItemCount = Math.max((list.size() - offset - maxEntityCount), 0);
		}

		rtn.addKeyValuePairToJSON("selectedEntityIDsAsCSV", (selectedQuestions == null ? "" : CollectionUtil.getCSVofIDsFromListofEntities(selectedQuestions)));
		rtn.entities = CollectionUtil.pareListDownToSize(list, offset, maxEntityCount);
		
		return rtn;
	}
	
	public static AJAXReturnData getBlankQuestion() {
		AJAXReturnData rtn = new AJAXReturnData();
		List<Question> list = new ArrayList<Question>();
		
		Question q = new Question();
		
		q.setDifficulty(new Difficulty(DifficultyEnums.JUNIOR.getRank()));
		q.setQuestionType(new QuestionType(TypeConstants.SINGLE, TypeConstants.SINGLE_STR));
		q.setChoices(new HashSet<Choice>());
		q.setTopics(new HashSet<Topic>());
		q.setReferences(new HashSet<Reference>());
		
		User u = new User();
		u.setId(-1);
		
		q.setUser(u);
		
		list.add(q);
		
		rtn.entities = list;
		
		return rtn;
	}

	public static List<Question> getQuestions(FilterCollection fc) {
		int maxEntityCount = Integer.parseInt(fc.get(FilterConstants.MAX_ENTITY_COUNT_FILTER).toString());
		int offset = Integer.parseInt(fc.get(FilterConstants.OFFSET_FILTER).toString());

		List<Question> rtn = getFilteredListOfQuestions(fc);
		
		List<Question> paginatedList = CollectionUtil.pareListDownToSize(rtn, offset, maxEntityCount);
		
		return paginatedList;
	}
	
	private static List<Question> filterList(FilterCollection fc, List<Question> list) {
		ArrayList<ShouldRemoveAnObjectCommand> arr = new ArrayList<ShouldRemoveAnObjectCommand>();
		
		String filter = fc.get(FilterConstants.TOPIC_CONTAINS_FILTER).toString();
		
		if (!StringUtil.isNullOrEmpty(filter)) {
			arr.add(new QuestionTopicFilter(filter));
		}
		
		filter = fc.get(FilterConstants.QUESTION_TYPE_FILTER).toString();
		
		if (!StringUtil.isNullOrEmpty(filter) && (!filter.equals(TypeConstants.ALL_TYPES+""))) {
			arr.add(new QuestionTypeFilter(Integer.parseInt(filter)));
		}
		
		filter = fc.get(FilterConstants.QUESTION_CONTAINS_FILTER) != null ? fc.get(FilterConstants.QUESTION_CONTAINS_FILTER).toString() : "";
		
		if (!StringUtil.isNullOrEmpty(filter)) {
			arr.add(new QuestionFilter(filter));
		}
		
		filter = fc.get(FilterConstants.DIFFICULTY_FILTER).toString();
		filter = filter != null ? filter.toString() : "";
		
		if (!StringUtil.isNullOrEmpty(filter) && !filter.equals(DifficultyEnums.ALL_DIFFICULTIES.getRank()+"")) {
			arr.add(new DifficultyFilter(Integer.parseInt(filter)));
		}
		
		ListFilterer lf = new ListFilterer();
		ArrayList al = new ArrayList(lf.process(list,arr));
		
		return al;
	}
	
//	private static Collection<Question> filterQuestionListByTopicAndQuestionType(
//			final String topicFilterText, final Integer questionType,
//			List<Question> rtn) {
//		ArrayList<ShouldRemoveAnObjectCommand<Question>> arr = new ArrayList<ShouldRemoveAnObjectCommand<Question>>();
//		
//		if (!StringUtil.isNullOrEmpty(topicFilterText))
//			arr.add(new QuestionTopicFilter(topicFilterText));
//
//		if (questionType != null && questionType != TypeConstants.ALL_TYPES )
//			arr.add(new QuestionTypeFilter(questionType));
//
//		return new ListFilterer<Question>().process(rtn, arr);
//	}
	
	public static boolean userCanEditThisQuestion(Question q, User u)
	{
		boolean rtn = false;
		
		if (u != null)
			rtn = (q.getUser().getId() == u.getId()); // its simple now, but in the future we'll flesh this method out..
		
		return rtn;
	}
	
}
