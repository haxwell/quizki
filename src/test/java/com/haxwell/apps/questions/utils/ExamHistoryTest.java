package com.haxwell.apps.questions.utils;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.QuestionType;
import com.haxwell.apps.questions.entities.Topic;

public class ExamHistoryTest {

	@Test
	public void testGetFieldnamesSelectedAsAnswersToCurrentQuestion_WithQuestionsOfTypeSingle() {

		// Create an ExamHistory 
		ExamHistory eh = new ExamHistory(getExam_withTwoQuestionsOfTypeSingle());
		
		// Simulate getting the first question in the exam
		Question questionWeSuppliedAnswersFor = eh.getNextQuestion();
		
		// Record some answers to the question
		Map<String, String> answersMap = getMapOfAnswers(questionWeSuppliedAnswersFor);
		eh.recordAnswerToCurrentQuestion(answersMap);
		
		// Move to the next question in the exam
		eh.getNextQuestion();
		
		// Move back to the question we submitted answers for
		eh.getPrevQuestion();
		
		// Verify that we get back the answers we set on the first question
		Collection<String> coll = eh.getFieldnamesSelectedAsAnswersToCurrentQuestion();
		
		assertTrue(coll.size() == answersMap.size());
		
		Iterator<String> iterator = answersMap.values().iterator();
		
		while (iterator.hasNext())
			assertTrue(coll.contains(iterator.next()));
	}

	@Test
	public void testGetFieldnamesSelectedAsAnswersToCurrentQuestion_WithQuestionsOfTypeMultiple() {

		// Create an ExamHistory 
		ExamHistory eh = new ExamHistory(getExam_withTwoQuestionsOfTypeMultiple());
		
		// Simulate getting the first question in the exam
		Question questionWeSuppliedAnswersFor = eh.getNextQuestion();
		
		// Record some answers to the question
		Map<String, String> answersMap = getMapOfAnswers(questionWeSuppliedAnswersFor);
		eh.recordAnswerToCurrentQuestion(answersMap);
		
		// Move to the next question in the exam
		eh.getNextQuestion();
		
		// Move back to the question we submitted answers for
		eh.getPrevQuestion();
		
		// Verify that we get back the answers we set on the first question
		Collection<String> coll = eh.getFieldnamesSelectedAsAnswersToCurrentQuestion();
		
		assertTrue(coll.size() == answersMap.size());
		
		Iterator<String> iterator = answersMap.keySet().iterator();
		
		while (iterator.hasNext())
			assertTrue(coll.contains(iterator.next()));
	}

	@Test
	public void testGetFieldnamesSelectedAsAnswersToCurrentQuestion_WithQuestionsOfTypeString() {

		// Create an ExamHistory 
		ExamHistory eh = new ExamHistory(getExam_withTwoQuestionsOfTypeString());
		
		// Simulate getting the first question in the exam
		Question questionWeSuppliedAnswersFor = eh.getNextQuestion();
		
		// Record some answers to the question
		Map<String, String> answersMap = getMapWithCorrectAnswerToStringQuestion(questionWeSuppliedAnswersFor);
		eh.recordAnswerToCurrentQuestion(answersMap);
		
		// Move to the next question in the exam
		eh.getNextQuestion();
		
		// Move back to the question we submitted answers for
		eh.getPrevQuestion();
		
		// Verify that we get back the answers we set on the first question
		Collection<String> coll = eh.getFieldnamesSelectedAsAnswersToCurrentQuestion();
		
		assertTrue(coll.size() == 1);
		
		Iterator<String> iterator = answersMap.keySet().iterator();
		
		while (iterator.hasNext())
			assertTrue(coll.contains(iterator.next()));
	}

