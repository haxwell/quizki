package com.haxwell.apps.questions.utils;

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
	

	
}
