package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.entities.AbstractEntity;

public class VoteData {

	public AbstractEntity e;
	public int upVoteCount;
	public int downVoteCount;
	
	public VoteData(AbstractEntity e, int upVoteCount, int downVoteCount)
	{
		this.e = e;
		this.upVoteCount = upVoteCount;
		this.downVoteCount = downVoteCount;
	}
	
}
