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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.haxwell.apps.questions.entities.Question;

public abstract class AbstractQuestionTypeChecker {

	public Question question;
	
	protected AbstractQuestionTypeChecker()
	{
		
	}
	
	protected AbstractQuestionTypeChecker(Question q)
	{
		this.question = q;
	}
	
	public abstract boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues);
	
	protected Set<String> getChoiceIdsFromMapOfQuestionIdChoiceIdToValue(
			Map<String, String> mapOfFieldNamesToValues) {
		Map<String, String> mapOfChoiceIdsToValues = new HashMap<>();
		
		Set<String> keys = mapOfFieldNamesToValues.keySet();
		
		for (String compoundKey : keys) {
			// the compound key, at present looks like 'questionId,choiceId' or 'questionId,choiceId,fieldId'
			// we get the begin and end indexes to handle both cases.
			int beginIndex = compoundKey.indexOf(',')+1;
			int endIndex = compoundKey.indexOf(',', beginIndex);
			
			String key = compoundKey.substring(compoundKey.indexOf(',')+1, Math.max(endIndex, compoundKey.length()));
						
			mapOfChoiceIdsToValues.put(key, mapOfFieldNamesToValues.get(compoundKey));
		}
		
		return mapOfChoiceIdsToValues.keySet();
	}
}
