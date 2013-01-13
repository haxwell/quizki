package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.utils.ExamHistory;

public class ExamManager extends Manager {

	public static Logger log = Logger.getLogger(ExamManager.class.getName());
	
	public static Exam newExam()
	{
		Exam exam = new Exam();
		return exam;
	}
	
	public static long persistExam(Exam exam) 
	{
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		Exam rtn = em.merge(exam);
		
		em.getTransaction().commit();
		
		return rtn.getId();
	}
	
	public static Set<String> getAllQuestionTopics(Exam exam)
	{
		Set<String> rtn = new HashSet<String>();
		
		for (Question q : exam.getQuestions())
		{
			for (Topic t: q.getTopics())
			{
				rtn.add(t.getText());
			}
		}
		
		return rtn;
	}
	
	public static void addQuestion(Exam exam, Question question)
	{
		Set<Question> set = exam.getQuestions();
		set.add(question);
	}
	
	public static void removeQuestion(Exam exam, Question question)
	{
		Set<Question> set = exam.getQuestions();
		set.remove(question);
	}

	/**
	 * Returns true if any exam question by any of the given ids was removed.
	 * 
	 * @param exam
	 * @param questionId
	 * @return
	 */
	public static boolean removeQuestion(Exam exam, Set<Integer> questionIds)
	{
		boolean rtn = false;
		Set<Question> set = exam.getQuestions();
		List<Question> list = new ArrayList<Question>();
		
		Iterator<Question> iterator = set.iterator();
		
		while (iterator.hasNext()){
			Question q = iterator.next();
			
			if (questionIds.contains(q.getId()))
			{
				list.add(q);
			}
		}
		
		if (list.size() > 0)
			for (Question q: list)
				set.remove(q);
		
		exam.setQuestions(set);
		
		return rtn;
	}
	
	public static boolean removeQuestions(Exam exam, String csvID)
	{
		StringTokenizer tokenizer = new StringTokenizer(csvID, ",");
		boolean rtn = true;
		
		Set<Integer> set = new HashSet<Integer>();
		
		while (tokenizer.hasMoreTokens())
		{
			String str = tokenizer.nextToken();
			
			set.add(Integer.parseInt(str));
		}
		
		return removeQuestion(exam, set);
	}

	public static void addQuestions(Exam exam, Collection<Question> questionsToAdd) 
	{
		for (Question q : questionsToAdd)
			addQuestion(exam, q);
	}
	
	public static Exam getExam(long examId)
	{
		EntityManager em = emf.createEntityManager();
		
		Exam rtn = em.find(Exam.class, examId);
		
		em.detach(rtn);
		
		return rtn;
	}

	public static List<Question> getQuestionList(Exam exam)
	{
		Set<Question> choices = exam.getQuestions();
		
		ArrayList<Question> list = new ArrayList<Question>(choices);
		
		Collections.sort(list, Manager.ID_COMPARATOR);
		
		return list;
	}
	
	public static Question getQuestion(Exam exam, int i) {
		Set<Question> set = exam.getQuestions();
		
		return set.iterator().next();
	}
	
	public static ExamHistory initializeExamHistory(Exam e)
	{
		return new ExamHistory(e);
	}
	
	public static int getNumberOfQuestionsAnsweredCorrectly(ExamHistory eh)
	{
//		List<ExamHistory.AnsweredQuestion> list = eh.getListOfAnswersAlreadyProvided();
		Iterator iterator = eh.iterator();
		int rtn = 0;
		
		int counter = 0;
		
		log.log(Level.INFO, "in getNumberOfQuestionsAnsweredCorrectly");
		
		//for (ExamHistory.AnsweredQuestion aq : list)
		while (iterator.hasNext())
		{
			ExamHistory.AnsweredQuestion aq = (ExamHistory.AnsweredQuestion)iterator.next();
			log.log(Level.INFO, counter++ + " aq is " + ((aq == null) ? "NULL" : " NOT NULL"));			
			
			if (aq.isCorrect == true)
				rtn++;
		}
		
		return rtn;
	}

	
	// TODO: Create a standard getAll() method for Managers in general
	public static Collection<Exam> getAllExams() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT e FROM Exam e");
		
		return (Collection<Exam>)query.getResultList();
	}

	public static Collection<Exam> getAllExamsForUser(long id) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT e FROM Exam e, User u WHERE e.user.id = u.id AND u.id = ?1", Exam.class);		
		
		query.setParameter(1, id);
		
		return (Collection<Exam>)query.getResultList();
	}

	public static void delete(Exam e, Question q) {
		e.getQuestions().remove(q);
	}
}
