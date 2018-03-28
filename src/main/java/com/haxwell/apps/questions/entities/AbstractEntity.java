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

public abstract class AbstractEntity {

	/*
	 * The interface Comparable was never implemented (Eclipse can't resolve it) and there are no other references to it. Questions are
	 * compared but no other entities are, or would make sense to be. Choices might if we want to sort them into an order but it's still
	 * not a generic requirement for all entity types so I removed this.
	 */
	
	
	
	protected final boolean APPEND_COMMA = true;
	
	protected Map<String, Object> dynamicData = new HashMap<String, Object>();

	/*	
	 * removed @Override
	 * This might have been related to the planned (or removed) Comparable interface that was never implemented.
	 * 
	 * Jonathan I'm planning on removing compareTo() from here. It's only used to compare Questions in 6 places in the project. None of
	 * the other entities use it. Since Question does implement it simply removing it here will not affect it or any of the other entities. Is
	 * that correct (besides the Comparable interface problem)? It is commented out here to test that, but I am not sure the test suite would catch it.
	 * 
	 * How can method removals be tested?
	 * 
	 * I resolved the problem with the @Override related compilation error in this version.
	 * 
	 * (In a separate issue I am getting an error from maven "Could not find a value for QUIZKI_JDBC_URL in System.getProperty()" that is preventing
	 * the test suite from connecting to the database when I build the project. Where is that variable set and to what?)
	 * 
	 * Thank you in advance for any help on this
	 
	
	public int compareTo(Object o) {
		return 0;
	}
*/
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
