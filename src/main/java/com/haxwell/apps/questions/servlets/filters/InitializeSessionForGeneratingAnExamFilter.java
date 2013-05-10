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
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForGeneratingAnExamFilter")
public class InitializeSessionForGeneratingAnExamFilter extends AbstractFilter {

    public InitializeSessionForGeneratingAnExamFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(InitializeSessionForGeneratingAnExamFilter.class.getName());
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

			session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);
			session.setAttribute(Constants.MRU_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM, Constants.DEFAULT_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM);
			
			log.log(Level.FINE, "InitializeSessionForGeneratingAnExamFilter just finished...");
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
