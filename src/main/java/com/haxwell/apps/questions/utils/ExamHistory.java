package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.constants.TypeConstants;
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
	public boolean recordAnswerToCurrentQuestion(Map<String, String> answers)
	{
		log.log(Level.FINE, "Begin RecordAnswerToCurrentQuestion");
		log.log(Level.FINER, "Answers passed in: " + StringUtil.getToStringOfEach(answers.values()));

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
	
	public AnsweredQuestion getUserSuppliedAnswers(Question q)
	{
		int index = mapOfQuestionToDisplayIndex.get(q);
		
		return mapOfExistingAnswers.get(index);
	}

	public class AnsweredQuestion
	{
		public Question question;
		public Map<String, String> answers;
		public Map<String, Object> metadata;
		public boolean isCorrect = false;
		
		public AnsweredQuestion(Question q, Map<String, String> answers, boolean b)
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

		public Map<String, String> getAnswers() {
			return answers;
		}

		public void setAnswers(Map<String, String> answers) {
			this.answers = answers;
		}

		public boolean getIsCorrect() {
			return isCorrect;
		}

		public void setIsCorrect(boolean isCorrect) {
			this.isCorrect = isCorrect;
		}
		
		public Map<String, Object> getMetadata() {
			if (metadata == null)
				metadata = new HashMap<String, Object>();
			
			return metadata;
		}

		public void setMetadata(Map<String, Object> metadata) {
			this.metadata = metadata;
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
	public Collection<String> getFieldnamesSelectedAsAnswersToCurrentQuestion() {
		Collection<String> rtn = new ArrayList<String>();
		AnsweredQuestion aq = mapOfExistingAnswers.get(currentQuestionNumber); 
		
		// HACK.. What really should be done here is that this method should get the map
		//  from aq.answers, and then pass it to an object which will return the appropriate 
		//  collection (either keys or values). This object should be of a type specifically
		//  associated with the QuestionType of the given question, ala the Checker classes.
		//  Fix this one day when there's not much else to do..
		
		if (aq != null)
			if (aq.getQuestion().getQuestionType().getId() == TypeConstants.SINGLE)
				rtn = aq.getAnswers().values();
			else 
				rtn = aq.getAnswers().keySet();
		
		return rtn;
	}
	
	public Collection<String> getFieldnamesSelectedAsAnswersForQuestion(Question q)
	{
		Integer key = mapOfQuestionToDisplayIndex.get(q);
		
		Collection<String> rtn = null;
		
		if (key != null)
		{
			AnsweredQuestion aq = mapOfExistingAnswers.get(key);
			
			// HACK.. What really should be done here is that this method should get the map
			//  from aq.answers, and then pass it to an object which will return the appropriate 
			//  collection (either keys or values). This object should be of a type specifically
			//  associated with the QuestionType of the given question, ala the Checker classes.
			//  Fix this one day when there's not much else to do..
			
			if (aq != null)
				if (q.getQuestionType().getId() == TypeConstants.SINGLE)
					rtn = aq.getAnswers().values();
				else 
					rtn = aq.getAnswers().keySet();
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
