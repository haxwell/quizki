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
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.servlets.actions.SetListOfUserNotificationsInSessionAction;
import com.haxwell.apps.questions.servlets.actions.SetTotalQuestionAndExamCountInSessionAction;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForAdminProfilePages")
public class InitializeSessionForAdminProfilePagesFilter extends AbstractFilter {

    public InitializeSessionForAdminProfilePagesFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(InitializeSessionForAdminProfilePagesFilter.class.getName());
		
		log.log(Level.FINE, "beginning InitializeSessionForAdminProfilePagesFilter...");

		HttpServletRequest req = ((HttpServletRequest)request);
		HttpSession session = req.getSession();
		
		if (session.getAttribute(Constants.DO_NOT_INITIALIZE_PROFILE_MRU_SETTINGS) == null) {
			session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);
			session.setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, Constants.DEFAULT_PAGINATION_PAGE_SIZE);
			session.setAttribute(Constants.MRU_FILTER_QUESTION_TYPE, TypeConstants.ALL_TYPES);
			session.setAttribute(Constants.SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS, Constants.ALL_ITEMS);
		}
		
		new SetTotalQuestionAndExamCountInSessionAction().doAction(request, response);
		
		log.log(Level.FINE, "ending InitializeSessionForAdminProfilePagesFilter...");
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
