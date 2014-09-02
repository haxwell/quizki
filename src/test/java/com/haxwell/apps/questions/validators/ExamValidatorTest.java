package com.haxwell.apps.questions.validators;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.TestQuestionUtil;
import com.haxwell.apps.questions.validators.ExamValidator;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class ExamValidatorTest {

	@Test
	public void testValidate_examHasNoTitle() {
		Exam exam = new Exam();
		Map<String, List<String>> map = new HashMap<>();
		Set<Question> set = TestQuestionUtil.getSetOfQuestions(3);
		
		exam.setQuestions(set);
		
		ExamValidator sut = new ExamValidator();
		
		assertFalse(sut.validate(exam, map));
		assertTrue(map.size() == 1);
		
		List<String> list = map.get("examValidationErrors");
		assertNotEquals(null, list);
		assertTrue(list.size() == 1);
	}

	@Test
	public void testValidate_examHasNoQuestions() {
		Exam exam = new Exam();
		Map<String, List<String>> map = new HashMap<>();
		
		exam.setTitle("examTitle");
		
		ExamValidator sut = new ExamValidator();
		
		assertFalse(sut.validate(exam, map));
		assertTrue(map.size() == 1);
		
		List<String> list = map.get("examValidationErrors");
		assertNotEquals(null, list);
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testValidate_examIsValid() {
		Exam exam = new Exam();
		Map<String, List<String>> map = new HashMap<>();
		Set<Question> set = TestQuestionUtil.getSetOfQuestions(3);
		
		exam.setQuestions(set);
		exam.setTitle("examTitle");
		
		ExamValidator sut = new ExamValidator();
		
		assertTrue(sut.validate(exam, map));
		assertTrue(map.size() == 0);
		
		List<String> list = map.get("examValidationErrors");
		assertEquals(null, list);
	}
}
