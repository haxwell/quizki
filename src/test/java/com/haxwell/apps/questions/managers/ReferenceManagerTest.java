package com.haxwell.apps.questions.managers;

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
