package com.haxwell.apps.questions.checkers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

public class SequenceQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public SequenceQuestionTypeChecker(Question q) {
		this.question = q;
	}
	
	public boolean questionHasBeenAnswered(Map<String, String> mapOfFieldNamesToValues)
	{
		boolean rtn = true;
		Collection<String> coll = mapOfFieldNamesToValues.values();
		
		rtn &= (question.getChoices().size() == coll.size());

		for (String str : coll)
			rtn &= (!StringUtil.isNullOrEmpty(str));
		
		return rtn;
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
			String userSuppliedValue = mapOfFieldNamesToValues.get(this.question.getId() + "," + c.getId());
			rtn &= (userSuppliedValue != null && userSuppliedValue.equals(c.getSequence()+""));
		}
		
		return rtn;
	}

}
