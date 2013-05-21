package com.haxwell.apps.questions.utils;

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.Vote;
import com.haxwell.apps.questions.factories.EntityTypeFactory;
import com.haxwell.apps.questions.managers.VoteManager;

public class VoteUtil {

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
