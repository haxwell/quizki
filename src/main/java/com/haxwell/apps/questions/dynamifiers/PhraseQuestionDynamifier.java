package com.haxwell.apps.questions.dynamifiers;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.StringUtil;

public class PhraseQuestionDynamifier extends AbstractDynamifier {

	public void dynamify(AbstractEntity ae, HttpServletRequest request) {
		if (ae instanceof Question) {
			Question q = (Question)ae;
			
			HttpSession session = request.getSession();
			
			String dynamification = (String)session.getAttribute(q.getId() + "_QuestionTypePhraseDynamification");
			
			if (dynamification == null) {
				
				String text = q.getText();
				
				int numfields = StringUtil.getCountOfDynamicFields(text);
				
				dynamification = (RandomIntegerUtil.getRandomInteger(numfields)+1) + "";				
				
				session.setAttribute(q.getId() + "_QuestionTypePhraseDynamification", dynamification);				
			}
			
			q.setDynamicData("dynamicFieldToBeBlankedOut", dynamification);
		}		
	}
}
