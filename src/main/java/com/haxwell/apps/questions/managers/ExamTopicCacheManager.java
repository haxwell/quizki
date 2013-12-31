package com.haxwell.apps.questions.managers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;

public class ExamTopicCacheManager extends Manager {

	public static Logger log = Logger.getLogger(ExamTopicCacheManager.class.getName());
	
	protected static ExamTopicCacheManager instance = null;
	
	public static Manager getInstance() {
		if (instance == null)
			instance = new ExamTopicCacheManager();
		
		return instance;
	}

	public static void cacheQuestion(Exam e, Question q) {
//		EntityManager em = emf.createEntityManager();
//
//		em.getTransaction().begin();
//
//		Query query = em.createNativeQuery("INSERT INTO exam_topics_cache VALUES (?1, ?2, ?3)");
//		query.setParameter(1, e.getId());
//		query.setParameter(2, topicId);
//		query.setParameter(3, count);
//		
//		em.getTransaction().commit();
//		
//		em.close();
	}
	
	public static void cacheQuestion(Question q) {
		List<Long> examsWhichContainQ = ExamManager.getExamsWhichContain(q);
		
		Set<Topic> topics = q.getTopics();
		
		Map<Long, Long> topicIdToAppearanceCountMap = new HashMap<Long, Long>();
		
		// get the counts for each topic on a given exam
		for (Long l : examsWhichContainQ) {
			Exam e = ExamManager.getExam(l);
			
			for (Topic t : topics) {
				long numTimesTopicAppears = getNumberOfTimesGivenTopicIDAppearsInTheGivenExam(e, t);
				
				topicIdToAppearanceCountMap.put(t.getId(), numTimesTopicAppears);
			}
		}
		
		for (Topic t : topics) {
			long count = topicIdToAppearanceCountMap.get(t.getId());
			
			//set count in cache table
			
			// think we need a column for question id.. in order to be able to tell
			//  update the topic count without knowing beforehand 'was this topic just
			//  added, or removed?'
		}
	}
	
	public static void cacheExam(Exam e) {
		Set<Question> qSet = e.getQuestions();
		
		Map<Long, List<Topic>> map = new HashMap<Long, List<Topic>>();
		
		for (Question q : qSet) {
			Set<Topic> tSet = q.getTopics();
			
			for (Topic t : tSet) {
				List<Topic> l = map.get(t.getId());
				
				if (l == null) {
					l = new LinkedList<Topic>();
					
					map.put(t.getId(),  l);
				}
				
				l.add(t);
			}
		}
		
		delete(e);
		
		Set<Long> topicIds = map.keySet();
		
		for (Long l : topicIds) {
			insert(e.getId(), l, map.get(l).size());
		}
	}
	
	protected static void insert(long examId, long topicId, int count) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Query query = em.createNativeQuery("INSERT INTO exam_topics_cache VALUES (?1, ?2, ?3)");
		query.setParameter(1, examId);
		query.setParameter(2, topicId);
		query.setParameter(3, count);

		query.executeUpdate();

		em.getTransaction().commit();

		em.close();
	}
	
	protected static void delete(Exam e) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Query query = em.createNativeQuery("DELETE FROM exam_topics_cache WHERE exam_id = ?1");
		query.setParameter(1, e.getId());
		
		query.executeUpdate();
		
		em.getTransaction().commit();
		
		em.close();
	}
	
	protected static void updateCount(Exam e, Question q) {
		EntityManager em = emf.createEntityManager();

		Set<Topic> topics = q.getTopics();
		
		for (Topic t : topics) {
			em.getTransaction().begin();
	
			Query query = em.createNativeQuery("DELETE FROM exam_topics_cache WHERE exam_id = ?1 AND topic_id = ?2");
			query.setParameter(1, e.getId());
			query.setParameter(2, t.getId());
			
			query.executeUpdate();
			
			em.getTransaction().commit();
		}
		
		em.close();
	}
	
	protected static void decrementCount(Exam e, long topicId) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Query query = em.createNativeQuery("INSERT INTO exam_topics_cache WHERE exam_id = ?1 AND topic_id = ?2");
		query.setParameter(1, e.getId());
		query.setParameter(2, topicId);
		
		query.executeUpdate();
		
		em.getTransaction().commit();
		
		em.close();
	}
	
	public static long getNumberOfTimesGivenTopicIDAppearsInTheGivenExam(Exam e, Topic t) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Query query = em.createNativeQuery("SELECT count FROM exam_topics_cache WHERE exam_id = ?1");
		query.setParameter(1, e.getId());
		
		long rtn = (Long)query.getSingleResult();
		
		em.getTransaction().commit();
		
		em.close();
		
		return rtn;
	}
}
