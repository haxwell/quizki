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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;

public class CollectionUtil {

	public static final boolean ADD_OPENING_CLOSING_CURLY_BRACES = true;
	public static final boolean DONT_ADD_OPENING_CLOSING_CURLY_BRACES = false;
	
	public static String getCSVofIDsFromListofEntities(Collection<? extends EntityWithAnIntegerIDBehavior> list)
	{
		StringBuffer sb = new StringBuffer();
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = list.iterator();
		
		while (iterator.hasNext())
		{
			EntityWithAnIntegerIDBehavior entity = iterator.next();
			sb.append(entity.getId());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}
	
	public static String getCSV(Collection coll) {
		StringBuffer sb = new StringBuffer();
		Iterator iterator = coll.iterator();
		
		while (iterator.hasNext()) {
			sb.append(iterator.next().toString());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}
	
	// TODO: Allow the user to pass the class of the type of collection they want.. rather than just returning HashSet.. perhaps they 
	//  want to allow duplicates.
	public static Set<String> getSetFromCSV(String csv) {
		StringTokenizer tokenizer = new StringTokenizer(csv);
		
		Set<String> rtn = new HashSet<>();
		
		while (tokenizer.hasMoreTokens()) 
			rtn.add(tokenizer.nextToken());
		
		return rtn;
	}
	
	public static List<Long> getListOfIds(Collection<? extends EntityWithAnIntegerIDBehavior> list)
	{
		List<Long> rtn = new ArrayList<Long>();
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = list.iterator();

		while (iterator.hasNext())
			rtn.add(iterator.next().getId());
		
		return rtn;
	}
	
	public static boolean contains(Collection coll, Object o)
	{
		return coll != null && coll.contains(o);
	}

	public static String toJSON(Collection<? extends AbstractEntity> coll, boolean addOpenAndClosingCurlyBraces) {
		String rtn = null;
		
		if (coll != null) {
			StringBuffer sb = new StringBuffer();
			Iterator<? extends AbstractEntity> iterator = coll.iterator();
	
			if (addOpenAndClosingCurlyBraces)
				sb.append("{ ");
			
			if (iterator.hasNext()) {
				AbstractEntity ae = coll.iterator().next();
	
				if (ae != null) {
					sb.append("\"" + ae.getEntityDescription() + "\": [");
					
					while (iterator.hasNext()) {
						sb.append(iterator.next().toJSON());
						if (iterator.hasNext())	sb.append(", ");
					}
					
					sb.append("]");
				}
			}
			
			if (addOpenAndClosingCurlyBraces) sb.append("}");
			
			rtn = sb.toString();
		}
		
		return rtn;
	}
	
	public static String toJSON(Collection<? extends AbstractEntity> coll) {
		return toJSON(coll, true);
	}
	
	public static String toJSON(Map<String, List<String>> map) {
		StringBuffer sb = new StringBuffer();
		
		Iterator<String> keyIterator = map.keySet().iterator();
		
		sb.append("{ ");
		
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			sb.append("\"" + key + "\": [");
			
			Iterator<String> listIterator = map.get(key).iterator();
			
			while (listIterator.hasNext()) {
				sb.append("\"" + listIterator.next() + "\"");
				
				if (listIterator.hasNext())
					sb.append(", ");
			}
			
			sb.append("]");
			
			if (keyIterator.hasNext())
				sb.append(", ");
		}
		
		sb.append("} ");
		
		return sb.toString();
	}
	
	/**
	 * @deprecated No longer necessary.
	 * 
	 * @param list
	 * @param offset
	 * @param maxEntityCount
	 * @return
	 */
	public static List pareListDownToSize(List<? extends AbstractEntity> list, int offset, int maxEntityCount) {
		List paginatedList = new ArrayList();
		
		int itemCount = 0;
		for (int i = offset; i < list.size() && itemCount < maxEntityCount; i++) {
			paginatedList.add(list.get(i));
			itemCount++;
		}

		return paginatedList;
	}
}
