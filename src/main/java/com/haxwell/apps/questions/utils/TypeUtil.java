package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.QuestionType;

public class TypeUtil {

	public static long convertToLong(QuestionType qt) {
		return convertToLong(qt.getText());
	}
	
	public static long convertToLong(String parameter) {

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
	
	public static String convertToString(int n) {
		if (n == TypeConstants.SINGLE)
			return TypeConstants.SINGLE_STR;
		if (n == TypeConstants.STRING)
			return TypeConstants.STRING_STR;
		if (n == TypeConstants.MULTIPLE)
			return TypeConstants.MULTIPLE_STR;
		if (n == TypeConstants.SEQUENCE)
			return TypeConstants.SEQUENCE_STR;
		
		return TypeConstants.ALL_TYPES_STR;
	}

	public static QuestionType convertToObject(String parameter) {
		QuestionType qt = new QuestionType(parameter);
		
		long l = Long.parseLong(parameter);
		
		if (l < TypeConstants.SINGLE) // if the parameter they sent us is less than our lowest value, assume they meant our lowest value
			l = TypeConstants.SINGLE;
				
		qt.setId(l);
		qt.setText(convertToString((int)l));
		
		return qt;
	}
	
	public static QuestionType getObjectFromStringTypeId(String iint) {
		if (iint == null) iint = TypeConstants.SINGLE+"";
		
		int n = Integer.parseInt(iint);
		
		return convertToObject(convertToString(n));
	}
}
