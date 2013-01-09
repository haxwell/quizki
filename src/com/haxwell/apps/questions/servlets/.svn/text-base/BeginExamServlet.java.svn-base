package com.haxwell.apps.questions.servlets;

import java.io.IOException;	
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Servlet implementation class BeginExamServlet
 */
@WebServlet("/BeginExamServlet")
public class BeginExamServlet extends AbstractHttpServlet {
	
	Logger log = Logger.getLogger(BeginExamServlet.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BeginExamServlet() {
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
		
		log.log(java.util.logging.Level.SEVERE, "FINALLY! In BeginExamServlet!!!");
		
		String fwdPage = "/takeExam.jsp"; //"/takeExam?examId=5";
		
		
		String examId = request.getParameter("examId");
		String topicId = request.getParameter("topicId");
		
		if (!StringUtil.isNullOrEmpty(examId))
			fwdPage += "?examId=" + examId;
		else if (!StringUtil.isNullOrEmpty(topicId))
			fwdPage += "?topicId=" + topicId;
		
		redirectToJSP(request, response, fwdPage);
	}
}
