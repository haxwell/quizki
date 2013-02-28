package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.factories.QuestionTypeCheckerFactory;
import com.haxwell.apps.questions.utils.ListFilterer;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TypeUtil;

public class QuestionManager extends Manager {
	
	public static Logger log = Logger.getLogger(QuestionManager.class.getName());
	
	public static long persistQuestion(Question question) 
	{
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		Question rtn = em.merge(question);
		
		em.getTransaction().commit();
		
		em.close();
		
		return rtn.getId();
	}
	
	public static void deleteQuestion(Question question)
	{
		EntityManager em = emf.createEntityManager();
		
		long questionId = question.getId();		
		
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
			
			for (Long l : examIds)
			{
				// Are there any more exams using this question?
				query = em.createNativeQuery("SELECT eq.question_id FROM exam_question eq WHERE eq.exam_id = ?1");
				query.setParameter(1, l);
				
				List<Long> list = (List<Long>)query.getResultList();
				
				// If not, delete this exam, too..
				if (list.size() == 0) {
					NotificationManager.issueNotification_emptyExamWasDeleted(l);
					
					query = em.createNativeQuery("DELETE FROM exam WHERE id = ?1");
					query.setParameter(1, l);
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
	}
	
	public static Question newQuestion()
	{
		return new Question();
	}
	
	public static Collection<Question> getQuestionsByTopic(long topicId)
	{
		EntityManager em = emf.createEntityManager();
		boolean b = em.isOpen();

		Query query = em.createNativeQuery("SELECT qt.question_id FROM question_topic qt WHERE qt.topic_id = ?1");
		
		query.setParameter(1, topicId);
				
		List<Long> list = (List<Long>)query.getResultList();
		
		Collection<Question> coll = getQuestionsById(StringUtil.getCSVString(list));
		
		return coll;
	}
	
	public static Collection<Question> getAllQuestions() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT q FROM Question q");
		
		return (Collection<Question>)query.getResultList();
	}

	/**
	 * Takes comma delimited list of IDs, and returns a collection of the Question 
	 * object represented by those IDs.
	 * 
	 * @param questionIDs
	 * @return
	 */
	public static Collection<Question> getQuestionsById(String questionIDs) {
		Collection<Question> rtn = new ArrayList<Question>();
		
		EntityManager em = emf.createEntityManager();
		
		StringTokenizer tokenizer = new StringTokenizer(questionIDs, ",");
		
		if (tokenizer.hasMoreTokens())
		{
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
			
			rtn = (Collection<Question>)query.getResultList();
		}
		
		return rtn;
	}
	
	public static Question getQuestionById(String aSingleId)
	{
		Question rtn;
		
		EntityManager em = emf.createEntityManager();
		
		{
			String queryStr = "SELECT q FROM Question q WHERE q.id=?1";

			Query query = em.createQuery(queryStr);
			
			query.setParameter(1, Long.parseLong(aSingleId));
			
			rtn = (Question)query.getSingleResult();
		}
		
		return rtn;
	}
	
	public static boolean isAnsweredCorrectly(Question question, Map<String, String> answers)
	{
		AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(question);
		return checker.questionIsCorrect(answers);
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

	public static Collection<Question> getQuestionsThatContain(String topicFilterText, String filterText, int maxDifficulty) {
		EntityManager em = emf.createEntityManager();
		
		String queryString = "SELECT q FROM Question q WHERE ";
		
		if (!StringUtil.isNullOrEmpty(filterText))
			queryString += "q.text LIKE ?2 OR q.description LIKE ?2 AND ";
		
		queryString += "q.difficulty.id <= ?1";
		
		Query query = em.createQuery(queryString, Question.class);
		
		if (!StringUtil.isNullOrEmpty(filterText))
			query.setParameter(2, "%" + filterText + "%");
		
		query.setParameter(1, maxDifficulty);
		
		Collection<Question> rtn = (Collection<Question>)query.getResultList();

		if (!StringUtil.isNullOrEmpty(topicFilterText)) {
			// TODO: Abstract this out into its own utility function..
			//  take a list, then remove any from that list that do
			//  not have an attribute that matches a given string..
			List<Question> toBeFilteredList = new ArrayList<Question>();
			boolean keepThisQuestion;
			
			for (Question q : rtn)
			{
				keepThisQuestion = false;
				Set<Topic> set = q.getTopics();
				
				for (Topic t : set) {
					if (t.getText().contains(topicFilterText))
						keepThisQuestion = true;
				}
				
				if (!keepThisQuestion)
					toBeFilteredList.add(q);
			}
			
			for (Question q: toBeFilteredList)
				rtn.remove(q);
		}
		
		return rtn;
	}
	
	public static Collection<Question> getQuestionsCreatedByAGivenUserThatContain(long userId, String topicFilterText, String filterText, Integer maxDifficulty) {
		return getQuestionsCreatedByAGivenUserThatContain(userId, topicFilterText, filterText, maxDifficulty, null);
	}
	
	public static Collection<Question> getQuestionsCreatedByAGivenUserThatContain(long userId, final String topicFilterText, String filterText, Integer maxDifficulty, final Integer questionType) {
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
		
		Collection<Question> rtn = (Collection<Question>)query.getResultList();

//		if (!StringUtil.isNullOrEmpty(topicFilterText)) {

			ArrayList<ShouldRemoveAnObjectCommand<Question>> arr = new ArrayList<ShouldRemoveAnObjectCommand<Question>>();
			
			arr.add(new ShouldRemoveAnObjectCommand<Question>() {
				public boolean shouldRemove(Question q) {
					Set<Topic> set = q.getTopics();

					boolean rtn = true;

					if (!StringUtil.isNullOrEmpty(topicFilterText)) {
						boolean matchFound = false;
						
						for (Topic t : set) {
							if (!matchFound && t.getText().contains(topicFilterText))
								matchFound = true;
						}
						
						rtn = matchFound;
					}

					return !rtn;
				}
			});

			arr.add(new ShouldRemoveAnObjectCommand<Question>() {
				public boolean shouldRemove(Question q) {
					boolean rtn = false;

					if (questionType != null && questionType != TypeConstants.ALL_TYPES && TypeUtil.convertToInt(q.getQuestionType()) != questionType)
						rtn = true;

					return rtn;
				}
			});
			
			rtn = new ListFilterer<Question>().process(rtn, arr);
			
			// TODO: Abstract this out into its own utility function..
			//  take a list, then remove any from that list that do
			//  not have an attribute that matches a given string..
//			List<Question> toBeFilteredList = new ArrayList<Question>();
//			boolean keepThisQuestion = false;
//			
//			for (Question q : rtn)
//			{
//				Set<Topic> set = q.getTopics();
//				
//				for (Topic t : set) {
//					if (t.getText().contains(topicFilterText))
//						keepThisQuestion = true;
//					
//					if (questionType != null && questionType != TypeConstants.ALL_TYPES && questionType == TypeUtil.convertToInt(q.getQuestionType()))
//						keepThisQuestion = true;
//				}
//				
//				if (!keepThisQuestion)
//					toBeFilteredList.add(q);
//			}
//			
//			for (Question q: toBeFilteredList)
//				rtn.remove(q);
//		}
		
		return rtn;
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
