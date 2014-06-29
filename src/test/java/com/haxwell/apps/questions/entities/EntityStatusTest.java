package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;		

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.EntityStatusConstants;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class EntityStatusTest {

	@Test
	public void testObjectDefaultConstructor() {
		EntityStatus sut = new EntityStatus();
		
		assertTrue(sut.getEntityId() == 0);
		assertTrue(sut.getEntityType() == 0);
	}
	
	@Test
	public void testSettersAndGetters() {
		EntityStatus sut = new EntityStatus();
		
		sut.setEntityId(1);
		sut.setEntityType(EntityStatusConstants.ACTIVATED);
		
		assertTrue(sut.getEntityId() == 1 );
		assertTrue(sut.getEntityType() == EntityStatusConstants.ACTIVATED);
	}
}
