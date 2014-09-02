package com.haxwell.apps.questions.managers;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.utils.CollectionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Used to collect information from a Manager to return to a JSP that has been called by AJAX
 * 
 * @author johnathanj
 */
public class AJAXReturnData {
	
	public Collection<? extends AbstractEntity> entities = null;

	public int additionalItemCount = -1;
	public int additionalInfoCode = -1;
	
	public enum DynamificationStatus { NOT_NEEDED, NEEDED, COMPLETED };
	protected DynamificationStatus ds = DynamificationStatus.NOT_NEEDED;
	protected Map<String, String> jsonStringMap = new HashMap<String, String>();
	
	public void setDynamificationStatus(DynamificationStatus ds) {
		this.ds = ds;
	}
	
	public DynamificationStatus getDynamificationStatus() {
		return ds;
	}
	
	public void addKeyValuePairToJSON(String key, String value) {
		jsonStringMap.put(key, value);
	}
	
	public String toJSON() {
		String rtn = "{ ";
		
		Iterator<String> iterator = jsonStringMap.keySet().iterator();
		
		while (iterator.hasNext())
		{
			String key = iterator.next();
			
			rtn += "\"" + key + "\": \"" + jsonStringMap.get(key) + "\", ";
		}
		
		rtn += "\"addlItemCount\":\"" + additionalItemCount + "\", \"addlInfoCode\":\"" + additionalInfoCode + "\" ";
		
		String entitiesAsJSONString = CollectionUtil.toJSON(entities, CollectionUtil.DONT_ADD_OPENING_CLOSING_CURLY_BRACES);
		
		if (!StringUtil.isNullOrEmpty(entitiesAsJSONString))
			rtn += ", " + entitiesAsJSONString;
		
		rtn += " }";
		
		return rtn;
	}
	
	public Collection<? extends AbstractEntity> getEntities() {
		return entities;
	}

	public void setEntities(Collection<? extends AbstractEntity> entities) {
		this.entities = entities;
	}

	public int getAdditionalItemCount() {
		return additionalItemCount;
	}

	public void setAdditionalItemCount(int additionalItemCount) {
		this.additionalItemCount = additionalItemCount;
	}

	public int getAdditionalInfoCode() {
		return additionalInfoCode;
	}

	public void setAdditionalInfoCode(int additionalInfoCode) {
		this.additionalInfoCode = additionalInfoCode;
	}
}
