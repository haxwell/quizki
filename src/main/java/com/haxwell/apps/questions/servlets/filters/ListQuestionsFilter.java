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
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;

/**
 * Puts a list of all questions in the request as an attribute.
 */
@WebFilter("/ListQuestionsFilter")
public class ListQuestionsFilter extends AbstractFilter {

	Logger log = Logger.getLogger(ListQuestionsFilter.class.getName());
	
	public ListQuestionsFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.log(Level.INFO, "...in ListQuestionsFilter()");
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			
			if (req.getSession().getAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED) == null) {

				User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
				Collection<Question> coll = null;

				if (user == null && req.getSession().getAttribute(Constants.SHOULD_ALL_QUESTIONS_BE_DISPLAYED) != null) {
					coll = QuestionManager.getAllQuestions();
					req.getSession().setAttribute(Constants.SHOULD_ALL_QUESTIONS_BE_DISPLAYED, null);					
				}
				else if (user != null) {
					coll = QuestionManager.getAllQuestionsForUser(user.getId());
				}

				req.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
				req.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);

				if (coll != null)
					log.log(Level.INFO, "Just added " + coll.size() + " questions to the " + Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + " list");
				else
					log.log(Level.INFO, "No questions were added to the " + Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + " list");
			}
			else
				log.log(Level.INFO, Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + " was not null so ListQuestionsFilter did not reset the list of questions to be displayed.");
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
