package com.haxwell.apps.questions.entities;

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
