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
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.QuestionManager;

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
		
		log.log(Level.INFO, "beginning InitializeSessionForCreatingAnExamFilter...");

		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

			User user = (User)session.getAttribute(Constants.CURRENT_USER_ENTITY);
			
			if (user == null) {
				// do something
			}
			else {
				long questionCount = QuestionManager.getNumberOfQuestionsCreatedByUser(user.getId());
				
				long examCount = ExamManager.getNumberOfExamsCreatedByUser(user.getId());
				
				session.setAttribute(Constants.NUMBER_OF_USER_CONTRIBUTED_QUESTIONS, questionCount);
				session.setAttribute(Constants.NUMBER_OF_USER_CONTRIBUTED_EXAMS, examCount);
			}
			
			session.setAttribute(Constants.SHOULD_ALL_QUESTIONS_BE_DISPLAYED, Boolean.FALSE);			
		}
		
		log.log(Level.INFO, "ending InitializeSessionForCreatingAnExamFilter...");
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
