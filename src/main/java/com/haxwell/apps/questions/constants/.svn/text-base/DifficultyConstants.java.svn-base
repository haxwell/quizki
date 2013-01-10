package com.haxwell.apps.questions.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Indicates what level of knowledge is necessary to answer a given question
 * 
 * @author johnathanj
 */
public class DifficultyConstants {

	public static final int JUNIOR = 1;
	public static final int INTERMEDIATE = 2;
	public static final int WELL_VERSED = 3;
	public static final int GURU = 4;
	
	public static final String JUNIOR_STR = "junior";
	public static final String INTERMEDIATE_STR = "intermediate";
	public static final String WELL_VERSED_STR = "wellversed";
	public static final String GURU_STR = "guru";
	
	protected static Map<Integer, String> map1;
	protected static Map<String, Integer> map2;
	
	
	static {
		map1 = new HashMap<Integer, String>();
		
		map1.put(JUNIOR, JUNIOR_STR);
		map1.put(INTERMEDIATE, INTERMEDIATE_STR);
		map1.put(WELL_VERSED, WELL_VERSED_STR);
		map1.put(GURU, GURU_STR);
		map1 = new HashMap<Integer, String>();
		
		map2 = new HashMap<String, Integer>();
		
		map2.put(JUNIOR_STR, JUNIOR);
		map2.put(INTERMEDIATE_STR, INTERMEDIATE);
		map2.put(WELL_VERSED_STR, WELL_VERSED);
		map2.put(GURU_STR, GURU);
	}

	public static String getString(int i) {
		return map1.get(i);
	}
	
	public static Integer getInteger(String s) {
		return map2.get(s);
	}
}
