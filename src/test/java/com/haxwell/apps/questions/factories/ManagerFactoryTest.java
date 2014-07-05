package com.haxwell.apps.questions.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.EntityTypeConstants;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.Manager;
import com.haxwell.apps.questions.managers.QuestionManager;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class ManagerFactoryTest {

	@Test
	public void testObjectDefaultConstructor() {
		assertTrue(new ManagerFactory() != null);
	}

	@Test
	public void testGetManager_Exam() {
		Manager m = ManagerFactory.getManager(EntityTypeConstants.EXAM_STR);
		
		assertNotEquals(null, m);
		assertTrue(m instanceof ExamManager);
	}

	@Test
	public void testGetManager_Question() {
		Manager m = ManagerFactory.getManager(EntityTypeConstants.QUESTION_STR);
		
		assertNotEquals(null, m);
		assertTrue(m instanceof QuestionManager);
	}

	@Test
	public void testGetManager_UnknownEntityType() {
		Manager m = ManagerFactory.getManager("unknownEntityType");
		assertEquals(null, m);
	}
}
