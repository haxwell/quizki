package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamGenerationManager;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.utils.ExamHistory;
import com.haxwell.apps.questions.utils.ExamUtil;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForRunningAnExamFilter")
public class InitializeSessionForRunningAnExamFilter extends AbstractFilter {

    public InitializeSessionForRunningAnExamFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Logger log = Logger.getLogger(InitializeSessionForRunningAnExamFilter.class.getName());
		
		log.log(java.util.logging.Level.FINE, "In the InitializeSessionForRunningAnExamFilter::doFilter() method!");
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();
			
			boolean examInProgress = (session.getAttribute(Constants.EXAM_IN_PROGRESS) != null);
						
			if (!examInProgress)
			{
				log.log(java.util.logging.Level.FINER, "Exam is not in progress, so proceeding to initialize session exam variables.");
				
				// 1. Get an exam object
				Exam exam = null;
				Object examIdRequestParameter = req.getParameter("examId");
				
				// If the request does not have an exam ID, look for other parameters
				if (examIdRequestParameter == null)
				{
					Object topicIdRequestParameter = req.getParameter("topicId");
					
					if (topicIdRequestParameter != null) {
						List<Long> list = new ArrayList<Long>();
						list.add(Long.parseLong(topicIdRequestParameter.toString()));
						
						exam = ExamGenerationManager.generateExam(3, list, new ArrayList<Long>(), DifficultyConstants.INTERMEDIATE, false);
					}
					else if (session.getAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_TAKEN) != null)
					{
						session.setAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_TAKEN, null);
						exam = (Exam)session.getAttribute(Constants.CURRENT_EXAM);
					}
				}
				else {
					long examId = Long.parseLong(examIdRequestParameter.toString());
					exam = ExamManager.getExam(examId);
				}
				
				// 2. Set session attributes based on the exam object we just got..
				if (exam != null) {
					ExamHistory examHistory = ExamManager.initializeExamHistory(exam);
					
					String examDifficulty = new ExamUtil().getExamDifficultyAsString(exam);
					exam.setDifficulty(examDifficulty);
					
					session.setAttribute(Constants.CURRENT_EXAM_HISTORY, null);
					session.setAttribute(Constants.CURRENT_EXAM_HISTORY, examHistory);
					
					session.setAttribute(Constants.CURRENT_EXAM, null);
					session.setAttribute(Constants.CURRENT_EXAM, exam);
		
					Question currentQuestion = examHistory.getNextQuestion();
					
					setCurrentQuestion(req, Constants.CURRENT_QUESTION, null);
					setCurrentQuestion(req, Constants.CURRENT_QUESTION, currentQuestion);
					
					int qn = examHistory.getCurrentQuestionNumber();
					if (qn == 0) qn = 1;
					
					session.setAttribute(Constants.CURRENT_QUESTION_NUMBER, qn);
					session.setAttribute(Constants.TOTAL_POTENTIAL_QUESTIONS, new Integer(examHistory.getTotalPotentialQuestions()));
					
					session.setAttribute(Constants.EXAM_HISTORY_QUESTION_INDEX_LIST, examHistory.getQuestionIDListAsCSV());
					
					List<String> list = new ArrayList<String>();
					
					list.add("");list.add("");list.add("");
					
					session.setAttribute("listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion", list);
					
					session.setAttribute(Constants.EXAM_IN_PROGRESS, true);
					
					session.setAttribute(Constants.QUESTION_TOPICS, ExamManager.getAllQuestionTopicsAsStrings(exam));
					
					User user = (User)session.getAttribute(Constants.CURRENT_USER_ENTITY);
					
					if (user != null) {
						session.setAttribute(Constants.FEEDBACK_FOR_CURRENT_USER_AND_EXAM, ExamManager.getFeedback(user.getId(), exam.getId()));
					}
					
					log.log(java.util.logging.Level.FINER, "Exam is NOW in progress, session exam variables initialized.");				
				}
				else
					log.log(Level.FINER, "Exam is running, so nothing to do...");
				
				session.setAttribute(Constants.SHOULD_LOGIN_LINK_BE_DISPLAYED, Boolean.FALSE);
			}
			else
				log.log(Level.FINER, "Exam is already running.. no need to initialize the session for running an exam..");
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		log.log(Level.FINE, "Leaving InitializeSessionForRunningAnExamFilter");
	}
}
