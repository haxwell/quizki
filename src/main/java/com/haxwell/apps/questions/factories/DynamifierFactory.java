package com.haxwell.apps.questions.factories;

import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.dynamifiers.AbstractDynamifier;
import com.haxwell.apps.questions.dynamifiers.SetQuestionDynamifier;
import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Question;

public class DynamifierFactory {

	public static AbstractDynamifier getDynamifier(AbstractEntity ae) {
		AbstractDynamifier rtn = null;
		
		if (ae instanceof Question) {
			Question q = (Question)ae;
			
			if (q.getQuestionType().getId() == TypeConstants.SET)
				return new SetQuestionDynamifier();
		}
		
		return rtn;
	}
}
