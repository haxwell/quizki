package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.QuestionType;

public class TypeUtil {

	public static int convertToInt(QuestionType qt) {
		return convertToInt(qt.getText());
	}
	
	public static int convertToInt(String parameter) {

		if (parameter.toLowerCase().equals(StringUtil.removeQuotes(TypeConstants.SINGLE_STR)))
			return TypeConstants.SINGLE;
		if (parameter.toLowerCase().equals(StringUtil.removeQuotes(TypeConstants.MULTIPLE_STR)))
			return TypeConstants.MULTIPLE;
		if (parameter.toLowerCase().equals(StringUtil.removeQuotes(TypeConstants.STRING_STR)))
			return TypeConstants.STRING;
		if (parameter.toLowerCase().equals(StringUtil.removeQuotes(TypeConstants.SEQUENCE_STR)))
			return TypeConstants.SEQUENCE;
		
		return TypeConstants.ALL_TYPES;
	}

	public static QuestionType convertToObject(String parameter) {
		QuestionType qt = new QuestionType(parameter);
		
		qt.setId(convertToInt(parameter));
		
		return qt;
	}
}
