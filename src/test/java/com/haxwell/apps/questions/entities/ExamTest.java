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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.DifficultyEnums;
import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TestQuestionUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class ExamTest {
	
	@Test
	public void testSettersAndGetters() {
		Exam exam = new Exam();
		
		long id = 1L;
		String title = "examTitle";
		String message = "examMessage";
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
		
		assertTrue(exam.getId() == id);
		assertTrue(StringUtil.equals(exam.getTitle(), title));
		assertTrue(StringUtil.equals(exam.getText(), title));
		assertTrue(StringUtil.equals(exam.getMessage(), message));
		assertTrue(exam.getUser() != null);
		assertTrue(exam.getUser().getId() == 1);
		assertTrue(exam.getUser().getUsername().equals("username"));
		assertTrue(exam.getEntityStatus() == EntityStatusConstants.ACTIVATED);
		assertTrue(exam.getDifficulty().getId() == DifficultyEnums.JUNIOR.getRank());
		assertTrue(StringUtil.equals(exam.getDifficulty().getText(), DifficultyEnums.JUNIOR.getValString()));
		
		Set<Question> examQuestions = exam.getQuestions();
		for (Question q : examQuestions) {
			assertTrue(qset.contains(q));
		}
		
		Set<Topic> examTopics = exam.getTopics();
		for (Topic t : examTopics) {
			assertTrue(topics.contains(t));
		}
	}
	
	@Test
	public void testAddQuestion() {
		Exam exam = new Exam();
		
		exam.addQuestion(TestQuestionUtil.getQuestion());
		
		assertTrue(exam.getQuestions() != null);
		assertTrue(exam.getQuestions().size() == 1);
		assertTrue(exam.getNumberOfQuestions() == 1);
	}
	
	@Test
	public void testAddQuestions2() {
		Exam exam = new Exam();
		
		assertTrue(exam.getQuestions() != null);

		exam.addQuestion(TestQuestionUtil.getQuestion());
		
		assertTrue(exam.getQuestions().size() == 1);
		assertTrue(exam.getNumberOfQuestions() == 1);
	}
	
	@Test
	public void testGetQuestions() {
		Exam exam = new Exam();
		
		assertTrue(exam.getQuestions() != null);
		assertTrue(exam.getQuestions().size() == 0);
		assertTrue(exam.getNumberOfQuestions() == 0);
	}
/*
 * 
 * get/setDynamicData was removed and is not referenced anywhere in the project on this entity (except here)
 * 	
	@Test
	public void testNoDynamicData() {
		Exam exam = new Exam();
		
		exam.setDynamicData("key", "value");
		
		assertTrue(exam.getDynamicData("key") == null);
	}
*/
	@Test
	public void testEntityDescription() {
		Exam exam = new Exam();
		
		assertTrue(StringUtil.equals(exam.getEntityDescription(), "exam"));
	}
	
	@Test
	public void testToJSON_withCompleteDifficultyObject_andEnsureNoDynamicDataInJSON() {
		Exam sut = new Exam();
		
		long id = 1L;
		String title = "examTitle";
		String message = "examMessage";
		User user = new User(); user.setId(1); user.setUsername("username");
		long entityStatus = EntityStatusConstants.ACTIVATED;
		Difficulty difficulty = new Difficulty(DifficultyEnums.JUNIOR.getValString(), DifficultyEnums.JUNIOR.getRank());
		Set<Topic> topics = TestQuestionUtil.getSetOfTopics();
		
		sut.setId(id);
		sut.setTitle(title);
		sut.setMessage(message);
		sut.setUser(user);
		sut.setDifficulty(difficulty);
		sut.setEntityStatus(entityStatus);
		sut.setTopics(topics);

		/*
		 * get/setDynamicData was removed from Exam and could not produce data in the JSON object
		 * 
		String dynamicDataKey1 = "dd1";
		String dynamicDataValue1 = "v1";
		String dynamicDataKey2 = "dd2";
		String dynamicDataValue2 = "v2";
		
		sut.setDynamicData(dynamicDataKey1, dynamicDataValue1);
		sut.setDynamicData(dynamicDataKey2, dynamicDataValue2);
		*/
		String json = sut.toJSON();
		
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		assertTrue(jobj != null);
		
		assertTrue(StringUtil.equals(jobj.get("id"), id+""));
		assertTrue(StringUtil.equals(jobj.get("title"), title));
		assertTrue(StringUtil.equals(jobj.get("message"), message));
		assertTrue(StringUtil.equals(jobj.get("owningUserId"), user.getId()+""));
		
		JSONArray arr = (JSONArray)jobj.get("topics");
		
		assertTrue(arr.size() == 2);
		
		for (int i = 0; i < arr.size(); i++) {
			JSONObject topicobj = (JSONObject)arr.get(i);
			assertFalse(StringUtil.isNullOrEmpty(topicobj.get("id").toString()));
			assertFalse(StringUtil.isNullOrEmpty(topicobj.get("text").toString()));
		}
		
		assertTrue(StringUtil.equals(jobj.get("difficulty"), difficulty.getId()));
		assertTrue(StringUtil.equals(jobj.get("difficulty_text"), difficulty.getText()));
		
		assertTrue(StringUtil.equals(jobj.get("entityStatus"), entityStatus));
		
		JSONArray ddFieldNames = (JSONArray)jobj.get("dynamicDataFieldNames");
		assertTrue(ddFieldNames == null);
	}

	
	@Test
	public void testToJSON_withNoDifficultyObjectSet() {
		Exam sut = new Exam();
		
		long id = 1L;
		String title = "examTitle";
		String message = "examMessage";
		User user = new User(); user.setId(1); user.setUsername("username");
		long entityStatus = EntityStatusConstants.ACTIVATED;
//		Difficulty difficulty = new Difficulty(DifficultyEnums.JUNIOR.getRank());
		Set<Topic> topics = TestQuestionUtil.getSetOfTopics();
		
		sut.setId(id);
		sut.setTitle(title);
		sut.setMessage(message);
		sut.setUser(user);
//		sut.setDifficulty(difficulty);
		sut.setEntityStatus(entityStatus);
		sut.setTopics(topics);

		String json = sut.toJSON();
		
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		assertTrue(jobj != null);
		
		assertTrue(StringUtil.equals(jobj.get("id"), id+""));
		assertTrue(StringUtil.equals(jobj.get("title"), title));
		assertTrue(StringUtil.equals(jobj.get("message"), message));
		assertTrue(StringUtil.equals(jobj.get("owningUserId"), user.getId()+""));
		
		JSONArray arr = (JSONArray)jobj.get("topics");
		
		assertTrue(arr.size() == 2);
		
		for (int i = 0; i < arr.size(); i++) {
			JSONObject topicobj = (JSONObject)arr.get(i);
			assertFalse(StringUtil.isNullOrEmpty(topicobj.get("id").toString()));
			assertFalse(StringUtil.isNullOrEmpty(topicobj.get("text").toString()));
		}
		
		assertTrue(StringUtil.equals(jobj.get("difficulty"), DifficultyEnums.UNDEFINED.getRank()));
		assertTrue(StringUtil.equals(jobj.get("difficulty_text"), DifficultyEnums.UNDEFINED.getValString()));
		
		assertTrue(StringUtil.equals(jobj.get("entityStatus"), entityStatus));
	}
	
	@Test
	public void testToString() {
		Exam sut = new Exam();
		
		sut.setId(1);
		sut.setTitle("entity");
		
		assertTrue(sut.toString().contains("ID: "));
		assertTrue(sut.toString().contains("1"));
		assertTrue(sut.toString().contains("Title: "));
		assertTrue(sut.toString().contains("entity"));
	}
}
