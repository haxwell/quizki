package com.haxwell.apps.questions.constants;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Indicates what level of knowledge is necessary to answer a given question
 * 
 * @author johnathanj
 */
public class DifficultyConstants {

	public static final int UNDEFINED = -1;
	public static final int JUNIOR = 1;
	public static final int INTERMEDIATE = 2;
	public static final int SENIOR = 3;
	public static final int GURU = 4;
	public static final int ALL_DIFFICULTIES = 0;
	
	public static final String UNDEFINED_STR = "undefined";
	public static final String JUNIOR_STR = "Junior";
	public static final String INTERMEDIATE_STR = "intermediate";
	public static final String SENIOR_STR = "senior";
	public static final String GURU_STR = "guru";
	public static final String ALL_DIFFICULTIES_STR = "all";
	
	protected static Map<Integer, String> map1;
	protected static Map<String, Integer> map2;
	
	
	static {
		map1 = new HashMap<Integer, String>();
		
		map1.put(JUNIOR, JUNIOR_STR);
		map1.put(INTERMEDIATE, INTERMEDIATE_STR);
		map1.put(SENIOR, SENIOR_STR);
		map1.put(GURU, GURU_STR);
		map1 = new HashMap<Integer, String>();
		
		map2 = new HashMap<String, Integer>();
		
		map2.put(JUNIOR_STR, JUNIOR);
		map2.put(INTERMEDIATE_STR, INTERMEDIATE);
		map2.put(SENIOR_STR, SENIOR);
		map2.put(GURU_STR, GURU);
	}

	public static String getString(int i) {
		return map1.get(i);
	}
	
	public static Integer getInteger(String s) {
		return map2.get(s);
	}
}
