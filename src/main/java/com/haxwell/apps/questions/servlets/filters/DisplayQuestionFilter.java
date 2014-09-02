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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;

/**
 * Puts the things that DisplayQuestions.jsp needs in the session
 * 
 */
@WebFilter("/DisplayQuestionFilter")
public class DisplayQuestionFilter extends AbstractFilter {

	private static Logger log = LogManager.getLogger();
	
	public DisplayQuestionFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.entry();
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			Question question = QuestionManager.getQuestionById(req.getParameter("questionId"));
			
			if (question != null) {
				req.getSession().setAttribute(FilterConstants.ENTITY_ID_FILTER, question.getId());

				User u = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
				req.getSession().setAttribute(Constants.SHOULD_ALLOW_QUESTION_EDITING, QuestionManager.userCanEditThisQuestion(question, u));
			}
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		log.exit();
	}
}
