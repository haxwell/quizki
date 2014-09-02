package com.haxwell.apps.questions.managers;

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

import static org.junit.Assert.assertTrue;	

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // for now.. I think this may actually be a functional test
public class ExamManagerTest {

	private Exam exam;
	private Question question1;
	private Question question2;
	private Question question3;
	
	@Before
	public void setup() {
		exam = new Exam();
		Set<Question> questions = new HashSet<Question>();

		Question question1 = new Question();
		question1.setId(1);
		
		Question question2 = new Question();
		question1.setId(2);
		
		Question question3 = new Question();
		question1.setId(3);
		
		questions.add(question1);
		questions.add(question2);
		questions.add(question3);
		
		exam.setQuestions(questions);
	}
	
	@After
	public void teardown() {
		
	}
	
	@Test
	public void testRemoveQuestion() {
		ExamManager.removeQuestion(exam, question1);
		assertTrue(exam.getQuestions().size() == 2);
	}
	
	@Test
	public void testRemoveQuestionWithSet() {
		Set<Integer> questionIds = new HashSet<Integer>();
		questionIds.add(1);
		
		ExamManager.removeQuestion(exam, questionIds);

		assertTrue(exam.getQuestions().size() == 2);
	}
}
