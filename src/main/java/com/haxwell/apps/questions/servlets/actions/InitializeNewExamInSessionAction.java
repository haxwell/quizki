package com.haxwell.apps.questions.servlets.actions;

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

