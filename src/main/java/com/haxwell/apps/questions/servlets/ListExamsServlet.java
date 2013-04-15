package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.servlets.actions.InitializeNewExamInSessionAction;
import com.haxwell.apps.questions.utils.FilterUtil;
import com.haxwell.apps.questions.utils.PaginationData;

/**
 * Servlet implementation class ListExamServlet
 */
@WebServlet("/ListExamsServlet")
public class ListExamsServlet extends AbstractHttpServlet {
	
	Logger log = Logger.getLogger(ListExamsServlet.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListExamsServlet() {
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
		Object o = session.getAttribute(Constants.MRU_FILTER_MINE_OR_ALL);		
		String button = request.getParameter("button");
		
		Exam examObj = getExamBean(request);		

		String fwdPage = "/listExams.jsp";
		Collection<Exam> coll = null;
		
		if (button == null) button = "";
		
		if (button.equals("Apply Filter -->")) {
			handleFilterButtonPress(request, getExamPaginationData(request));
		}
		else if (button.equals("Clear Filter")) {
			handleClearFilterButtonPress(request, coll);
		}
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
		{// it wasn't the main page Button pressed, it was a detail row type button
			coll = (Collection<Exam>)request.getSession().getAttribute("fa_listofexamstobedisplayed");         
			
			if (coll != null) {
				Iterator<Exam> iterator = coll.iterator();
				String action = null;
				Exam e = null;
				
				while (iterator.hasNext() && action == null)
				{
					// for each exam in the collection, see if there is a parameter in the request with an ID
					//  matching the exam.. if so, then thats the one we're working on..
					e = iterator.next();
					action = request.getParameter("examButton_" + e.getId());
				}
				
				if (action != null) {
					if (action.equals("Take Exam"))
						fwdPage = "/beginExam.jsp?examId=" + e.getId();
					else if (action.equals("Detail Exam")) {
						fwdPage = "/displayExam.jsp?examId=" + e.getId();
						
						request.getSession().setAttribute(Constants.CURRENT_EXAM, e);
						request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "List Exams");
					}
				}
			}
		}

		forwardToJSP(request, response, fwdPage);
	}

	private void handleClearFilterButtonPress(HttpServletRequest request, Collection<Exam> coll) {
		request.getSession().setAttribute("fa_listofexamstobedisplayed", coll);

		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, null);
	}

	private void handleFilterButtonPress(HttpServletRequest request, PaginationData pd) {
		String filterText = request.getParameter("containsFilter");
		String mineOrAll = request.getParameter(Constants.SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS);
		
		HttpSession session = request.getSession();
		
		Object o = session.getAttribute(Constants.MRU_FILTER_MINE_OR_ALL);
		
		List<Exam> list = null;
		
		if (mineOrAll.equals(Constants.MY_ITEMS_STR))
		{
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

			if (user != null)
				list = ExamManager.getAllExamsCreatedByAGivenUserWithTitlesThatContain(user.getId(), filterText, pd);
		}
		else 
		{
			list = ExamManager.getAllExamsWithTitlesThatContain(filterText, pd);
		}
		
		if (list != null)
			ExamManager.setTopicsAttribute(list);

		session.setAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED, list);

		session.setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		session.setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, FilterUtil.convertToInt(mineOrAll));
		session.setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, pd.getPageSize());		
	}
	
	private void refreshListOfExamsToBeDisplayed(HttpServletRequest request, PaginationData pd) {
		String filterText = request.getParameter("containsFilter");
		int mineOrAll = ((Integer)request.getSession().getAttribute(Constants.MRU_FILTER_MINE_OR_ALL)).intValue();

		HttpSession session = request.getSession();
		List<Exam> list = null;
		
		if (mineOrAll == Constants.MY_ITEMS)
		{
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

			if (user != null)
				list = ExamManager.getAllExamsCreatedByAGivenUserWithTitlesThatContain(user.getId(), filterText, pd);
		}
		else 
		{
			list = ExamManager.getAllExamsWithTitlesThatContain(filterText, pd);
		}
		
		ExamManager.setTopicsAttribute(list);		

		session.setAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED, list);

		session.setAttribute(Constants.MRU_FILTER_TEXT, filterText);
		session.setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, mineOrAll);
		session.setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, pd.getPageSize());		
	}
	
	private Exam getExamBean(HttpServletRequest request) {
		Exam exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);
		
		if (exam == null) {
			new InitializeNewExamInSessionAction().doAction(request, null);
			exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);			
		}

		return exam;
	}
	
	private PaginationData getExamPaginationData(HttpServletRequest request) {
		return (PaginationData)request.getSession().getAttribute(Constants.EXAM_PAGINATION_DATA);
	}

	private void setExamPaginationData(HttpServletRequest request, PaginationData pd) {
		request.getSession().setAttribute(Constants.EXAM_PAGINATION_DATA, pd);
	}
}