	@Test
	public void testGetFieldnamesSelectedAsAnswersToCurrentQuestion_WithQuestionsOfTypeSequence() {

		// Create an ExamHistory 
		ExamHistory eh = new ExamHistory(getExam_withTwoQuestionsOfTypeSequence());
		
		// Simulate getting the first question in the exam
		Question questionWeSuppliedAnswersFor = eh.getNextQuestion();
		
		// Record some answers to the question
		Map<String, String> answersMap = getMapWithCorrectAnswersToSequenceQuestion(questionWeSuppliedAnswersFor);
		eh.recordAnswerToCurrentQuestion(answersMap);
		
		// Move to the next question in the exam
		eh.getNextQuestion();
		
		// Move back to the question we submitted answers for
		eh.getPrevQuestion();
		
		// Verify that we get back the answers we set on the first question
		Collection<String> coll = eh.getFieldnamesSelectedAsAnswersToCurrentQuestion();

		assertTrue(coll.size() == answersMap.size());

		Iterator<String> iterator = answersMap.keySet().iterator();

		while (iterator.hasNext())
			assertTrue(coll.contains(iterator.next()));
	}

	@Test
	public void testGetFieldnamesSelectedAsAnswersForQuestionQuestion_Single() {

		// Create an ExamHistory 
		ExamHistory eh = new ExamHistory(getExam_withTwoQuestionsOfTypeSingle());
		
		// Simulate getting the first question in the exam
		Question questionWeSuppliedAnswersFor = eh.getNextQuestion();
		
		// Record some answers to the question
		Map<String, String> answersMap = getMapOfAnswers(questionWeSuppliedAnswersFor);
		eh.recordAnswerToCurrentQuestion(answersMap);
		
		// Move to the next question in the exam
		eh.getNextQuestion();
		
		// Verify that we get back the answers we set on the first question
		Collection<String> coll = eh.getFieldnamesSelectedAsAnswersForQuestion(questionWeSuppliedAnswersFor);
		
		assertTrue(coll.size() == answersMap.size());
		
		Iterator<String> iterator = answersMap.values().iterator();
		
		while (iterator.hasNext())
			assertTrue(coll.contains(iterator.next()));
	}

	@Test
	public void testGetFieldnamesSelectedAsAnswersForQuestionQuestion_Multiple() {
		
		// Create an ExamHistory 
		ExamHistory eh = new ExamHistory(getExam_withTwoQuestionsOfTypeMultiple());
		
		// Simulate getting the first question in the exam
		Question questionWeSuppliedAnswersFor = eh.getNextQuestion();
		
		// Record some answers to the question
		Map<String, String> answersMap = getMapOfAnswers(questionWeSuppliedAnswersFor);
		eh.recordAnswerToCurrentQuestion(answersMap);
		
		// Move to the next question in the exam
		eh.getNextQuestion();
		
		// Verify that we get back the answers we set on the first question
		Collection<String> coll = eh.getFieldnamesSelectedAsAnswersForQuestion(questionWeSuppliedAnswersFor);
		
		assertTrue(coll.size() == answersMap.size());
		
		Iterator<String> iterator = answersMap.keySet().iterator();
		
		while (iterator.hasNext())
			assertTrue(coll.contains(iterator.next()));
	}
	
	@Test
	public void testGetFieldnamesSelectedAsAnswersForQuestionQuestion_String() {
		
		// Create an ExamHistory 
		ExamHistory eh = new ExamHistory(getExam_withTwoQuestionsOfTypeString());

		// Simulate getting the first question in the exam
		Question questionWeSuppliedAnswersFor = eh.getNextQuestion();

		// Record some answers to the question
		Map<String, String> answersMap = getMapWithCorrectAnswerToStringQuestion(questionWeSuppliedAnswersFor);
		eh.recordAnswerToCurrentQuestion(answersMap);

		// Move to the next question in the exam
		eh.getNextQuestion();

		// Verify that we get back the answers we set on the first question
		Collection<String> coll = eh.getFieldnamesSelectedAsAnswersForQuestion(questionWeSuppliedAnswersFor);
		
		assertTrue(coll.size() == 1);
		
		Iterator<String> iterator = answersMap.keySet().iterator();
		
		while (iterator.hasNext())
			assertTrue(coll.contains(iterator.next()));
	}
	
