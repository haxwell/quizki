package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.interfaces.IChoice;
import com.haxwell.apps.questions.interfaces.IQuestion;

public class ChoiceManager extends Manager {
	
	private static long nextId = 1;
	
	static {
		nextId = getNextChoiceID();
	}
	
	public static Object newChoice(String text) {
		Choice choice = new Choice();
		
		choice.setText(text);
		
		return choice;
	}
	
	public static Choice newChoice(String text, boolean isCorrect)
	{
		Choice choice = new Choice();
		
//		choice.setId(ChoiceManager.getNextChoiceID());
		choice.setId(++nextId);
		
		choice.setText(text);
		choice.setIsCorrect(isCorrect ? 1 : 0);
		
		return choice;
	}
	
	public static Choice newChoice(String text, boolean isCorrect, int sequence)
	{
		Choice choice = newChoice(text, isCorrect);
		
		choice.setSequence(sequence);
		
		return choice;
	}
	
	public static void persistChoices(IQuestion question) {
		EntityManager em = emf.createEntityManager();

		Set<Choice> choices = question.getChoices();
		
		em.getTransaction().begin();
		
		for (IChoice choice : choices) 
		{
			em.persist(choice);
		}
		
		em.getTransaction().commit();
	}
	
	public static List<String> validate(Question question)
	{
		final Set<Choice> choicesSet = question.getChoices();
		
		List<String> errors = new ArrayList<String>();
		
		if (choicesSet.size() < 2)
			errors.add("There must be at least two choices."); // Unless this is a String type question, then one
		
		Set<String> choiceTextsSet = new HashSet<String>();
		Iterator<Choice> iterator = choicesSet.iterator();
		boolean duplicateDetected = false;
		
		while (iterator.hasNext() && duplicateDetected == false)
		{
			Choice c = iterator.next();
			
			if (choiceTextsSet.contains(c.getText()))
			{
				errors.add("There are choices with duplicate text.. thats not very useful :)");
				duplicateDetected = true;
			}
			
			choiceTextsSet.add(c.getText());
		}
		
		int correctChoiceCount = 0;
		
		for (Choice c : choicesSet)
			if (c.getIsCorrect() == 1)
				correctChoiceCount++;
		
		boolean addedErrorRegardingMultipleCorrectChoicesNeeded = false;

		if (question.getQuestionType().getId() == TypeConstants.SINGLE && correctChoiceCount > 1)
			errors.add("The question type is set to Single, but there is more than one correct choice.");
		
		if (question.getQuestionType().getId() == TypeConstants.MULTI && choicesSet.size() >= 2 && correctChoiceCount < 2)
		{
			errors.add("The question type is set to Multiple, but there are not multiple correct choices.");
			addedErrorRegardingMultipleCorrectChoicesNeeded = true;
		}
		
		if (correctChoiceCount == 0 && addedErrorRegardingMultipleCorrectChoicesNeeded == false)
			errors.add("At least one of the choices must be correct.");
		
		return errors;
	}

	public static Choice getChoice(String text, Set<Choice> choices)
	{
		Choice rtn = null;
		
		for (Choice c : choices)
		{
			if (c.getText().equals(text))
				rtn = c;
		}
		
		return rtn;
	}
	
	public static void update(String text, boolean b, Set<Choice> choices, Choice c) {

		choices.remove(c);
		
		c.setText(text);
		c.setIscorrect(b);
		
		choices.add(c);
	}

	public static void delete(Set<Choice> choices, Choice c) {
		choices.remove(c);
	}
	
	private static long getNextChoiceID()
	{
		EntityManager em = emf.createEntityManager();
		boolean b = em.isOpen();

		Query query = em.createNativeQuery("SELECT count(*) FROM choice c");
		
		Long rtn = (Long)query.getSingleResult();
		
		em.close();
		
		return rtn;
	}
}
