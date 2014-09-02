package com.haxwell.apps.questions.validators;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.utils.StringUtil;

public class ExamValidator implements Validator {

	@Override
	public boolean validate(AbstractEntity e, Map<String, List<String>> errors) {
		boolean rtn = true;
		Exam exam = (Exam)e;
		List<String> errorsList = new ArrayList<String>();
		
		if (StringUtil.isNullOrEmpty(exam.getTitle())) {
			errorsList.add("The exam requires a title!");
			rtn = false;
		}
		
		if (exam.getQuestions().size() == 0) {
			errorsList.add("The exam requires some questions be assigned to it!");
			rtn = false;
		}
		
		if (!rtn)
			errors.put("examValidationErrors", errorsList);
		
		return rtn;
	}

}
