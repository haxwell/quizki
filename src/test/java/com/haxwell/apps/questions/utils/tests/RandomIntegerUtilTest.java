package com.haxwell.apps.questions.utils.tests;

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
