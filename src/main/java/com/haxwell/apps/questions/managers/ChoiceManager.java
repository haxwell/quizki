package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.interfaces.IChoice;
import com.haxwell.apps.questions.interfaces.IQuestion;

public class ChoiceManager extends Manager {
	
	private static EntityManagerFactory emf;
	
	static {
		emf = Persistence.createEntityManagerFactory(Constants.QUIZKI_PERSISTENCE_UNIT);
	}

	public static Object newChoice(String text) {
		Choice choice = new Choice();
		
		choice.setText(text);
		
		return choice;
	}
	
	public static Choice newChoice(String text, boolean isCorrect)
	{
		Choice choice = new Choice();
		
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
		Set<Choice> choicesSet = question.getChoices();
		
		List<String> errors = new ArrayList<String>();
		
		if (choicesSet.size() < 2)
			errors.add("There must be at least two choices."); // Unless this is a String type question, then one
		
		boolean b = false;
		
		for (Choice c : choicesSet)
			if (c.getIsCorrect() == 1)
				b = true;
		
		if (!b)
			errors.add("At least one of the choices must be correct.");
		
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
}
