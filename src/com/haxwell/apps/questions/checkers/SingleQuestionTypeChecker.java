package com.haxwell.apps.questions.checkers;

import java.util.ArrayList;
import java.util.List;

import com.haxwell.apps.questions.entities.Question;

public class SingleQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public SingleQuestionTypeChecker(Question q) {
		this.question = q;
	}
	
	@Override
	public List<String> getKeysToPossibleUserSelectedAttributesInTheRequest() {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("group1");
		
		return list;
	}

}
