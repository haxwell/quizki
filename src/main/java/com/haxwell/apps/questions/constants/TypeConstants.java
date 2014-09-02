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
 * Defines the type of choices that a given question is presenting in order to be answered correctly.
 * 
 * @author johnathanj
 */
public class TypeConstants {

	public static final int ALL_TYPES = 0;
	public static final int SINGLE = 1;
	public static final int MULTIPLE = 2; 
	public static final int PHRASE = 3;
	public static final int SEQUENCE = 4;
	public static final int SET = 5;
	
	public static final String ALL_TYPES_STR = "all";
	public static final String SINGLE_STR = "single";
	public static final String MULTIPLE_STR = "multiple";
	public static final String PHRASE_STR = "phrase";
	public static final String SEQUENCE_STR = "sequence";
	public static final String SET_STR = "set";
	
	protected static Map<Integer, String> map1;
	protected static Map<String, Integer> map2;
	
	
	static {
		map1 = new HashMap<Integer, String>();
		
		map1.put(SINGLE, SINGLE_STR);
		map1.put(MULTIPLE, MULTIPLE_STR);
		map1.put(PHRASE, PHRASE_STR);
		map1.put(SEQUENCE, SEQUENCE_STR);
		map1.put(SET, SET_STR);
		
		map2 = new HashMap<String, Integer>();
		
		map2.put(SINGLE_STR, SINGLE);
		map2.put(MULTIPLE_STR, MULTIPLE);
		map2.put(PHRASE_STR, PHRASE);
		map2.put(SEQUENCE_STR, SEQUENCE);
		map2.put(SET_STR, SET);
	}

	public static String getString(int i) {
		return map1.get(i);
	}
	
	public static Integer getInteger(String s) {
		return map2.get(s);
	}
}
