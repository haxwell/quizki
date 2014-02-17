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

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.ExamFeedback;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.utils.CollectionUtil;
import com.haxwell.apps.questions.utils.ExamHistory;
import com.haxwell.apps.questions.utils.PaginationData;
import com.haxwell.apps.questions.utils.PaginationDataUtil;
import com.haxwell.apps.questions.utils.StringUtil;

public class ExamManager extends Manager {

	public static Logger log = Logger.getLogger(ExamManager.class.getName());
	
	protected static ExamManager instance = null;
	
	public static Manager getInstance() {
		if (instance == null)
			instance = new ExamManager();
		
		return instance;
	}
	
	@Override
	public AbstractEntity getEntity(String entityId) {
		return getExam(Long.parseLong(entityId));
	}
	
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
	
	public static void deleteExam(String str)
	{
		deleteExam(getExam(Long.parseLong(str)));
	}
	
	public static void deleteExam(Exam exam)
	{
		EntityManager em = emf.createEntityManager();
		
		long examId = exam.getId();
		
		em.getTransaction().begin();
		
		Query query = em.createNativeQuery("DELETE FROM exam_question WHERE exam_id = ?1");
		query.setParameter(1, examId);
		query.executeUpdate();
		
		query = em.createNativeQuery("DELETE FROM exam WHERE id = ?1");
		query.setParameter(1, examId);
		query.executeUpdate();
		
		em.getTransaction().commit();
		
		em.close();
	}
	
	public static Set<Topic> getAllQuestionTopics(Exam exam)
	{
		Set<Topic> rtn = new HashSet<Topic>();
		
		for (Question q : exam.getQuestions())
		{
			for (Topic t: q.getTopics())
			{
				rtn.add(t);
			}
		}
		
		return rtn;
	}
	
	// TODO: Can this method be removed? Can whatever is using it, use Topic objects instead?
	public static Set<String> getAllQuestionTopicsAsStrings(Exam exam)
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
			
			if (questionIds.contains((int)q.getId()))
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
		Exam rtn = null;
		
		EntityManager em = emf.createEntityManager();
		
		{
			String queryStr = "SELECT e FROM Exam e WHERE e.id=?1";

			Query query = em.createQuery(queryStr);
			
			query.setParameter(1, examId);
			query.setHint("javax.persistence.cache.retrieveMode", "BYPASS"); // skip the EntityManager cache, and go directly to the DB
			
			/**
			 * Sooo.. let me explain why that hint was necessary. When creating the functionality to delete a question,
			 * I discovered that if an exam was created, before a question it contained was deleted, the question would
			 * still show up in the exam, and when the exam was run, Quizki would try to display it, but it would be in
			 * an invalid state. Specifically, Question text would be there, but the rest would be gone.
			 * 
			 * I discovered JPA has a cache, and that this exam was being stored in the cache, and not updated once the
			 * question it contained was removed. I found info on setting query-level cache directives at 
			 * http://docs.oracle.com/javaee/6/tutorial/doc/gkjjj.html . 
			 * 
			 * Ideally, once an object was removed from the system, all objects referring to it would be invalid, but I
			 * didn't see how to make that happen easily. (or at all). Since hitting the DB directly like this won't be
			 * an impact for a long time, till many, many more users are concurrently in Quizki, I decided to take the hit.
			 * 
			 * Anyway. Carry on.
			 *
			 */
			
			rtn = (Exam)query.getSingleResult();

			setTopicsAttribute(rtn);
		}

		em.close();
		
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
		Iterator iterator = eh.iterator();
		int rtn = 0;
		
		int counter = 0;
		
		log.log(Level.FINER, "in getNumberOfQuestionsAnsweredCorrectly");
		
		while (iterator.hasNext())
		{
			ExamHistory.AnsweredQuestion aq = (ExamHistory.AnsweredQuestion)iterator.next();
			log.log(Level.FINER, counter++ + " aq is " + ((aq == null) ? "NULL" : " NOT NULL"));			
			
			if (aq.isCorrect == true)
				rtn++;
		}
		
