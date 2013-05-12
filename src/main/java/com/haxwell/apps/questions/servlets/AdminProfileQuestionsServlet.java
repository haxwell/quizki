package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.servlets.actions.InitializeListOfProfileQuestionsInSessionAction;
import com.haxwell.apps.questions.servlets.actions.SetUserContributedQuestionAndExamCountInSessionAction;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.PaginationData;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TypeUtil;

/**
 * Servlet implementation class ProfileQuestionsServlet
 */
@WebServlet("/secured/AdminProfileQuestionsServlet")
public class AdminProfileQuestionsServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminProfileQuestionsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String fwdPage = "/secured/admin-profile.jsp";
		
		String button = request.getParameter("button");
		
		if (button == null) button = "";
		
		if (button.equals("Apply Filter -->"))
			handleFilterButtonPress(request, getQuestionPaginationData(request));
		else if (button.equals("<< FIRST"))
		{
			PaginationData pd = getQuestionPaginationData(request);
			boolean pdValuesChanged = false;
			
			int quantity = Integer.parseInt(getIdAppendedToRequestParameter(request, "quantity"));
			
			if (quantity != pd.getPageSize()) {
				pd.setPageSize(quantity);
				pdValuesChanged = true;
			}
			else {
				if (pd.getPageNumber() != pd.FIRST_PAGE) {			
					pd.initialize();
					pdValuesChanged = true;
				}
			}
			
			if (pdValuesChanged) {
				refreshListOfQuestionsToBeDisplayed(request, pd);
				setQuestionPaginationData(request, pd);
			}
		}
		else if (button.equals("< PREV"))
		{
			PaginationData pd = getQuestionPaginationData(request);
			boolean pdValuesChanged = false;
			
			int quantity = Integer.parseInt(getIdAppendedToRequestParameter(request, "quantity"));
			
			if (quantity != pd.getPageSize()) {
				pd.setPageSize(quantity);
				pdValuesChanged = true;
			}
			
			if (pd.canDecrementPageNumber()) {
				pd.decrementPageNumber();
				pdValuesChanged = true;
			}
			
			if (pdValuesChanged) {
				refreshListOfQuestionsToBeDisplayed(request, pd);
				setQuestionPaginationData(request, pd);
			}
		}
		else if (button.equals("NEXT >"))
		{
			PaginationData pd = getQuestionPaginationData(request);
			boolean pdValuesChanged = false;
			
			int quantity = Integer.parseInt(getIdAppendedToRequestParameter(request, "quantity"));
			
			if (quantity != pd.getPageSize()) {
				pd.setPageSize(quantity);
				pdValuesChanged = true;
			}
			
			if (pd.canIncrementPageNumber())
			{
				pd.incrementPageNumber();
				pdValuesChanged = true;
			}
			
			if (pdValuesChanged) {
				refreshListOfQuestionsToBeDisplayed(request, pd);
				setQuestionPaginationData(request, pd);
			}
		}
		else if (button.equals("LAST >>"))
		{
			PaginationData pd = getQuestionPaginationData(request);
			boolean pdValuesChanged = false;
			
			int quantity = Integer.parseInt(getIdAppendedToRequestParameter(request, "quantity"));
			
			if (quantity != pd.getPageSize()) {
				pd.setPageSize(quantity);
				pdValuesChanged = true;
			}
			else {
				int maxPageNumber = pd.getMaxPageNumber();
				
				if (pd.getPageNumber() != maxPageNumber) {
					pd.setPageNumber(maxPageNumber);
					pdValuesChanged = true;
				}
			}
			
			if (pdValuesChanged) {
				refreshListOfQuestionsToBeDisplayed(request, pd);
				setQuestionPaginationData(request, pd);
			}
		}
		else if (button.equals("REFRESH")) 
		{
			PaginationData pd = getQuestionPaginationData(request);
			
			int quantity = Integer.parseInt(getIdAppendedToRequestParameter(request, "quantity"));
			
			if (quantity != pd.getPageSize()) {
				pd.setPageSize(quantity);
			}

			refreshListOfQuestionsToBeDisplayed(request, pd);
			setQuestionPaginationData(request, pd);
		}
		else
		{
			String id = getIdAppendedToRequestParameter(request, "nameOfLastPressedButton");
			String btnValue = request.getParameter("valueOfLastPressedButton");
				
			if (!StringUtil.isNullOrEmpty(id))
			{
				if (btnValue != null)
				{
					if (btnValue.equals("Edit"))
						fwdPage = "/secured/question.jsp?questionId=" + id;
					else if (btnValue.equals("Delete")) {
						User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
						
						QuestionManager.deleteQuestion(user.getId(), id);
						request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, null);
						
						new InitializeListOfProfileQuestionsInSessionAction().doAction(request, response);
						new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
						
						//request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);							
					}
				}
			}
		}
		
		request.getSession().setAttribute("tabIndex", Constants.PROFILE_QUESTION_TAB_INDEX);
		
		redirectToJSP(request, response, fwdPage);
	}

	private void handleFilterButtonPress(HttpServletRequest request, PaginationData pd) {
		String filterText = request.getParameter("containsFilter");
		String topicFilterText = request.getParameter("topicFilter");
		int questionType = TypeUtil.convertToInt(request.getParameter("questionTypeFilter"));
		int maxDifficulty = DifficultyUtil.convertToInt(request.getParameter("difficultyFilter"));
		
		HttpSession session = request.getSession();
		
		Collection<Question> coll = null; 
		
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		
		if (user != null)
			coll = QuestionManager.getQuestionsThatContain(topicFilterText, filterText, maxDifficulty, questionType, pd);
		
		if (parametersIndicateThatFilterWasApplied(filterText, topicFilterText, maxDifficulty, questionType))
				pd.setTotalItemCount(coll.size());

		request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);

		// store the filter we just used
		session.setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		session.setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
		session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
		session.setAttribute(Constants.MRU_FILTER_QUESTION_TYPE, questionType);
		session.setAttribute(Constants.DO_NOT_INITIALIZE_PROFILE_MRU_SETTINGS, Boolean.TRUE);		
		session.setAttribute(Constants.DO_NOT_INITIALIZE_QUESTIONS_TO_BE_DISPLAYED, Boolean.TRUE);

		/*
		 * This event is thrown because when this list is set, the 'shouldAllowEditing' attribute should be cleared.
		 * We can't depend on the AttributeListener, because it only activates handlers to be called when some other event
		 * happens. The event is now, and we need the handlers to do their thing, now.
		 */
		EventDispatcher.getInstance().fireEvent(request, EventConstants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED_SET_IN_SESSION);
	}
	
	private void refreshListOfQuestionsToBeDisplayed(HttpServletRequest request, PaginationData pd) {
		String filterText = (String)request.getSession().getAttribute(Constants.MRU_FILTER_TEXT);
		String topicFilterText = (String)request.getSession().getAttribute(Constants.MRU_FILTER_TOPIC_TEXT);
		Object o = request.getSession().getAttribute(Constants.MRU_FILTER_DIFFICULTY);
		int questionType = (Integer)request.getSession().getAttribute(Constants.MRU_FILTER_QUESTION_TYPE);
		int maxDifficulty = DifficultyConstants.GURU;

		HttpSession session = request.getSession();
		
		if (o != null)
			maxDifficulty = Integer.parseInt(o.toString());

		List<Question> coll = null; 
		
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		
		if (user != null)
			coll = QuestionManager.getQuestionsThatContain(topicFilterText, filterText, maxDifficulty, questionType, pd);

		if (parametersIndicateThatFilterWasApplied(filterText, topicFilterText, maxDifficulty, questionType))
				pd.setTotalItemCount(coll.size());
		
		// store the filter we just used
		session.setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
		session.setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		session.setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
		session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
		session.setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, pd.getPageSize());
		session.setAttribute(Constants.MRU_FILTER_QUESTION_TYPE, questionType);
		
		session.setAttribute(Constants.DO_NOT_INITIALIZE_PROFILE_MRU_SETTINGS, Boolean.TRUE);
		
		/*
		 * This event is thrown because when this list is set, the 'shouldAllowEditing' attribute should be cleared.
		 * We can't depend on the AttributeListener, because it only activates handlers to be called when some other event
		 * happens. The event is now, and we need the handlers to do their thing, now.
		 */
		EventDispatcher.getInstance().fireEvent(request, EventConstants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED_SET_IN_SESSION);
	}
	
	private boolean parametersIndicateThatFilterWasApplied(String filterText, String topicFilterText, int maxDifficulty, int questionType) {
		
		if (!StringUtil.isNullOrEmpty(filterText)) return true;
		if (!StringUtil.isNullOrEmpty(topicFilterText)) return true;
		if (maxDifficulty < DifficultyUtil.convertToInt(DifficultyConstants.GURU_STR)) return true;
		if (questionType != TypeConstants.ALL_TYPES) return true;
		
		return false;
	}
	
	private PaginationData getQuestionPaginationData(HttpServletRequest request) {
		return (PaginationData)request.getSession().getAttribute(Constants.QUESTION_PAGINATION_DATA);
	}
	
	private void setQuestionPaginationData(HttpServletRequest request, PaginationData pd) {
		request.getSession().setAttribute(Constants.QUESTION_PAGINATION_DATA, pd);
	}
	
}
