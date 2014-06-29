package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;

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
}
