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

import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TypeUtil;

public class QuestionFilter implements ShouldRemoveAnObjectCommand<Question> {
	private String filterText;
	
	public QuestionFilter(String filterText) {
		this.filterText = filterText;
	}
	
	public boolean shouldRemove(Question q) {
		boolean rtn = false;
		String upperCaseFilterText = filterText.toUpperCase();
		
		if (!StringUtil.isNullOrEmpty(q.getTextWithoutHTML())) {
			rtn = !q.getTextWithoutHTML().toUpperCase().contains(upperCaseFilterText); 
		}
		else {
			rtn = !q.getDescription().toUpperCase().contains(upperCaseFilterText);
		}

		return rtn;
	}
}
