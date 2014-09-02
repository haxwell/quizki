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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class StringUtilTest {

	@Test
	public void testEquals() {
		assertTrue(StringUtil.equals("", ""));

		assertTrue(StringUtil.equals(null, null));
		assertFalse(StringUtil.equals(null, ""));
		assertFalse(StringUtil.equals(null, "nonEmptyString"));
		assertFalse(StringUtil.equals("", null));
		assertFalse(StringUtil.equals("nonEmptyString", null));
		
		assertTrue(StringUtil.equals("test", "test"));
		assertFalse(StringUtil.equals("TeSt", "test"));
		assertFalse(StringUtil.equals("test", "TeSt"));
	}
	
	@Test
	public void testFieldIterator_withDynamicMarkerString() {
		String str = "A [[string]] with [[dynamic]] markers.";
		
		Iterator<String> iterator = new StringUtil.FieldIterator(str, "[[", "]]");
		
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "string"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "dynamic"));
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testFieldIterator_withSingleFieldMarkerString() {
		String str = "A,string,with,comma,delimiters,";
		
		Iterator<String> iterator = new StringUtil.FieldIterator(str, ",");
		
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "A"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "string"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "with"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "comma"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "delimiters"));
		assertFalse(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), null));
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testFieldIterator_withOneSingleThreeCharacterFieldMarkerString() {
		String str = "A,,,string,,,with,,,comma,,,delimiters,,,";
		
		Iterator<String> iterator = new StringUtil.FieldIterator(str, ",,,");
		
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "A"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "string"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "with"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "comma"));
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "delimiters"));
		assertFalse(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), null));
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testFieldIterator_withSingleFieldMarkerString_butNoMarkerInTheString() {
		String str = "A string with comma delimiters";
		
		Iterator<String> iterator = new StringUtil.FieldIterator(str, ",");
		
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "A string with comma delimiters"));
		assertFalse(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), null));
	}

	@Test
	public void testFieldIterator_withSingleThreeCharacterFieldMarkerString_butNoMarkerInTheString() {
		String str = "A string with comma delimiters";
		
		Iterator<String> iterator = new StringUtil.FieldIterator(str, ",,,");
		
		assertTrue(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), "A string with comma delimiters"));
		assertFalse(iterator.hasNext());
		assertTrue(StringUtil.equals(iterator.next(), null));
	}
	
	@Test
	public void testGetStringWithEllipsis_initialStringLongerThan_TO_BE_shortenedLength() {
		String str = "This string should be shortened";
		
		String str2 = StringUtil.getStringWithEllipsis(str, 10);
		
		assertFalse(str2.equals(str));
		assertTrue(str2.length() == 10);
		assertTrue(str2.endsWith("..."));
	}
	
	@Test
	public void testGetStringWithEllipsis_initialStringShorterThan_TO_BE_shortenedLength() {
		String str = "testing!";
		
		String str2 = StringUtil.getStringWithEllipsis(str, 25);
		
		assertTrue(str2.equals(str));
		assertFalse(str2.length() == 25);
		assertFalse(str2.endsWith("..."));
	}
}
