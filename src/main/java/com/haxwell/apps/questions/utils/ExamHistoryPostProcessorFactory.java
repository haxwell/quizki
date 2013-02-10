package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Question;

public class ExamHistoryPostProcessorFactory {

	public static AbstractExamHistoryPostProcessor get(Question q)
	{
		// SINGLE, MULTIPLE, and SEQUENCE questions all have random choice indexes.. 
		AbstractExamHistoryPostProcessor rtn = new QuestionWithRandomChoiceIndexesExamHistoryPostProcessor();
		
		// STRING has only one input field, so random-ness does not apply.. it needs its own processor..
		if (q.getQuestionType().getId() == TypeConstants.STRING)
			rtn = new StringExamHistoryPostProcessor();
		
		return rtn;
	}
}
