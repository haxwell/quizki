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
import java.util.Set;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

public class SetQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public SetQuestionTypeChecker(Question q) {
		super(q);
	}
		
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues)
	{
		Set<String> selectedChoiceIds = getChoiceIdsFromMapOfQuestionIdChoiceIdToValue(mapOfFieldNamesToValues);
		
		boolean rtn = true;
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		String dynamicData = (String)this.question.getDynamicData("choiceIdsToBeAnswered");
		String fieldNum = StringUtil.getField(2, ";", ";", dynamicData);

		for (Choice c : choices)
		{
			long choiceId = c.getId();

			if (selectedChoiceIds.contains(choiceId+","+fieldNum)) {
				String text = null;
				
				if (StringUtil.equals(fieldNum, "-1")) {
					text = c.getText();
				}
				else {
					text = StringUtil.getField(Integer.parseInt(fieldNum), "[[", "]]", c.getText());
				}

				String str = mapOfFieldNamesToValues.get(this.question.getId() + "," + choiceId + "," + fieldNum); 
				rtn &= str.toLowerCase().equals(text.toLowerCase());
			}
		}
		
		return rtn;
	}
}
