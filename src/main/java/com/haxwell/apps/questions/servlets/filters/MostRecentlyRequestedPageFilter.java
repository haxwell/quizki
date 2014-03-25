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
