package com.haxwell.apps.questions.utils;

import java.util.HashMap;
import java.util.Iterator;

public class FilterCollection {

	HashMap<String, Object> map = new HashMap<String, Object>();
	
	public FilterCollection() { }
		
	public void add(String filterName, Object filterValue) {
		map.put(filterName, filterValue);
	}
	
	public boolean contains(String filterName) {
		return map.containsKey(filterName);
	}
	
	public Object get(String filterName)
	{
		Object rtn = map.get(filterName);
		return rtn == null ? "" : rtn;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iterator = map.keySet().iterator();
		
		sb.append("FilterCollection --> ");
		
		while (iterator.hasNext()) {
			String key = iterator.next();
			sb.append(key + ": " + map.get(key) + " ");
		}
		
		return sb.toString();
	}
}
