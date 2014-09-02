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

/**
 * Takes the current request URL and QueryString, and places them in
 * the Session attribute "originallyRequestedPage"
 */
@WebFilter("/MostRecentlyRequestedPageFilter")
public class MostRecentlyRequestedPageFilter extends AbstractFilter {

    public MostRecentlyRequestedPageFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			
			String url = req.getRequestURL().toString();
	
			if (thisRequestShouldBeSaved(url)) {
				String queryString = req.getQueryString();
				
				String prevRequestedPage = (String)req.getSession().getAttribute("originallyRequestedPage");
				req.getSession().setAttribute("originallyRequestedPage", (queryString == null ? url : url + "?" + queryString));
				req.getSession().setAttribute("previouslyRequestedPage", prevRequestedPage);
			}
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	private boolean thisRequestShouldBeSaved(String url) {
		String lowercaseurl = url.toLowerCase();
		
		return lowercaseurl.endsWith(".jsp") && !lowercaseurl.contains("login") && !lowercaseurl.contains("register.jsp") && !lowercaseurl.contains("/ajax/"); 
	}

}
