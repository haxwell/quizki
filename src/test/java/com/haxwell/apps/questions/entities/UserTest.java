package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TestQuestionUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class UserTest {

	@Test
	public void testObjectDefaultConstructor() {
		User user = new User();
		
		assertTrue(user.getId() == 0);
		assertTrue(StringUtil.isNullOrEmpty(user.getUsername()));
		assertTrue(StringUtil.isNullOrEmpty(user.getPassword()));
		assertTrue(user.getQuestions() == null);
		assertTrue(user.getExams() == null);
		assertTrue(user.getUserRoles() == null);
	}
	
	@Test
	public void testObjectStringConstructor() {
		String username = "username";
		
		User sut = new User(username);
		
		assertTrue(sut.getId() == 0);
		assertTrue(StringUtil.equals(sut.getUsername(), username));
		assertTrue(StringUtil.isNullOrEmpty(sut.getPassword()));
		assertTrue(sut.getQuestions() == null);
		assertTrue(sut.getExams() == null);
		assertTrue(sut.getUserRoles() == null);
	}
	
	@Test
	public void testSettersAndGetters() {
		long id = 1;
		String username = "username";
		String password = "password";
		Set<Question> qset = TestQuestionUtil.getSetOfQuestions(3);
		Set<Exam> eset = TestQuestionUtil.getSetOfExams(3);
		Set<UserRole> urset = TestQuestionUtil.getSetOfUserRoles(3);
		
		User sut = new User();
		
		sut.setId(id);
		sut.setUsername(username);
		sut.setPassword(password);
		sut.setExams(eset);
		sut.setQuestions(qset);
		sut.setUserRoles(urset);
		
		assertTrue(sut.getId() == id);
		assertTrue(StringUtil.equals(sut.getUsername(), username));
		assertTrue(StringUtil.equals(sut.getPassword(), password));
		
		Set<? extends AbstractEntity> set = sut.getExams();
		for (AbstractEntity ae : set) {
			assertTrue(eset.contains(ae));
		}
		
		set = sut.getQuestions();
		for (AbstractEntity ae : set) {
			assertTrue(qset.contains(ae));
		}
		
		for (UserRole ur : sut.getUserRoles()) {
			assertTrue(urset.contains(ur));
		}
	}
}