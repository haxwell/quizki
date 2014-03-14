package com.haxwell.apps.questions.checkers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

public class StringQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public StringQuestionTypeChecker(Question q) {
		this.question = q;
	}
	
	public boolean questionHasBeenAnswered(Map<String, String> mapOfFieldNamesToValues)
	{
		boolean rtn = false;
		Collection<String> coll = mapOfFieldNamesToValues.values();
		
		rtn = (!StringUtil.isNullOrEmpty(coll.iterator().next()));
		
		return rtn;
	}
	
	@Override
	public List<String> getKeysToPossibleUserSelectedAttributesInTheRequest() {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("stringAnswer");
		
		return list;
	}
	
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfQAndCIDsToValues)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = false;
		
		if (mapOfQAndCIDsToValues.size() > 1)
			throw new IllegalArgumentException("For Questions of type String, there should only be one answer supplied when taking the exam.");

		String answer = mapOfQAndCIDsToValues.values().iterator().next().toLowerCase();
		
		for (Choice c : choices)
		{
			if (!rtn)
				rtn = c.getText().toLowerCase().equals(answer);
		}
		
		return rtn;
	}

}
