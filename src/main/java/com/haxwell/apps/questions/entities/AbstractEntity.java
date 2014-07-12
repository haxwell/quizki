package com.haxwell.apps.questions.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONObject;

public abstract class AbstractEntity implements Comparable {

	protected final boolean APPEND_COMMA = true;
	
	protected Map<String, Object> dynamicData = new HashMap<String, Object>();
	
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	public Object getDynamicData(String key) {
		return dynamicData.get(key);
	}
	
	public void setDynamicData(String key, Object o) {
		dynamicData.put(key, o);
	}
	
	public long getId() {
		return Long.MIN_VALUE;
	}
	
	public User getUser() {
		return null;
	}
	
	public String getEntityDescription()  {
		return null;
	}
	
	public String getText() {
		return null;
	}
	
	public String toJSON() {
		return null;
	}

	protected void addDynamicDataToJSONObject(JSONObject j) {
		Set<String> keys = dynamicData.keySet();
		
		j.put("dynamicDataFieldNames", keys.toArray());
		
		for(String key : keys) {
			j.put(key,  dynamicData.get(key).toString());
		}
	}
}
