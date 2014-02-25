package com.haxwell.apps.questions.utils;

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
