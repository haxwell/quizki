package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.filters.DifficultyFilter;
import com.haxwell.apps.questions.filters.QuestionFilter;
import com.haxwell.apps.questions.filters.QuestionTopicFilter;
import com.haxwell.apps.questions.filters.QuestionTypeFilter;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.servlets.actions.InitializeNewExamInSessionAction;
import com.haxwell.apps.questions.utils.CollectionUtil;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.FilterCollection;
import com.haxwell.apps.questions.utils.FilterUtil;
import com.haxwell.apps.questions.utils.ListFilterer;
import com.haxwell.apps.questions.utils.PaginationData;
import com.haxwell.apps.questions.utils.PaginationDataUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TypeUtil;

/**
 * Servlet implementation class ExamServlet
 */
@WebServlet("/secured/ExamServlet")
public class ExamServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
    
	/**
	 * 
	 * THIS CLASS CAN BE REMOVED! Everything is being done by AJAX now..
	 * 
	 */

	
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
		
		HttpSession session = request.getSession();
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
		else if (button.equals("Apply Filter -->")) 
		{
			handleFilterButtonPress(request, getQuestionPaginationData(request));
			
			setExamTitleAndMessageFromFormParameter(request, examObj);
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);
		}
		else if (button.equals("Clear Filter")) 
		{
			clearMRUFilterSettings(request);
			refreshListOfQuestionsToBeDisplayed(request, getQuestionPaginationData(request));
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);
		}
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
			
			setExamTitleAndMessageFromFormParameter(request, examObj);
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);			
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
			
			setExamTitleAndMessageFromFormParameter(request, examObj);
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);			
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
			
			setExamTitleAndMessageFromFormParameter(request, examObj);
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);			
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
			
			setExamTitleAndMessageFromFormParameter(request, examObj);
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);			
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
			
			setExamTitleAndMessageFromFormParameter(request, examObj);
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);			
		}

		if (examWasPersisted) {
			clearMRUFilterSettings(request);
			refreshListOfQuestionsToBeDisplayed(request, getQuestionPaginationData(request));
		}
		else
			session.setAttribute(Constants.CURRENT_EXAM, examObj);
		
		forwardToJSP(request, response, "/secured/exam.jsp");
	}

	private PaginationData getQuestionPaginationData(HttpServletRequest request) {
		return (PaginationData)request.getSession().getAttribute(Constants.QUESTION_PAGINATION_DATA);
	}

	private void setQuestionPaginationData(HttpServletRequest request, PaginationData pd) {
		request.getSession().setAttribute(Constants.QUESTION_PAGINATION_DATA, pd);
	}

	private void clearMRUFilterSettings(HttpServletRequest request) {
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, null);
	}
	
	private boolean handleAddExamButtonPress(HttpServletRequest request,
			Exam examObj, List<String> errors, List<String> successes) {
		
		boolean rtn = false;
		
		setExamTitleAndMessageFromFormParameter(request, examObj);
		
		if (validation(request, errors))
		{
			long id = ExamManager.persistExam(examObj);
			
			successes.add("Exam '" + examObj.getTitle() + "' has been saved successfully. <a href=\"/secured/exam.jsp?examId=" + id + "\">(edit it)</a>, <a href=\"/beginExam.jsp?examId=" + id + "\">(take it)</a>");
			request.setAttribute(Constants.SUCCESS_MESSAGES, successes);
			
			request.setAttribute(Constants.CURRENT_EXAM, null);
			request.getSession().setAttribute(Constants.CURRENT_EXAM, null);
			
			clearMRUFilterSettings(request);
			
			request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);
			
			EventDispatcher.getInstance().fireEvent(request, EventConstants.EXAM_WAS_PERSISTED);
			
			rtn = true;
		}
		else
		{
			request.setAttribute(Constants.VALIDATION_ERRORS, errors);
			
			saveExamSelectedQuestionIdsInSession(request);
		}
		
		return rtn;
	}
	
	// TODO: These following two methods can be combined..
	private void handleFilterButtonPress(HttpServletRequest request, PaginationData pd) {
//		String filterText = request.getParameter("containsFilter");
//		String topicFilterText = request.getParameter("topicContainsFilter");
//		int questionType = TypeUtil.convertToInt(request.getParameter("questionTypeFilter"));
//		int maxDifficulty = DifficultyUtil.convertToInt(request.getParameter("difficultyFilter"));
//		
//		String mineOrAllOrSelected = request.getParameter(Constants.SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS_OR_SELECTED_ITEMS);
//		
//		FilterCollection fc = new FilterCollection();
//		fc.add(FilterConstants.QUESTION_CONTAINS_FILTER, filterText);
//		fc.add(FilterConstants.TOPIC_CONTAINS_FILTER, topicFilterText);
//		fc.add(FilterConstants.QUESTION_TYPE_FILTER, questionType + "");
//		fc.add(FilterConstants.DIFFICULTY_FILTER, maxDifficulty + "");
//
//		List<Question> coll = null;
//		
//		if (mineOrAllOrSelected.equals(Constants.MY_ITEMS_STR)) 
//		{
//			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
//			
//			if (user != null)
//				coll = QuestionManager.getQuestionsCreatedByAGivenUserThatContain(user.getId(), topicFilterText, filterText, maxDifficulty, questionType, pd);
//		}
//		else if (mineOrAllOrSelected.equals(Constants.ALL_ITEMS_STR))
//		{
//			//coll = QuestionManager.getQuestionsThatContain(topicFilterText, filterText, maxDifficulty, questionType, pd);
//			coll = QuestionManager.getQuestions(fc);
//		}
//		else if (mineOrAllOrSelected.equals(Constants.SELECTED_ITEMS_STR))
//		{
//			final Exam exam = getExamBean(request);
//			Set<Question> selectedQuestions = exam.getQuestions();
//			String csvList = StringUtil.getCSVFromCollection(selectedQuestions);
//			
//			pd.setPageNumber(PaginationData.FIRST_PAGE);
//			
//			String quantity = getIdAppendedToRequestParameter(request, "quantity");
//			
//			if (quantity != null) {
//				pd.setPageSize(Integer.parseInt(quantity));
//			}
//			
//			coll = QuestionManager.getQuestionsById(csvList, null);
//			
//			coll = filterCollectionWithAllCategories(filterText,
//					topicFilterText, questionType, maxDifficulty, coll);
//
//			pd.setTotalItemCount(coll.size());
//			
//			coll = (List<Question>)PaginationDataUtil.reduceListSize(pd, coll);
//		}
//
//		if (coll != null) {
//			saveExamSelectedQuestionIdsInSession(request);
//		}
//		
//		// store the filter we just used
//		request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
//		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, filterText);
//		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
//		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
//		request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL_OR_SELECTED, FilterUtil.convertToInt(mineOrAllOrSelected));
//		request.getSession().setAttribute(Constants.MRU_FILTER_QUESTION_TYPE, questionType);
//		request.getSession().setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, pd.getPageSize());
//		
//		/*
//		 * This event is thrown because when this list is set, the 'shouldAllowEditing' attribute should be cleared.
//		 * We can't depend on the AttributeListener, because it only activates handlers to be called when some other event
//		 * happens. The event is now, and we need the handlers to do their thing, now.
//		 */
//		EventDispatcher.getInstance().fireEvent(request, EventConstants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED_SET_IN_SESSION);
	}

	private void saveExamSelectedQuestionIdsInSession(HttpServletRequest request) {
		Collection<Long> selectedQuestionIds = CollectionUtil.getListOfIds(getExamBean(request).getQuestions());
		request.getSession().setAttribute(Constants.CURRENT_EXAM_SELECTED_QUESTION_IDS, selectedQuestionIds);
	}

	private void refreshListOfQuestionsToBeDisplayed(HttpServletRequest request, PaginationData pd) {
//		String filterText = (String)request.getSession().getAttribute(Constants.MRU_FILTER_TEXT);
//		String topicFilterText = (String)request.getSession().getAttribute(Constants.MRU_FILTER_TOPIC_TEXT);
//		int mineOrAllOrSelected = ((Integer)request.getSession().getAttribute(Constants.MRU_FILTER_MINE_OR_ALL_OR_SELECTED)).intValue();
//		Object o = request.getSession().getAttribute(Constants.MRU_FILTER_DIFFICULTY);
//		int questionType = (Integer)request.getSession().getAttribute(Constants.MRU_FILTER_QUESTION_TYPE);
//		int maxDifficulty = DifficultyConstants.GURU;
//
//		HttpSession session = request.getSession();
//		
//		if (o != null)
//			maxDifficulty = Integer.parseInt(o.toString());
//
//		List<Question> coll = null;
//		
//		if (mineOrAllOrSelected == Constants.MY_ITEMS) 
//		{
//			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
//			
//			if (user != null)
//				coll = QuestionManager.getQuestionsCreatedByAGivenUserThatContain(user.getId(), topicFilterText, filterText, maxDifficulty, questionType, pd);
//		}
//		else if (mineOrAllOrSelected == Constants.ALL_ITEMS)
//		{
//			coll = QuestionManager.getQuestionsThatContain(topicFilterText, filterText, maxDifficulty, questionType, pd);
//		}
//		else if (mineOrAllOrSelected == Constants.SELECTED_ITEMS)
//		{
//			final Exam exam = getExamBean(request);
//			Set<Question> selectedQuestions = exam.getQuestions();
//			String csvList = StringUtil.getCSVFromCollection(selectedQuestions);
//			
//			//pd.setPageNumber(PaginationData.FIRST_PAGE);
//			
//			coll = QuestionManager.getQuestionsById(csvList, null);
//			
//			coll = filterCollectionWithAllCategories(filterText,
//					topicFilterText, questionType, maxDifficulty, coll);
//
//			pd.setTotalItemCount(coll.size());			
//			
//			coll = (List<Question>)PaginationDataUtil.reduceListSize(pd, coll);
//		}
//
//		if (coll != null) {
//			saveExamSelectedQuestionIdsInSession(request);
//		}
//		
//		session.setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);
//		session.setAttribute(Constants.MRU_FILTER_TEXT, filterText);
//		session.setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
//		session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
//		session.setAttribute(Constants.MRU_FILTER_MINE_OR_ALL_OR_SELECTED, mineOrAllOrSelected);
//		session.setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, pd.getPageSize());
//		
//		/*
//		 * This event is thrown because when this list is set, the 'shouldAllowEditing' attribute should be cleared.
//		 * We can't depend on the AttributeListener, because it only activates handlers to be called when some other event
//		 * happens. The event is now, and we need the handlers to do their thing, now.
//		 */
//		EventDispatcher.getInstance().fireEvent(request, EventConstants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED_SET_IN_SESSION);
	}

