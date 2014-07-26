package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.List;

import com.haxwell.apps.questions.entities.EntityWithIDAndTextValuePairBehavior;
import com.haxwell.apps.questions.entities.Reference;

public class ReferenceManager extends EntityWithIDAndTextValuePairManager {

	static ReferenceManager instance = null;
	
	public static EntityWithIDAndTextValuePairManager getInstance() {
		if (instance == null)
			instance = new ReferenceManager();
		
		return instance;
	}
	
	public String getDBTableName() {
		return "reference";
	}
	
	public Class getEntityClass() {
		return Reference.class;
	}
	
	public List<? extends EntityWithIDAndTextValuePairBehavior> getNewList() {
		return new ArrayList<Reference>();
	}
	
	public String getEntityName() {
		return "reference";
	}
}
