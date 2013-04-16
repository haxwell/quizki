package com.haxwell.apps.questions.filters;

import java.util.Set;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;

public class ExamTopicFilter implements ShouldRemoveAnObjectCommand<Exam> {
	private String topicFilterText;
	
	public ExamTopicFilter(String filter) {
		this.topicFilterText = filter;
	}
	
	public boolean shouldRemove(Exam e) {
		Set<Topic> set = e.getTopics();

		boolean rtn = true;
		boolean matchFound = false;
		
		for (Topic t : set) {
			if (!matchFound && t.getText().contains(topicFilterText))
				matchFound = true;
		}
		
		rtn = matchFound;

		return !rtn;
	}
}