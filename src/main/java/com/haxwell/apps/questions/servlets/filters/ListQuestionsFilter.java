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

import com.haxwell.apps.questions.servlets.actions.InitializeListOfQuestionsInSessionAction;

/**
 * Puts a list of all questions in the request as an attribute.
 */
@WebFilter("/ListQuestionsFilter")
public class ListQuestionsFilter extends AbstractFilter {

	Logger log = Logger.getLogger(ListQuestionsFilter.class.getName());
	
	public ListQuestionsFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.log(Level.INFO, "...in ListQuestionsFilter()");

		new InitializeListOfQuestionsInSessionAction().doAction(request, response);

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
