package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.entities.Difficulty;

public class DifficultyUtil {

	public static int convertToInt(Difficulty d) {
		return convertToInt(d.getText());
	}
	
	public static int convertToInt(String parameter) {
		
		if (parameter.toLowerCase().equals(DifficultyConstants.JUNIOR_STR.toLowerCase()))
			return 1;
		if (parameter.toLowerCase().equals(DifficultyConstants.INTERMEDIATE_STR.toLowerCase()))
			return 2;
		if (parameter.toLowerCase().equals(DifficultyConstants.WELL_VERSED_STR.toLowerCase()))
			return 3;
		if (parameter.toLowerCase().equals(DifficultyConstants.GURU_STR.toLowerCase()))
			return 4;
		
		return 1;
	}

	public static String getDisplayString(String str)
	{
		if (str.toLowerCase().equals(DifficultyConstants.JUNIOR_STR.toLowerCase()))
			return "Junior";
		
		if (str.toLowerCase().equals(DifficultyConstants.JUNIOR_STR.toLowerCase()))
			return "Intermediate";
		
		if (str.toLowerCase().equals(DifficultyConstants.WELL_VERSED_STR.toLowerCase()))
			return "Well-versed";
		
		if (str.toLowerCase().equals(DifficultyConstants.JUNIOR_STR.toLowerCase()))
			return "Guru";
		
		return str;
	}
	
	public static Difficulty convertToObject(String parameter) {
		Difficulty d = new Difficulty(getDisplayString(parameter));
		
		d.setId(convertToInt(parameter));
		
		return d;
	}
}
