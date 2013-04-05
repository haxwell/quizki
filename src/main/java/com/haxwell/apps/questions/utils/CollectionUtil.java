package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;

public class CollectionUtil {

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
	
	public static Collection<Long> getCollectionOfIds(Collection<? extends EntityWithAnIntegerIDBehavior> list)
	{
		Collection<Long> rtn = new ArrayList<Long>();
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = list.iterator();

		while (iterator.hasNext())
			rtn.add(iterator.next().getId());
		
		return rtn;
	}
	
	public static boolean contains(Collection coll, Object o)
	{
		return coll != null && coll.contains(o);
	}
	
}
