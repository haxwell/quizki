package com.haxwell.apps.questions.factories;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeEnums;
import com.haxwell.apps.questions.dynamifiers.AbstractDynamifier;
import com.haxwell.apps.questions.dynamifiers.PhraseQuestionDynamifier;
import com.haxwell.apps.questions.dynamifiers.SetQuestionDynamifier;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionAttributeSetterUtil;
import com.haxwell.apps.questions.utils.TestQuestionUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class DynamifierFactoryTest {

	@Test
	public void testObjectDefaultConstructor() {
		assertTrue(new DynamifierFactory() != null);
	}
	
	@Test
	public void testGetDynamifier_forASetQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeEnums.SET.getRank());
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertNotEquals(null, ad);
		assertTrue(ad instanceof SetQuestionDynamifier);
	}
	
	@Test
	public void testGetDynamifier_forAPhraseQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_CONTAINS_FILTER, "Phrase Question Text");
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeEnums.PHRASE.getRank());
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertNotEquals(null, ad);
		assertTrue(ad instanceof PhraseQuestionDynamifier);
	}

	@Test
	public void testGetDynamifier_forASingleQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeEnums.SINGLE.getRank());
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertEquals(null, ad);
	}

	@Test
	public void testGetDynamifier_forAMultipleQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeEnums.MULTIPLE.getRank());
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertEquals(null, ad);
	}

	@Test
	public void testGetDynamifier_forASequenceQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeEnums.SEQUENCE.getRank());
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertEquals(null, ad);
	}

	@Test
	public void testGetDynamifier_forAnExam() {
		Exam exam = TestQuestionUtil.getExam();
		
		assertEquals(null, DynamifierFactory.getDynamifier(exam));
	}
}
