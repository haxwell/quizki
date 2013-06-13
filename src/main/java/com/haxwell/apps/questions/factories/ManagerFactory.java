package com.haxwell.apps.questions.factories;

import com.haxwell.apps.questions.constants.EntityTypeConstants;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.Manager;
import com.haxwell.apps.questions.managers.QuestionManager;

public class ManagerFactory {

	public static Manager getManager(String entityType) {
		if (entityType.equals(EntityTypeConstants.EXAM_STR))
			return ExamManager.getInstance();
		else if (entityType.equals(EntityTypeConstants.QUESTION_STR))
			return QuestionManager.getInstance();
			
		return null;
	}
}
