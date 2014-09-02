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

import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.managers.ExamManager;

/**
 * Creates a new, clean Exam instance and sets it in the session as the current exam.
 * 
 * @author johnathanj
 */
public class InitializeNewExamInSessionAction implements AbstractServletAction {

	Logger log = Logger.getLogger(InitializeNewExamInSessionAction.class.getName());
	
	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);

			Exam exam = ExamManager.newExam();
			
			EventDispatcher.getInstance().fireEvent(req, EventConstants.NEW_EXAM_CREATED, exam);
			
			req.getSession().setAttribute(Constants.CURRENT_EXAM, exam);
		}
		
		return 0;
	}
}

