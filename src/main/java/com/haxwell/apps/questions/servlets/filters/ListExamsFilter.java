package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.Collection;
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
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.servlets.actions.InitializeListOfExamsInSessionAction;
import com.haxwell.apps.questions.utils.FilterUtil;

/**
 * Puts a list of all exams in the request as an attribute.
 */
@WebFilter("/ListExamsFilter")
public class ListExamsFilter extends AbstractFilter {

	Logger log = Logger.getLogger(ListExamsFilter.class.getName());
	
    public ListExamsFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		User user = (User)((HttpServletRequest)request).getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		boolean getAllExams = (user != null && ExamManager.getNumberOfExamsCreatedByUser(user.getId()) < 1);
		
		new InitializeListOfExamsInSessionAction().doAction(request, response, getAllExams);
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
