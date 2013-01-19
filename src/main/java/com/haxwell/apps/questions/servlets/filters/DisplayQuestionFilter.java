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

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.utils.ExamHistory;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.ExamHistory.AnsweredQuestion;

/**
 * Puts the things that DisplayQuestions.jsp needs in the session
 * 
 */
@WebFilter("/DisplayQuestionFilter")
public class DisplayQuestionFilter extends AbstractFilter {

	Logger log = Logger.getLogger(DisplayQuestionFilter.class.getName());
	
	public DisplayQuestionFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.log(Level.INFO, "...in DisplayQuestionsFilter()");
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);

			String str = req.getParameter("questionId");
			
			Question question = QuestionManager.getQuestionById(str);
			
			req.getSession().setAttribute(Constants.CURRENT_QUESTION, question);
			
			ExamHistory examHistory = (ExamHistory)req.getSession().getAttribute(Constants.CURRENT_EXAM_HISTORY);
			
			List<String> areThereExistingAnswersToCurrentQuestionList = new ArrayList<String>();
			req.getSession().setAttribute("booleanExamHistoryIsPresent", examHistory != null);
			
			if (examHistory != null) {
				areThereExistingAnswersToCurrentQuestionList = getListSayingAnElementIsCheckedOrNot(examHistory, question);
				req.getSession().setAttribute("listSayingAnElementIsCheckedOrNot", areThereExistingAnswersToCurrentQuestionList);				
				
				// Special handling for String questions
				if (question.getQuestionType().getId() == TypeConstants.STRING) {
					AnsweredQuestion aq = examHistory.getUserSuppliedAnswers(question);
					
					String userSuppliedAnswer = aq.answers.values().iterator().next();
					req.getSession().setAttribute("userSuppliedAnswerToStringQuestion", "\"" + userSuppliedAnswer + "\"");					
				}
			}
			

		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
	
	private List<String> getListSayingAnElementIsCheckedOrNot(ExamHistory eh, Question q)
	{
		return QuestionUtil.getUIArray_FieldWasSelected(eh.getFieldnamesSelectedAsAnswersForQuestion(q), q);
	}

}
