package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.servlets.actions.InitializeListOfQuestionsInSessionAction;
import com.haxwell.apps.questions.servlets.actions.SetUserContributedQuestionAndExamCountInSessionAction;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TypeUtil;

/**
 * Servlet implementation class ProfileQuestionsServlet
 */
@WebServlet("/secured/ProfileQuestionsServlet")
public class ProfileQuestionsServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileQuestionsServlet() {
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

		String fwdPage = "/secured/profile.jsp";
		
		String value = request.getParameter("runFilter");
		
		if (value == null) value = "";
		
		if (value.equals("Run Filter -->"))
			handleFilterButtonPress(request);
		else
		{
			String id = getIdAppendedToRequestParameter(request, "nameOfLastPressedButton");
			String btnValue = request.getParameter("valueOfLastPressedButton");
				
			if (!StringUtil.isNullOrEmpty(id))
			{
				if (btnValue != null)
				{
					if (btnValue.equals("Edit Question"))
						fwdPage = "/secured/question.jsp?questionId=" + id;
					else if (btnValue.equals("Delete Question")) {
						User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
						
						QuestionManager.deleteQuestion(user.getId(), id);
						request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, null);
						
						new InitializeListOfQuestionsInSessionAction().doAction(request, response);
						new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
						
						//request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);							
					}
				}
			}
		}
		
		request.getSession().setAttribute("tabIndex", Constants.PROFILE_QUESTION_TAB_INDEX);
		
		redirectToJSP(request, response, fwdPage);
	}

	private void handleFilterButtonPress(HttpServletRequest request) {
		String filterText = request.getParameter("containsFilter");
		String topicFilterText = request.getParameter("topicFilter");
		int questionTypeFilter = TypeUtil.convertToInt(request.getParameter("questionTypeFilter"));
		int maxDifficulty = DifficultyUtil.convertToInt(request.getParameter("difficultyFilter"));
		
		Collection<Question> coll = null; 
		
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		
//		if (user != null)
//			coll = QuestionManager.getQuestionsCreatedByAGivenUserThatContain(user.getId(), topicFilterText, filterText, maxDifficulty, questionTypeFilter, null);

		request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);

		// store the filter we just used
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
		request.getSession().setAttribute(Constants.MRU_FILTER_QUESTION_TYPE, questionTypeFilter);
	}

}
