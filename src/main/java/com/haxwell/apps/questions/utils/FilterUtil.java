package com.haxwell.apps.questions.utils;

import java.util.logging.Logger;

import com.haxwell.apps.questions.constants.Constants;

public class FilterUtil {
	
	public static Logger log = Logger.getLogger(FilterUtil.class.getName());
	
	public static int convertToInt(String parameter) {

		if (parameter.toLowerCase().equals(Constants.MY_ITEMS_STR))
			return Constants.MY_ITEMS;
		if (parameter.toLowerCase().equals(Constants.ALL_ITEMS_STR))
			return Constants.ALL_ITEMS;
		if (parameter.toLowerCase().equals(Constants.SELECTED_ITEMS_STR))
			return Constants.SELECTED_ITEMS;
		
		return -1;
	}
	
	public static String convertToString(int i) {

		if (i == Constants.MY_ITEMS)
			return Constants.MY_ITEMS_STR;
		if (i == Constants.ALL_ITEMS)
			return Constants.ALL_ITEMS_STR;
		if (i == Constants.SELECTED_ITEMS)
			return Constants.SELECTED_ITEMS_STR;
		
		return null;
	}
	
}
