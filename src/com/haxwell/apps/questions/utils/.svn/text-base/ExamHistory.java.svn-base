package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.QuestionManager;

public class ExamHistory implements Iterable {
	
	private Question currentQuestion;
	private Question mostRecentlyUsedQuestion;
	
	List<Question> originalQuestionList; 
	List<Integer> questionIndexList;
	
	Map<Integer, AnsweredQuestion> mapOfExistingAnswers;
	Map<Question, Integer> mapOfQuestionToDisplayIndex; // display index = current question number at press time 	

	int currentIndex = -1;
	int currentQuestionNumber = 0;
	int highestQuestionNumber = 0;
	
	boolean currentQuestionAnswerHasBeenRecorded = false;
	
	private Iterator iterator;
	
	private Logger log;
	
	public ExamHistory(Exam exam)
	{
		log = Logger.getLogger(ExamHistory.class.getName());
		originalQuestionList = ExamManager.getQuestionList(exam);
		mapOfExistingAnswers = new HashMap<Integer, AnsweredQuestion>();
		mapOfQuestionToDisplayIndex = new HashMap<Question, Integer>();
		
		questionIndexList = RandomIntegerUtil.getRandomListOfNumbers(originalQuestionList.size());
	}
	
	public Question getNextQuestion() 
	{
		log.log(Level.INFO, "In getNextQuestion()");
		
		if (currentQuestion == null || currentQuestionAnswerHasBeenRecorded) 
		{
			currentQuestionNumber++;	
			
			if (currentQuestionNumber > highestQuestionNumber && currentQuestionNumber <= originalQuestionList.size())
				highestQuestionNumber = currentQuestionNumber;

			if (currentQuestionNumber <= originalQuestionList.size()) {
			
				setCurrentQuestion(originalQuestionList.get(questionIndexList.get(currentQuestionNumber - 1)));
				
				currentQuestionAnswerHasBeenRecorded = false;
				
				log.log(Level.INFO, "ExamHistory currentQuestionNUmber = " + currentQuestionNumber);
			}
			else
			{
				setCurrentQuestion(null);
			}
		}
		
		return currentQuestion;
	}
	
	public Question getPrevQuestion()
	{
		log.log(Level.INFO, "Beginning getPrevQuestion()");
		
		if (currentQuestionNumber > 1)
		{
			currentQuestionNumber--;
			
			setCurrentQuestion(originalQuestionList.get(questionIndexList.get(currentQuestionNumber - 1)));
			
			log.log(Level.INFO, "currentQuestionNumber = " + currentQuestionNumber + " // currentIndex = " + currentIndex + " currentQuestion = " + currentQuestion);
		}
		
		log.log(Level.INFO, "Ending getPrevQuestion()");
		log.log(Level.INFO, "returning currentQuestion = " + currentQuestion);
		
		return currentQuestion;
	}

	/**
	 * 
	 * @param answers
	 * @return true if the current question is correctly answered
	 */
	public boolean recordAnswerToCurrentQuestion(List<String> answers)
	{
		log.log(Level.FINE, "Begin RecordAnswerToCurrentQuestion");
		log.log(Level.FINER, "Answers array passed in: " + StringUtil.getToStringOfEach(answers));

		boolean b = false;
		
		if (currentQuestion != null) 
		{
			b = QuestionManager.isAnsweredCorrectly(currentQuestion, answers);
			
			log.log(Level.INFO, "currentQuestionNumber = " + currentQuestionNumber);
			log.log(Level.INFO, "This answer is " + (b ? "" : "NOT") + " correct.");
			
			log.log(Level.INFO, "Putting new AnsweredQuestionObject in mapOfExistingAnswers, key " + currentQuestionNumber);
			mapOfExistingAnswers.put(currentQuestionNumber, new AnsweredQuestion(currentQuestion, answers, b));
			mapOfQuestionToDisplayIndex.put(currentQuestion, currentQuestionNumber);
			
			currentQuestionAnswerHasBeenRecorded = true;
		}
		
		log.log(Level.FINE, "Ending RecordAnswerToCurrentQuestion");
		log.log(Level.FINER, "Returning: "+ b);
		
		return b;
	}

	public class AnsweredQuestion
	{
		public Question question;
		public List<String> answers;
		public boolean isCorrect = false;
		
		public AnsweredQuestion(Question q, List<String> answers, boolean b)
		{
			question = q;
			this.answers = answers;
			this.isCorrect = b;
		}

		public Question getQuestion() {
			return question;
		}

		public void setQuestion(Question question) {
			this.question = question;
		}

		public List<String> getAnswers() {
			return answers;
		}

		public void setAnswers(List<String> answers) {
			this.answers = answers;
		}

		public boolean getIsCorrect() {
			return isCorrect;
		}

		public void setIsCorrect(boolean isCorrect) {
			this.isCorrect = isCorrect;
		}
	}

	public int getCurrentQuestionNumber() {
		return currentQuestionNumber;
	}

	public Object getTotalPotentialQuestions() {
		return originalQuestionList.size();
	}

	/**
	 * Returns a list of the field names that were selected as answers to the current question.
	 * So if a question had field names A, B, and C, and the user had selected C, only field name C would
	 * occur in this list.
	 * 
	 * @return
	 */
	public List<String> getFieldnamesSelectedAsAnswersToCurrentQuestion() {
		List<String> rtn = new ArrayList<String>();
		AnsweredQuestion aq = mapOfExistingAnswers.get(currentQuestionNumber); 
		
		if (aq != null)
			rtn = aq.answers;
		
		return rtn;
	}
	
	public List<String> getFieldnamesSelectedAsAnswersForQuestion(Question q)
	{
		Integer key = mapOfQuestionToDisplayIndex.get(q);
		
		List<String> rtn = null;
		
		if (key != null)
		{
			AnsweredQuestion aq = mapOfExistingAnswers.get(key);
			
			if (aq != null)
				rtn = aq.getAnswers();
		}
		
		return rtn;
	}

	public int getTotalNumberOfQuestions()
	{
		return highestQuestionNumber;
	}

	public Question getMostRecentlyUsedQuestion()
	{
		return mostRecentlyUsedQuestion;
	}
	
	public void setCurrentQuestion(Question q)
	{
		currentQuestion = q;
		
		if (q != null)
			mostRecentlyUsedQuestion = q;
	}
	
	public class EHIterator implements Iterator<AnsweredQuestion>
	{
		private int index = 0;
		
		private EHIterator() {
			
		}
		
		@Override
		public boolean hasNext() {
			return index < mapOfExistingAnswers.size();
		}

		@Override
		public AnsweredQuestion next() {
			return mapOfExistingAnswers.get(++index);
		}

		@Override
		public void remove() {
			
		}
	}

	@Override
	public Iterator iterator() {
		return new EHIterator();
	}
	
	public Iterator getIterator()
	{
		return iterator();
	}
}
