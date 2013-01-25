package com.haxwell.apps.questions.utils;

import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;

public class StringExamHistoryPostProcessor extends
		AbstractExamHistoryPostProcessor {
	
	public void processOnNext(HttpServletRequest request, ExamHistory eh)
	{
		request.getSession().setAttribute(Constants.LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS, null);
	}

	// Assumes the previous question has already been set in ExamHistory
	public void processOnPrevious(HttpServletRequest request, ExamHistory eh)
	{
		ExamHistory.AnsweredQuestion aq = eh.getUserSuppliedAnswers(eh.getMostRecentlyUsedQuestion());
		
		StringBuffer previouslySuppliedAnswers = new StringBuffer(StringUtil.startJavascriptArray());
		StringUtil.addToJavascriptArray(previouslySuppliedAnswers, aq.getAnswers().get(Constants.STRING_QUESTION_TYPE_FIELDNAME));
		StringUtil.closeJavascriptArray(previouslySuppliedAnswers);
		
		request.getSession().setAttribute(Constants.LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS, previouslySuppliedAnswers.toString());
	}
}
