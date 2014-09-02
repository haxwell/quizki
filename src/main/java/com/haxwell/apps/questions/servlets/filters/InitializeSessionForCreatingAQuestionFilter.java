package com.haxwell.apps.questions.servlets.filters;

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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.utils.UserUtil;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForCreatingAQuestionFilter")
public class InitializeSessionForCreatingAQuestionFilter extends AbstractFilter {

	private static Logger log = LogManager.getLogger();
	
    public InitializeSessionForCreatingAQuestionFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.entry();
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

			String questionId = req.getParameter("questionId");
			
			if (questionId != null) {
				Question q = QuestionManager.getQuestionById(questionId);
				
				session.setAttribute(Constants.CURRENT_QUESTION, q);
				session.setAttribute(FilterConstants.ENTITY_ID_FILTER, questionId);
				
				if (q != null) {
					User user = (User)session.getAttribute(Constants.CURRENT_USER_ENTITY);

					if (user == null || ((q.getUser().getId() != user.getId()) && !UserUtil.isAdministrator(user))) {
						request.setAttribute("doNotAllowEntityEditing", Boolean.TRUE);
					}
					else {
						session.setAttribute(Constants.IN_EDITING_MODE, Boolean.TRUE);
					}
				}
				else {
					log.trace("QuestionId ["+ questionId + "] was requested.. That ID is invalid.");
				}
			}
		}

		log.exit();
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
