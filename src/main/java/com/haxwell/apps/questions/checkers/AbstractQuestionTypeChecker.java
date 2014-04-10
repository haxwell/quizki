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
			String key = compoundKey.substring(compoundKey.indexOf(',')+1);
			
			mapOfChoiceIdsToValues.put(key, mapOfFieldNamesToValues.get(compoundKey));
		}
		
		return mapOfChoiceIdsToValues.keySet();
	}
}
