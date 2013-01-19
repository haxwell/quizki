package com.haxwell.apps.questions.checkers;

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public class MultiQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public MultiQuestionTypeChecker(Question q) {
		this.question = q;
	}
	
	@Override
	public List<String> getKeysToPossibleUserSelectedAttributesInTheRequest() {
		return QuestionUtil.getFieldnamesForChoices(question);
	}

	/**
	 * Returns true if the list of selected field names matches the choices on the given question that have 
	 * indicated they are CORRECT.
	 * 
	 * @param mapOfFieldNamesToValues a list of Strings, representing the field names that the user selected
	 * @return
	 */
	public boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = true;
		
		for (Choice c : choices)
		{
			if (c.getIscorrect() > 0) 
				rtn &= mapOfFieldNamesToValues.containsKey(QuestionUtil.getFieldnameForChoice(question, c));
			else
				rtn &= !mapOfFieldNamesToValues.containsKey(QuestionUtil.getFieldnameForChoice(question, c));
		}
		
		return rtn;
	}
}
