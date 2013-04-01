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
		
		log.log(Level.INFO, "beginning InitializeSessionForProfilePagesFilter...");

		new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
		new SetListOfUserNotificationsInSessionAction().doAction(request, response);
		
		log.log(Level.INFO, "ending InitializeSessionForProfilePagesFilter...");
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
