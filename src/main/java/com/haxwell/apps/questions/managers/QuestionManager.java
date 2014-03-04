package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
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
import com.haxwell.apps.questions.factories.QuestionTypeCheckerFactory;
import com.haxwell.apps.questions.filters.DifficultyFilter;
import com.haxwell.apps.questions.filters.QuestionFilter;
import com.haxwell.apps.questions.filters.QuestionTopicFilter;
import com.haxwell.apps.questions.filters.QuestionTypeFilter;
import com.haxwell.apps.questions.utils.CollectionUtil;
import com.haxwell.apps.questions.utils.FilterCollection;
import com.haxwell.apps.questions.utils.ListFilterer;
import com.haxwell.apps.questions.utils.PaginationData;
import com.haxwell.apps.questions.utils.PaginationDataUtil;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.StringUtil;

public class QuestionManager extends Manager {
	
	public static Logger log = Logger.getLogger(QuestionManager.class.getName());
	
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
	
	public static Collection<Question> getQuestionsByTopic(long topicId, PaginationData pd)
	{
		EntityManager em = emf.createEntityManager();
		boolean b = em.isOpen();

		Query query = em.createNativeQuery("SELECT qt.question_id FROM question_topic qt WHERE qt.topic_id = ?1");
		
		query.setParameter(1, topicId);
				
		List<Long> list = (List<Long>)query.getResultList();
		
		Collection<Question> coll = getQuestionsById(StringUtil.getCSVString(list), pd);
		
		return coll;
	}
	
	public static Collection<Question> getAllQuestions() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT q FROM Question q");
		
		Collection<Question> rtn = query.getResultList();
		
		em.close();
		
