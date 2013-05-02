package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.utils.CollectionUtil;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForCreatingAnExamFilter")
public class InitializeSessionForCreatingAnExamFilter extends AbstractFilter {

    public InitializeSessionForCreatingAnExamFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(InitializeSessionForCreatingAnExamFilter.class.getName());
		
		log.log(Level.INFO, "beginning InitializeSessionForCreatingAnExamFilter...");

		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

//			boolean currentExamHasBeenPersisted = (session.getAttribute(Constants.CURRENT_EXAM_HAS_BEEN_PERSISTED) != null);
			boolean mruMineAllOrSelectedHasBeenSet = false;
			
//			if (currentExamHasBeenPersisted) {
//				session.setAttribute(Constants.CURRENT_EXAM, null);
//				session.setAttribute(Constants.CURRENT_EXAM_HAS_BEEN_PERSISTED, null);
//				session.setAttribute(Constants.IN_EDITING_MODE, null);
//			}
			
			String examId = req.getParameter("examId");
			
			if (examId != null) {
				Exam exam = ExamManager.getExam(Integer.parseInt(examId));
				
				User user = (User)session.getAttribute(Constants.CURRENT_USER_ENTITY);
				
				if (user == null || exam.getUser().getId() != user.getId()) {
					request.setAttribute("doNotAllowEntityEditing", Boolean.TRUE);
				}
				else {
					session.setAttribute(Constants.CURRENT_EXAM, exam);
					session.setAttribute(Constants.IN_EDITING_MODE, Boolean.TRUE);					
					
					Set<Question> questionSet = exam.getQuestions();
					
					List<Long> selectedQuestionIds = CollectionUtil.getListOfIds(questionSet);
					
					session.setAttribute(Constants.CURRENT_EXAM_SELECTED_QUESTION_IDS, selectedQuestionIds);
					session.setAttribute(Constants.ONLY_SELECTED_QUESTIONS_SHOULD_BE_SHOWN, Boolean.TRUE);
					
					if (selectedQuestionIds.size() > 0) {
						session.setAttribute(Constants.MRU_FILTER_MINE_OR_ALL_OR_SELECTED, Constants.SELECTED_ITEMS);
						mruMineAllOrSelectedHasBeenSet = true;
					}
				}
			}
			
			if (req.getSession().getAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS) == null) {
//				if (!mruMineAllOrSelectedHasBeenSet)
//					session.setAttribute(Constants.MRU_FILTER_MINE_OR_ALL_OR_SELECTED, Constants.ALL_ITEMS);

				session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);
				session.setAttribute(Constants.MRU_FILTER_PAGINATION_QUANTITY, Constants.DEFAULT_PAGINATION_PAGE_SIZE);
				session.setAttribute(Constants.MRU_FILTER_QUESTION_TYPE, TypeConstants.ALL_TYPES);
				session.setAttribute(Constants.SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS, Constants.MY_ITEMS);
			}
		}
		
		log.log(Level.INFO, "ending InitializeSessionForCreatingAExamFilter...");
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
