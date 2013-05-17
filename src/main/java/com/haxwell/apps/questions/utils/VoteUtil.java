package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.VoteManager;

public class VoteUtil {

	public static boolean userHasVotedForThisEntity(User u, AbstractEntity e) {
		return VoteManager.getVote(u, e) != null;
	}
}
