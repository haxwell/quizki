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

import com.haxwell.apps.questions.constants.DifficultyEnums;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.utils.StringUtil;

public class DifficultyUtil {

	public static int convertToInt(Difficulty d) {
		return convertToInt(d.getText());
	}
	
	public static int convertToInt(String parameter) {

		for (DifficultyEnums de : DifficultyEnums.values()) {
			if (parameter.toLowerCase().equals(de.getValString().toLowerCase()))
				return (int)de.getRank();
		}
		return 1; 
	}


	public static String convertToString(int i) {

		for (DifficultyEnums de : DifficultyEnums.values()) {
			if (i == de.getRank()) return de.getValString();
		}
		return null;
	}
	


	public static String getDisplayString(String str){
		
		for (DifficultyEnums de : DifficultyEnums.values()) {
			String s = de.getValString().toLowerCase();
			if (str.toLowerCase().equals(s))

				return StringUtil.capitalize(s);	
		}
		return str;
	}
	
	public static Difficulty convertToObject(String parameter) {
		Difficulty d = new Difficulty(getDisplayString(parameter));
		
		d.setId(convertToInt(parameter));
		
		return d;
	}

	public static Difficulty getDifficulty(int i) {
		if (i > DifficultyEnums.GURU.getRank() || i < DifficultyEnums.JUNIOR.getRank())
			throw new IllegalArgumentException("Cannot create a Difficulty object with the ID [" + i + "]");
		
		Difficulty d = new Difficulty();
		
		d.setId(i);
		d.setText(convertToString(i));
		
		return d;
	}
	
	public static Difficulty getDifficulty(String iint) {
		if (iint == null) iint = DifficultyEnums.JUNIOR.getRank()+"";
		
		return getDifficulty(Integer.parseInt(iint));
	}
}
