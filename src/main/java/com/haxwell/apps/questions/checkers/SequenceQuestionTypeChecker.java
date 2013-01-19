package com.haxwell.apps.questions.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public class SequenceQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public SequenceQuestionTypeChecker(Question q) {
		this.question = q;
	}
	
	@Override
	public List<String> getKeysToPossibleUserSelectedAttributesInTheRequest() {
		ArrayList<String> list = new ArrayList<String>();
		
		Set<Choice> choices = this.question.getChoices();
		
		for (Choice c : choices)
		{
			list.add(QuestionUtil.getFieldnameForChoice(this.question, c));			
		}
		
		return list;
	}
	
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = true;

		for (Choice c : choices)
		{
			String fieldName = QuestionUtil.getFieldnameForChoice(this.question, c);
			
			String userSuppliedValue = mapOfFieldNamesToValues.get(fieldName);

			rtn &= userSuppliedValue.equals(c.getSequence()+"");
		}
		
		return rtn;
	}

}
