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

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;

public class ExamFilter implements ShouldRemoveAnObjectCommand<Exam> {
	private String filterText;
	
	public ExamFilter(String filterText) {
		this.filterText = filterText;
	}
	
	public boolean shouldRemove(Exam e) {
		boolean rtn = false;

		rtn = !e.getTitle().contains(filterText);

		return rtn;
	}
}
