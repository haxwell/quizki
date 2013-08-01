package com.haxwell.apps.questions.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.TopicManager;

public class TopicUtil {
	
	public static Logger log = Logger.getLogger(TopicUtil.class.getName());

	public static Set<Topic> getSetFromCSV(String csv) {

		StringTokenizer tokenizer = new StringTokenizer(csv, ",");
		Set<Topic> topics = new HashSet<Topic>();
		
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken().trim();
			Topic topic = TopicManager.getTopic(token);
			
			if (topic == null)
				topic = new Topic(token);

			if (!topics.contains(topic))
				topics.add(topic);
		}
		
		return topics;
	}
}
