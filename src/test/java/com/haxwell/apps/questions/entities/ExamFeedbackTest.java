package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class ExamFeedbackTest {

	@Test
	public void testObjectDefaultConstructor() {
		ExamFeedback sut = new ExamFeedback();
		
		assertTrue(sut.getId() == 0);
		assertTrue(sut.getExam() == null);
		assertTrue(sut.getCommentingUser() == null);
		assertTrue(sut.getComment() == null);
	}
}
