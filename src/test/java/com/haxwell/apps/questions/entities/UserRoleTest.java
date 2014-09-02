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

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class UserRoleTest {

	@Test
	public void testDefaultConstructor() {
		UserRole sut = new UserRole();
		
		assertTrue(sut.getId() == 0);
		assertTrue(StringUtil.isNullOrEmpty(sut.getText()));
	}
	
	@Test
	public void testSettersAndGetters() {
		UserRole sut = new UserRole();
		
		long id = 1;
		String text = "text";
		
		sut.setId(id);
		sut.setText(text);
		
		assertTrue(sut.getId() == id);
		assertTrue(StringUtil.equals(sut.getText(), text));
	}
}
