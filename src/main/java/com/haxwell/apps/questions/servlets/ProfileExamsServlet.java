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
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.servlets.actions.InitializeListOfExamsInSessionAction;
import com.haxwell.apps.questions.servlets.actions.SetUserContributedQuestionAndExamCountInSessionAction;

/**
 * Servlet implementation class LoginServlet
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
		
		String button = request.getParameter("runFilter");
		
		if (button == null) button = "";
		
		if (button.equals("Run Filter -->"))
			handleFilterButtonPress(request);
		else
		{
			Collection<Exam> coll = (Collection<Exam>)request.getSession().getAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED);
			
			if (coll != null)
			{
				boolean buttonFound = false;
				Iterator<Exam> iterator = coll.iterator();
				
				while (!buttonFound && iterator.hasNext())
				{
					Exam e = iterator.next();
					button = request.getParameter("examButton_"+e.getId());
					
					if (button != null)
					{
						if (button.equals("Take Exam"))
							fwdPage = "/beginExam.jsp?examId=" + e.getId();
						else if (button.equals("Edit Exam"))
							fwdPage = "/secured/exam.jsp?examId=" + e.getId();
						else if (button.equals("Delete Exam")) {
							ExamManager.deleteExam(e);
							request.getSession().setAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED, null);
							
							new InitializeListOfExamsInSessionAction().doAction(request, response);
							new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
						}
						
						buttonFound = true;
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
