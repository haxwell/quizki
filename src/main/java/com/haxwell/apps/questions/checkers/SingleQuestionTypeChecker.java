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

public class SingleQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public SingleQuestionTypeChecker(Question q) {
		super(q);
	}
	
	/**
	 * Returns true if the map of field names contains (as a value) the field name of the choice on the given question that has 
	 * indicated that it is CORRECT.
	 * 
	 * @param mapOfQAndCIdsToValues a map of Strings to strings, the question,choiceId to the selected value
	 * @return
	 */
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfQAndCIdsToValues)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = true;
		
		for (Choice c : choices)
		{
			if (c.getIscorrect() > 0) 
				// if choice is correct, check that we have the choice in our answer map..
				rtn &= mapOfQAndCIdsToValues.containsKey(this.question.getId() + "," + c.getId());
			else
				// ..ensure that we DO NOT have this choice in our answer map; its incorrect!
				rtn &= !mapOfQAndCIdsToValues.containsKey(this.question.getId() + "," + c.getId());
		}
		
		return rtn;
	}
}