	@Test
	public void testGetFieldnamesSelectedAsAnswersForQuestionQuestion_Sequence() {
		
		// Create an ExamHistory 
		ExamHistory eh = new ExamHistory(getExam_withTwoQuestionsOfTypeSequence());
		
		// Simulate getting the first question in the exam
		Question questionWeSuppliedAnswersFor = eh.getNextQuestion();
		
		// Record some answers to the question
		Map<String, String> answersMap = getMapWithCorrectAnswersToSequenceQuestion(questionWeSuppliedAnswersFor);
		eh.recordAnswerToCurrentQuestion(answersMap);
		
		// Move to the next question in the exam
		eh.getNextQuestion();
		
		// Verify that we get back the answers we set on the first question
		Collection<String> coll = eh.getFieldnamesSelectedAsAnswersForQuestion(questionWeSuppliedAnswersFor);
		
		assertTrue(coll.size() == answersMap.size());
		
		Iterator<String> iterator = answersMap.keySet().iterator();
		
		while (iterator.hasNext())
			assertTrue(coll.contains(iterator.next()));
	}
	
	private Set<Topic> getSetOfTopics()
	{
		Set<Topic> setOfTopics = new HashSet<Topic>();
		
		setOfTopics.add(new Topic("Topic1"));
		setOfTopics.add(new Topic("Topic2"));

		return setOfTopics;
	}
	
	private Set<Choice> getSetOfChoices_SingleQuestionFirstChoiceIsCorrect()
	{
		Set<Choice> setOfChoices = new HashSet<Choice>();
		
		setOfChoices.add(new Choice(1, "Choice 1", true));
		setOfChoices.add(new Choice(2, "Choice 2", false));
		setOfChoices.add(new Choice(3, "Choice 3", false));
		setOfChoices.add(new Choice(4, "Choice 4", false));
		
		return setOfChoices;
	}
	
	private Set<Choice> getSetOfChoices_MultipleQuestionFirstAndThirdChoiceIsCorrect()
	{
		Set<Choice> setOfChoices = new HashSet<Choice>();
		
		setOfChoices.add(new Choice(1, "Choice 1", true));
		setOfChoices.add(new Choice(2, "Choice 2", false));
		setOfChoices.add(new Choice(3, "Choice 3", true));
		setOfChoices.add(new Choice(4, "Choice 4", false));
		
		return setOfChoices;
	}
	
	private Set<Choice> getSetOfChoices_SequenceQuestionWithFourChoices()
	{
		Set<Choice> setOfChoices = new HashSet<Choice>();
		
		setOfChoices.add(new Choice(1, "Choice 1", true, 1));
		setOfChoices.add(new Choice(2, "Choice 2", true, 2));
		setOfChoices.add(new Choice(3, "Choice 3", true, 3));
		setOfChoices.add(new Choice(4, "Choice 4", true, 4));
		
		return setOfChoices;
	}
	
	private Set<Choice> getSetOfChoices_StringQuestionThreeChoicesAllAreCorrect()
	{
		Set<Choice> setOfChoices = new HashSet<Choice>();
		
		setOfChoices.add(new Choice(1, "Choice 1", true));
		setOfChoices.add(new Choice(2, "Choice 2", true));
		setOfChoices.add(new Choice(3, "Choice 3", true));
		
		return setOfChoices;
	}
	
	private Question getSingleQuestion(int index)
	{
		Question question = new Question();
		
		question.setChoices(getSetOfChoices_SingleQuestionFirstChoiceIsCorrect());
		question.setTopics(getSetOfTopics());
		question.setText("this is test SINGLE question " + index);
		question.setQuestionType(new QuestionType(TypeConstants.SINGLE));
		
		return question;
	}
	
	private Question getMultipleQuestion(int index)
	{
		Question question = new Question();
		
		question.setChoices(getSetOfChoices_MultipleQuestionFirstAndThirdChoiceIsCorrect());
		question.setTopics(getSetOfTopics());
		question.setText("this is a MULTIPLE test question " + index);
		question.setQuestionType(new QuestionType(TypeConstants.MULTI));
		
		return question;
	}
	
