package com.haxwell.apps.questions.servlets;

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
//@ WebServlet("/BeginExamServlet")
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
		
		log.log(java.util.logging.Level.FINE, "In BeginExamServlet...");
		
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
