package com.haxwell.apps.questions.utils;

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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.haxwell.apps.questions.constants.DifficultyEnums;
import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.constants.TypeEnums;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.QuestionType;
import com.haxwell.apps.questions.entities.Reference;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.UserRole;

public class TestQuestionUtil {
	
	public static Question getQuestion() {
		Question q = new Question();
		
		long id = RandomIntegerUtil.getRandomInteger(1000);
		String description = "description"+id;
		String text = "<p>Some [[dynamic]] question text for question number " + id + "</p>";
		Difficulty difficulty = new Difficulty(DifficultyEnums.GURU.getValString(), DifficultyEnums.GURU.getRank());
		QuestionType qType = new QuestionType(TypeEnums.SINGLE.getRank(), TypeEnums.SINGLE.getValString());
		long entityStatus = EntityStatusConstants.ACTIVATED;
		User user = new User(); user.setId(1); user.setUsername("username");
		Set<Choice> choices = getSetOfChoices();
		Set<Topic> topics = getSetOfTopics();
		Set<Reference> refs = getSetOfReferences();
		
		q.setId(id);
		q.setDescription(description);
		q.setText(text);
		q.setDifficulty(difficulty);
		q.setQuestionType(qType);
		q.setEntityStatus(entityStatus);
		q.setUser(user);
		q.setChoices(choices);
		q.setTopics(topics);
		q.setReferences(refs);
		
		String dynamicDataKey1 = "dd1";
		String dynamicDataValue1 = "v1";
		String dynamicDataKey2 = "dd2";
		String dynamicDataValue2 = "v2";

		q.setDynamicData(dynamicDataKey1, dynamicDataValue1);
		q.setDynamicData(dynamicDataKey2, dynamicDataValue2);

		return q;
	}
	
	public static Set<Question> getSetOfQuestions(int count) {
		Set<Question> set = new HashSet<>();
		
		List<Integer> ids = RandomIntegerUtil.getRandomlyOrderedListOfUniqueIntegers(count);
		
		for (int i = 0; i < count; i++) {
			Question q = getQuestion();
			
			q.setId(ids.get(i));
			
			set.add(q);
		}
		
		return set;
	}
	
	public static Set<Choice> getSetOfChoices() {
		Set<Choice> set = new HashSet<>();
		
		set.add(new Choice(1L, "choice1", Choice.CORRECT));
		set.add(new Choice(2L, "choice2", Choice.NOT_CORRECT));
		set.add(new Choice(3L, "choice3", Choice.NOT_CORRECT));
		
		return set;
	}
	
	public static Set<Topic> getSetOfTopics() {
		Set<Topic> set = new HashSet<>();
		
		set.add(new Topic("topic1"));
		set.add(new Topic("topic2"));
		
		return set;
	}

	public static Set<Reference> getSetOfReferences() {
		Set<Reference> set = new HashSet<>();
		
		set.add(new Reference("reference1"));
		set.add(new Reference("reference2"));
		
		return set;
	}
	
	public static Exam getExam() {
		Exam exam = new Exam();
		
		long id = RandomIntegerUtil.getRandomInteger(1000);
		String title = "examTitle"+id;
		String message = "examMessage"+id;
		User user = new User(); user.setId(1); user.setUsername("username");
		long entityStatus = EntityStatusConstants.ACTIVATED;
		Difficulty difficulty = new Difficulty(DifficultyEnums.JUNIOR.getValString(), DifficultyEnums.JUNIOR.getRank());
		Set<Question> qset = TestQuestionUtil.getSetOfQuestions(3);
		Set<Topic> topics = TestQuestionUtil.getSetOfTopics();
		
		exam.setId(id);
		exam.setTitle(title);
		exam.setMessage(message);
		exam.setUser(user);
		exam.setDifficulty(difficulty);
		exam.setEntityStatus(entityStatus);
		exam.setQuestions(qset);
		exam.setTopics(topics);

		return exam;
	}
	
	public static Set<Exam> getSetOfExams(int count) {
		Set<Exam> set = new HashSet<>();
		
		List<Integer> ids = RandomIntegerUtil.getRandomlyOrderedListOfUniqueIntegers(count);
		
		for (int i = 0; i<count; i++) {
			Exam e = getExam();
			
			e.setId(ids.get(i));
			
			set.add(e);
		}
		
		return set;
	}
	
	public static Set<UserRole> getSetOfUserRoles(int count) {
		Set<UserRole> set = new HashSet<>();
		
		List<Integer> ids = RandomIntegerUtil.getRandomlyOrderedListOfUniqueIntegers(count);
		
		for (int i=0; i<count; i++) {
			UserRole ur = new UserRole();
			
			Integer id = ids.get(i);
			
			ur.setId(id);
			ur.setText("userRole" + id);
			
			set.add(ur);
		}
		
		return set;
	}
}
