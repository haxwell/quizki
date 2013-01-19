package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.factories.QuestionTypeCheckerFactory;
import com.haxwell.apps.questions.managers.Manager;

public class QuestionUtil {

	public static String getFieldnameForChoice(Question q, Choice c)
	{
		return "field_" + "question" + q.getId() + "choice" + c.getId();
	}
	
	public static List<String> getFieldnamesForChoices(Question q)
	{
		List<Choice> choices = getChoiceList(q);
		
		LinkedList<String> list = new LinkedList<String>();
		
		for (Choice c: choices)	{
			list.add(getFieldnameForChoice(q, c));
		}
		
		return list;
	}
	
	// lists of choices are always ordered by ID. A question could have 20 choices, and 
	//  in any list of items related to those 20 choices, the first item in the list relates
	//  to the choice with the highest ID. The second item, the second highest ID, etc.
	public static List<Choice> getChoiceList(Question currentQuestion) {
		Set<Choice> choices = currentQuestion.getChoices();
		
		ArrayList<Choice> list = new ArrayList<Choice>(choices);
		
		Collections.sort(list, Manager.ID_COMPARATOR);
		
		return list;
	}
	
	/**
	 * Returns a map of the fieldnames to the values chosen for each.
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getChosenAnswers(HttpServletRequest request)
	{
		Logger log = Logger.getLogger(QuestionUtil.class.getName());
		ExamHistory eh = (ExamHistory)request.getSession().getAttribute(Constants.CURRENT_EXAM_HISTORY);
		AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(eh.getMostRecentlyUsedQuestion());
		
		// take the list of field names from this question, check for that parameter in the request,
		List<String> fieldNames = checker.getKeysToPossibleUserSelectedAttributesInTheRequest(); 
		
		Map<String, String> map = new HashMap<String, String>(); // map fieldnames to the values chosen for them
		
		// now that we have the list of potentially chosen names, which are in the request? which were actually chosen?
		for (String fieldName: fieldNames)
		{
			String str = request.getParameter(fieldName);
			
			//  add the value of this chosen Choice to the list..
			if (str != null)
				map.put(fieldName, str);
		}
		
		log.log(Level.INFO, "Chosen answers: " + StringUtil.getToStringOfEach(map.values()));

		return map;
	}
	
	/**
	 * Given a collection of fieldnames, and a question, returns an array of equal size to the number of choices on the question.
	 * For each element in the resulting array, the word 'true' appears if the corresponding element in the question's list of choices 
	 * appears in the list of fieldnames.
	 * 
	 * @param fieldnamesChosenAsAnswersToGivenQuestion
	 * @param fieldnamesForChoices
	 * @return
	 */
	public static List<String> getUIArray_FieldWasSelected(
			Collection<String> fieldnamesChosenAsAnswersToGivenQuestion,
			Question question) {
		
		if (fieldnamesChosenAsAnswersToGivenQuestion == null) // Maybe we want to return null? No se?
			return new ArrayList<String>();
		
		List<String> rtn = QuestionUtil.getFieldnamesForChoices(question);
		
		for (int i = 0; i < rtn.size(); i++)
		{
				String str = rtn.get(i);
				
				if (fieldnamesChosenAsAnswersToGivenQuestion.contains(str))
					rtn.set(i, "'true'");
				else
					rtn.set(i, "");
		}

		return rtn;
	}
	
}
