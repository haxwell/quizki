package com.haxwell.apps.questions.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AbstractEntity implements Comparable {

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
	
	protected String getJSON(String fieldName, String value) {
		return getJSON(fieldName, value, false);
	}
	
	protected String getJSON(String fieldName, String value, boolean commaShouldBeAppended) {
		if (value == null)
			value = "";
		else
			value = value.replace("\"", "\\\"");
		
		String rtn = "\"" + fieldName + "\":\"" + value + "\"";
		
		if (commaShouldBeAppended)
			rtn += ", ";
		
		return rtn;
	}
	
	protected String getJSON(String fieldName, Iterator<? extends AbstractEntity> iterator) {
		return getJSON(fieldName, iterator, false);
	}
	
	protected String getJSON(String fieldName, Iterator<? extends AbstractEntity> iterator, boolean commaShouldBeAppended) {
		StringBuffer rtn = new StringBuffer("");
		
		rtn.append("\"" + fieldName + "\": [");
		
		while (iterator.hasNext()) {
			rtn.append(iterator.next().toJSON());
			
			if (iterator.hasNext())
				rtn.append(", ");
		}
		
		rtn.append("]");
		
		if (commaShouldBeAppended)
			rtn.append(", ");
		
		return rtn.toString();
	}
	
	protected String getJSONOpening() {
		return "{ ";
	}
	
	protected String getJSONClosing() {
		return "}";
	}
}
