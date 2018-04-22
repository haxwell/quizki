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

import com.haxwell.apps.questions.constants.TypeEnums;
import com.haxwell.apps.questions.entities.QuestionType;

public class TypeUtil {

	public static long convertToLong(QuestionType qt) {
		return convertToLong(qt.getText());
	}
	
	public static long convertToLong(String parameter) {

		for (TypeEnums te : TypeEnums.values()) {
			if (parameter.toLowerCase().equals(te.getValString())) {
				return te.getRank();
			}
		}
		return TypeEnums.ALL_TYPES.getRank();
	}
	
	public static String convertToString(long l) {

		for (TypeEnums te : TypeEnums.values()) {
			if (l == te.getRank()) {
				return te.getValString();
			}
		}
		return TypeEnums.ALL_TYPES.getValString();
	}

	@Deprecated //use getObjectFromStringTypeId() instead
	public static QuestionType convertToObject(String parameter) {
		QuestionType qt = new QuestionType(parameter);
		
		long l = Long.parseLong(parameter);
		
		if (l < TypeEnums.SINGLE.getRank()) // if the parameter they sent us is less than our lowest value, assume they meant our lowest value
			l = TypeEnums.SINGLE.getRank();
				
		qt.setId(l);
		qt.setText(convertToString((int)l));
		
		return qt;
	}
	
	/**
	 * Returns a complete QuestionType object that is based on the numeric value of a given string.
	 *  
	 * @param str a numeric value as a String
	 * @return a complete QuestionType object that is based on the numeric value of a given string.
	 */
	public static QuestionType getObjectFromStringTypeId(String str) {
		if (str == null) 
			str = TypeEnums.SINGLE.getValString()+"";
		
		long l = Long.parseLong(str);
		
		return new QuestionType(l, convertToString(l));
	}
}
