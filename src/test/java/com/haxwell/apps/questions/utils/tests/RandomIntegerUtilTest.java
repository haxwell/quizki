package com.haxwell.apps.questions.utils.tests;

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

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.RandomIntegerUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class RandomIntegerUtilTest {

	@Test
	public void testDefaultConstructor() {
		assertTrue(new RandomIntegerUtil() != null);
	}
	
	@Test
	public void testGetRandomlyOrderedListOfUniqueIntegers() {
		final int LIST_SIZE = 10;
		List<Integer> list = RandomIntegerUtil.getRandomlyOrderedListOfUniqueIntegers(LIST_SIZE);
		
		assertTrue(list != null);
		assertTrue(list.size() == LIST_SIZE);
		
		int count = 0;
		boolean inOrder = true;
		
		for (; count < LIST_SIZE && inOrder; count++) {
			Integer i = list.get(count);
			
			inOrder = i.equals(count);
		}
		
		assertTrue(count != LIST_SIZE);
	}
	
	@Test
	public void testGetRandomNumber() {
		final int MAX = 455;
		int i = RandomIntegerUtil.getRandomInteger(MAX);
		
		assertTrue(i > -1);
		assertTrue(i < MAX);
	}
}
