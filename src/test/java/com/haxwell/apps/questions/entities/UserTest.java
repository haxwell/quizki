package com.haxwell.apps.questions.entities;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import static org.junit.Assert.assertFalse;
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
	
	@Test
	public void testEquals() {
		User sut1 = new User();
		User sut2 = new User();
		
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		
		sut1.setId(1);
		
		assertFalse(sut1.equals(sut2));
		
		sut2.setId(1);
		
		assertTrue(sut1.equals(sut2));
		
		sut2.setUsername("username");
		
		assertFalse(sut1.equals(sut2));
		
		assertFalse(sut1.equals("test"));
	}
	
	@Test
	public void testToString() {
		User sut = new User();
		
		assertTrue(sut.toString().contains("ID: "));
		assertTrue(sut.toString().contains("Username: "));
	}

}
