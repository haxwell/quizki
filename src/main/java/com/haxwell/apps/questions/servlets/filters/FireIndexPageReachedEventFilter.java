package com.haxwell.apps.questions.servlets.filters;

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

import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.events.EventDispatcher;

@WebFilter("/FireIndexPageReachedEventFilter")
public class FireIndexPageReachedEventFilter extends AbstractFilter {

	private static Logger log = LogManager.getLogger();
	
    public FireIndexPageReachedEventFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.entry();
		
		EventDispatcher.getInstance().fireEvent(((HttpServletRequest)request), EventConstants.INDEX_PAGE);
		
		// pass the request along the filter chain
		chain.doFilter(request, response);

		log.exit();
	}
}
