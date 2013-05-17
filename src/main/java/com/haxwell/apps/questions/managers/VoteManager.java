package com.haxwell.apps.questions.managers;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.Vote;
import com.haxwell.apps.questions.factories.EntityTypeFactory;

public class VoteManager extends Manager {

	public static void voteUp(User u, AbstractEntity e) {
		mergeVote(u, e, Boolean.TRUE);
	}
	
	public static void voteDown(User u, AbstractEntity e)  {
		mergeVote(u, e, Boolean.FALSE);
	}
	
	public static Vote getVote(User u, AbstractEntity e) {
		Vote rtn = null;
		
		EntityManager em = emf.createEntityManager();
		
		String queryStr = "SELECT v FROM Vote v WHERE v.user.id=?1 AND v.entityId=?2 AND v.entityType.id=?3";

		Query query = em.createQuery(queryStr);
		
		query.setParameter(1, u.getId());
		query.setParameter(2, e.getId());
		query.setParameter(3, EntityTypeFactory.getEntityTypeFor(e).getId());
		
		try {
			rtn = (Vote)query.getSingleResult();
		}
		catch (Exception ex) 
		{
			String strr = ex.getMessage();
		}
		finally {
			em.close();
		}

		return rtn;
	}
	
	private static void mergeVote(User u, AbstractEntity e, Boolean isPositiveVote) {
		EntityManager em = emf.createEntityManager();
		
		Vote vote = getNewVote(u, e);
		
		if (isPositiveVote)
			vote.setThumbsUp(Boolean.TRUE);
		else
			vote.setThumbsDown(Boolean.TRUE);
		
		em.getTransaction().begin();
		
		em.merge(vote);
		
		em.getTransaction().commit();		
		
		em.close();
	}
	
	private static Vote getNewVote(User u, AbstractEntity e) {
		Vote vote = new Vote();
		
		vote.setEntityId(e.getId());
		vote.setEntityType(EntityTypeFactory.getEntityTypeFor(e));
		vote.setUser(u);
		
		return vote;
	}
}
