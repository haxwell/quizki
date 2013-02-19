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
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForCreatingAQuestionFilter")
public class InitializeSessionForCreatingAQuestionFilter extends AbstractFilter {

    public InitializeSessionForCreatingAQuestionFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(InitializeSessionForCreatingAQuestionFilter.class.getName());
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

			session.setAttribute(Constants.URL_TO_REDIRECT_TO_WHEN_BACK_BUTTON_PRESSED, null);
			
			boolean currentQuestionHasBeenPersisted = (session.getAttribute(Constants.CURRENT_QUESTION_HAS_BEEN_PERSISTED) != null);
			
			if (currentQuestionHasBeenPersisted) {
				setCurrentQuestion(req, Constants.CURRENT_QUESTION, null);
				
				session.setAttribute(Constants.CURRENT_QUESTION_HAS_BEEN_PERSISTED, null);
				session.setAttribute(Constants.IN_EDITING_MODE, null);
			}
			
			String questionId = req.getParameter("questionId");
			
			if (questionId != null) {
				Question q = QuestionManager.getQuestionById(questionId);
				User user = (User)session.getAttribute(Constants.CURRENT_USER_ENTITY);
				
				if (user == null || q.getUser().getId() != user.getId()) {
					request.setAttribute("doNotAllowEntityEditing", Boolean.TRUE);
				}
				else {
					session.setAttribute(Constants.CURRENT_QUESTION, q);
					session.setAttribute(Constants.IN_EDITING_MODE, Boolean.TRUE);
				}
			}
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
