package com.haxwell.apps.questions.checkers;

import java.util.List;

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

}
