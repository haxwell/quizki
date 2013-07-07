package com.haxwell.apps.questions.utils;

import java.util.HashMap;

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
		return map.get(filterName);
	}
}
