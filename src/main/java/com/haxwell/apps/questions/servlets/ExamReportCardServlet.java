package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;

/**
 * Servlet implementation class ExamServlet
 */
@WebServlet("/ExamReportCardServlet")
public class ExamReportCardServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 135739L;
	private Logger log = Logger.getLogger(ExamReportCardServlet.class.getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExamReportCardServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String comment = request.getParameter("examFeedbackTextarea");
		Exam e = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);
		User u = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		
		ExamManager.addExamFeedback(e, u, comment);
		
//		redirectToJSP(request, response, fwdPage);
	}
}
