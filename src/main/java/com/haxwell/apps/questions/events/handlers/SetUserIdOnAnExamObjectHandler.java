package com.haxwell.apps.questions.events.handlers;

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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;

public class SetUserIdOnAnExamObjectHandler implements IObjectEventHandler {
	
	Logger log = Logger.getLogger(SetUserIdOnAnExamObjectHandler.class.getName());
	
	@Override
	public void setObject(Object o) {
		// do nothing
	}
	
	@Override
	public void execute(HttpServletRequest req, Object o) {
		Exam exam = (Exam)o;

		User user = (User)req.getSession().getAttribute("currentUserEntity");
		exam.setUser(user);
		
		log.log(Level.FINER, ":: just set the current user entity on an exam object");
	}

	public String toString() {
		return "SetUserIdOnExamObjectHandler";
	}
}
