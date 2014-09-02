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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.haxwell.apps.questions.utils.RandomIntegerUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // think this may be a functional test
public class SequenceQuestionTypeCheckerTest {

	@Test
	public void testQuestionIsCorrect() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SEQUENCE);
		attributes.put(FilterConstants.ENTITY_ID_FILTER, RandomIntegerUtil.getRandomInteger(1000));
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);

		SequenceQuestionTypeChecker sut = new SequenceQuestionTypeChecker(q);
		
		// asserts
		assertTrue(sut.questionIsCorrect(getMapRepresentingCorrectChoices(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingAllIncorrectlySequencedChoices(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingOneIncorrectSequence(q)));
		assertFalse(sut.questionIsCorrect(new HashMap<String, String>()));
	}

	protected Map<String, String> getMapRepresentingCorrectChoices(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		for (Choice c : set) {
			if (c.getIscorrect() == Choice.CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getSequence()+"");
			}
		}
		
		return map;
	}
	
	protected Map<String, String> getMapRepresentingAllIncorrectlySequencedChoices(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		int[] intArray = {4,3,2,1};
		
		for (Choice c : set) {
			if (c.getIscorrect() == Choice.CORRECT) {
				int choiceId = Integer.parseInt(c.getId()+"");
				map.put(q.getId() + "," + choiceId, intArray[choiceId-1]+"");
			}
		}
		
		return map;
	}
	
	protected Map<String, String> getMapRepresentingOneIncorrectSequence(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		int[] intArray = {1,2,3,5};
		
		for (Choice c : set) {
			if (c.getIscorrect() == Choice.CORRECT) {
				int choiceId = Integer.parseInt(c.getId()+"");
				map.put(q.getId() + "," + choiceId, intArray[choiceId-1]+"");
			}
		}
		
		return map;
	}
}
