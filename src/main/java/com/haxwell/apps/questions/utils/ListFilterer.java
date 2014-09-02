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
import java.util.Iterator;
import java.util.List;

/**
 * Returns a list that has been filtered. It iterates over each item in a source list,
 * passing the item to an object which says 'keep it' or 'filter it'. The resulting
 * list of the 'keep it' items is returned.
 *  
 * @author jjames
 *
 * @param <T>
 */
public class ListFilterer<T> {

	public Collection<T> process(Collection<T> source, Collection<ShouldRemoveAnObjectCommand<T>> filters)
	{
		if (filters != null) {
			ArrayList<T> toBeRemoved = new ArrayList<T>(); 
			
			for (T t : source) {
				Iterator<ShouldRemoveAnObjectCommand<T>> iterator = filters.iterator();
				
				boolean removed = false;
				while (iterator.hasNext() && removed == false) {
					if (iterator.next().shouldRemove(t)) {
						toBeRemoved.add(t);
						removed = true;
					}
				}
			}
			
			for (T t : toBeRemoved)
				source.remove(t);
		}
		
		return source;
	}
	
	public Collection<T> process(Collection<T> source, ShouldRemoveAnObjectCommand<T> filter)
	{
		ArrayList<ShouldRemoveAnObjectCommand<T>> filters = new ArrayList<ShouldRemoveAnObjectCommand<T>>();
		
		filters.add(filter);
		
		return process(source, filters);
	}
	
}
