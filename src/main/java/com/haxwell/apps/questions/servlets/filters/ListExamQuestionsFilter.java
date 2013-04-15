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
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.servlets.actions.InitializeListOfExamQuestionsInSessionAction;

/**
 * Puts a list of questions appropriate for creating an exam in the request as an attribute.
 */
@WebFilter("/ListExamQuestionsFilter")
public class ListExamQuestionsFilter extends AbstractFilter {

	Logger log = Logger.getLogger(ListExamQuestionsFilter.class.getName());
	
	public ListExamQuestionsFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.log(Level.INFO, "...in ListQuestionsFilter()");

		new InitializeListOfExamQuestionsInSessionAction().doAction(request, response);
		
		if (((HttpServletRequest)request).getSession().getAttribute(Constants.MRU_FILTER_DIFFICULTY) == null)
			((HttpServletRequest)request).getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
