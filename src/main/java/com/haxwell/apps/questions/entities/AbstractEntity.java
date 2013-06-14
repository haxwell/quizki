package com.haxwell.apps.questions.entities;

import java.util.Iterator;

public class AbstractEntity implements Comparable {

	protected final boolean APPEND_COMMA = true;
	
	@Override
	public int compareTo(Object o) {
		return 0;
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
