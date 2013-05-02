package com.haxwell.apps.questions.events.handlers;

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
		
		log.log(Level.INFO, ":: just set the current user entity on an exam object");
	}

	public String toString() {
		return "SetUserIdOnExamObjectHandler";
	}
}
