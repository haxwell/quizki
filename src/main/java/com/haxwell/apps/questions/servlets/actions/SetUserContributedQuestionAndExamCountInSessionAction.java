package com.haxwell.apps.questions.servlets.actions;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.QuestionManager;

public class SetUserContributedQuestionAndExamCountInSessionAction implements
		AbstractServletAction {

	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

			User user = (User)session.getAttribute(Constants.CURRENT_USER_ENTITY);
			
			if (user == null) {
				// do something
			}
			else {
				long questionCount = QuestionManager.getNumberOfQuestionsCreatedByUser(user.getId());
				
				long examCount = ExamManager.getNumberOfExamsCreatedByUser(user.getId());
				
				session.setAttribute(Constants.NUMBER_OF_USER_CONTRIBUTED_QUESTIONS, questionCount);
				session.setAttribute(Constants.NUMBER_OF_USER_CONTRIBUTED_EXAMS, examCount);
			}
		}

		return 0;
	}

}
