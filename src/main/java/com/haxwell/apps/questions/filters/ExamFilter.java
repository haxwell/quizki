package com.haxwell.apps.questions.filters;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;

public class ExamFilter implements ShouldRemoveAnObjectCommand<Exam> {
	private String filterText;
	
	public ExamFilter(String filterText) {
		this.filterText = filterText;
	}
	
	public boolean shouldRemove(Exam e) {
		boolean rtn = false;

		rtn = !e.getTitle().contains(filterText);

		return rtn;
	}
}
