package com.haxwell.apps.questions.entities;

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
