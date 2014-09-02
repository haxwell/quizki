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
