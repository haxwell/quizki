package com.haxwell.apps.questions.managers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // for now.. I think this may actually be a functional test
public class TopicManagerTest extends EntityWithIDAndTextValuePairManagerTest {

	@Test
	public void testConstructor() {
		// TODO: Use reflection to verify this constructor is private
		// TopicManager rm = new TopicManager();
		
		TopicManager rm = (TopicManager)TopicManager.getInstance();
		
		assertTrue(rm == TopicManager.getInstance());
	}

}
