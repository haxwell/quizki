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
	private static final long serialVersionUID = 135739L;
	private Logger log = Logger.getLogger(TakeExamServlet.class.getName());
	
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
		
		String fwdPage = "/takeExam.jsp";
		String button = request.getParameter("button");
		ExamHistory examHistory = (ExamHistory)request.getSession().getAttribute(Constants.CURRENT_EXAM_HISTORY);
		
		if (button.equals("NEXT >"))
		{
			log.log(Level.INFO, "****** In NEXT > handler");
			
			Map<String, String> answers = QuestionUtil.getChosenAnswers(request);
			boolean b = examHistory.recordAnswerToCurrentQuestion(answers);
			
			AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(examHistory.getMostRecentlyUsedQuestion());			
			
			if (!checker.questionHasBeenAnswered(answers)) {
				ArrayList<String> errors = new ArrayList<String>();
				errors.add("You did not answer the question!");
				
				request.setAttribute(Constants.VALIDATION_ERRORS, errors);
				
				AbstractExamHistoryPostProcessor aehpp = ExamHistoryPostProcessorFactory.get(examHistory.getMostRecentlyUsedQuestion());
				if (aehpp != null)
				{
					aehpp.afterQuestionDisplayedWithoutBeingAnswered(request, examHistory);
					// and then, since we're going to be displaying the same unanswered question...
					aehpp.beforeQuestionDisplayed(request, examHistory);
				}
			}
			else {
				
				log.log(Level.INFO, "....and the answers just recorded are: ");
				log.log(Level.INFO, StringUtil.getToStringOfEach(examHistory.getFieldnamesSelectedAsAnswersToCurrentQuestion()));
				
				AbstractExamHistoryPostProcessor aehpp = ExamHistoryPostProcessorFactory.get(examHistory.getMostRecentlyUsedQuestion());
				if (aehpp != null) aehpp.afterQuestionDisplayed(request, examHistory);
				
				Question nextQuestion = examHistory.getNextQuestion();
				request.getSession().setAttribute(Constants.CURRENT_QUESTION, nextQuestion);
				
				if (nextQuestion != null)
					aehpp = ExamHistoryPostProcessorFactory.get(nextQuestion);
				else
					aehpp = null;
				
				if (aehpp != null) aehpp.beforeQuestionDisplayed(request, examHistory);
				
				if (nextQuestion == null)
					fwdPage = "/examWillBeGraded.jsp";
				else {
					List<String> areThereExistingAnswersToCurrentQuestionList = getListOfFieldnamesInWhichUserInteractedWithAsAnAnswerToCurrentQuestion(examHistory, nextQuestion);
					request.getSession().setAttribute("listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion", areThereExistingAnswersToCurrentQuestionList);
				}
				
				request.getSession().setAttribute(Constants.CURRENT_QUESTION_NUMBER, examHistory.getCurrentQuestionNumber());
				request.getSession().setAttribute(Constants.TOTAL_POTENTIAL_QUESTIONS, new Integer(examHistory.getTotalPotentialQuestions()));
			}
		}
		else if (button.equals("< PREV") || button.equals("Go Back!"))
		{
			log.log(Level.INFO, "***** IN PREV method ********");
			
			handleAfterQuestionDisplayedTasks(request, examHistory);
			handleBeforeQuestionDisplayedTasks(request, examHistory, examHistory.getPrevQuestion());
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
		else if (button.equals("<< FIRST"))
		{
			log.log(Level.INFO, "***** IN << FIRST method ********");
			
			handleAfterQuestionDisplayedTasks(request, examHistory);
			handleBeforeQuestionDisplayedTasks(request, examHistory, examHistory.getFirstQuestion());
		}
		else if (button.equals("LAST >>"))
		{
			log.log(Level.INFO, "***** IN LAST >> method ********");
			
			handleAfterQuestionDisplayedTasks(request, examHistory);
			handleBeforeQuestionDisplayedTasks(request, examHistory, examHistory.getLastQuestion());
		}
		else if (button.equals("Go To #")) {
			log.log(Level.INFO, "***** IN GO TO #... method ********");
			
			handleAfterQuestionDisplayedTasks(request, examHistory);

			int jumpToNumber = -1;
			boolean parseError = false;
			
			try {
				jumpToNumber = Integer.parseInt(request.getParameter("jumpToNumber"));
				parseError = ((jumpToNumber < 0) || (jumpToNumber > examHistory.getTotalPotentialQuestions())); 
			}
			catch (NumberFormatException nfe) {
				parseError = true;
			}
			finally {
				if (parseError)
					jumpToNumber = examHistory.getCurrentQuestionNumber();
			}

			handleBeforeQuestionDisplayedTasks(request, examHistory, examHistory.getQuestionByIndex(jumpToNumber));
		}

		redirectToJSP(request, response, fwdPage);
	}

	private void handleBeforeQuestionDisplayedTasks(HttpServletRequest request,
			ExamHistory examHistory, Question question) {
		AbstractExamHistoryPostProcessor aehpp;
		if (question != null) 
			request.getSession().setAttribute(Constants.CURRENT_QUESTION, question);
		
		aehpp = ExamHistoryPostProcessorFactory.get(examHistory.getMostRecentlyUsedQuestion());
		if (aehpp != null) aehpp.beforeQuestionDisplayed(request, examHistory);

		request.getSession().setAttribute(Constants.CURRENT_QUESTION_NUMBER, examHistory.getCurrentQuestionNumber());
		request.getSession().setAttribute(Constants.TOTAL_POTENTIAL_QUESTIONS, new Integer(examHistory.getTotalPotentialQuestions()));
		
		List<String> list = getListOfFieldnamesInWhichUserInteractedWithAsAnAnswerToCurrentQuestion(examHistory, question);

		log.log(Level.INFO, "The Existing answers to the current question");
		log.log(Level.INFO, StringUtil.getToStringOfEach(list));
		
		request.getSession().setAttribute("listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion", list);
	}

	private void handleAfterQuestionDisplayedTasks(HttpServletRequest request,
			ExamHistory examHistory) {
		Map<String, String> answers = QuestionUtil.getChosenAnswers(request);
		boolean b = examHistory.recordAnswerToCurrentQuestion(answers);
		
		AbstractExamHistoryPostProcessor aehpp = ExamHistoryPostProcessorFactory.get(examHistory.getMostRecentlyUsedQuestion());
		if (aehpp != null) aehpp.afterQuestionDisplayed(request, examHistory);
	}
	
	private List<String> getListOfFieldnamesInWhichUserInteractedWithAsAnAnswerToCurrentQuestion(ExamHistory eh, Question q)
	{
		return QuestionUtil.getUIArray_FieldWasSelected(eh.getFieldnamesSelectedAsAnswersToCurrentQuestion(), q);
	}
}
