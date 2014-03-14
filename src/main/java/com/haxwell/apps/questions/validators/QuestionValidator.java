package com.haxwell.apps.questions.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.StringUtil;

public class QuestionValidator implements Validator {

	@Override
	public boolean validate(AbstractEntity e, Map<String, List<String>> errors) {
		boolean rtn = true;
		Question q = (Question)e;
		List<String> errorsList = new ArrayList<String>();
		
		Set<Choice> choices = q.getChoices();
		
		if (choices.size() == 0) {
			errorsList.add("No choices have been added!");
			rtn = false;
		}
		
		if (!rtn)
			errors.put("examValidationErrors", errorsList);
		
		return rtn;
	}

}
