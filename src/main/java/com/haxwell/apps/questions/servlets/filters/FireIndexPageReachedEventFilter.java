package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.events.EventDispatcher;

@WebFilter("/FireIndexPageReachedEventFilter")
public class FireIndexPageReachedEventFilter extends AbstractFilter {

    public FireIndexPageReachedEventFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(FireIndexPageReachedEventFilter.class.getName());
		
		log.log(java.util.logging.Level.FINE, "In the ResetStateFilter::doFilter() method!");
		
		EventDispatcher.getInstance().fireEvent(((HttpServletRequest)request), EventConstants.INDEX_PAGE);
		
		// pass the request along the filter chain
		chain.doFilter(request, response);

		log.log(java.util.logging.Level.FINE, "Ending the ResetStateFilter::doFilter() method!");		
	}
}
