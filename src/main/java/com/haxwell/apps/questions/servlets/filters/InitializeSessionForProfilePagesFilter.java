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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.haxwell.apps.questions.servlets.actions.SetListOfUserNotificationsInSessionAction;
import com.haxwell.apps.questions.servlets.actions.SetUserContributedQuestionAndExamCountInSessionAction;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForProfilePages")
public class InitializeSessionForProfilePagesFilter extends AbstractFilter {

    public InitializeSessionForProfilePagesFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(InitializeSessionForProfilePagesFilter.class.getName());
		
		log.log(Level.FINE, "beginning InitializeSessionForProfilePagesFilter...");
		
		new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
		new SetListOfUserNotificationsInSessionAction().doAction(request, response);
		
		log.log(Level.FINE, "ending InitializeSessionForProfilePagesFilter...");
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
