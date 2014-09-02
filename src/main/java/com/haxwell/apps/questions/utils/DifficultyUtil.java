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
		if (parameter.toLowerCase().equals(DifficultyConstants.SENIOR_STR.toLowerCase()))
			return 3;
		if (parameter.toLowerCase().equals(DifficultyConstants.GURU_STR.toLowerCase()))
			return 4;
		
		return 1;
	}
	
	public static String convertToString(int i) {
		if (i == DifficultyConstants.JUNIOR) return DifficultyConstants.JUNIOR_STR;
		if (i == DifficultyConstants.INTERMEDIATE) return DifficultyConstants.INTERMEDIATE_STR;
		if (i == DifficultyConstants.SENIOR) return DifficultyConstants.SENIOR_STR;
		if (i == DifficultyConstants.GURU) return DifficultyConstants.GURU_STR;
		
		return null;
	}

	public static String getDisplayString(String str)
	{
		if (str.toLowerCase().equals(DifficultyConstants.JUNIOR_STR.toLowerCase()))
			return "Junior";
		
		if (str.toLowerCase().equals(DifficultyConstants.JUNIOR_STR.toLowerCase()))
			return "Intermediate";
		
		if (str.toLowerCase().equals(DifficultyConstants.SENIOR_STR.toLowerCase()))
			return "Senior";
		
		if (str.toLowerCase().equals(DifficultyConstants.JUNIOR_STR.toLowerCase()))
			return "Guru";
		
		return str;
	}
	
	public static Difficulty convertToObject(String parameter) {
		Difficulty d = new Difficulty(getDisplayString(parameter));
		
		d.setId(convertToInt(parameter));
		
		return d;
	}

	public static Difficulty getDifficulty(int i) {
		if (i > DifficultyConstants.GURU || i < DifficultyConstants.JUNIOR)
			throw new IllegalArgumentException("Cannot create a Difficulty object with the ID [" + i + "]");
		
		Difficulty d = new Difficulty();
		
		d.setId(i);
		d.setText(convertToString(i));
		
		return d;
	}
	
	public static Difficulty getDifficulty(String iint) {
		if (iint == null) iint = DifficultyConstants.JUNIOR+"";
		
		return getDifficulty(Integer.parseInt(iint));
	}
}
