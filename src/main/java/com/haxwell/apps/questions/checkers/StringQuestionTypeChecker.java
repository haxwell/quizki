package com.haxwell.apps.questions.checkers;

import java.util.ArrayList;
import java.util.List;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public class StringQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public StringQuestionTypeChecker(Question q) {
		this.question = q;
	}
	
	@Override
	public List<String> getKeysToPossibleUserSelectedAttributesInTheRequest() {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("stringAnswer");
		
		return list;
	}
	
	@Override
	public boolean questionIsCorrect(List<String> answerList)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = false;
		
		if (answerList.size() > 1)
			throw new IllegalArgumentException("For Questions of type String, there should only be one answer supplied when taking the exam.");

		String answer = answerList.get(0).toLowerCase();
		
		for (Choice c : choices)
		{
			if (!rtn)
				rtn = c.getText().toLowerCase().equals(answer);
		}
		
		return rtn;
	}

}
