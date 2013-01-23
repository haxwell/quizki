package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Question;

public class ExamHistoryPostProcessorFactory {

	public static AbstractExamHistoryPostProcessor get(Question q)
	{
		if (q.getQuestionType().getId() == TypeConstants.SEQUENCE)
			return new SequenceExamHistoryPostProcessor();
		
		return null;
	}
}
