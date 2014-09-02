package com.haxwell.apps.questions.servlets.actions;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

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
