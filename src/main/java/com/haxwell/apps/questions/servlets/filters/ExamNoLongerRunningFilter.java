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
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/ExamNoLongerRunningFilter")
public class ExamNoLongerRunningFilter extends AbstractFilter {

    public ExamNoLongerRunningFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(ExamNoLongerRunningFilter.class.getName());
		
		log.log(java.util.logging.Level.INFO, "In the ExamNoLongerRunningFilter::doFilter() method!");
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();
			
			setCurrentQuestion(req, Constants.CURRENT_QUESTION, null);			

			session.setAttribute(Constants.EXAM_IN_PROGRESS, null);
			session.setAttribute(Constants.CURRENT_EXAM_HISTORY, null);
			session.setAttribute(Constants.CURRENT_EXAM, null);
			session.setAttribute(Constants.CURRENT_QUESTION_NUMBER, null);
			session.setAttribute(Constants.TOTAL_POTENTIAL_QUESTIONS, null);
			session.setAttribute("listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion", null);
			
			log.log(java.util.logging.Level.INFO, "Exam set to 'not in progress'.... exam state completely reset!");
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		log.log(java.util.logging.Level.INFO, "Ending the ExamNoLongerRunningFilter::doFilter() method!");		
	}
}
