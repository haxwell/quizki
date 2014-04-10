package com.haxwell.apps.questions.checkers;

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public class SequenceQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public SequenceQuestionTypeChecker(Question q) {
		this.question = q;
	}
		
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = true;

		for (Choice c : choices)
		{
			String userSuppliedValue = mapOfFieldNamesToValues.get(this.question.getId() + "," + c.getId());
			rtn &= (userSuppliedValue != null && userSuppliedValue.equals(c.getSequence()+""));
		}
		
		return rtn;
	}

}
