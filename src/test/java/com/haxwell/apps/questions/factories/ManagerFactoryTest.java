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

import com.haxwell.apps.questions.constants.EntityTypeEnums;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.Manager;
import com.haxwell.apps.questions.managers.QuestionManager;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class ManagerFactoryTest {

	@Test
	public void testObjectDefaultConstructor() {
		assertTrue(new ManagerFactory() != null);
	}

	@Test
	public void testGetManager_Exam() {
		Manager m = ManagerFactory.getManager(EntityTypeEnums.EXAM.getValString());
		
		assertNotEquals(null, m);
		assertTrue(m instanceof ExamManager);
	}

	@Test
	public void testGetManager_Question() {
		Manager m = ManagerFactory.getManager(EntityTypeEnums.QUESTION.getValString());
		
		assertNotEquals(null, m);
		assertTrue(m instanceof QuestionManager);
	}

	@Test
	public void testGetManager_UnknownEntityType() {
		Manager m = ManagerFactory.getManager("unknownEntityType");
		assertEquals(null, m);
	}
}
