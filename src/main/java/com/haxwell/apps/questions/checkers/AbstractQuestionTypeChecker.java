package com.haxwell.apps.questions.checkers;

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
