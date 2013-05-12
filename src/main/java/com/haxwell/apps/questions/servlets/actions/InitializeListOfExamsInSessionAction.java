package com.haxwell.apps.questions.servlets.actions;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.utils.FilterUtil;
import com.haxwell.apps.questions.utils.PaginationData;

/**
 * Ensures that the list of exams in the session is the most up to date it can be.
 * 
 * @author johnathanj
 */
public class InitializeListOfExamsInSessionAction implements AbstractServletAction {

	Logger log = Logger.getLogger(InitializeListOfExamsInSessionAction.class.getName());
	
	public final static boolean GET_ALL_EXAMS = true;
	
	public int doAction(ServletRequest request, ServletResponse response, boolean forceGetAllExams) {
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			
			if (req.getSession().getAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED) == null) {
				
				PaginationData epd = (PaginationData)req.getSession().getAttribute(Constants.EXAM_PAGINATION_DATA);
				
				User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
				Collection<Exam> coll = null;
	
				if (user == null || forceGetAllExams) { 
					coll = ExamManager.getAllExams(epd);
					req.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, Constants.ALL_ITEMS);
				} else { 
					coll = ExamManager.getAllExamsForUser(user.getId(), epd);
					req.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, Constants.MY_ITEMS);
				}
				
				req.getSession().setAttribute("fa_listofexamstobedisplayed", coll);
				
				ExamManager.setTopicsAttribute(coll);
				
				log.log(Level.FINER, "Just added " + coll.size() + " exams to the fa_listofexamstobedisplayed list");
		}}
		
		return 0;
	}
	
	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		return doAction(request, response, false);
	}
}

