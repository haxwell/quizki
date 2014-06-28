package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TestQuestionUtil;

public class ExamTest {
	
	@Test
	public void testSettersAndGetters() {
		Exam exam = new Exam();
		
		long id = 1L;
		String title = "examTitle";
		String message = "examMessage";
		User user = new User(); user.setId(1); user.setUsername("username");
		long entityStatus = EntityStatusConstants.ACTIVATED;
		Difficulty difficulty = new Difficulty(DifficultyConstants.JUNIOR_STR, DifficultyConstants.JUNIOR);
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
		assertTrue(exam.getDifficulty().getId() == DifficultyConstants.JUNIOR);
		assertTrue(StringUtil.equals(exam.getDifficulty().getText(), DifficultyConstants.JUNIOR_STR));
		
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
		Difficulty difficulty = new Difficulty(DifficultyConstants.JUNIOR_STR, DifficultyConstants.JUNIOR);
		Set<Topic> topics = TestQuestionUtil.getSetOfTopics();
		
		sut.setId(id);
		sut.setTitle(title);
		sut.setMessage(message);
		sut.setUser(user);
		sut.setDifficulty(difficulty);
		sut.setEntityStatus(entityStatus);
		sut.setTopics(topics);

		String dynamicDataKey1 = "dd1";
		String dynamicDataValue1 = "v1";
		String dynamicDataKey2 = "dd2";
		String dynamicDataValue2 = "v2";
		
		sut.setDynamicData(dynamicDataKey1, dynamicDataValue1);
		sut.setDynamicData(dynamicDataKey2, dynamicDataValue2);

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
//		Difficulty difficulty = new Difficulty(DifficultyConstants.JUNIOR);
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
		
		assertTrue(StringUtil.equals(jobj.get("difficulty"), DifficultyConstants.UNDEFINED));
		assertTrue(StringUtil.equals(jobj.get("difficulty_text"), DifficultyConstants.UNDEFINED_STR));
		
		assertTrue(StringUtil.equals(jobj.get("entityStatus"), entityStatus));
	}
}
