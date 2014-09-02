package com.haxwell.apps.questions.utils;

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

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.Vote;
import com.haxwell.apps.questions.factories.EntityTypeFactory;
import com.haxwell.apps.questions.factories.ManagerFactory;
import com.haxwell.apps.questions.managers.VoteManager;

public class VoteUtil {

	public static String registerVote(String voteDirection, String entityType, String entityId, User user) {
		AbstractEntity entity = ManagerFactory.getManager(entityType).getEntity(entityId);
		
		if (entity != null) {
			if (voteDirection.equals("down"))
				VoteManager.voteDown(user, entity);
			else if (voteDirection.equals("up"))
				VoteManager.voteUp(user, entity);
		}
		
		//writer.print(voteDirection + " " + entityKey + " ");
		return voteDirection + " vote registered successfully!";
	}

	public static boolean userHasVotedForThisEntity(User u, AbstractEntity e) {
		return VoteManager.getVote(u, e) != null;
	}
	
	public static String getVoteKey(AbstractEntity ae) {
		return ae.getId() + "-" +  EntityTypeFactory.getEntityTypeFor(ae).getId();
	}
	
	public static VoteData getMetricsOnListOfVotes(AbstractEntity e, List<Vote> list) {
		
		int up=0, down=0;
		
		for (Vote v: list) {
			if (v.isThumbsUp())
				up++; 
			else
				down++;
		}
		
		return new VoteData(e, up, down);
	}
	
	public static Integer getUpVotesForEntity(AbstractEntity e, Map map)
	{
		Integer rtn = 0;
		
		if (map != null) {
			String key = getVoteKey(e);
			
			VoteData data = (VoteData)map.get(key);
			
			rtn = data.upVoteCount;
		}
		
		return rtn;
	}

	public static Integer getDownVotesForEntity(AbstractEntity e, Map map)
	{
		Integer rtn = 0;
		
		if (map != null) {
			String key = getVoteKey(e);
			
			VoteData data = (VoteData)map.get(key);
			
			rtn = data.downVoteCount;
		}
		
		return rtn;
	}
}
