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
		
		if (request instanceof HttpServletRequest && 
				((HttpServletRequest)request).getSession().getAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED) == null) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			
			User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			Collection<Exam> coll = null;

			if (user == null ) {
				coll = ExamManager.getAllExams();
				req.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, Constants.ALL_ITEMS_STR);
			}
			else if (user != null) {
				coll = ExamManager.getAllExamsForUser(user.getId());
				req.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, Constants.MY_ITEMS_STR);
			}
			
			req.getSession().setAttribute("fa_listofexamstobedisplayed", coll);
			
			log.log(Level.INFO, "Just added " + coll.size() + " exams to the fa_listofexamstobedisplayed list");
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
