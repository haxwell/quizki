package com.haxwell.apps.questions.validators;

import java.util.ArrayList;	
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Reference;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.ChoiceManager;
import com.haxwell.apps.questions.utils.StringUtil;

public class QuestionValidator implements Validator {

	public boolean validate(AbstractEntity q, Map<String, List<String>> errors) {
		Question questionObj = (Question)q;
		List<String> errorsList = new ArrayList<String>();

		errorsList.addAll(ChoiceManager.validate(questionObj));
		
		String questionText = questionObj.getText();
		Set<Topic> topics = questionObj.getTopics();
		
		if (topics.size() < 1)
			errorsList.add("Question must have at least one topic.");
		
		for (Topic t : topics) {
			if (t.getText().indexOf("\"") != -1)
				errorsList.add("Topics cannot have quotes in them. Why not try an apostrophe instead?");
		}
		
		Set<Reference> references = questionObj.getReferences();
		
		for (Reference r : references) {
			if (r.getText().indexOf("\"") != -1)
				errorsList.add("References cannot have quotes in them. Why not try an apostrophe instead?");
		}
		
		if (StringUtil.isNullOrEmpty(questionText))
			errorsList.add("Question must have some text!");
		
		if (questionText != null && questionText.length() > Constants.MAX_QUESTION_TEXT_LENGTH)
			errorsList.add("Question text cannot be longer than " + Constants.MAX_QUESTION_TEXT_LENGTH + " characters. Perhaps a seperate question is in order!");
		
		if (questionObj.getDescription() != null && questionObj.getDescription().length() > Constants.MAX_QUESTION_DESCRIPTION_LENGTH)
			errorsList.add("Question description cannot be longer than " + Constants.MAX_QUESTION_DESCRIPTION_LENGTH + " characters.");
		
		if (errorsList.size() > 0)
			errors.put("questionValidationErrors", errorsList);
		
		return errorsList.size() > 0;
	}
}
