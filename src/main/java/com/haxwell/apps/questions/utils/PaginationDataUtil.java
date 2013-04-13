package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.List;

import com.haxwell.apps.questions.entities.AbstractEntity;

public class PaginationDataUtil {

	public static List<? extends AbstractEntity> reduceListSize(PaginationData pd, List<? extends AbstractEntity> list) {
		
		int pageSize = pd.getPageSize();
		int pageNumber = pd.getPageNumber();
		
		List<AbstractEntity> paginatedList = new ArrayList<AbstractEntity>();		

		// calculate a far end to pull from the passed in list as either the number of items in the list, 
		//  or as the number representing the end of the current page
		int max = ((pageSize * pageNumber) + pageSize) > list.size() ? list.size() : ((pageSize * pageNumber) + pageSize); 
		
		for (int i = pageSize * pageNumber; i < max; i++) {
			paginatedList.add((AbstractEntity)list.get(i));
		}
		
		return paginatedList;
	}
}
