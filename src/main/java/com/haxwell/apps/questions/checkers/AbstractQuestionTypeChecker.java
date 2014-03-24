package com.haxwell.apps.questions.checkers;

import java.util.Map;

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
}
