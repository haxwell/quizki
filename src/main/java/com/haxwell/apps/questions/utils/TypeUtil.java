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
	
	public static String convertToString(long l) {
		if (l == TypeConstants.SINGLE)
			return TypeConstants.SINGLE_STR;
		if (l == TypeConstants.STRING)
			return TypeConstants.STRING_STR;
		if (l == TypeConstants.MULTIPLE)
			return TypeConstants.MULTIPLE_STR;
		if (l == TypeConstants.SEQUENCE)
			return TypeConstants.SEQUENCE_STR;
		
		return TypeConstants.ALL_TYPES_STR;
	}

	@Deprecated //use getObjectFromStringTypeId() instead
	public static QuestionType convertToObject(String parameter) {
		QuestionType qt = new QuestionType(parameter);
		
		long l = Long.parseLong(parameter);
		
		if (l < TypeConstants.SINGLE) // if the parameter they sent us is less than our lowest value, assume they meant our lowest value
			l = TypeConstants.SINGLE;
				
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
			str = TypeConstants.SINGLE+"";
		
		long l = Long.parseLong(str);
		
		return new QuestionType(l, convertToString(l));
	}
}