		return rtn;
	}
	
	public static Collection<Question> getAllQuestions(PaginationData pd)
	{
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT q FROM Question q");
		
		int pageSize = pd.getPageSize();
		int pageNumber = pd.getPageNumber();
		
		query.setMaxResults(pageSize);
		query.setFirstResult(pageNumber * pageSize);
		
		Collection<Question> rtn = query.getResultList();
		
		em.close();
		
		pd.setTotalItemCount(getNumberOfQuestionsInTotal());		
		
		return rtn;
	}

	/**
	 * Takes comma delimited list of IDs, and returns a collection of the Question 
	 * object represented by those IDs.
	 * 
	 * @param questionIDs
	 * @return
	 */
	public static List<Question> getQuestionsById(String questionIDs, PaginationData pd) {
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
			
			if (pd != null) {
				int pageSize = pd.getPageSize();
				int pageNumber = pd.getPageNumber();
				
				query.setMaxResults(pageSize);
				query.setFirstResult(pageNumber * pageSize);
				
				pd.setTotalItemCount(tokenCount);				
			}
			
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
	
	public static boolean isAnsweredCorrectly(Question question, Map<String, String> answers)
	{
		AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(question);
		return checker.questionIsCorrect(answers);
	}

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

	public static Collection<Question> getAllQuestionsForUser(long id, PaginationData pd) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT q FROM Question q, User u WHERE q.user.id = u.id AND u.id = ?1", Question.class);
		
		query.setParameter(1, id);
		
		int pageSize = pd.getPageSize();
		int pageNumber = pd.getPageNumber();
		
		query.setMaxResults(pageSize);
		query.setFirstResult(pageNumber * pageSize);
		
		Collection<Question> rtn = query.getResultList();
		
		em.close();
		
		pd.setTotalItemCount(getNumberOfQuestionsCreatedByUser(id));
		
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

		boolean filterTextIsNullOrEmpty = StringUtil.isNullOrEmpty(filterText);
		if (!filterTextIsNullOrEmpty || maxDifficulty > 0 || includeOnlyUserCreatedEntities)
			queryString += " WHERE "; 
		
		if (maxDifficulty > 0) 
			queryString += "q.difficulty.id = ?1 ";
		
		if (maxDifficulty > 0 && !filterTextIsNullOrEmpty)
			queryString += " AND ";
		
		if (!filterTextIsNullOrEmpty)
			queryString += " UPPER(q.text) LIKE ?2 OR UPPER(q.description) LIKE ?2"; 
		
		if (!filterTextIsNullOrEmpty && includeOnlyUserCreatedEntities)
			queryString += " AND ";
		
		if (includeOnlyUserCreatedEntities)
			queryString += "q.user.id = ?3";
		
		Query query = em.createQuery(queryString, Question.class);
		
		if (maxDifficulty > 0) 
			query.setParameter(1, maxDifficulty);
		
		if (!filterTextIsNullOrEmpty)
			query.setParameter(2, "%" + filterText.toUpperCase() + "%");
		
		if (includeOnlyUserCreatedEntities)
			query.setParameter(3, Long.parseLong(user.getId()+""));
		
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

		rtn.addKeyValuePairToJSON("selectedEntityIDsAsCSV", CollectionUtil.getCSVofIDsFromListofEntities(selectedQuestions));
		rtn.entities = CollectionUtil.pareListDownToSize(list, offset, maxEntityCount);
		
		return rtn;
	}
	
	public static AJAXReturnData getBlankQuestion() {
		AJAXReturnData rtn = new AJAXReturnData();
		List<Question> list = new ArrayList<Question>();
		
		Question q = new Question();
		
		q.setDifficulty(new Difficulty(DifficultyConstants.JUNIOR));
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
	
	@Deprecated //with the advent of smooth scrolling, the pagination data object is no longer used
	public static List<Question> getQuestionsThatContain(final String topicFilterText, final String filterText, final int maxDifficulty, final Integer questionType, PaginationData pd) {
//		EntityManager em = emf.createEntityManager();
//		
//		String queryString = "SELECT q FROM Question q WHERE ";
//		
//		if (!StringUtil.isNullOrEmpty(filterText))
//			queryString += "q.text LIKE ?2 OR q.description LIKE ?2 AND ";
//		
//		queryString += "q.difficulty.id <= ?1";
//		
//		Query query = em.createQuery(queryString, Question.class);
//		
//		if (!StringUtil.isNullOrEmpty(filterText))
//			query.setParameter(2, "%" + filterText + "%");
//		
//		query.setParameter(1, maxDifficulty);
//		
//		List<Question> rtn = (List<Question>)query.getResultList();
//
//		rtn = (List<Question>)filterQuestionListByTopicAndQuestionType(topicFilterText, questionType, rtn);
//		
//		pd.setTotalItemCount(rtn.size());
//		
//		List<Question> paginatedList = new ArrayList<Question>();
//		
//		int rtnSize = rtn.size();
//		
//		if (rtnSize > pd.getPageSize())
//		{
//			int pageSize = pd.getPageSize();
//			int pageNumber = pd.getPageNumber();
//			
//			for (int i = pageSize * pageNumber; i < Math.min(rtnSize, ((pageSize * pageNumber) + pageSize)); i++) {
//				paginatedList.add(rtn.get(i));
//			}
//		}
//		else
//			paginatedList = rtn;
//
//		return paginatedList;
		
		return null;
	}
	
	public static List<Question> getQuestionsCreatedByAGivenUserThatContain(long userId, final String topicFilterText, String filterText, Integer maxDifficulty, final Integer questionType, PaginationData pd) {
		EntityManager em = emf.createEntityManager();
		
		String queryString = "SELECT q FROM Question q, User u WHERE q.user.id = u.id AND u.id = ?1 AND ";
		
		if (!StringUtil.isNullOrEmpty(filterText))
			queryString += "((q.text LIKE ?2 AND q.description =\"\") OR q.description LIKE ?2) AND ";
		
		queryString += "q.difficulty.id <= ?3";
		
		Query query = em.createQuery(queryString, Question.class);
		
		query.setParameter(1, userId);
		
		if (!StringUtil.isNullOrEmpty(filterText))
			query.setParameter(2, "%" + filterText + "%");
		
		query.setParameter(3, maxDifficulty);
		
		//
		// Get query results
		List<Question> rtn = (List<Question>)query.getResultList();
		
		pd.setTotalItemCount(rtn.size());

		rtn = (List<Question>)filterQuestionListByTopicAndQuestionType(topicFilterText, questionType, rtn);
		
		int rtnSize = rtn.size();
		
		if (pd.getPageNumber() > pd.getMaxPageNumber())
			pd.setPageNumber(pd.getMaxPageNumber());
		
		if (rtnSize > pd.getPageSize()) {
			rtn = (List<Question>)PaginationDataUtil.reduceListSize(pd, rtn);
		}

		return rtn;
	}

	// TODO: this should probably be in a util somewhere
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
		
		filter = fc.get(FilterConstants.DIFFICULTY_FILTER) != null ? fc.get(FilterConstants.DIFFICULTY_FILTER).toString() : "";
		
		if (!StringUtil.isNullOrEmpty(filter)) {
			arr.add(new DifficultyFilter(Integer.parseInt(filter)));
		}
		
		ListFilterer lf = new ListFilterer();
		ArrayList al = new ArrayList(lf.process(list,arr));
		
		return al;
	}
	
	private static Collection<Question> filterQuestionListByTopicAndQuestionType(
			final String topicFilterText, final Integer questionType,
			List<Question> rtn) {
		ArrayList<ShouldRemoveAnObjectCommand<Question>> arr = new ArrayList<ShouldRemoveAnObjectCommand<Question>>();
		
		if (!StringUtil.isNullOrEmpty(topicFilterText))
			arr.add(new QuestionTopicFilter(topicFilterText));

		if (questionType != null && questionType != TypeConstants.ALL_TYPES )
			arr.add(new QuestionTypeFilter(questionType));

		return new ListFilterer<Question>().process(rtn, arr);
	}
	
	public static List<String> validate(Question questionObj) {
		List<String> errors = new ArrayList<String>();

		errors.addAll(ChoiceManager.validate(questionObj));
		
		String questionText = questionObj.getText();
		
		if (questionObj.getTopics().size() < 1)
			errors.add("Question must have at least one topic.");
		
		if (StringUtil.isNullOrEmpty(questionText))
			errors.add("Question must have some text!");
		
		if (questionText != null && questionText.length() > Constants.MAX_QUESTION_TEXT_LENGTH)
			errors.add("Question text cannot be longer than " + Constants.MAX_QUESTION_TEXT_LENGTH + " characters. Perhaps a seperate question is in order!");
		
		if (questionObj.getDescription() != null && questionObj.getDescription().length() > Constants.MAX_QUESTION_DESCRIPTION_LENGTH)
			errors.add("Question description cannot be longer than " + Constants.MAX_QUESTION_DESCRIPTION_LENGTH + " characters.");
		
		return errors;
	}
	
	public static boolean userCanEditThisQuestion(Question q, User u)
	{
		boolean rtn = false;
		
		if (u != null)
			rtn = (q.getUser().getId() == u.getId()); // its simple now, but in the future we'll flesh this method out..
		
		return rtn;
	}
	
}
