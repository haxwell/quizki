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

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.ExamFeedback;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.filters.DifficultyFilter;
import com.haxwell.apps.questions.filters.ExamFilter;
import com.haxwell.apps.questions.filters.ExamTopicFilter;
import com.haxwell.apps.questions.utils.CollectionUtil;
import com.haxwell.apps.questions.utils.ExamHistory;
import com.haxwell.apps.questions.utils.ExamUtil;
import com.haxwell.apps.questions.utils.FilterCollection;
import com.haxwell.apps.questions.utils.ListFilterer;
import com.haxwell.apps.questions.utils.PaginationData;
import com.haxwell.apps.questions.utils.PaginationDataUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
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
	
//	public static ExamHistory initializeExamHistory(Exam e)
//	{
//		return new ExamHistory(e);
//	}
	
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
		List<Long> rtn = null;

		if (topics != null && topics.size() > 0) {
			
			EntityManager em = emf.createEntityManager();
			
			// I did have this using ?1 as a parameter marker which was replaced with the value from a call to CollectionUtil.getCSVofIDsFromListofEntities(topics),
			// but when I did that, I wasn't getting any results in my test case. To reproduce, replace the CollectionUtil.getCSVofIDsFromListofEntities(topics) with ?1
			// and then add a line saying query.setParameter(1, CollectionUtil.getCSVofIDsFromListofEntities(topics)), and have the parameter value be "2,1".
			// messed around with it for a while, changing to named parameters (ie :topicIdList), but couldn't figure exactly what was wrong.. maybe one day I'll have time
			String queryString = "SELECT exam_id FROM exam_question eq WHERE eq.question_id IN (SELECT question_id FROM question_topic WHERE topic_id in (" + CollectionUtil.getCSVofIDsFromListofEntities(topics) + "))";
			
			Query query = em.createNativeQuery(queryString);
			
			rtn = query.getResultList();
			
			em.close();
			
			if (rtn.size() == 0)
				rtn = null;
		}
		
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
	
	public static AJAXReturnData getAJAXReturnObject(FilterCollection fc) {
		int maxEntityCount = Integer.parseInt(fc.get(FilterConstants.MAX_ENTITY_COUNT_FILTER).toString());
		int offset = Integer.parseInt(fc.get(FilterConstants.OFFSET_FILTER).toString());

		AJAXReturnData rtn = null;

		if (fc.get(FilterConstants.RANGE_OF_ENTITIES_FILTER).equals(Constants.SELECTED_ITEMS+"")) {
			rtn = handleTheSelectedExamsCase(fc, null, maxEntityCount, offset);
		}
		else {  // this is a request for questions from the db
			String entityIdFilter = (String)fc.get(FilterConstants.ENTITY_ID_FILTER);
			
			if (!StringUtil.isNullOrEmpty(entityIdFilter))
				rtn = handleTheGetEntityByIdCase(fc, null);
			else
				rtn = handleTheOtherCases(fc, null, maxEntityCount, offset);
		}
		
		return rtn == null ? new AJAXReturnData() : rtn;
	}

	/* Helper method for ::getAJAXReturnObject() */
	private static AJAXReturnData handleTheSelectedExamsCase(FilterCollection fc, Set selectedExams, int maxEntityCount, int offset) {
		AJAXReturnData rtn = new AJAXReturnData();
		List list = null;

		list = new ArrayList();
		
		if (selectedExams != null && selectedExams.size() > 0)
		{
			Iterator iterator = selectedExams.iterator();
			
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
		
		long examId = Long.parseLong(fc.get(FilterConstants.ENTITY_ID_FILTER)+"");
		
		Exam e = getExam(examId);
		
		if (e == null)
			rtn.additionalInfoCode = Manager.ADDL_INFO_NO_ENTITIES_MATCHING_GIVEN_FILTER;
		else {
			ArrayList<Exam> arr = new ArrayList<Exam>();
			arr.add(e);
			
			rtn.entities = arr; 
			
			if (selectedQuestions != null) {
				Iterator<Question> iterator = selectedQuestions.iterator();
				List<Exam> listOfSelectedEntitiesMatchingFilter = new ArrayList<Exam>();
				
				while (iterator.hasNext())
				{
					Question selectedQuestion = iterator.next();
					if (selectedQuestion.getId() == e.getId())
						listOfSelectedEntitiesMatchingFilter.add(e);
				}
				
				rtn.addKeyValuePairToJSON("selectedEntityIDsAsCSV", CollectionUtil.getCSVofIDsFromListofEntities(listOfSelectedEntitiesMatchingFilter));				
			}
		}
		
		return rtn;
	}
	
	/* Helper method for ::getAJAXReturnObject() */
	private static AJAXReturnData handleTheOtherCases(FilterCollection fc, Set<Question> selectedQuestions, int maxEntityCount, int offset) {
		AJAXReturnData rtn = new AJAXReturnData();
		List<Exam> list = null;

		list = getFilteredListOfExams(fc);
		
		if (list.size() == 0) {
			if (getNumberOfExamsCreatedByUser( Long.parseLong((String)fc.get(FilterConstants.USER_ID_FILTER))) == 0)
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
	
	// TODO: this should probably be in a util somewhere
	private static List filterList(FilterCollection fc, List list) {
		ArrayList<ShouldRemoveAnObjectCommand> arr = new ArrayList<ShouldRemoveAnObjectCommand>();
		
		String filter = fc.get(FilterConstants.TOPIC_CONTAINS_FILTER).toString();
		
		if (!StringUtil.isNullOrEmpty(filter)) {
			arr.add(new ExamTopicFilter(filter));
		}
		
		filter = fc.get(FilterConstants.EXAM_CONTAINS_FILTER) != null ? fc.get(FilterConstants.EXAM_CONTAINS_FILTER).toString() : "";
		
		if (!StringUtil.isNullOrEmpty(filter)) {
			arr.add(new ExamFilter(filter));
		}
		
		filter = fc.get(FilterConstants.DIFFICULTY_FILTER) != null ? fc.get(FilterConstants.DIFFICULTY_FILTER).toString() : "";
		
		if (!StringUtil.isNullOrEmpty(filter)) {
			arr.add(new DifficultyFilter(Integer.parseInt(filter)));
		}
		
		return new ListFilterer().process(list, arr);
	}
	
	private static List<Exam> getFilteredListOfExams(FilterCollection fc) {
		EntityManager em = emf.createEntityManager();
		
		String filterText = fc.get(FilterConstants.EXAM_CONTAINS_FILTER).toString();
		String topicFilterText = fc.get(FilterConstants.TOPIC_CONTAINS_FILTER).toString();
		int maxDifficulty = Integer.parseInt(fc.get(FilterConstants.DIFFICULTY_FILTER).toString());
		boolean includeOnlyUserCreatedEntities = fc.get(FilterConstants.RANGE_OF_ENTITIES_FILTER).equals(Constants.MY_ITEMS.toString());
		User user = (User)fc.get(FilterConstants.USER_ID_ENTITY);
		
		String queryString = "SELECT e FROM Exam e";
		
		boolean filterTextIsNullOrEmpty = StringUtil.isNullOrEmpty(filterText);
		if (!filterTextIsNullOrEmpty || includeOnlyUserCreatedEntities)
			queryString += " WHERE ";
		
		if (!filterTextIsNullOrEmpty)
			queryString += "e.title LIKE ?1";
		
		if (!filterTextIsNullOrEmpty && includeOnlyUserCreatedEntities)
			queryString += " AND ";
		
		if (includeOnlyUserCreatedEntities)
			queryString += "e.user.id = ?2";
		
		Query query = em.createQuery(queryString, Exam.class);
		
		if (!filterTextIsNullOrEmpty)
			query.setParameter(1, "%" + filterText + "%");
		
		if (includeOnlyUserCreatedEntities)
			query.setParameter(2, Long.parseLong(user.getId()+""));
		
		List<Exam> rtn = (List<Exam>)query.getResultList();

		// TODO: Pretty sure this is going to cause a major performance hit.. should we just remove the ability to filter on difficulty for exams, or is there some caching we can do? 
		for (Exam e : rtn) {
			e.setDifficulty(ExamUtil.getExamDifficulty(e));
			e.setTopics(ExamUtil.getExamTopics(e));
		}
		
		// TODO: figure out a way to get the query to do this...
		FilterCollection fc2 = new FilterCollection();
		fc2.add(FilterConstants.TOPIC_CONTAINS_FILTER, topicFilterText);
		fc2.add(FilterConstants.DIFFICULTY_FILTER, maxDifficulty);

		rtn = (List<Exam>)filterList(fc2, rtn);
		
		return rtn;
	}
}
