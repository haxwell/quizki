package com.haxwell.apps.questions.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.utils.StringUtil;

public class ExamValidator implements Validator {

	@Override
	public boolean validate(AbstractEntity e, Map<String, List<String>> errors) {
		boolean rtn = true;
		Exam exam = (Exam)e;
		List<String> errorsList = new ArrayList<String>();
		
		if (StringUtil.isNullOrEmpty(exam.getTitle())) {
			errorsList.add("The exam requires a title!");
			rtn = false;
		}
		
		if (exam.getQuestions().size() == 0) {
			errorsList.add("The exam requires some questions be assigned to it!");
			rtn = false;
		}
		
		if (!rtn)
			errors.put("examValidationErrors", errorsList);
		
		return rtn;
	}

}
