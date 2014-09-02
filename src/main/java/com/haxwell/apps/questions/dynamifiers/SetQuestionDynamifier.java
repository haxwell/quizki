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

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Sets the dynamic data on the question and the session.
 * 
 * @author jjames
 */
public class SetQuestionDynamifier extends AbstractDynamifier {

	public void dynamify(AbstractEntity ae, HttpServletRequest request) {
		if (ae instanceof Question) {
			Question q = (Question)ae;
			
			HttpSession session = request.getSession();
			
			String csv = (String)session.getAttribute(q.getId() + "_QuestionTypeSetDynamification");
			
			if (csv == null) {
				
				Set<Choice> set = q.getChoices();
				
				int randomIndex = RandomIntegerUtil.getRandomInteger(set.size());

				Iterator<Choice> iterator = set.iterator();
				
				Choice c = null;
				while (randomIndex-- >= 0 && iterator.hasNext()) c = iterator.next();
				
				// For now, we're only doing one value, but I can see in the future that there will be multiple values..
				int fieldNum = getDynamifiedTextFieldNumber(c);
				
				csv = c.getId() + ";" + fieldNum;
				
				session.setAttribute(q.getId() + "_QuestionTypeSetDynamification", csv);				
			}
			
			q.setDynamicData("choiceIdsToBeAnswered", csv);
		}		
	}

	private int getDynamifiedTextFieldNumber(Choice c) {
		int rtn = -1;
		String text = c.getText();
		
		int occurrances = StringUtil.getCountOfDynamicFields(text);
		
		if (occurrances > 0) {
			if (occurrances ==  1) {
				rtn = 1;
			} else {
				rtn = RandomIntegerUtil.getRandomInteger(occurrances) + 1;
			}
		}
		
		return rtn;
	}
}
