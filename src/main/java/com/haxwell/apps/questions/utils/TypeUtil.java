package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.QuestionType;

public class TypeUtil {

	public static int convertToInt(String parameter) {

		if (parameter.equals(TypeConstants.SINGLE_STR))
			return TypeConstants.SINGLE;
		if (parameter.equals(TypeConstants.MULTIPLE_STR))
			return TypeConstants.MULTIPLE;
		if (parameter.equals(TypeConstants.STRING_STR))
			return TypeConstants.STRING;
		if (parameter.equals(TypeConstants.SEQUENCE_STR))
			return TypeConstants.SEQUENCE;
		
		return TypeConstants.SINGLE;
	}

	public static QuestionType convertToObject(String parameter) {
		QuestionType qt = new QuestionType(parameter);
		
		qt.setId(convertToInt(parameter));
		
		return qt;
	}
}
