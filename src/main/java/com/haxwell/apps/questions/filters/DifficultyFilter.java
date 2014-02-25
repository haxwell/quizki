package com.haxwell.apps.questions.filters;

import com.haxwell.apps.questions.entities.EntityWithADifficultyObjectBehavior;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;

public class DifficultyFilter implements ShouldRemoveAnObjectCommand<EntityWithADifficultyObjectBehavior> {
	public static final long DIFFICULTY_IS_EQUAL = 2334l;
	public static final long DIFFICULTY_IS_GREATER_THAN = 8338l;
	
	private long criteria = DIFFICULTY_IS_GREATER_THAN;
	private long difficulty;
	
	public DifficultyFilter(long filter) {
		this.difficulty = filter;
	}
	
	public DifficultyFilter(long filter, long criteria) {
		this.criteria = criteria;
		this.difficulty = filter;
	}
	
	public boolean shouldRemove(EntityWithADifficultyObjectBehavior e) {
		boolean rtn = false;

		if (this.criteria == DIFFICULTY_IS_GREATER_THAN && DifficultyUtil.convertToInt(e.getDifficulty()) > this.difficulty)
			rtn = true;
		else if (this.criteria == DIFFICULTY_IS_EQUAL && DifficultyUtil.convertToInt(e.getDifficulty()) != this.difficulty)
			rtn = true;
		
		return rtn;
	}
}