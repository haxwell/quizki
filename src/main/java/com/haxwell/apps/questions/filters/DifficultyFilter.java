package com.haxwell.apps.questions.filters;

import com.haxwell.apps.questions.entities.EntityWithADifficultyObjectBehavior;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;

public class DifficultyFilter implements ShouldRemoveAnObjectCommand<EntityWithADifficultyObjectBehavior> {
	private int difficulty;
	
	public DifficultyFilter(int filter) {
		this.difficulty = filter;
	}
	
	public boolean shouldRemove(EntityWithADifficultyObjectBehavior e) {
		boolean rtn = false;

		if (DifficultyUtil.convertToInt(e.getDifficulty()) > this.difficulty)
			rtn = true;

		return rtn;
	}
}