package com.haxwell.apps.questions.utils;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

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
