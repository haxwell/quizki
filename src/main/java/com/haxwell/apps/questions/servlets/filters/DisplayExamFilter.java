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
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.managers.ExamManager;

/**
 * Puts the things that DisplayExam.jsp needs in the session
 * 
 */
@WebFilter("/DisplayExamFilter")
public class DisplayExamFilter extends AbstractFilter {

	Logger log = Logger.getLogger(DisplayExamFilter.class.getName());
	
	public DisplayExamFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.log(Level.FINE, "...in DisplayExamFilter()");
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			Exam exam = ExamManager.getExam(Long.parseLong(req.getParameter("examId")));
			
			req.getSession().setAttribute(Constants.CURRENT_EXAM, exam);
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		log.log(Level.FINE, "...Leaving DisplayExamFilter");
	}
}