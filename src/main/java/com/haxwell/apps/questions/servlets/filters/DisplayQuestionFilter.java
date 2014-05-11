package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;	

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;

/**
 * Puts the things that DisplayQuestions.jsp needs in the session
 * 
 */
@WebFilter("/DisplayQuestionFilter")
public class DisplayQuestionFilter extends AbstractFilter {

	private static Logger log = LogManager.getLogger();
	
	public DisplayQuestionFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.entry();
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			Question question = QuestionManager.getQuestionById(req.getParameter("questionId"));
			
			if (question != null) {
				req.getSession().setAttribute(FilterConstants.ENTITY_ID_FILTER, question.getId());

				User u = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
				req.getSession().setAttribute(Constants.SHOULD_ALLOW_QUESTION_EDITING, QuestionManager.userCanEditThisQuestion(question, u));
			}
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		log.exit();
	}
}
