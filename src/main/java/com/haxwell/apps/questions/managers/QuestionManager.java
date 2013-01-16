package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.factories.QuestionTypeCheckerFactory;
import com.haxwell.apps.questions.interfaces.IQuestion;
import com.haxwell.apps.questions.utils.StringUtil;

public class QuestionManager extends Manager {
	
	public static Logger log = Logger.getLogger(QuestionManager.class.getName());
	
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public static long persistQuestion(Question question) 
	{
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		Question rtn = em.merge(question);
		
		em.getTransaction().commit();
		
		return rtn.getId();
	}
	
	public static Question newQuestion()
	{
		IQuestion rtn = new Question();
		
		//TEMPORARY!  -- Get it from the user object, and pass it in
//		rtn.setOwnerId(1);
		
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

	public static boolean isAnsweredCorrectly(Question question, List<String> answers)
	{
		AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(question);
		return checker.questionIsCorrect(answers);
	}

	public static Collection<Question> getAllQuestionsForUser(int id) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT q FROM Question q, User u WHERE q.user.id = u.id AND u.id = ?1", Question.class);
		
		query.setParameter(1, id);
		
		return (Collection<Question>)query.getResultList();
	}

	public static Collection<Question> getQuestionsThatContain(String topicFilterText, String filterText, int maxDifficulty) {
		EntityManager em = emf.createEntityManager();
		
		String queryString = "SELECT q FROM Question q WHERE ";
		
		if (!StringUtil.isNullOrEmpty(filterText))
			queryString += "q.text LIKE ?2 AND ";
		
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
					// remember maven.
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
	
	public static List<String> validate(Question questionObj) {
		List<String> errors = new ArrayList<String>();

		errors.addAll(ChoiceManager.validate(questionObj));
		
		if (questionObj.getTopics().size() < 1)
			errors.add("Question must have at least one topic.");
		
		if (StringUtil.isNullOrEmpty(questionObj.getText()))
			errors.add("Question must have some text!");
		
//		if (StringUtil.isNullOrEmpty(questionObj.getDescription()))
//			errors.add("Question must have a description!");
		
		return errors;
	}
}
