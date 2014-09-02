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

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.EntityTypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.EntityType;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Reference;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TestQuestionUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class EntityTypeFactoryTest {

	@Test
	public void testObjectDefaultConstructor() {
		assertTrue(new EntityTypeFactory() != null);
	}

	@Test
	public void testGetEntityTypeFor_Exam() {
		Exam exam = TestQuestionUtil.getExam();
		
		EntityType et = EntityTypeFactory.getEntityTypeFor(exam);
		
		assertNotEquals(null, et);
		assertEquals(et.getId(), EntityTypeConstants.EXAM);
		assertTrue(StringUtil.equals(et.getText(), "Exam"));
	}

	@Test
	public void testGetEntityTypeFor_Question() {
		Question q = TestQuestionUtil.getQuestion();
		
		EntityType et = EntityTypeFactory.getEntityTypeFor(q);
		
		assertNotEquals(null, et);
		assertEquals(et.getId(), EntityTypeConstants.QUESTION);
		assertTrue(StringUtil.equals(et.getText(), "Question"));
	}
	
	@Test
	public void testGetEntityTypeFor_Topic() {
		Topic t = new Topic();
		
		EntityType et = EntityTypeFactory.getEntityTypeFor(t);
		
		assertEquals(null, et);
	}
	
	@Test
	public void testGetEntityTypeFor_Reference() {
		Reference r = new Reference();
		
		EntityType et = EntityTypeFactory.getEntityTypeFor(r);
		
		assertEquals(null, et);
	}
	
	@Test
	public void testGetEntityTypeFor_Choice() {
		Choice c = new Choice();
		
		EntityType et = EntityTypeFactory.getEntityTypeFor(c);
		
		assertEquals(null, et);
	}
}
