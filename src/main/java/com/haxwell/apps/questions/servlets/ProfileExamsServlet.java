package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.servlets.actions.InitializeListOfExamsInSessionAction;
import com.haxwell.apps.questions.servlets.actions.SetUserContributedQuestionAndExamCountInSessionAction;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Servlet implementation class ProfileExamsServlet
 */
@WebServlet("/secured/ProfileExamsServlet")
public class ProfileExamsServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileExamsServlet() {
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
			String name = request.getParameter("exam_nameOfLastPressedButton");
			String btnValue = request.getParameter("exam_valueOfLastPressedButton");
			
			if (!StringUtil.isNullOrEmpty(name))
			{
				String id= name.substring(name.indexOf('_')+1);
			
				if (btnValue != null)
				{
					if (btnValue.equals("Take Exam"))
						fwdPage = "/beginExam.jsp?examId=" + id;
					else if (btnValue.equals("Edit Exam"))
						fwdPage = "/secured/exam.jsp?examId=" + id;
					else if (btnValue.equals("Delete Exam")) {
						ExamManager.deleteExam(id);
						request.getSession().setAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED, null);
						
						new InitializeListOfExamsInSessionAction().doAction(request, response);
						new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
					}
				}
			}
		}
		
		request.getSession().setAttribute("tabIndex", Constants.PROFILE_EXAM_TAB_INDEX);
		
		forwardToJSP(request, response, fwdPage);
	}

	private void handleFilterButtonPress(HttpServletRequest request) {
		String filterText = request.getParameter("containsFilter");
		
		Collection<Exam> coll = null; 
		
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
		
		if (user != null)
			coll = ExamManager.getAllExamsCreatedByAGivenUserWithTitlesThatContain(user.getId(), filterText);

		request.getSession().setAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED, coll);

		// store the filter we just used
		request.getSession().setAttribute(Constants.MRU_FILTER_TEXT, filterText);
	}
}
