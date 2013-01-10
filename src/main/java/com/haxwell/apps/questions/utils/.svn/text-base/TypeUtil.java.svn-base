package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.QuestionType;

public class TypeUtil {

	public static int convertToInt(String parameter) {

		if (parameter.equals(TypeConstants.SINGLE_STR))
			return 1;
		if (parameter.equals(TypeConstants.MULTI_STR))
			return 2;
		if (parameter.equals(TypeConstants.SEQUENCE_STR))
			return 3;
		if (parameter.equals(TypeConstants.STRING_STR))
			return 4;
		
		return 1;
	}

	public static QuestionType convertToObject(String parameter) {
		QuestionType qt = new QuestionType(parameter);
		
		qt.setId(convertToInt(parameter));
		
		return qt;
	}
}
