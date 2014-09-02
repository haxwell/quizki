package com.haxwell.apps.questions.managers;

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

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // for now.. I think this may actually be a functional test
public class ReferenceManagerTest extends EntityWithIDAndTextValuePairManagerTest {

	@Test
	public void testConstructor() {
		// TODO: Use reflection to verify this constructor is private
		// ReferenceManager rm = new ReferenceManager();
		
		ReferenceManager rm = (ReferenceManager)ReferenceManager.getInstance();
		
		assertTrue(rm == ReferenceManager.getInstance());
	}
	
}
