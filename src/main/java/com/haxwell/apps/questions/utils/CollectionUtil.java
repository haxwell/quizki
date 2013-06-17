package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.haxwell.apps.questions.entities.AbstractEntity;
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

	public static String toJSON(Collection<? extends AbstractEntity> coll) {
		StringBuffer sb = new StringBuffer();
		
		Iterator<? extends AbstractEntity> iterator = coll.iterator();

		sb.append("{ ");
		
		if (iterator.hasNext()) {
			AbstractEntity ae = coll.iterator().next();

			if (ae != null) {
				sb.append("\"" + ae.getEntityDescription() + "\": [");
				
				while (iterator.hasNext()) {
					sb.append(iterator.next().toJSON());
				
					if (iterator.hasNext())
						sb.append(", ");
				}
				
				sb.append("]");
			}
		}
		
		sb.append("}");
		
		return sb.toString();
	}
}
