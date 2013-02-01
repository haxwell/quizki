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
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.utils.ExamHistory;
import com.haxwell.apps.questions.utils.ExamHistory.AnsweredQuestion;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

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
			Question question = QuestionManager.getQuestionById(req.getParameter("questionId"));
			
			req.getSession().setAttribute(Constants.CURRENT_QUESTION, question);
			
			User u = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			
			if (QuestionManager.userCanEditThisQuestion(question, u))
			{
				req.getSession().setAttribute(Constants.SHOULD_ALLOW_QUESTION_EDITING, Boolean.TRUE);
			}
			
			ExamHistory examHistory = (ExamHistory)req.getSession().getAttribute(Constants.CURRENT_EXAM_HISTORY);
			
			List<String> areThereExistingAnswersToCurrentQuestionList = new ArrayList<String>();
			req.getSession().setAttribute("booleanExamHistoryIsPresent", examHistory != null);

			List<Choice> questionChoiceList = QuestionUtil.getChoiceList(question);			
			
			if (examHistory != null) {
				areThereExistingAnswersToCurrentQuestionList = getListSayingAnElementIsCheckedOrNot(examHistory, question);
				req.getSession().setAttribute("listSayingAnElementIsCheckedOrNot", areThereExistingAnswersToCurrentQuestionList);				
				
				long qtID = question.getQuestionType().getId();
				AnsweredQuestion aq = examHistory.getUserSuppliedAnswers(question);
				
				// Special handling for String questions
				if (qtID == TypeConstants.STRING) {
					String userSuppliedAnswer = aq.answers.values().iterator().next();
					req.getSession().setAttribute("userSuppliedAnswerToStringQuestion", "\"" + userSuppliedAnswer + "\"");					
				}
				
				if (qtID == TypeConstants.SEQUENCE) {
					StringBuffer chosenSequenceNumbers = new StringBuffer(StringUtil.startJavascriptArray());
					
					for (Choice c : questionChoiceList)
					{
						String fieldname = QuestionUtil.getFieldnameForChoice(question, c);
						String chosenSeqNumForChoice = aq.answers.get(fieldname);
						
						StringUtil.addToJavascriptArray(chosenSequenceNumbers, chosenSeqNumForChoice);
					}

					StringUtil.closeJavascriptArray(chosenSequenceNumbers);
					
					req.getSession().setAttribute(Constants.LIST_OF_SEQUENCE_NUMBERS_THE_USER_CHOSE, chosenSequenceNumbers.toString());
				}
			}

			handleSequenceTypeQuestions(req, questionChoiceList, question);
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	private void handleSequenceTypeQuestions(HttpServletRequest req, List<Choice> questionChoiceList, Question question)
	{
		if (question != null && question.getQuestionType().getId() == TypeConstants.SEQUENCE) {
			
			////////////////////
			StringBuffer sb = new StringBuffer(StringUtil.startJavascriptArray());
			List<Choice> listBySequenceNumber = QuestionUtil.getChoiceListBySequenceNumber(question);
			
			for (Choice c: listBySequenceNumber)
			{
				int index = questionChoiceList.indexOf(c);
				
				StringUtil.addToJavascriptArray(sb, index+"");
			}
			
			StringUtil.closeJavascriptArray(sb);
			
			req.getSession().setAttribute(Constants.LIST_OF_INDEXES_TO_CHOICE_LIST_BY_SEQUENCE_NUMBER, sb.toString());
		}
	}
	
	private List<String> getListSayingAnElementIsCheckedOrNot(ExamHistory eh, Question q)
	{
		return QuestionUtil.getUIArray_FieldWasSelected(eh.getFieldnamesSelectedAsAnswersForQuestion(q), q);
	}

}
