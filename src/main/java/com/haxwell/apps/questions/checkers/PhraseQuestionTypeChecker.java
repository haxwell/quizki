package com.haxwell.apps.questions.checkers;

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public class PhraseQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public PhraseQuestionTypeChecker(Question q) {
		super(q);
	}
	
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfQAndCIDsToValues)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = false;
		
		if (mapOfQAndCIDsToValues == null || mapOfQAndCIDsToValues.size() == 0)
			throw new IllegalArgumentException("Cannot pass a null or empty map to the PhraseQuestionTypeChecker");

		if (mapOfQAndCIDsToValues.size() > 1)
			throw new IllegalArgumentException("For Questions of type Phrase, there should only be one answer supplied when taking the exam.");

		String answer = mapOfQAndCIDsToValues.values().iterator().next().toLowerCase();
		
		for (Choice c : choices)
		{
			if (!rtn)
				rtn = c.getText().toLowerCase().equals(answer);
		}
		
		return rtn;
	}

}
