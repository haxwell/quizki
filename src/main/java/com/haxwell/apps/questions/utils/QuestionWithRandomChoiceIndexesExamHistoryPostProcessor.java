package com.haxwell.apps.questions.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;

/**
 * Preserves the indexes that have been randomly assigned to choices on a question,
 * so that the indexes are generated once, and each next time, the same indexes are 
 * used. Otherwise, the indexes would be different each time the question was displayed
 * and the choices would then be in a different position each time. 
 * 
 * @author johnathanj
 */
public class QuestionWithRandomChoiceIndexesExamHistoryPostProcessor extends
		AbstractExamHistoryPostProcessor {

	public void beforeQuestionDisplayed(HttpServletRequest request, ExamHistory eh) {
		ExamHistory.AnsweredQuestion aq = eh.getUserSuppliedAnswers(eh.getMostRecentlyUsedQuestion());
		
		if (aq != null) {
			Object o = aq.getMetadata().get(Constants.LIST_OF_RANDOM_CHOICE_INDEXES);
			
			if (o != null)
				request.getSession().setAttribute(Constants.LIST_OF_RANDOM_CHOICE_INDEXES, o);
			
			request.getSession().setAttribute(Constants.SHOULD_GENERATE_NEW_RANDOM_CHOICE_INDEXES, Boolean.FALSE);
			
			List<String> fieldnames = QuestionUtil.getFieldnamesForChoices(eh.getMostRecentlyUsedQuestion());
			
			StringBuffer previouslySuppliedAnswers = new StringBuffer(StringUtil.startJavascriptArray());
			
			for (String str: fieldnames) {
				StringUtil.addToJavascriptArray(previouslySuppliedAnswers, aq.getAnswers().get(str));
			}
	
			StringUtil.closeJavascriptArray(previouslySuppliedAnswers);
			
			request.getSession().setAttribute(Constants.LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS, previouslySuppliedAnswers.toString());
		}
	}
	
	public void afterQuestionDisplayed(HttpServletRequest request, ExamHistory eh) {
		String randomNumberIndexes = (String)request.getSession().getAttribute(Constants.LIST_OF_RANDOM_CHOICE_INDEXES);
		ExamHistory.AnsweredQuestion aq = eh.getUserSuppliedAnswers(eh.getMostRecentlyUsedQuestion());
		
		if (aq != null)
		{
			aq.getMetadata().put(Constants.LIST_OF_RANDOM_CHOICE_INDEXES, randomNumberIndexes);
			request.getSession().setAttribute(Constants.LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS, null); // TODO: should this be done here? Perhaps better in a filter?			
		}
	}
	
	public void afterQuestionDisplayedWithoutBeingAnswered(HttpServletRequest request, ExamHistory examHistory) {
		request.getSession().setAttribute(Constants.SHOULD_GENERATE_NEW_RANDOM_CHOICE_INDEXES, Boolean.FALSE);
	}
}
