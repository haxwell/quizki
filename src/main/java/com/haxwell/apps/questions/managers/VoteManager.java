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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.Vote;
import com.haxwell.apps.questions.factories.EntityTypeFactory;
import com.haxwell.apps.questions.utils.EntityTypeUtil;
import com.haxwell.apps.questions.utils.VoteData;
import com.haxwell.apps.questions.utils.VoteUtil;

public class VoteManager extends Manager {

	public static void voteUp(User u, AbstractEntity e) {
		Vote vote = getVote(u, e);
		
		if (vote == null) {
			persistVote(u, e, Boolean.TRUE);
			NotificationManager.issueNotification_entityWasVotedOn(e.getId(), EntityTypeUtil.getEnityTypeConstant(e));			
		}
		else
			updateVote(u, e, Boolean.TRUE);
	}
	
	public static void voteDown(User u, AbstractEntity e)  {
		Vote vote = getVote(u, e);
		
		if (vote == null) {
			persistVote(u, e, Boolean.FALSE);
			NotificationManager.issueNotification_entityWasVotedOn(e.getId(), EntityTypeUtil.getEnityTypeConstant(e));
		}
		else
			updateVote(u, e, Boolean.FALSE);
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
	
	// TODO: change this to List once the change in InitializeListInProfileQuestionsInSessionAction is changed to List
	public static Map<String, List<Vote>> getVotes(Collection<? extends AbstractEntity> entities)
	{
		EntityManager em = emf.createEntityManager();
		
		Map<String, List<Vote>> rtn = new HashMap<String, List<Vote>>();
		
		// get the number of positive and negative votes for each entity
		for (AbstractEntity e : entities) {
			String queryStr = "SELECT v FROM Vote v WHERE v.entityId=?1 AND v.entityType.id=?2";
			
			Query query = em.createQuery(queryStr);
			
			query.setParameter(1, e.getId());
			query.setParameter(2, EntityTypeFactory.getEntityTypeFor(e).getId());
			
			try {
				List<Vote> votes = query.getResultList();
				
				String key = VoteUtil.getVoteKey(e);
				rtn.put(key, votes);
			}
			catch (Exception ex)
			{
				
			}
		}
		
		em.close();
		
		return rtn;
	}
	
	public static Map<String, VoteData> getSummarizedVotes(Collection<? extends AbstractEntity> entities)
	{
		Map<String, VoteData> rtn = null;
		
		if (entities != null) {
			Map<String, List<Vote>> detailedVoteMap = getVotes(entities);
			
			rtn = new HashMap<String, VoteData>();			
			
			for (AbstractEntity e : entities) {
				String key = VoteUtil.getVoteKey(e);
				
				List<Vote> voteList = detailedVoteMap.get(key);
				
				VoteData voteData = VoteUtil.getMetricsOnListOfVotes(e, voteList);
				
				rtn.put(key, voteData);
			}
		}
		
		return rtn;
	}
	
	public static List<Vote> getVotes(AbstractEntity e) 
	{
		EntityManager em = emf.createEntityManager();
		
		List<Vote> rtn = new ArrayList<Vote>();
		
		String queryStr = "SELECT v FROM Vote v WHERE v.entityId=?1 AND v.entityType.id=?2";
		
		Query query = em.createQuery(queryStr);
		
		query.setParameter(1, e.getId());
		query.setParameter(2, EntityTypeFactory.getEntityTypeFor(e).getId());
		
		try {
			rtn = query.getResultList();
		}
		catch (Exception ex)
		{
			
		}
		
		em.close();
		
		return rtn;
	}
	
	// TODO: maybe we should just pass in the vote, and use its ID in the where clause..  for simplicity and accuracy's sake..
	private static void updateVote(User u, AbstractEntity e, Boolean isPositiveVote) {
		EntityManager em = emf.createEntityManager();
		
		String queryStr = "UPDATE Vote v SET v.thumbsUp=:thumbsUp, v.thumbsDown=:thumbsDown WHERE v.user.id=:userId AND v.entityId=:entityId AND v.entityType.id=:entityType";
		
		Query query = em.createQuery(queryStr);
		
		query.setParameter("thumbsUp", (isPositiveVote ? 1 : 0));
		query.setParameter("thumbsDown", (isPositiveVote ? 0 : 1));
		query.setParameter("userId", u.getId());
		query.setParameter("entityId", e.getId());
		query.setParameter("entityType", EntityTypeFactory.getEntityTypeFor(e).getId());
		
		em.getTransaction().begin();
		
		query.executeUpdate();
		
		em.getTransaction().commit();
		
		em.close();
	}
	
	private static void persistVote(User u, AbstractEntity e, Boolean isPositiveVote) {
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
