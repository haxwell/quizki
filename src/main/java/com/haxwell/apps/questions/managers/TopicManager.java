package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.haxwell.apps.questions.entities.EntityWithIDAndTextValuePairBehavior;
import com.haxwell.apps.questions.entities.Topic;

public class TopicManager extends EntityWithIDAndTextValuePairManager {

	static TopicManager instance = null;
	
	public static EntityWithIDAndTextValuePairManager getInstance() {
		if (instance == null)
			instance = new TopicManager();
		
		return instance;
	}
	
	public String getDBTableName() {
		return "Topic";
	}
	
	public Class getEntityClass() {
		return Topic.class;
	}
	
	public List<? extends EntityWithIDAndTextValuePairBehavior> getNewList() {
		return new ArrayList<Topic>();
	}
	
	public String getEntityName() {
		return "topic";
	}
}