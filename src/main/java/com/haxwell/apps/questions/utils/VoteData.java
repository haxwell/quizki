package com.haxwell.apps.questions.utils;

import java.io.Serializable;

import com.haxwell.apps.questions.entities.AbstractEntity;

public class VoteData implements Serializable {

	private static final long serialVersionUID = 123409L;
	
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
