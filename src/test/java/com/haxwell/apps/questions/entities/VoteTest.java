package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class VoteTest {

	@Test
	public void testObjectDefaultConstructor() {
		Vote sut = new Vote();
		
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.isThumbsDown() == false);
		assertTrue(sut.getThumbsUp() == 0);
		assertTrue(sut.isThumbsUp() == false);
		
		assertTrue(sut.getId() == 0);
		assertTrue(sut.getEntityId() == 0);
		assertTrue(sut.getEntityType() == null);
		assertTrue(sut.getUser() == null);
	}
}
