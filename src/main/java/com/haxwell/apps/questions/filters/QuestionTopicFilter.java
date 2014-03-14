package com.haxwell.apps.questions.filters;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.ExamGenerationManager;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;

public class QuestionTopicFilter implements ShouldRemoveAnObjectCommand<Question> {
	public static Logger log = Logger.getLogger(QuestionTopicFilter.class.getName());
	
	private String topicFilterText = null;
	private Long topicFilterId = null;
	private List<Long> topicFilterIdList = null;
	
	public QuestionTopicFilter(String filter) {
		this.topicFilterText = filter;
	}
	
	public QuestionTopicFilter(Long id) {
		this.topicFilterId = id;
	}
	
	public QuestionTopicFilter(List<Long> listOfIDs) {
		this.topicFilterIdList = listOfIDs;
	}
	
	public boolean shouldRemove(Question q) {
		Set<Topic> set = q.getTopics();

		boolean rtn = true;
		boolean matchFound = false;
		
		for (Topic t : set) {
			if (!matchFound && topicMatchesCriteria(t))
				matchFound = true;
		}
		
		rtn = matchFound;

		return !rtn;
	}
	
	private boolean topicMatchesCriteria(Topic t) {
		boolean rtn = true;
		
		if (topicFilterId == null && topicFilterText != null) {
			rtn = t.getText().toUpperCase().contains(topicFilterText.toUpperCase());
		}
		else if (this.topicFilterId != null)
			rtn = topicFilterId.equals(t.getId());
		else if (this.topicFilterIdList != null) {
			Iterator<Long> iterator = topicFilterIdList.iterator();
			boolean found = false;
			
			while (!found && iterator.hasNext()) {
				found = iterator.next().equals(t.getId());
			}
			
			rtn = !found;
		}
		else
			throw new IllegalStateException("Either topicFilterText, topicFilterId or topicFilterIdList must be set for this filter to work.");
		
		log.log(Level.FINER, "Topic " + t.getId() + " / " + t.getText() + " " + (rtn ? "MATCHED " : "DID NOT MATCH ") + " criteria");
		
		return rtn;
	}
}