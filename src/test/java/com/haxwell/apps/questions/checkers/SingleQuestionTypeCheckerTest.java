package com.haxwell.apps.questions.checkers;

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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionAttributeSetterUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // think this may be a functional test
public class SingleQuestionTypeCheckerTest {

	@Test
	public void testQuestionIsCorrect() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SINGLE);

		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		SingleQuestionTypeChecker sut = new SingleQuestionTypeChecker(q);
		
		assertTrue(sut.questionIsCorrect(getMapRepresentingCorrectChoices(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingASingleIncorrectlyChosenChoice(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingACorrectChoiceAndAnIncorrectChoice(q)));
		assertFalse(sut.questionIsCorrect(new HashMap<String, String>()));
	}
	
	protected Map<String, String> getMapRepresentingCorrectChoices(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		for (Choice c : set) {
			if (c.getIscorrect() == Choice.CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getText());
			}
		}
		
		return map;
	}
	
	protected Map<String, String> getMapRepresentingASingleIncorrectlyChosenChoice(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		for (Choice c : set) {
			if (map.size() == 0 && c.getIscorrect() == Choice.NOT_CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getText());
			}
		}
		
		return map;
	}
	
	protected Map<String, String> getMapRepresentingACorrectChoiceAndAnIncorrectChoice(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		boolean correctAdded = false;
		boolean incorrectAdded = false;
		
		for (Choice c : set) {
			if (!correctAdded && c.getIscorrect() == Choice.CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getText());
				correctAdded = true;
			}
			
			if (!incorrectAdded && c.getIscorrect() == Choice.NOT_CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getText());
				incorrectAdded = true;
			}
		}
		
		return map;
	}
}
