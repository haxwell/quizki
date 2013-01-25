package com.haxwell.apps.questions.checkers;

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public abstract class AbstractQuestionTypeChecker {

	public Question question;
	
	protected AbstractQuestionTypeChecker()
	{
		
	}
	
	protected AbstractQuestionTypeChecker(Question q)
	{
		this.question = q;
	}
	
	/**
	 * Returns true if an answer (correct or not, complete or not) has been given.
	 * 
	 * @param mapOfFieldNamesToValues
	 * @return
	 */
	public boolean questionHasBeenAnswered(Map<String, String> mapOfFieldNamesToValues)
	{
		return mapOfFieldNamesToValues.size() > 0;
	}
	
	public abstract boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues);
	
	public abstract List<String> getKeysToPossibleUserSelectedAttributesInTheRequest();	
}
