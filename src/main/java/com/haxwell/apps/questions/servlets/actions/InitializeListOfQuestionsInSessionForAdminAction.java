package com.haxwell.apps.questions.servlets.actions;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.utils.PaginationData;

/**
 * Ensures that the list of profile page questions in the session is the most up to date it can be.
 * 
 * @author johnathanj
 */
public class InitializeListOfQuestionsInSessionForAdminAction implements AbstractServletAction {

	Logger log = Logger.getLogger(InitializeListOfQuestionsInSessionForAdminAction.class.getName());
	
	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();
			
			User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			Collection<Question> coll = null;

			PaginationData qpd = (PaginationData)req.getSession().getAttribute(Constants.QUESTION_PAGINATION_DATA);
			
			if (user != null) {
				if (req.getSession().getAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED_HAS_BEEN_FILTERED) == null) {
					coll = QuestionManager.getAllQuestions(qpd);
					session.setAttribute(Constants.MRU_FILTER_MINE_OR_ALL_OR_SELECTED, Constants.ALL_ITEMS);
				}
				else {
					String filterText = (String)session.getAttribute(Constants.MRU_FILTER_TEXT);
					String topicFilterText = (String)session.getAttribute(Constants.MRU_FILTER_TOPIC_TEXT);
					int questionType = (Integer)session.getAttribute(Constants.MRU_FILTER_QUESTION_TYPE);
					int maxDifficulty = (Integer)session.getAttribute(Constants.MRU_FILTER_DIFFICULTY);

					coll = QuestionManager.getQuestionsThatContain(topicFilterText, filterText, maxDifficulty, questionType, qpd);
				}
			}

			req.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
			
			EventDispatcher.getInstance().fireEvent(req, EventConstants.LIST_OF_QUESTIONS_SET_IN_SESSION_FOR_ADMIN);
			EventDispatcher.getInstance().fireEvent(req, EventConstants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED_SET_IN_SESSION);
	
			if (coll != null)
				log.log(Level.FINER, "Just set " + Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + "to have " + coll.size() + " items.");
			else
				log.log(Level.FINER, "coll was null. No changes made to the " + Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + " list");
		}
		
		return 0;
	}
}

