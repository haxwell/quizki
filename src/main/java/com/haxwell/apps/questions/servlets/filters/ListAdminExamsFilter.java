package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.haxwell.apps.questions.servlets.actions.InitializeListOfExamsInSessionAction;

/**
 * Puts a list of all exams in the request as an attribute.
 */
@WebFilter("/ListAdminExamsFilter")
public class ListAdminExamsFilter extends AbstractFilter {

	Logger log = Logger.getLogger(ListAdminExamsFilter.class.getName());
	
    public ListAdminExamsFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		new InitializeListOfExamsInSessionAction().doAction(request, response, InitializeListOfExamsInSessionAction.GET_ALL_EXAMS);
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
