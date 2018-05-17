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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.mock.web.MockHttpServletRequest;

import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeEnums;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.factories.DynamifierFactory;
import com.haxwell.apps.questions.utils.QuestionAttributeSetterUtil;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // think this may be a functional test
public class SetQuestionTypeCheckerTest {

	@Test
	public void testQuestionIsCorrect() {
		
		// Generate the question object
		Map<String, Object> attributes = new HashMap<>();
		attributes.put(FilterConstants.ENTITY_ID_FILTER, RandomIntegerUtil.getRandomInteger(1000));
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeEnums.SET.getRank());
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);

		// get the dynamifier to pick a choice and field to answer
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		DynamifierFactory.getDynamifier(q).dynamify(q, mockRequest);
		assertTrue(q.getDynamicData("choiceIdsToBeAnswered") instanceof String);

		String str = (String)q.getDynamicData("choiceIdsToBeAnswered");
		assertTrue(StringUtil.getNumberOfOccurrances(";", str) == 1);
		
		// verify things are like we expect them to be
		SetQuestionTypeChecker sut = new SetQuestionTypeChecker(q);
		assertTrue(sut != null);
		
		Map<String, String> map = getMapRepresentingCorrectAnswerToDynamifiedChoice(q);
		assertTrue(map != null);
		assertTrue(map.size() == 1);
		assertTrue(sut.questionIsCorrect(map));
		
		map = getMapRepresentingIncorrectAnswerToDynamifiedChoice(q);
		assertTrue(map != null);
		assertTrue(map.size() == 1);
		assertFalse(sut.questionIsCorrect(map));
	}
	
	public Map<String, String> getMapRepresentingCorrectAnswerToDynamifiedChoice(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> choices = q.getChoices();
		Iterator<Choice> iterator = choices.iterator();
		
		String str = (String)q.getDynamicData("choiceIdsToBeAnswered");
		String choiceId = StringUtil.getField(1, ";", ";", str);
		String choiceFieldNum = StringUtil.getField(2, ";", ";", str);
		
		while (iterator.hasNext()) {
			Choice c = iterator.next();
			
			if (StringUtil.equals(choiceId, c.getId())) {
				String text = c.getText();
				String answer = (StringUtil.equals(choiceFieldNum, "-1")) ? text : StringUtil.getField(Integer.parseInt(choiceFieldNum), "[[", "]]", text);
				
				map.put(q.getId() + "," + c.getId() + "," + choiceFieldNum, answer);
			}
		}
		
		return map;
	}
	
	public Map<String, String> getMapRepresentingIncorrectAnswerToDynamifiedChoice(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> choices = q.getChoices();
		Iterator<Choice> iterator = choices.iterator();
		
		String str = (String)q.getDynamicData("choiceIdsToBeAnswered");
		String choiceId = StringUtil.getField(1, ";", ";", str);
		String choiceFieldNum = StringUtil.getField(2, ";", ";", str);
		
		while (iterator.hasNext()) {
			Choice c = iterator.next();
			
			if (StringUtil.equals(choiceId, c.getId())) {
				String text = c.getText();
//				String answer = StringUtil.getField(Integer.parseInt(choiceFieldNum), ";", ";", text);
				
				map.put(q.getId() + "," + c.getId() + "," + choiceFieldNum, "INCORRECT_ANSWER");
			}
		}
		
		return map;
	}
	
}
