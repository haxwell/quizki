package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TestQuestionUtil;

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
	
	@Test
	public void testObjectExamUserStringConstructor() {
		Exam e = TestQuestionUtil.getExam();
		User u = new User();
		String feedback = "userFeedback";
		
		ExamFeedback sut = new ExamFeedback(e, u, feedback);
		
		assertTrue(sut.getExam().equals(e));
		assertTrue(sut.getCommentingUser().equals(u));
		assertTrue(StringUtil.equals(sut.getComment(), feedback));
		assertTrue(sut.getId() == 0);
	}
	
	@Test
	public void testSettersAndGetters() {
		ExamFeedback sut = new ExamFeedback();

		Exam e = TestQuestionUtil.getExam();
		User u = new User();
		String feedback = "userFeedback";
		
		sut.setId(1);
		sut.setExam(e);
		sut.setCommentingUser(u);
		sut.setComment(feedback);
		
		assertTrue(sut.getId() == 1);
		assertTrue(sut.getExam().equals(e));
		assertTrue(StringUtil.equals(sut.getComment(), feedback));
		assertTrue(sut.getCommentingUser().equals(u));
	}
}
