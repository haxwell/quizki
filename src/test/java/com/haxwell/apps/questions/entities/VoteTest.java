package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.EntityTypeConstants;

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
	
	@Test
	public void testSettersAndGetters() {
		Vote sut = new Vote();

		long id = 1;
		User user = new User();
		long entityId = 1;
		EntityType entityType = new EntityType(EntityTypeConstants.QUESTION_STR);
		
		sut.setId(id);
		sut.setUser(user);
		sut.setEntityId(entityId);
		sut.setEntityType(entityType);
		
		assertTrue(sut.getId() == id);
		assertTrue(sut.getUser().equals(user));
		assertTrue(sut.getEntityId() == entityId);
		assertTrue(sut.getEntityType().equals(entityType));
		
		// assert that this vote is neutral
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 0);
		
		assertFalse(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());
		
		// test the down cases
		sut.setThumbsDown(true);
		assertTrue(sut.getThumbsDown() == 1);
		assertTrue(sut.getThumbsUp() == 0);
		assertTrue(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());
		
		sut.setThumbsDown(false);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 0);
		assertFalse(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());
		
		sut.setThumbsDown(1);
		assertTrue(sut.getThumbsDown() == 1);
		assertTrue(sut.getThumbsUp() == 0);
		assertTrue(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());

		sut.setThumbsDown(0);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 0);
		assertFalse(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());
		
		sut.setThumbsDown(23);
		assertTrue(sut.getThumbsDown() == 1);
		assertTrue(sut.getThumbsUp() == 0);
		assertTrue(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());

		sut.setThumbsDown(-342);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 0);
		assertFalse(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());

		// vote is neutral again.. test the up cases.
		sut.setThumbsUp(true);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 1);
		assertFalse(sut.isThumbsDown());
		assertTrue(sut.isThumbsUp());
		
		sut.setThumbsUp(false);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 0);
		assertFalse(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());
		
		sut.setThumbsUp(1);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 1);
		assertFalse(sut.isThumbsDown());
		assertTrue(sut.isThumbsUp());

		sut.setThumbsUp(0);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 0);
		assertFalse(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());
		
		sut.setThumbsUp(23);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 1);
		assertFalse(sut.isThumbsDown());
		assertTrue(sut.isThumbsUp());

		sut.setThumbsUp(-342);
		assertTrue(sut.getThumbsDown() == 0);
		assertTrue(sut.getThumbsUp() == 0);
		assertFalse(sut.isThumbsDown());
		assertFalse(sut.isThumbsUp());
		
		// verify that the vote can't be in Up and Down state at same time.
		sut.setThumbsDown(true);
		sut.setThumbsUp(true);
		
		assertFalse(sut.isThumbsDown());
		
		sut.setThumbsDown(true);
		
		assertFalse(sut.isThumbsUp());
	}
	
	@Test
	public void testEquals() {
		Vote sut1 = new Vote();
		Vote sut2 = new Vote();
		
		assertTrue(sut1.equals(sut2));
		
		sut1.setThumbsUp(true);
		
		assertFalse(sut1.equals(sut2));
		
		sut1.setThumbsDown(true);
		
		assertFalse(sut1.equals(sut2));
		
		sut1.setId(1);
		
		assertFalse(sut1.equals(sut2));
		
		sut2.setId(2);
		
		assertFalse(sut1.equals(sut2));
		
		sut2.setId(1);
		
		sut2.setThumbsDown(true);
		
		assertTrue(sut1.equals(sut2)); // both have id=1 and thumbsDown=true at this point.. should be considered equal
	}
}
