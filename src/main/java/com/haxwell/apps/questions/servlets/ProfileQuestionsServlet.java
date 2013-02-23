package com.haxwell.apps.questions.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/secured/ProfileQuestionsServlet")
public class ProfileQuestionsServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileQuestionsServlet() {
        super();
        // TODO Auto-generated constructor stub
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

		String fwdPage = "/secured/profile.jsp?tab=2";
		
		String containsFilter = request.getParameter("containsFilter");
		String topicFilter = request.getParameter("topicFilter");
		String questionTypeFilter = request.getParameter("questionTypeFilter");
		String difficultyFiter = request.getParameter("difficultyFilter");
		
		forwardToJSP(request, response, fwdPage);
	}
}
