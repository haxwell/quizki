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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haxwell.apps.questions.constants.Constants;

/**
 * Ensure the user is logged in.. if not, forward to the login page
 */
@WebFilter("/UserIsAuthenticatedFilter")
public class UserIsAuthenticatedFilter extends AbstractFilter {

	private static Logger log = LogManager.getLogger();

	public UserIsAuthenticatedFilter() { /* Do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.entry();
		
		HttpSession session = ((HttpServletRequest)request).getSession();
		
		if (session.getAttribute(Constants.CURRENT_USER_ENTITY) == null) {
			log.trace("CURRENT_USER_ENTITY is null.");
			((HttpServletResponse)response).sendRedirect(Constants.LOGIN_JSP_URL);
		}
		else
			log.trace("CURRENT_USER_ENTITY is NOT null.");
		
		log.exit();
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
