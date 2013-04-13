package com.haxwell.apps.questions.filters;

import java.util.Set;

import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;

public class TopicFilter implements ShouldRemoveAnObjectCommand<Question> {
	private String topicFilterText;
	
	public TopicFilter(String filter) {
		this.topicFilterText = filter;
	}
	
	public boolean shouldRemove(Question q) {
		Set<Topic> set = q.getTopics();

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