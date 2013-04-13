package com.haxwell.apps.questions.filters;

import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.TypeUtil;

public class QuestionTypeFilter implements ShouldRemoveAnObjectCommand<Question> {
	private int filterQuestionType;
	
	public QuestionTypeFilter(int filter) {
		this.filterQuestionType = filter;
	}
	
	public boolean shouldRemove(Question q) {
		boolean rtn = false;

		if (TypeUtil.convertToInt(q.getQuestionType()) != filterQuestionType)
			rtn = true;

		return rtn;
	}
}
