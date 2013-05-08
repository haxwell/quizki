package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets attributes on the HTTP header saying "Do not cache anything!!"
 * 
 * This is so that back button presses will come back to the server, allowing it to update Session settings.
 * 
 */
@WebFilter("/SetDoNotCacheAttributesFilter")
public class SetDoNotCacheAttributesFilter extends AbstractFilter {

    public SetDoNotCacheAttributesFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletResponse resp = ((HttpServletResponse)response);
			
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
			resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
			resp.setDateHeader("Expires", 0); // Proxies.
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
