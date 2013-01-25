package com.haxwell.apps.questions.utils;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractExamHistoryPostProcessor {

	public void beforeQuestionDisplayed(HttpServletRequest request, ExamHistory eh)
	{
		
	}
	
	public void afterQuestionDisplayed(HttpServletRequest request, ExamHistory eh)
	{
		
	}

	public void afterQuestionDisplayedWithoutBeingAnswered(HttpServletRequest request, ExamHistory examHistory) 
	{

	}
}
