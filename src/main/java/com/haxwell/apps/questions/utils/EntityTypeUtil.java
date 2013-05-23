package com.haxwell.apps.questions.utils;

import com.haxwell.apps.questions.constants.EntityTypeConstants;
import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;


public class EntityTypeUtil {
	
	public static int getEnityTypeConstant(AbstractEntity e) {
		if (e instanceof Exam)
			return EntityTypeConstants.EXAM;
		else if (e instanceof Question) 
			return EntityTypeConstants.QUESTION;
		
		return -1;
	}
}
