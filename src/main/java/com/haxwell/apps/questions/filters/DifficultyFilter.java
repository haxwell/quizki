package com.haxwell.apps.questions.filters;

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

import com.haxwell.apps.questions.entities.EntityWithADifficultyObjectBehavior;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;

public class DifficultyFilter implements ShouldRemoveAnObjectCommand<EntityWithADifficultyObjectBehavior> {
	public static final long DIFFICULTY_IS_EQUAL = 2334l;
	public static final long DIFFICULTY_IS_GREATER_THAN = 8338l;
	public static final long DIFFICULTY_IS_LESS_THAN = 6990l;
	
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
		else if (this.criteria == DIFFICULTY_IS_LESS_THAN && DifficultyUtil.convertToInt(e.getDifficulty()) < this.difficulty)
			rtn = true;
		else if (this.criteria == DIFFICULTY_IS_EQUAL && DifficultyUtil.convertToInt(e.getDifficulty()) != this.difficulty)
			rtn = true;
		
		return rtn;
	}
}