package com.haxwell.apps.questions.checkers;

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

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public class PhraseQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public PhraseQuestionTypeChecker(Question q) {
		super(q);
	}
	
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfQAndCIDsToValues)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = false;
		
		if (mapOfQAndCIDsToValues == null || mapOfQAndCIDsToValues.size() == 0)
			throw new IllegalArgumentException("Cannot pass a null or empty map to the PhraseQuestionTypeChecker");

		if (mapOfQAndCIDsToValues.size() > 1)
			throw new IllegalArgumentException("For Questions of type Phrase, there should only be one answer supplied when taking the exam.");

		String answer = mapOfQAndCIDsToValues.values().iterator().next().toLowerCase();
		
		for (Choice c : choices)
		{
			if (!rtn)
				rtn = c.getText().toLowerCase().equals(answer);
		}
		
		return rtn;
	}

}
