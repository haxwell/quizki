package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.entities.EntityWithIDAndTextValuePairBehavior;
import com.haxwell.apps.questions.entities.Topic;

public abstract class EntityWithIDAndTextValuePairManager extends Manager {

	public abstract String getDBTableName();
	
	public abstract Class getEntityClass();
	
	public abstract List<? extends EntityWithIDAndTextValuePairBehavior> getNewList();
	
	public abstract String getEntityName();
	
	public Collection<? extends EntityWithIDAndTextValuePairBehavior> getById(String csvString) {
		EntityManager em = emf.createEntityManager();
		
		String strQuery = "SELECT * FROM " + getEntityName() + " WHERE id=";
		StringTokenizer tokenizer = new StringTokenizer(csvString, ",");
		
		while (tokenizer.hasMoreTokens())
		{
			strQuery += tokenizer.nextToken();
			
			if (tokenizer.hasMoreTokens())
				strQuery += " OR id=";
		}
				
		Query query = em.createNativeQuery(strQuery, getEntityClass());
		
		List<? extends EntityWithIDAndTextValuePairBehavior> list = getNewList();
		list = query.getResultList();
		
		em.close();
		
		return list;
	}
	
	public EntityWithIDAndTextValuePairBehavior getById(int i)
	{
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT * FROM " + getEntityName() + " WHERE id = ?1", getEntityClass());
		
		query.setParameter(1, i);
		
		List<? extends EntityWithIDAndTextValuePairBehavior> list = getNewList();
		list = query.getResultList();
		
		em.close();

		return (list.size() > 0 ? list.get(0) : null);
	}
	
	public EntityWithIDAndTextValuePairBehavior getByText(String text)
	{
		EntityManager em = emf.createEntityManager();
		
		// Look around the database, is there an existing Entity?
		Query query = em.createNativeQuery("SELECT * FROM " + getEntityName() + " WHERE text = ?1", getEntityClass());
		
		query.setParameter(1, text);
		
		List<? extends EntityWithIDAndTextValuePairBehavior> list = getNewList();
		list = query.getResultList();
		
		em.close();

		return (list.size() > 0 ? list.get(0) : null);
	}
	
	public List<? extends EntityWithIDAndTextValuePairBehavior> getEntities(Set<String> entityTexts) {
		EntityManager em = emf.createEntityManager();
		
		String queryStr = "SELECT * FROM " + getEntityName() + " ";
		
		if (entityTexts.size() > 0) {
			
			queryStr += "WHERE ";
			
			boolean b = false;
			for (String t : entityTexts) {
				if (b)
					queryStr += " OR ";
				
				queryStr += "text = '" + t + "'";
				
				b = true;
			}
		}

		Query query = em.createNativeQuery(queryStr, getEntityClass());
		
		List<? extends EntityWithIDAndTextValuePairBehavior> list = getNewList();
		list = query.getResultList();
		
		em.close();

		return list;
	}
	
	public List<? extends EntityWithIDAndTextValuePairBehavior> getAllEntitiesForQuestionsCreatedByAGivenUser(long userId)
	{
		EntityManager em = emf.createEntityManager();
		
		String queryString = "SELECT * FROM " + getEntityName() + " WHERE id IN (SELECT " + getEntityName() + "_id FROM question_" + getEntityName() + " WHERE question_id IN (SELECT id FROM question WHERE user_id= ?1))";
		
		Query query = em.createNativeQuery(queryString, getEntityClass());
		
		query.setParameter(1, userId);
		
		List<? extends EntityWithIDAndTextValuePairBehavior> list = getNewList();
		list = query.getResultList();
		
		em.close();
		
		return list;
	}

	public List<? extends EntityWithIDAndTextValuePairBehavior> getAllEntitiesForQuestionsCreatedByAGivenUserThatContain(long id, String filterText) 
	{
		EntityManager em = emf.createEntityManager();
		
		String queryString = "SELECT * FROM " + getEntityName() + " e WHERE e.id IN (SELECT " + getEntityName() + "_id FROM question_" + getEntityName() + " WHERE question_id IN (SELECT id FROM question WHERE user_id= ?1)) AND e.text LIKE ?2";
		
		Query query = em.createNativeQuery(queryString, getEntityClass());
		query.setParameter(1, id);
		query.setParameter(2, "%" + filterText + "%");
		
		List<? extends EntityWithIDAndTextValuePairBehavior> list = getNewList();
		list = query.getResultList();

		em.close();
		
		return list;
	}
	
	public List<? extends EntityWithIDAndTextValuePairBehavior> getAllEntitiesThatContain(String filterText)
	{
		EntityManager em = emf.createEntityManager();
		
		String queryString = "SELECT e FROM " + getDBTableName() + " e WHERE e.text LIKE ?1";
		
		Query query = em.createQuery(queryString, getEntityClass());
		query.setParameter(1, "%" + filterText + "%");
		
		List<? extends EntityWithIDAndTextValuePairBehavior> list = getNewList();
		list = query.getResultList();

		em.close();
		
		return list;
	}
	
	// TODO: Create a standard getAll() method for Managers in general
	public List<? extends EntityWithIDAndTextValuePairBehavior> getAllEntities() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT e FROM " + getDBTableName() + " e");
		
		List<? extends EntityWithIDAndTextValuePairBehavior> list = getNewList();
		list = query.getResultList();
		
		em.close();
		
		return list;
	}

	public Long getTotalNumberOfEntities()	{
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT count(*) FROM " + getEntityName());
		
		Long rtn = (Long)query.getSingleResult();
		
		em.close();
		
		return rtn;
	}
	
	public Integer getNumberOfQuestionsForTopic(EntityWithIDAndTextValuePairBehavior entity) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT count(*) FROM question_" + getEntityName() + " WHERE " + getEntityName() + "_id = ?1", Integer.class);
		
		query.setParameter(1, entity.getId());
		
		Integer rtn = (Integer)query.getSingleResult();
		
		em.close();
		
		return rtn;
	}
	
	public List<? extends EntityWithIDAndTextValuePairBehavior> getAllEntitiesWithMoreThanXXQuestions(int min)
	{
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT qmt." + getEntityName() + "_id, count(question_id) FROM question_" + getEntityName() + " qmt group by " + getEntityName() + "_id");
		
		List<EntityWithIDAndTextValuePairBehavior> rtn = new ArrayList<>();
		
		List list = query.getResultList();
		
		for (int i = 0; i < list.size(); i++) {
			
			Object[] o = (Object[])list.get(i);
			
			int entityId = Integer.parseInt(o[0].toString());
			int count = Integer.parseInt(o[1].toString());
			
			if (count > min)
				rtn.add(getById(entityId));
		}
		
		em.close();
		
		return rtn;
	}
	
	public void delete(Set<EntityWithIDAndTextValuePairBehavior> entities, EntityWithIDAndTextValuePairBehavior e)
	{
		entities.remove(e);
	}
}
