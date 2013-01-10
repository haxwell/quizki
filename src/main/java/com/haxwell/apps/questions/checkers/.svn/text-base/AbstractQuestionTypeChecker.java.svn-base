package com.haxwell.apps.questions.checkers;

import java.util.List;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public abstract class AbstractQuestionTypeChecker {

	public Question question;
	
	protected AbstractQuestionTypeChecker()
	{
		
	}
	
	protected AbstractQuestionTypeChecker(Question q)
	{
		this.question = q;
	}
	
	/**
	 * Returns true if the list of selected field names matches the choices on the given question that have 
	 * indicated they are CORRECT.
	 * 
	 * @param listOfSelectedFieldNames a list of Strings, representing the field names that the user selected
	 * @return
	 */
	public boolean questionIsCorrect(List<String> listOfSelectedFieldNames)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = true;
		
		for (Choice c : choices)
		{
			if (c.getIscorrect() > 0)
				rtn &= listOfSelectedFieldNames.contains(QuestionUtil.getFieldnameForChoice(question, c));
		}
		
		return rtn;
	}
	
	public abstract List<String> getKeysToPossibleUserSelectedAttributesInTheRequest();	
}