//	private List<Question> filterCollectionWithAllCategories(
//			String filterText, String topicFilterText, int questionType,
//			int maxDifficulty, List<Question> coll) {
//		ArrayList<ShouldRemoveAnObjectCommand<Question>> arr = new ArrayList<ShouldRemoveAnObjectCommand<Question>>();			
//
//		if (!StringUtil.isNullOrEmpty(filterText))
//			arr.add(new QuestionFilter(filterText));
//		
//		if (!StringUtil.isNullOrEmpty(topicFilterText))
//			arr.add(new QuestionTopicFilter(topicFilterText));
//
//		if (questionType != TypeConstants.ALL_TYPES )
//			arr.add(new QuestionTypeFilter(questionType));
//		
//		arr.add(new DifficultyFilter(maxDifficulty));
//
//		coll = new ListFilterer<Question>().process(coll, arr);
//		return coll;
//	}

	private void setExamTitleAndMessageFromFormParameter(HttpServletRequest request, Exam examObj) {
		String title = request.getParameter("examTitle");
		String message = request.getParameter("examMessage");
		
		if (title != null) 
			examObj.setTitle(title);
		
		if (message != null)
			examObj.setMessage(message);
	}

	private Exam getExamBean(HttpServletRequest request) {
		Exam exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);
		
		if (exam == null) {
			new InitializeNewExamInSessionAction().doAction(request, null);
			exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);			
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
