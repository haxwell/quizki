package com.haxwell.apps.questions.servlets.actions;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.QuestionManager;

public class SetTotalQuestionAndExamCountInSessionAction implements
		AbstractServletAction {

	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

			session.setAttribute(Constants.TOTAL_NUMBER_OF_QUESTIONS, QuestionManager.getNumberOfQuestionsInTotal());
			session.setAttribute(Constants.TOTAL_NUMBER_OF_EXAMS, ExamManager.getNumberOfExamsInTotal());
		}

		return 0;
	}

}
