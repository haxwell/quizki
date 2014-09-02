package com.haxwell.apps.questions.filters;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
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