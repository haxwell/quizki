package com.haxwell.apps.questions.utils;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractExamHistoryPostProcessor {

	public abstract void processOnNext(HttpServletRequest request, ExamHistory eh);
	public abstract void processOnPrevious(HttpServletRequest request, ExamHistory eh);
	
}
