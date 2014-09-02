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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionAttributeSetterUtil;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // think this may be a functional test
public class PhraseQuestionTypeCheckerTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testQuestionIsCorrect_withASimplePhraseQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_CONTAINS_FILTER, "A Simple Phrase Question");
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.PHRASE);
		attributes.put(FilterConstants.ENTITY_ID_FILTER, RandomIntegerUtil.getRandomInteger(1000));
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);

		PhraseQuestionTypeChecker sut = new PhraseQuestionTypeChecker(q);
		
		assertTrue(sut.questionIsCorrect(getMapRepresentingTheFirstOfTwoChoicesGivenAsTheCorrectChoice(q)));
		assertTrue(sut.questionIsCorrect(getMapRepresentingTheSecondOfTwoChoicesGivenAsTheCorrectChoice(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingAnIncorrectAnswer(q)));
	}
	
	@Test
	public void testQuestionIsCorrect_withADynamicPhraseQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_CONTAINS_FILTER, "A [[Dynamic]] Phrase [[Question]]");
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.PHRASE);
		attributes.put(FilterConstants.ENTITY_ID_FILTER, RandomIntegerUtil.getRandomInteger(1000));
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);

		PhraseQuestionTypeChecker sut = new PhraseQuestionTypeChecker(q);

		assertTrue(sut.questionIsCorrect(getMapRepresentingTheFirstOfTwoChoicesGivenAsTheCorrectChoice(q)));
		assertTrue(sut.questionIsCorrect(getMapRepresentingTheSecondOfTwoChoicesGivenAsTheCorrectChoice(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingAnIncorrectAnswer(q)));
	}
	
	@Test
	public void testQuestionIsCorrect_withEmptyAnswerMap() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_CONTAINS_FILTER, "A [[Dynamic]] Phrase [[Question]]");
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.PHRASE);
		attributes.put(FilterConstants.ENTITY_ID_FILTER, RandomIntegerUtil.getRandomInteger(1000));
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);

		PhraseQuestionTypeChecker sut = new PhraseQuestionTypeChecker(q);

		exception.expect(IllegalArgumentException.class);
		sut.questionIsCorrect(new HashMap<String, String>());
	}
	
	@Test
	public void testQuestionIsCorrect_NullMapOfQAndCIdsToValues() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_CONTAINS_FILTER, "A [[Dynamic]] Phrase [[Question]]");
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.PHRASE);
		attributes.put(FilterConstants.ENTITY_ID_FILTER, RandomIntegerUtil.getRandomInteger(1000));
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);

		PhraseQuestionTypeChecker sut = new PhraseQuestionTypeChecker(q);
		
		exception.expect(IllegalArgumentException.class);
		sut.questionIsCorrect(null);
	}

	@Test
	public void testQuestionIsCorrect_MapOfQAndCIdsToValues_sizeGreaterThanOne() {
		Map<String, String> map = new HashMap<>();
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_CONTAINS_FILTER, "A [[Dynamic]] Phrase [[Question]]");
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.PHRASE);
		attributes.put(FilterConstants.ENTITY_ID_FILTER, RandomIntegerUtil.getRandomInteger(1000));
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);

		PhraseQuestionTypeChecker sut = new PhraseQuestionTypeChecker(q);
		
		map.put("x", "x");
		map.put("x1", "x1");
		map.put("x2", "x2");
		
		exception.expect(IllegalArgumentException.class);
		sut.questionIsCorrect(map);
	}

	public Map<String, String> getMapRepresentingTheFirstOfTwoChoicesGivenAsTheCorrectChoice(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> choices = q.getChoices();
		Iterator<Choice> iterator = choices.iterator();
		
		Choice c = iterator.next();
		map.put(q.getId() + "," + c.getId(), c.getText());
		
		return map;
	}

	public Map<String, String> getMapRepresentingTheSecondOfTwoChoicesGivenAsTheCorrectChoice(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> choices = q.getChoices();
		Iterator<Choice> iterator = choices.iterator();
		
		iterator.next();
		
		Choice c = iterator.next();
		map.put(q.getId() + "," + c.getId(), c.getText());
		
		return map;
	}
	
	public Map<String, String> getMapRepresentingAnIncorrectAnswer(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> choices = q.getChoices();
		Iterator<Choice> iterator = choices.iterator();
		
		Choice c = iterator.next();
		map.put(q.getId() + "," + c.getId(), "an incorrect answer");
		
		return map;
	}
}
