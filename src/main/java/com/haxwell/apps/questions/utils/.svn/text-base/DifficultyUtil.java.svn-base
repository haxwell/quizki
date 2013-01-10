package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.entities.Difficulty;

public class DifficultyUtil {

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

	public static Difficulty convertToObject(String parameter) {
		Difficulty d = new Difficulty(parameter);
		
		d.setId(convertToInt(parameter));
		
		return d;
	}

}