	private Question getStringQuestion(int index)
	{
		Question question = new Question();
		
		question.setChoices(getSetOfChoices_StringQuestionThreeChoicesAllAreCorrect());
		question.setTopics(getSetOfTopics());
		question.setText("this is a STRING test question " + index);
		question.setQuestionType(new QuestionType(TypeConstants.STRING));
		
		return question;
	}
	
	private Question getSequenceQuestion(int index)
	{
		Question question = new Question();
		
		question.setChoices(getSetOfChoices_SequenceQuestionWithFourChoices());
		question.setTopics(getSetOfTopics());
		question.setText("this is a SEQUENCE test question " + index);
		question.setQuestionType(new QuestionType(TypeConstants.SEQUENCE));
		
		return question;
	}
	
	private Exam getExam_withTwoQuestionsOfTypeSingle()
	{
		Question question1 = getSingleQuestion(1);
		Question question2 = getSingleQuestion(2);
		
		Exam exam = new Exam();

		exam.addQuestion(question1);
		exam.addQuestion(question2);
		
		return exam;
	}
	
	private Exam getExam_withTwoQuestionsOfTypeMultiple()
	{
		Question question1 = getMultipleQuestion(1);
		Question question2 = getMultipleQuestion(2);
		
		Exam exam = new Exam();

		exam.addQuestion(question1);
		exam.addQuestion(question2);
		
		return exam;
	}
	
	private Exam getExam_withTwoQuestionsOfTypeString()
	{
		Question question1 = getStringQuestion(1);
		Question question2 = getStringQuestion(2);
		
		Exam exam = new Exam();

		exam.addQuestion(question1);
		exam.addQuestion(question2);
		
		return exam;
	}
	
	private Exam getExam_withTwoQuestionsOfTypeSequence()
	{
		Question question1 = getSequenceQuestion(1);
		Question question2 = getSequenceQuestion(2);
		
		Exam exam = new Exam();

		exam.addQuestion(question1);
		exam.addQuestion(question2);
		
		return exam;
	}
	
	/**
	 * Returns some fake answers to a SINGLE or MULTIPLE question. May work for others, not tested on those yet though..
	 * 
	 * @param q
	 * @return
	 */
	private Map<String, String> getMapOfAnswers(Question q)
	{
		Map<String, String> answers = new HashMap<String, String>();
		
		List<String> fieldnames = QuestionUtil.getFieldnamesForChoices(q);
		
		answers.put(fieldnames.get(0), "answer 1");
		answers.put(fieldnames.get(2), "answer 3");
		
		return answers;
	}
	
	/**
	 * Returns a map with a single correct answer to a STRING question.
	 * 
	 * @param q
	 * @return
	 */
	private Map<String, String> getMapWithCorrectAnswerToStringQuestion(Question q)
	{
		if (q.getQuestionType().getId() != TypeConstants.STRING)
			throw new IllegalArgumentException();
		
		Map<String, String> answers = new HashMap<String, String>();
		
		Iterator<Choice> iterator = q.getChoices().iterator();
		String correctAnswer = null;
		String fieldName = null;
		
		while (iterator.hasNext() && correctAnswer == null)
		{
			Choice c = iterator.next();
			
			if (c.getIsCorrect() > 0)
			{
				correctAnswer = c.getText();
				fieldName = QuestionUtil.getFieldnameForChoice(q, c);
			}
		}
		
		answers.put(fieldName, correctAnswer);
		
		return answers;
	}
	
	/**
	 * Returns a map with correct answers to a SEQUENCE question.
	 * 
	 * @param q
	 * @return
	 */
	private Map<String, String> getMapWithCorrectAnswersToSequenceQuestion(Question q)
	{
		if (q.getQuestionType().getId() != TypeConstants.SEQUENCE)
			throw new IllegalArgumentException();

		Map<String, String> answers = new HashMap<String, String>();
		
		for (Choice c : q.getChoices())
		{
			String fieldname = QuestionUtil.getFieldnameForChoice(q, c);
			answers.put(fieldname, c.getSequence()+"");
		}
		
		return answers;
	}
}
