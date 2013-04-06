package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Servlet implementation class ListQuestionsServlet
 */
@WebServlet("/ListQuestionsServlet")
public class ListQuestionsServlet extends AbstractHttpServlet {

	Logger log = Logger.getLogger(ListQuestionsServlet.class.getName());

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ListQuestionsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String fwdPage = "/secured/listQuestions.jsp";
		
		User user = (User) request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		Collection<Question> coll = null;

		if (user == null) {
			coll = QuestionManager.getAllQuestions();
		} else if (user != null) {
			String button = request.getParameter("button");

			if (!StringUtil.isNullOrEmpty(button)) {
			
				if (button.equals("Filter")) 
				{
					handleFilterButtonPress(request);
				}
				else if (button.equals("Clear Filter")) 
				{
					clearMRUFilterSettings(request);
					refreshListOfQuestionsToBeDisplayed(request);
				}
			}
			else
				fwdPage = handleEditQuestion(request, fwdPage);
		}

		redirectToJSP(request, response, fwdPage);
	}

	private String handleEditQuestion(HttpServletRequest request, String fwdPage) {
		Collection<Question> coll;
		coll = (Collection<Question>)request.getSession().getAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED);
		Iterator<Question> iterator = coll.iterator();
		Question q = null;
		String action = null;
		
		while (iterator.hasNext() && action == null) {
			q = iterator.next();
			action = request.getParameter("questionButton_" + q.getId());
		}
		
		if (action != null) {
			if (action.equals("Edit Question"))
				fwdPage = "/secured/question.jsp?questionId=" + q.getId();
			else if (action.equals("Delete Question")) {
				User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
				
				QuestionManager.deleteQuestion(user.getId(), q);
				request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, null);
			}
		}
		return fwdPage;
	}
	
	// TODO: These following two methods can be combined.. 
	private void handleFilterButtonPress(HttpServletRequest request) {
		String mineOrAll = request.getParameter(Constants.SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS);
		String filterText = request.getParameter("containsFilter");
		String topicFilterText = request.getParameter("topicContainsFilter");
		int maxDifficulty = DifficultyUtil.convertToInt(request.getParameter("difficulty"));
		
		Collection<Question> coll = null; 
		
		if (mineOrAll.equals(Constants.MY_ITEMS_STR)) 
		{
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			
			if (user != null)
				coll = QuestionManager.getQuestionsCreatedByAGivenUserThatContain(user.getId(), topicFilterText, filterText, maxDifficulty, null, null);
		}
		else if (mineOrAll.equals(Constants.ALL_ITEMS_STR))
		{
			coll = QuestionManager.getQuestionsThatContain(topicFilterText, filterText, maxDifficulty, null, null);
		}

		request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);

		// store the filter we just used
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
		request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, mineOrAll);
	}

	private void refreshListOfQuestionsToBeDisplayed(HttpServletRequest request) {
		String filterText = (String)request.getSession().getAttribute(Constants.MRU_FILTER_TEXT);
		String topicFilterText = (String)request.getSession().getAttribute(Constants.MRU_FILTER_TOPIC_TEXT);
		Object o = request.getSession().getAttribute(Constants.MRU_FILTER_DIFFICULTY);
		int maxDifficulty = DifficultyConstants.GURU;
		
		if (o != null)
			maxDifficulty = Integer.parseInt(o.toString());
		
		Collection<Question> coll = null;
		
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		
		if (user != null) {
			coll = QuestionManager.getQuestionsCreatedByAGivenUserThatContain(user.getId(), topicFilterText, filterText, maxDifficulty, null, null);
			request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, Constants.MY_ITEMS_STR);			
		}
		else {
			coll = QuestionManager.getQuestionsThatContain(topicFilterText, filterText, maxDifficulty, null, null);
			request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, Constants.ALL_ITEMS_STR);
		}
			
		request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
	}

	private void clearMRUFilterSettings(HttpServletRequest request) {
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, null);
	}
	
}
