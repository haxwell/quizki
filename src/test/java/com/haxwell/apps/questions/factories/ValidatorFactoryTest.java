package com.haxwell.apps.questions.factories;

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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.validators.ExamValidator;
import com.haxwell.apps.questions.validators.QuestionValidator;
import com.haxwell.apps.questions.validators.Validator;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class ValidatorFactoryTest {

	@Test
	public void testObjectDefaultConstructor() {
		assertTrue(new ValidatorFactory() != null);
	}

	@Test
	public void testGetValidator_exam() {
		Exam exam = new Exam();
		
		Validator v = ValidatorFactory.getValidator(exam);
		
		assertNotEquals(null, v);
		assertTrue(v instanceof ExamValidator);
	}

	@Test
	public void testGetValidator_question() {
		Question q = new Question();
		
		Validator v = ValidatorFactory.getValidator(q);
		
		assertNotEquals(null, v);
		assertTrue(v instanceof QuestionValidator);
	}
	
	@Test
	public void testGetValidator_choice() {
		Choice c = new Choice();
		
		Validator v = ValidatorFactory.getValidator(c);
		
		assertEquals(null, v);
	}
}
