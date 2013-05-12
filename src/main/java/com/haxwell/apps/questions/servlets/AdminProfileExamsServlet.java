package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.servlets.actions.InitializeListOfExamsInSessionAction;
import com.haxwell.apps.questions.servlets.actions.SetUserContributedQuestionAndExamCountInSessionAction;
import com.haxwell.apps.questions.utils.PaginationData;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Servlet implementation class AdminProfileExamsServlet
 */
@WebServlet("/secured/AdminProfileExamsServlet")
public class AdminProfileExamsServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminProfileExamsServlet() {
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
			handleFilterButtonPress(request, getExamPaginationData(request));
		else if (button.equals("<< FIRST"))
		{
			PaginationData pd = getExamPaginationData(request);
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
				refreshListOfExamsToBeDisplayed(request, pd);
				setExamPaginationData(request, pd);
			}
		}
		else if (button.equals("< PREV"))
		{
			PaginationData pd = getExamPaginationData(request);
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
				refreshListOfExamsToBeDisplayed(request, pd);
				setExamPaginationData(request, pd);
			}
		}
		else if (button.equals("NEXT >"))
		{
			PaginationData pd = getExamPaginationData(request);
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
				refreshListOfExamsToBeDisplayed(request, pd);
				setExamPaginationData(request, pd);
			}
		}
		else if (button.equals("LAST >>"))
		{
			PaginationData pd = getExamPaginationData(request);
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
				refreshListOfExamsToBeDisplayed(request, pd);
				setExamPaginationData(request, pd);
			}
		}
		else if (button.equals("REFRESH")) 
		{
			PaginationData pd = getExamPaginationData(request);
			
			int quantity = Integer.parseInt(getIdAppendedToRequestParameter(request, "quantity"));
			
			if (quantity != pd.getPageSize()) {
				pd.setPageSize(quantity);
			}

			refreshListOfExamsToBeDisplayed(request, pd);
			setExamPaginationData(request, pd);
		}
		else
		{
			String id = getIdAppendedToRequestParameter(request, "exam_nameOfLastPressedButton");
			String btnValue = request.getParameter("exam_valueOfLastPressedButton");
			
			if (!StringUtil.isNullOrEmpty(id))
			{
				if (btnValue != null)
				{
					if (btnValue.equals("Take Exam"))
						fwdPage = "/beginExam.jsp?examId=" + id;
					else if (btnValue.equals("Edit")) {
						fwdPage = "/secured/exam.jsp?examId=" + id;
						
						EventDispatcher.getInstance().fireEvent(request, EventConstants.EDIT_EXAM_BEFORE);
					}
					else if (btnValue.equals("Delete")) {
						EventDispatcher.getInstance().fireEvent(request, EventConstants.DELETE_EXAM_BEFORE);
						
						ExamManager.deleteExam(id);
						
						new InitializeListOfExamsInSessionAction().doAction(request, response);
						new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
					}
					else if (btnValue.equals("Detail Exam")) {
						fwdPage = "/displayExam.jsp?examId=" + id;
						
						request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "Admin Page List of Exams");						
					}
				}
			}
		}
		
		request.getSession().setAttribute("tabIndex", Constants.PROFILE_EXAM_TAB_INDEX);
		
		forwardToJSP(request, response, fwdPage);
	}

	private void handleFilterButtonPress(HttpServletRequest request, PaginationData pd) {
		HttpSession session = request.getSession();
		String filterText = request.getParameter("containsFilter");
		
		Collection<Exam> coll = null; 
		
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		
		if (user != null)
			coll = ExamManager.getAllExamsWithTitlesThatContain(filterText, pd);

		if (parametersIndicateThatFilterWasApplied(filterText))
			pd.setTotalItemCount(coll.size());

		request.getSession().setAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED, coll);

		// store the filter we just used
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, pd.getPageSize());
		
		session.setAttribute(Constants.DO_NOT_INITIALIZE_PROFILE_MRU_SETTINGS, Boolean.TRUE);		
	}

	private void refreshListOfExamsToBeDisplayed(HttpServletRequest request, PaginationData pd) {
		HttpSession session = request.getSession();
		
		String filterText = request.getParameter("containsFilter");
		
		Collection<Exam> coll = null; 
		
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		
		if (user != null)
			coll = ExamManager.getAllExamsWithTitlesThatContain(filterText, pd);
		
		if (parametersIndicateThatFilterWasApplied(filterText))
			pd.setTotalItemCount(coll.size());

		request.getSession().setAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED, coll);

		// store the filter we just used
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, pd.getPageSize());

		session.setAttribute(Constants.DO_NOT_INITIALIZE_PROFILE_MRU_SETTINGS, Boolean.TRUE);
	}
	
	private PaginationData getExamPaginationData(HttpServletRequest request) {
		return (PaginationData)request.getSession().getAttribute(Constants.EXAM_PAGINATION_DATA);
	}
	
	private void setExamPaginationData(HttpServletRequest request, PaginationData pd) {
		request.getSession().setAttribute(Constants.EXAM_PAGINATION_DATA, pd);
	}
	
	private boolean parametersIndicateThatFilterWasApplied(String filterText) {
		return !StringUtil.isNullOrEmpty(filterText);
	}
	
}
