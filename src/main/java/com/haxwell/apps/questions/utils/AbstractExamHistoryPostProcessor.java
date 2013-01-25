package com.haxwell.apps.questions.utils;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractExamHistoryPostProcessor {

	public void processOnNext(HttpServletRequest request, ExamHistory eh)
	{

	}

	public void processOnPrevious(HttpServletRequest request, ExamHistory eh)
	{
		
	}
}
