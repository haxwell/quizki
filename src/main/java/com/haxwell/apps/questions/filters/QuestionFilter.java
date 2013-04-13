package com.haxwell.apps.questions.filters;

import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TypeUtil;

public class QuestionFilter implements ShouldRemoveAnObjectCommand<Question> {
	private String filterText;
	
	public QuestionFilter(String filterText) {
		this.filterText = filterText;
	}
	
	public boolean shouldRemove(Question q) {
		boolean rtn = false;

		if (!StringUtil.isNullOrEmpty(q.getTextWithoutHTML())) {
			rtn = !q.getTextWithoutHTML().contains(filterText); 
		}
		else {
			rtn = !q.getDescription().contains(filterText);
		}

		return rtn;
	}
}
