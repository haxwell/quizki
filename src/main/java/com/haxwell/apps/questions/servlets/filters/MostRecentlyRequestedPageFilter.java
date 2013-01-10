package com.haxwell.apps.questions.servlets.filters;

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
				String temp = "http://localhost:8080/quizki";
				String queryString = req.getQueryString();
				
				String str = url.replace(temp, "");				
				req.getSession().setAttribute("originallyRequestedPage", (queryString == null ? str : str + "?" + queryString));
			}
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	private boolean thisRequestShouldBeSaved(String url) {
		boolean b = !url.toLowerCase().contains("login");
		b &= !url.endsWith(".css");
		b &= !url.toLowerCase().endsWith("servlet");
		
		return b;
	}

}
