package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.managers.TopicManager;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Servlet implementation class ExamServlet
 */
@WebServlet("/secured/ExamServlet")
public class ExamServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExamServlet() {
        super();
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
		
		String button = request.getParameter("button");
		
		Exam examObj = getExamBean(request);		

		List<String> errors = new ArrayList<String>();		
		List<String> successes = new ArrayList<String>();
		
		boolean examWasPersisted = false;
		
		// in case they press a button to delete a question.. this will be null..
		if (button == null) button = "";
		
		if (button.equals("Add Exam") || button.equals("Update Exam"))
		{
			examWasPersisted = handleAddExamButtonPress(request, examObj, errors, successes);
		}
		else if (button.equals("Add Questions"))
		{
			String questionIDs = getQuestionIDsToBeActedOn(request, ((Collection<Question>)request.getSession().getAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED)), "a_chkbox_");
			Collection<Question> questionsToAdd = QuestionManager.getQuestionsById(questionIDs);
			ExamManager.addQuestions(examObj, questionsToAdd);
			
			// Remove the questions already on the exam from the list of questions to be displayed.. no need allowing them to be selected again
			Collection<Question> coll = (Collection<Question>)request.getSession().getAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED);
			coll.removeAll(examObj.getQuestions());
			request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
			
			setExamTitleFromFormParameter(request, examObj);
		}
		else if (button.equals("Delete Questions"))
		{
			String questionIDs = getQuestionIDsToBeActedOn(request, examObj.getQuestions(), "d_chkbox_");
			
			ExamManager.removeQuestions(examObj, questionIDs);
			
			refreshListOfQuestionsToBeDisplayed(request);			
		}
		else if (button.equals("Filter")) 
		{
			handleFilterButtonPress(request);
		}
		else if (button.equals("Clear Filter")) 
		{
			clearMRUFilterSettings(request);
			refreshListOfQuestionsToBeDisplayed(request);
		}
		else
		{
			Iterator<Question> iterator = examObj.getQuestions().iterator();
			Question q = null;
			String action = null;
			
			while (iterator.hasNext() && action == null)
			{
				q = iterator.next();
				action = request.getParameter("questionButton_" + q.getId());
			}
			
			if (action != null) {
				if (action.equals("Delete"))
					ExamManager.delete(examObj, q);
			}
			
			setExamTitleFromFormParameter(request, examObj);
		}

		if (examWasPersisted) {
			request.getSession().setAttribute(Constants.CURRENT_EXAM_HAS_BEEN_PERSISTED, Boolean.TRUE);
			request.getSession().setAttribute(Constants.IN_EDITING_MODE, null); // HACK!! I would rather do this in the initialize..Exam filter, but its not being called by the forwardToJSP() call.
		}
		else
			request.getSession().setAttribute(Constants.CURRENT_EXAM, examObj);
		
		forwardToJSP(request, response, "/secured/exam.jsp");
	}

	private void clearMRUFilterSettings(HttpServletRequest request) {
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, null);
	}

	private String getQuestionIDsToBeActedOn(HttpServletRequest request, Collection<Question> coll, String fieldName)
	{
		Iterator<Question> iterator = coll.iterator();
		StringBuffer sb = new StringBuffer();
		
		while (iterator.hasNext())
		{
			Question q = iterator.next();
			String str = request.getParameter(fieldName + q.getId());
			
			if (!StringUtil.isNullOrEmpty(str)) {
				if (sb.length() > 0)
					sb.append(",");
				
				sb.append(q.getId());
			}
		}
		
		return sb.toString();
	}
	
	private boolean handleAddExamButtonPress(HttpServletRequest request,
			Exam examObj, List<String> errors, List<String> successes) {
		
		boolean rtn = false;
		
		setExamTitleFromFormParameter(request, examObj);
		
		if (validation(request, errors))
		{
			long id = ExamManager.persistExam(examObj);
			
			successes.add("Exam '" + examObj.getTitle() + "' has been saved successfully. <a href=\"/secured/exam.jsp?examId=" + id + "\">(edit it)</a>, <a href=\"/beginExam.jsp?examId=" + id + "\">(take it)</a>");
			request.setAttribute(Constants.SUCCESS_MESSAGES, successes);
			
			request.setAttribute(Constants.CURRENT_EXAM, null);
			request.getSession().setAttribute(Constants.CURRENT_EXAM, null);
			request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, null);
			
			clearMRUFilterSettings(request);
			
			request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);
			
			rtn = true;
		}
		else
		{
			request.setAttribute(Constants.VALIDATION_ERRORS, errors);
		}
		
		return rtn;
	}

	
	// TODO: These following two methods can be combined..
	private void handleFilterButtonPress(HttpServletRequest request) {
		String filterText = request.getParameter("containsFilter");
		String topicFilterText = request.getParameter("topicContainsFilter");
		int maxDifficulty = DifficultyUtil.convertToInt(request.getParameter("difficulty"));
		
		String mineOrAll = request.getParameter(Constants.SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS);

		Collection<Question> coll = null;
		
		if (mineOrAll.equals(Constants.MY_ITEMS_STR)) 
		{
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			
			if (user != null)
				coll = QuestionManager.getQuestionsCreatedByAGivenUserThatContain(user.getId(), topicFilterText, filterText, maxDifficulty);
		}
		else if (mineOrAll.equals(Constants.ALL_ITEMS_STR))
		{
			coll = QuestionManager.getQuestionsThatContain(topicFilterText, filterText, maxDifficulty);
		}

		if (coll != null) {
			// Remove the questions already on the exam from the list of questions to be displayed.. no need allowing them to be selected again
			coll.removeAll(getExamBean(request).getQuestions());
		}
		
		request.getSession().setAttribute("fa_listoquestionstobedisplayed", coll);		
		
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
		
		if (user != null)
			coll = QuestionManager.getQuestionsCreatedByAGivenUserThatContain(user.getId(), topicFilterText, filterText, maxDifficulty);

		if (coll != null) {
			// Remove the questions already on the exam from the list of questions to be displayed.. no need allowing them to be selected again
			coll.removeAll(getExamBean(request).getQuestions());
		}
		
		request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
		request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, Constants.MY_ITEMS_STR);
	}

	private void setExamTitleFromFormParameter(HttpServletRequest request, Exam examObj) {
		String title = request.getParameter("examTitle");
		examObj.setTitle(title);
	}

	private Exam getExamBean(HttpServletRequest request) {
		Exam exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);
		
		if (exam == null) {
			
			exam = ExamManager.newExam();
			
			// TODO: need some way of throwing an object up in the air saying "Hey! Just created this!"
			//  so that anyone who cares can set attributes on it. JMS message, or some other event listening/handler
			
			//  ..because I'm pretty sure I don't like doing this here..
			User user = (User)request.getSession().getAttribute("currentUserEntity");
			exam.setUser(user);
			
			request.getSession().setAttribute(Constants.CURRENT_EXAM, exam);
		}

		return exam;
	}
	
	private boolean validation(HttpServletRequest request, List<String> errors)
	{
		String title = request.getParameter("examTitle");
		boolean rtn = true;
		
		if (StringUtil.isNullOrEmpty(title))
		{
			errors.add("The exam requires a title!");
			rtn = false;
		}
		
		Exam examObj = getExamBean(request);
		
		if (examObj.getQuestions().size() == 0)
		{
			errors.add("The exam requires some questions be assigned to it!");
			rtn = false;
		}
		
		return rtn;
	}
}
