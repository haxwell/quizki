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
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;

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
		
		String fwdPage = "/listExams.jsp";
		String button = request.getParameter("button");
		Collection<Exam> coll = null;
		
		if (button == null) // it wasn't the main page Button pressed, it was a detail row type button 
		{
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
					else if (action.equals("Detail Exam"))
					{
						fwdPage = "/displayExam.jsp?examId=" + e.getId();
						
						request.getSession().setAttribute(Constants.CURRENT_EXAM, e);
						request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "List Exams");
					}
					else if (action.equals("Edit Exam"))
					{
						fwdPage = "/secured/exam.jsp?examId=" + e.getId();
					}
				}
			}
		}
		else // it was the main page Button
		{
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			
			String group1Param = request.getParameter("group1");
			
			if (user == null) 
			{
				if (group1Param != null && group1Param.equals("mine"))
					fwdPage = "/login.jsp";
				else
				{
					coll = ExamManager.getAllExams();
				}
			}
			else if (user != null) 
			{
				if (group1Param != null && group1Param.equals("mine"))
				{
					coll = ExamManager.getAllExamsForUser(user.getId());
				}
				else
				{
					coll = ExamManager.getAllExams();
				}
	
				request.getSession().setAttribute("fa_listofexamstobedisplayed", coll);
			}
		}
			
		redirectToJSP(request, response, fwdPage);
	}
}
