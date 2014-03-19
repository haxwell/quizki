package com.haxwell.apps.questions.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the type of choices that a given question is presenting in order to be answered correctly.
 * 
 * @author johnathanj
 */
public class TypeConstants {

	// TODO: Should this be an enumeration?
	
	public static final int ALL_TYPES = 0;
	public static final int SINGLE = 1;
	public static final int MULTIPLE = 2; 
	public static final int PHRASE = 3;
	public static final int SEQUENCE = 4;
	
	public static final String ALL_TYPES_STR = "all";
	public static final String SINGLE_STR = "single";
	public static final String MULTIPLE_STR = "multiple";
	public static final String PHRASE_STR = "phrase";
	public static final String SEQUENCE_STR = "sequence";
	
	protected static Map<Integer, String> map1;
	protected static Map<String, Integer> map2;
	
	
	static {
		map1 = new HashMap<Integer, String>();
		
		map1.put(SINGLE, SINGLE_STR);
		map1.put(MULTIPLE, MULTIPLE_STR);
		map1.put(PHRASE, PHRASE_STR);
		map1.put(SEQUENCE, SEQUENCE_STR);
		
		map2 = new HashMap<String, Integer>();
		
		map2.put(SINGLE_STR, SINGLE);
		map2.put(MULTIPLE_STR, MULTIPLE);
		map2.put(PHRASE_STR, PHRASE);
		map2.put(SEQUENCE_STR, SEQUENCE);
	}

	public static String getString(int i) {
		return map1.get(i);
	}
	
	public static Integer getInteger(String s) {
		return map2.get(s);
	}
}
