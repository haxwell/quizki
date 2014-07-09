package com.haxwell.apps.questions.utils.tests;

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
	public void testGetStringWithEllipsis() {
		String str = "This string should be shortened";
		
		String str2 = StringUtil.getStringWithEllipsis(str, 10);
		
		assertTrue(str2.length() == 10);
		assertTrue(str2.endsWith("..."));
	}
}
