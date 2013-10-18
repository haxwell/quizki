package com.haxwell.apps.questions.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.apps.questions.entities.Choice;
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
	
	public static Set<Topic> getSetFromJsonString(String str) {
		Set<Topic> rtn = new HashSet<Topic>();
		
		JSONValue jValue= new JSONValue();
		JSONArray arr = null;
		
		Object obj = jValue.parse(str);
		
		if (obj instanceof JSONObject)
			arr = (JSONArray)((JSONObject)obj).get("topics");
		else
			arr = (JSONArray)obj;
		
		for (int i=0; i < arr.size(); i++) {
			JSONObject o = (JSONObject)arr.get(i);
			
			Topic t = new Topic();
			
			t.setText((String)o.get("text"));
			
			Long id = Long.parseLong((String)o.get("id"));
			if (id != -1)
				t.setId(id);
			
			rtn.add(t);
		}
		
		return rtn;
	}
}
