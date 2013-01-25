package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.factories.QuestionTypeCheckerFactory;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.utils.AbstractExamHistoryPostProcessor;
import com.haxwell.apps.questions.utils.ExamHistory;
import com.haxwell.apps.questions.utils.ExamHistoryPostProcessorFactory;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Servlet implementation class ExamServlet
 */
@WebServlet("/TakeExamServlet")
public class TakeExamServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeExamServlet() {
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
		
		Logger log = Logger.getLogger(TakeExamServlet.class.getName());
		
		String fwdPage = "/takeExam.jsp";
		String button = request.getParameter("button");
		ExamHistory examHistory = (ExamHistory)request.getSession().getAttribute(Constants.CURRENT_EXAM_HISTORY);
		
		if (button.equals("NEXT >"))
		{
			log.log(Level.INFO, "****** In NEXT > handler");
			
			Map<String, String> answers = QuestionUtil.getChosenAnswers(request);
			AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(examHistory.getMostRecentlyUsedQuestion());
			
			if (!checker.questionHasBeenAnswered(answers)){
				ArrayList<String> errors = new ArrayList<String>();
				errors.add("You did not answer the question!");
				
				request.setAttribute(Constants.VALIDATION_ERRORS, errors);
			}
			else {
				
				boolean b = examHistory.recordAnswerToCurrentQuestion(answers);
				
				log.log(Level.INFO, "....and the answers just recorded are: ");
				log.log(Level.INFO, StringUtil.getToStringOfEach(examHistory.getFieldnamesSelectedAsAnswersToCurrentQuestion()));
				
				AbstractExamHistoryPostProcessor aehpp = ExamHistoryPostProcessorFactory.get(examHistory.getMostRecentlyUsedQuestion());
				if (aehpp != null) aehpp.processOnNext(request, examHistory);
				
				Question nextQuestion = examHistory.getNextQuestion();
				request.getSession().setAttribute(Constants.CURRENT_QUESTION, nextQuestion);
				
				
				// TODO.. I think the post processor method names need to be changed to 'beforeQuestionDisplayed' 'afterQuestionDisplayed'
				//  something like that.. and the beforeQuestionDisplayed needs to be called here.. the issue is that when String question
				//  type is after Sequence, the previously entered value for the String question is not being displayed.
				
				
				if (nextQuestion == null)
					fwdPage = "/examWillBeGraded.jsp";
				else {
					List<String> areThereExistingAnswersToCurrentQuestionList = getListSayingAnElementIsCheckedOrNot(examHistory, nextQuestion);
					request.getSession().setAttribute("listSayingAnElementIsCheckedOrNot", areThereExistingAnswersToCurrentQuestionList);
				}
				
				request.getSession().setAttribute(Constants.CURRENT_QUESTION_NUMBER, examHistory.getCurrentQuestionNumber());
				request.getSession().setAttribute(Constants.TOTAL_POTENTIAL_QUESTIONS, examHistory.getTotalPotentialQuestions());
			}
		}
		else if (button.equals("< PREV"))
		{
			log.log(Level.INFO, "***** IN PREV method ********");
			
			Map<String, String> answers = QuestionUtil.getChosenAnswers(request);
			
			boolean b = examHistory.recordAnswerToCurrentQuestion(answers);
			
			Question question = examHistory.getPrevQuestion();
			
			if (question != null) 
				request.getSession().setAttribute(Constants.CURRENT_QUESTION, question);
			
			AbstractExamHistoryPostProcessor aehpp = ExamHistoryPostProcessorFactory.get(examHistory.getMostRecentlyUsedQuestion());
			if (aehpp != null) aehpp.processOnPrevious(request, examHistory);

			request.getSession().setAttribute(Constants.CURRENT_QUESTION_NUMBER, examHistory.getCurrentQuestionNumber());
			request.getSession().setAttribute(Constants.TOTAL_POTENTIAL_QUESTIONS, examHistory.getTotalPotentialQuestions());
			
			List<String> areThereExistingAnswersToCurrentQuestionList = getListSayingAnElementIsCheckedOrNot(examHistory, question);

			log.log(Level.INFO, "The Existing answers to the current question");
			log.log(Level.INFO, StringUtil.getToStringOfEach(areThereExistingAnswersToCurrentQuestionList));
			
			request.getSession().setAttribute("listSayingAnElementIsCheckedOrNot", areThereExistingAnswersToCurrentQuestionList);
		}
		else if (button.equals("GRADE IT!"))
		{
			int correct = ExamManager.getNumberOfQuestionsAnsweredCorrectly(examHistory);
			int total = examHistory.getTotalNumberOfQuestions();
			
			request.getSession().setAttribute("totalNumberOfQuestions", total);
			request.getSession().setAttribute("numberOfQuestionsAnsweredCorrectly", correct);
			
			request.getSession().setAttribute(Constants.EXAM_IN_PROGRESS, null);
			
			fwdPage = "/examReportCard.jsp";
		}
		
		redirectToJSP(request, response, fwdPage);
	}
	
	private List<String> getListSayingAnElementIsCheckedOrNot(ExamHistory eh, Question q)
	{
		return QuestionUtil.getUIArray_FieldWasSelected(eh.getFieldnamesSelectedAsAnswersToCurrentQuestion(), q);
	}
}
