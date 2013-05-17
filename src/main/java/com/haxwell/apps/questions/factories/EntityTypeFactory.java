package com.haxwell.apps.questions.factories;

import com.haxwell.apps.questions.constants.EntityTypeConstants;
import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.EntityType;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;

public class EntityTypeFactory {

	public static EntityType getEntityTypeFor(AbstractEntity e) {
		if (e instanceof Exam) return new EntityType(EntityTypeConstants.EXAM, "Exam");
		if (e instanceof Question) return new EntityType(EntityTypeConstants.QUESTION, "Question");
		
		return null;
	}
}
