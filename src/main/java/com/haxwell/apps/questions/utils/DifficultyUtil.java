package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.entities.Difficulty;

public class DifficultyUtil {

	public static int convertToInt(Difficulty d) {
		return convertToInt(d.getText());
	}
	
	public static int convertToInt(String parameter) {
		
		if (parameter.equals(DifficultyConstants.JUNIOR_STR))
			return 1;
		if (parameter.equals(DifficultyConstants.INTERMEDIATE_STR))
			return 2;
		if (parameter.equals(DifficultyConstants.WELL_VERSED_STR))
			return 3;
		if (parameter.equals(DifficultyConstants.GURU_STR))
			return 4;
		
		return 1;
	}

	public static String getDisplayString(String str)
	{
		if (str.equals(DifficultyConstants.JUNIOR_STR))
			return "Junior";
		
		if (str.equals(DifficultyConstants.JUNIOR_STR))
			return "Intermediate";
		
		if (str.equals(DifficultyConstants.WELL_VERSED_STR))
			return "Well-versed";
		
		if (str.equals(DifficultyConstants.JUNIOR_STR))
			return "Guru";
		
		return str;
	}
	
	public static Difficulty convertToObject(String parameter) {
		Difficulty d = new Difficulty(getDisplayString(parameter));
		
		d.setId(convertToInt(parameter));
		
		return d;
	}
}
