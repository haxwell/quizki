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

import static org.junit.Assert.assertTrue;	
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class EntityTypeTest {

	@Test
	public void testObjectDefaultConstructor() {
		EntityType sut = new EntityType();
		
		assertTrue(sut.getId() == 0);
		assertTrue(sut.getText() == null);
	}
	
	@Test
	public void testObjectLongStringConstructor() {
		EntityType sut;
		
		long id = 1;
		String text = "testText";
		
		sut = new EntityType(id, text);
		
		assertTrue(sut.getId() == id);
		assertTrue(StringUtil.equals(sut.getText(), text));
	}
	
	@Test
	public void testObjectLongConstructor() {
		EntityType sut;
		
		long id = 1;
		
		sut = new EntityType(id);
		
		assertTrue(sut.getId() == id);
		assertTrue(sut.getText() == null);
	}
	
	@Test
	public void testObjectStringConstructor() {
		EntityType sut;
		
		String text = "testText";
		
		sut = new EntityType(text);
		
		assertTrue(sut.getId() == 0);
		assertTrue(StringUtil.equals(sut.getText(), text));
	}
	
	@Test
	public void testEquals() {
		EntityType sut1 = new EntityType();
		EntityType sut2 = new EntityType();
		
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		
		sut1.setId(1);
		
		assertFalse(sut1.equals(sut2));
		
		sut2.setId(1);
		
		assertTrue(sut1.equals(sut2));
		
		sut2.setText("text");
		
		assertFalse(sut1.equals(sut2));
		
		assertFalse(sut1.equals("aString"));
	}
	
	@Test
	public void testToString() {
		EntityType sut = new EntityType();
		
		long id = 1;
		String text = "testText";
		
		sut = new EntityType(id, text);

		String toString = sut.toString();
		
		assertTrue(toString.contains("id: "));
		assertTrue(toString.contains("type: "));
	}
}
