package com.haxwell.apps.questions.entities;

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

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class QuestionTypeTest {

	@Test
	public void testObjectDefaultConstructor() {
		QuestionType sut = new QuestionType();
		
		assertTrue(sut.getId() == 0);
		assertTrue(StringUtil.isNullOrEmpty(sut.getText()));
	}
	
	@Test
	public void testObjectLongConstructor() {
		QuestionType sut = new QuestionType(1L);
		
		assertTrue(sut.getId() == 1);
		assertTrue(StringUtil.isNullOrEmpty(sut.getText()));
	}
	
	@Test
	public void testSettersAndGetters() {
		QuestionType sut = new QuestionType();
		
		sut.setId(1);
		sut.setText("qt");
		
		assertTrue(sut.getId() == 1);
		assertTrue(StringUtil.equals(sut.getText(), "qt"));
	}
	
	@Test
	public void equals() {
		QuestionType sut1 = new QuestionType();
		QuestionType sut2 = new QuestionType();
		
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		
		sut1.setId(1);
		
		assertFalse(sut1.equals(sut2));
		
		sut2.setId(1);
		sut2.setText("fdsaf");
		
		assertFalse(sut1.equals(sut2));
		
		sut1.setText("FDSAf");
		assertTrue(sut1.equals(sut2));
		
		assertFalse(sut1.equals("aString"));
	}

	@Test
	public void testToString() {
		QuestionType sut = new QuestionType();
		
		sut.setId(1);
		sut.setText("entity");
		
		assertTrue(sut.toString().contains("id: "));
		assertTrue(sut.toString().contains("1"));
		assertTrue(sut.toString().contains("type: "));
		assertTrue(sut.toString().contains("entity"));
	}
}
