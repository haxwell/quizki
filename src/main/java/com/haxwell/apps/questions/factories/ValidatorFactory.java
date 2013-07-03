package com.haxwell.apps.questions.factories;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.validators.ExamValidator;
import com.haxwell.apps.questions.validators.Validator;

public class ValidatorFactory {

	public static Validator getValidator(AbstractEntity e) {
		
		if (e instanceof Exam) return new ExamValidator();
		
		return null;
	}
}
