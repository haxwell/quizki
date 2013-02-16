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
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;

/**
 * Once the user goes to index.jsp (the Home page), certain state in the application is no longer
 * necessary, or useful. This filter removes those.
 * 
 */
@WebFilter("/ResetStateFilter")
public class ResetStateFilter extends AbstractFilter {

    public ResetStateFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(ResetStateFilter.class.getName());
		
		log.log(java.util.logging.Level.INFO, "In the ResetStateFilter::doFilter() method!");
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();
			
			session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, null);
			session.setAttribute(Constants.MRU_FILTER_TEXT, null);
			session.setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, null);
			session.setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, null);
			session.setAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED, null);
			session.setAttribute(Constants.IN_EDITING_MODE, null);
			
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, null);
			
			session.setAttribute(Constants.NEXT_SEQUENCE_NUMBER, 0);
			session.setAttribute("userSuppliedAnswerToStringQuestion", null);
			session.setAttribute(Constants.SHOULD_QUESTIONS_BE_DISPLAYED, null);
			session.setAttribute(Constants.SHOULD_ALLOW_QUESTION_EDITING, null);
			
			session.setAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_TAKEN, null);
			session.setAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_EDITED, null);
			
			session.setAttribute(Constants.LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS, null);
			session.setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, null);
			session.setAttribute(Constants.SHOULD_LOGIN_LINK_BE_DISPLAYED, null);
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);

		log.log(java.util.logging.Level.INFO, "Ending the ResetStateFilter::doFilter() method!");		
	}
}
