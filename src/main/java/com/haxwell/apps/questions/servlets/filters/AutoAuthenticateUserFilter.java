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

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.servlets.actions.LoginWithTheRequestCredentialsAction;

/**
 * Auto log the user in.. so he doesn't have to keep clicking on the log in button for every little jsp change.
 */
@WebFilter("/AutoAuthenticateUserFilter")
public class AutoAuthenticateUserFilter extends AbstractFilter {

    public AutoAuthenticateUserFilter() { /* Do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest)request).getSession();

		if (session.getAttribute(Constants.IN_PRODUCTION_MODE) == null) {
			// TODO: get these from Spring instead
//			request.setAttribute("username", "johnathan");
//			request.setAttribute("password", "password");
//
//			new LoginWithTheRequestCredentialsAction().doAction(request, response);
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