		return rtn;
	}

	
	// TODO: Create a standard getAll() method for Managers in general
	public static Collection<Exam> getAllQuestions() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT q FROM Exam e");
		
		return (Collection<Exam>)query.getResultList();
	}

	public static Collection<Exam> getAllExams(PaginationData pd) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT e FROM Exam e");
		
		int pageSize = pd.getPageSize();
		int pageNumber = pd.getPageNumber();
		
		query.setMaxResults(pageSize);
		query.setFirstResult(pageNumber * pageSize);
		
		Collection<Exam> rtn = query.getResultList();
		
		em.close();
		
		pd.setTotalItemCount(getNumberOfExamsInTotal());
		
		return rtn;
	}

	public static long getNumberOfExamsCreatedByUser(long id) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT count(*) FROM exam WHERE user_id = ?1");
		
		query.setParameter(1, id);
		
		Long rtn = (Long)query.getSingleResult();
		
		em.close();
		
		return rtn;
	}

	public static Collection<Exam> getAllExamsForUser(long id, PaginationData pd) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT e FROM Exam e, User u WHERE e.user.id = u.id AND u.id = ?1", Exam.class);		
		
		query.setParameter(1, id);
		
		int pageSize = pd.getPageSize();
		int pageNumber = pd.getPageNumber();
		
		query.setMaxResults(pageSize);
		query.setFirstResult(pageNumber * pageSize);
		
		Collection<Exam> rtn = query.getResultList();
		
		em.close();
		
		pd.setTotalItemCount(getNumberOfExamsCreatedByUser(id));		
		
		return rtn;
	}

	public static List<Exam> getAllExamsWithTitlesThatContain(String filterText, PaginationData pd) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT e FROM Exam e WHERE e.title LIKE ?1", Exam.class);		
		
		query.setParameter(1, "%" + filterText + "%");;
		
		List<Exam> rtn = query.getResultList();
		
		em.close();
		
		int rtnSize = rtn.size();

		pd.setTotalItemCount(rtnSize);
	
		if (pd.getPageNumber() > pd.getMaxPageNumber())
			pd.setPageNumber(pd.getMaxPageNumber());
		
		if (rtnSize > pd.getPageSize()) {
			rtn = (List<Exam>)PaginationDataUtil.reduceListSize(pd, rtn);
		}

		return rtn;
	}

	public static List<Exam> getAllExamsCreatedByAGivenUserWithTitlesThatContain(long id, String filterText, PaginationData pd) {
		EntityManager em = emf.createEntityManager();
		
		Query query;
		String queryString = "SELECT e FROM Exam e, User u WHERE e.user.id = u.id AND u.id = ?1";
		
		if (!StringUtil.isNullOrEmpty(filterText))
			queryString += " AND e.title LIKE ?2";
		
		query = em.createQuery(queryString, Exam.class);
		
		query.setParameter(1, id);

		if (!StringUtil.isNullOrEmpty(filterText))
			query.setParameter(2, "%" + filterText + "%");

		List<Exam> rtn = query.getResultList();
		
		int rtnSize = rtn.size();

		pd.setTotalItemCount(rtnSize);
		
		em.close();
		
		if (pd.getPageNumber() > pd.getMaxPageNumber())
			pd.setPageNumber(pd.getMaxPageNumber());
		
		if (rtnSize > pd.getPageSize()) {
			rtn = (List<Exam>)PaginationDataUtil.reduceListSize(pd, rtn);
		}

		return rtn;
	}
	
	public static long getNumberOfExamsInTotal() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT count(*) FROM exam");
		
		Long rtn = (Long)query.getSingleResult();
		
		em.close();
		
		return rtn;
	}
	
	public static void addExamFeedback(Exam e, User commentingUser, String feedback) {
		EntityManager em = emf.createEntityManager();
		
		ExamFeedback ef = new ExamFeedback(e, commentingUser, feedback);

		em.getTransaction().begin();
		
		ExamFeedback ef2 = em.merge(ef);
		
		em.getTransaction().commit();
		
		em.close();
		
		NotificationManager.issueNotification_feedbackLeftForExam(e.getId(), commentingUser.getUsername());
	}
	
	public static List<ExamFeedback> getFeedback(long examId) {
		EntityManager em = emf.createEntityManager();

		Query query;
		String queryString = "SELECT ef FROM ExamFeedback ef WHERE ef.exam.id = ?1";
		
		query = em.createQuery(queryString,  ExamFeedback.class);
		query.setParameter(1, examId);
		
		List<ExamFeedback> rtn = query.getResultList();
		
		em.close();
		
		return rtn;
	}
	
	public static List<ExamFeedback> getFeedback(long userId, long examId) {
		EntityManager em = emf.createEntityManager();

		Query query;
		String queryString = "SELECT ef FROM ExamFeedback ef WHERE ef.commentingUser.id = ?1 AND ef.exam.id = ?2";
		
		query = em.createQuery(queryString,  ExamFeedback.class);
		query.setParameter(1, userId);
		query.setParameter(2, examId);
		
		List<ExamFeedback> rtn = query.getResultList();
		
		em.close();
		
		if (rtn.size() == 0)
			rtn = null;
		
		return rtn;
	}
	
	public static void deleteQuestionFromExam(Exam e, Question q) {
		e.getQuestions().remove(q);
	}

	public static void setTopicsAttribute(Collection<Exam> coll) {
		for (Exam e : coll) setTopicsAttribute(e);
	}
	
	public static void setTopicsAttribute(Exam e) {
		e.setTopics(getAllQuestionTopics(e));
	}
	
	public static List<Long> getExamsWhichContain(Question q) {
		EntityManager em = emf.createEntityManager();

		String queryString = "SELECT exam_id FROM exam_question eq WHERE eq.question_id = ?1";
		
		Query query = em.createNativeQuery(queryString);
		query.setParameter(1, q.getId());
		
		List<Long> rtn = query.getResultList();
		
		em.close();
		
		if (rtn.size() == 0)
			rtn = null;
		
		return rtn;
	}

	public static List<Long> getExamsWhichContain(List<Topic> topics) {
		EntityManager em = emf.createEntityManager();
		
		String queryString = "SELECT exam_id FROM exam_question eq WHERE eq.question_id IN (SELECT question_id FROM question_topic WHERE topic_id in (?1))";
		
		Query query = em.createNativeQuery(queryString);
		query.setParameter(1, CollectionUtil.getCSVofIDsFromListofEntities(topics));
		
		List<Long> rtn = query.getResultList();
		
		em.close();
		
		if (rtn.size() == 0)
			rtn = null;
		
		return rtn;
	}
	
	public static List<Exam> getExamsById(List<Long> list) {
		List<Exam> rtn = null;
		
		if (list != null) {
			rtn = new ArrayList<Exam>();
			
			Set<Long> set = new HashSet<Long>();
			
			for (Long l : list) {
				if (!set.contains(l))
				{
					set.add(l);
					rtn.add(getExam(l));
				}
			}
		}
		
		return rtn;
	}
}
