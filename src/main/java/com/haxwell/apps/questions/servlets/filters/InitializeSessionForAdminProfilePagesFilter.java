package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.haxwell.apps.questions.servlets.actions.SetTotalQuestionAndExamCountInSessionAction;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForAdminProfilePages")
public class InitializeSessionForAdminProfilePagesFilter extends AbstractFilter {

    public InitializeSessionForAdminProfilePagesFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(InitializeSessionForAdminProfilePagesFilter.class.getName());
		
		log.log(Level.FINE, "beginning InitializeSessionForAdminProfilePagesFilter...");

		new SetTotalQuestionAndExamCountInSessionAction().doAction(request, response);
		
		log.log(Level.FINE, "ending InitializeSessionForAdminProfilePagesFilter...");
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
