package com.haxwell.apps.questions.utils;

import java.util.Collection;
import java.util.Map;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.utils.ExamHistory.AnsweredQuestion;

public class AnsweredQuestionFieldnameCollectionReturner {

	//TODO: think of a better name.. its not always returning the fieldnames (the keys).. in fact, thats the point of even having this class..
	public static Collection<String> getFieldnameCollection(Map<String, String> map, int questionType) {

		Collection<String> rtn = null;
		
		// decided to implement this as a static class using IFs because its not doing a whole lot..
		// if it were doing more interesting things, like the ExamHistoryPostProcessors, 
		// or QuestionTypeCheckers, I could see it as its own set of classes, with a factory and all that..
		
		if (map == null)
			throw new IllegalArgumentException();
		
		if (questionType == TypeConstants.SINGLE)
			rtn = map.values();
		else 
			rtn = map.keySet();
		
		return rtn;
	}
	
	public static Collection<String> getFieldnameCollection(AnsweredQuestion aq)
	{
		Collection<String> rtn = null;
		
		if (aq != null)
			rtn = getFieldnameCollection(aq.getAnswers(), (int)aq.getQuestion().getQuestionType().getId());
		
		return rtn;
	}
}
