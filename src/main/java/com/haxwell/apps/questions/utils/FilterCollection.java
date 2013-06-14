package com.haxwell.apps.questions.utils;

import java.util.HashMap;

public class FilterCollection {

	HashMap<String, String> map = new HashMap<String, String>();
	
	public FilterCollection() { }
		
	public void add(String filterName, String filterValue) {
		map.put(filterName, filterValue);
	}
	
	public boolean contains(String filterName) {
		return map.containsKey(filterName);
	}
	
	public String get(String filterName)
	{
		return map.get(filterName);
	}
}
