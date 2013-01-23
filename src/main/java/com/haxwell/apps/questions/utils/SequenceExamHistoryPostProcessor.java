package com.haxwell.apps.questions.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;

public class SequenceExamHistoryPostProcessor extends
		AbstractExamHistoryPostProcessor {

	public void processOnNext(HttpServletRequest request, ExamHistory eh)
	{
		String randomNumberIndexes = (String)request.getSession().getAttribute(Constants.LIST_OF_RANDOM_CHOICE_INDEXES);
		ExamHistory.AnsweredQuestion aq = eh.getUserSuppliedAnswers(eh.getMostRecentlyUsedQuestion());
		
		aq.getMetadata().put(Constants.LIST_OF_RANDOM_CHOICE_INDEXES, randomNumberIndexes);
		
		request.getSession().setAttribute(Constants.LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS, null); // TODO: should this be done here? Perhaps better in a filter?		
	}
	
	// Assumes the previous question has already been set in ExamHistory
	public void processOnPrevious(HttpServletRequest request, ExamHistory eh)
	{
		ExamHistory.AnsweredQuestion aq = eh.getUserSuppliedAnswers(eh.getMostRecentlyUsedQuestion());
		
		request.getSession().setAttribute(Constants.LIST_OF_RANDOM_CHOICE_INDEXES, aq.getMetadata().get(Constants.LIST_OF_RANDOM_CHOICE_INDEXES));
		request.getSession().setAttribute(Constants.SHOULD_GENERATE_NEW_RANDOM_CHOICE_INDEXES, Boolean.FALSE);
		
		aq.getAnswers();
		
		List<String> fieldnames = QuestionUtil.getFieldnamesForChoices(eh.getMostRecentlyUsedQuestion());
		
		StringBuffer previouslySuppliedAnswers = new StringBuffer(StringUtil.startJavascriptArray());
		
		for (String str: fieldnames) {
			StringUtil.addToJavascriptArray(previouslySuppliedAnswers, aq.getAnswers().get(str));
		}

		StringUtil.closeJavascriptArray(previouslySuppliedAnswers);
		
		request.getSession().setAttribute(Constants.LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS, previouslySuppliedAnswers.toString());
	}
}
