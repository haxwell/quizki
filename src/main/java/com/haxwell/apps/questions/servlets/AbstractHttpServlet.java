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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AbstractHttpServlet
 */
public abstract class AbstractHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(AbstractHttpServlet.class.getName());

    protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response, String jsp) throws IOException, ServletException
	{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jsp);
		dispatcher.forward(request,response);
	}
    
    protected void redirectToJSP(HttpServletRequest request, HttpServletResponse response, String jsp) throws IOException, ServletException
	{
    	String path = /*Constants.APP_URL_ROOT + */ jsp;
    	
    	log.log(Level.FINER, "Attempting redirect to : " + path);
    	
    	response.sendRedirect(path);
	}

	protected String getIdAppendedToRequestParameter(HttpServletRequest request, String paramName) {
		String str = request.getParameter(paramName);
		
		return str == null ? null : str.substring(str.indexOf('_')+1);		
	}
}
