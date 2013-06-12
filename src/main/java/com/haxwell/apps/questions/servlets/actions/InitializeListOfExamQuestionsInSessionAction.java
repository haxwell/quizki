package com.haxwell.apps.questions.servlets.actions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import com.haxwell.apps.questions.managers.VoteManager;
import com.haxwell.apps.questions.utils.PaginationData;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.VoteData;

/**
 * Ensures that the list of questions in the session is the most up to date it can be.
 * 
 * @author johnathanj
 */
public class InitializeListOfExamQuestionsInSessionAction implements AbstractServletAction {

	Logger log = Logger.getLogger(InitializeListOfExamQuestionsInSessionAction.class.getName());
	
	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();
			
			if (req.getSession().getAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS) == null) {
				
				PaginationData qpd = (PaginationData)req.getSession().getAttribute(Constants.QUESTION_PAGINATION_DATA);

				User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
				Collection<Question> coll = null;
				Map<String, VoteData> collVoteData = null;

				if (user != null) {
					if (req.getSession().getAttribute(Constants.ONLY_SELECTED_QUESTIONS_SHOULD_BE_SHOWN) != null) {
						List<Long> selectedQuestionIds = (List<Long>)req.getSession().getAttribute(Constants.CURRENT_EXAM_SELECTED_QUESTION_IDS);
						coll = QuestionManager.getQuestionsById(StringUtil.getCSVString(selectedQuestionIds), qpd);
					}
					else {
						if (QuestionManager.getNumberOfQuestionsCreatedByUser(user.getId()) > 0) {
							coll = QuestionManager.getAllQuestionsForUser(user.getId(), qpd);
							session.setAttribute(Constants.MRU_FILTER_MINE_OR_ALL_OR_SELECTED, Constants.MY_ITEMS);
						}
						else {
							// if the user doesn't have any questions of their own, get all the questions.. 
							//  we want the user to see something when they go this screen. I think its a 
							//  better user experience..
							coll = QuestionManager.getAllQuestions(qpd);
							session.setAttribute(Constants.MRU_FILTER_MINE_OR_ALL_OR_SELECTED, Constants.ALL_ITEMS);
						}
					}
					
					EventDispatcher.getInstance().fireEvent(req, EventConstants.LIST_OF_EXAM_QUESTIONS_SET_IN_SESSION);
				}

				collVoteData = VoteManager.getSummarizedVotes(coll);
				
				req.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
				req.getSession().setAttribute(Constants.VOTE_DATA_FOR_LIST_OF_QUESTIONS_TO_BE_DISPLAYED, collVoteData);
				
				/*
				 * This event is thrown because when this list is set, the 'shouldAllowEditing' attribute should be cleared.
				 * We can't depend on the AttributeListener, because it only activates handlers to be called when some other event
				 * happens. The event is now, and we need the handlers to do their thing, now.
				 */
				EventDispatcher.getInstance().fireEvent(req, EventConstants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED_SET_IN_SESSION);

				if (coll != null)
					log.log(Level.FINER, "Just set " + Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + "to have " + coll.size() + " items.");
				else
					log.log(Level.FINER, "coll was null. No changes made to the " + Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + " list");
			}
			else
				log.log(Level.FINER, Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + " was not null so ListQuestionsFilter did not reset the list of questions to be displayed.");
		}
		
		return 0;
	}
}

