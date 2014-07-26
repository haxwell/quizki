package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.haxwell.apps.questions.constants.AutocompletionConstants;
import com.haxwell.apps.questions.entities.EntityWithIDAndTextValuePairBehavior;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.EntityWithIDAndTextValuePairManager;
import com.haxwell.apps.questions.managers.TopicManager;

public class TopicUtil extends AbstractQuestionAttributeUtil {
	
	public static Logger log = Logger.getLogger(TopicUtil.class.getName());

	static TopicUtil instance = null;
	
	private TopicUtil() {
		
	}
	
	public static TopicUtil getInstance() {
		if (instance == null)
			instance = new TopicUtil();
		
		return instance;
	}
	
	protected String getKeyToAutocompleteEntriesInTheRequest() {
		return "topicsAutocompleteEntries";
	}

	protected String getKeyToDeletedAutocompleteEntriesInTheRequest() {
		return "topicsDeletedAutocompleteEntries";
	}
	
	protected String getJSONArrayKey() {
		return "topics";
	}
	
	protected long getAutocompletionConstant() {
		return AutocompletionConstants.TOPICS;
	}

	protected EntityWithIDAndTextValuePairManager getManager() {
		return TopicManager.getInstance();
	}
	
	protected EntityWithIDAndTextValuePairBehavior getEntityViaManager(String text) {
		return getManager().getByText(text);
	}
	
	protected EntityWithIDAndTextValuePairBehavior getEntityViaManager(int id) {
		return getManager().getById(id);
	}
	
	protected EntityWithIDAndTextValuePairBehavior getNewEntity() {
		return new Topic();
	}
	
	public Set<Topic> getTopicsFromJSONString(String str) {
		Collection<EntityWithIDAndTextValuePairBehavior> coll = new ArrayList<>();
		
		getObjectsFromJSONString(str, coll);
		
		Set<Topic> rtn = new HashSet<>();
		
		for (EntityWithIDAndTextValuePairBehavior entity : coll) {
			Topic t = new Topic();

			t.setId(entity.getId());
			t.setText(entity.getText());
			
			rtn.add(t);
		}
		
		return rtn;
	}
}
