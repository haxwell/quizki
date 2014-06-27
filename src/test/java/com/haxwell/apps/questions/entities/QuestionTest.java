package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.utils.StringUtil;


public class QuestionTest {

	@Test
	public void testObjectDefaultConstructor() {
		Question sut = new Question();
		
		assertTrue(sut.getId() == Question.DEFAULT_ID);
		assertTrue(sut.getText() == Question.DEFAULT_TEXT);
		
		assertTrue(sut.DEFAULT_TEXT.equals(""));
	}
	
	@Test
	public void testSettersAndGetters() {
		Question sut = new Question();
		
		long id = Long.MAX_VALUE - 1;
		String description = "description";
		String text = "<p>Some [[dynamic]] question text</p>";
		String notMarkedUpText = "Some dynamic question text";
		Difficulty difficulty = new Difficulty(DifficultyConstants.GURU_STR, DifficultyConstants.GURU);
		QuestionType qType = new QuestionType(TypeConstants.PHRASE_STR);
		long entityStatus = EntityStatusConstants.ACTIVATED;
		User user = new User(); user.setId(1);
		Set<Choice> choices = getSetOfChoices();
		Set<Topic> topics = getSetOfTopics();
		Set<Reference> refs = getSetOfReferences();
		
		sut.setId(id);
		sut.setDescription(description);
		sut.setText(text);
		sut.setDifficulty(difficulty);
		sut.setQuestionType(qType);
		sut.setEntityStatus(entityStatus);
		sut.setUser(user);
		sut.setChoices(choices);
		sut.setTopics(topics);
		sut.setReferences(refs);
		
		assertTrue(sut.getId() == id);
		assertTrue(StringUtil.equals(sut.getDescription(), description));
		assertTrue(StringUtil.equals(sut.getText(), text));
		assertTrue(StringUtil.equals(sut.getTextWithoutHTML(), notMarkedUpText));
		assertTrue(sut.getDifficulty().equals(difficulty));
		assertTrue(sut.getQuestionType().equals(qType));
		assertTrue(sut.getEntityStatus() == entityStatus);
		assertTrue(sut.getUser().equals(user));
		
		Set<? extends AbstractEntity> set = sut.getChoices();
		for (AbstractEntity ae : set) {
			assertTrue(choices.contains(ae));
		}
		
		set = sut.getTopics();
		for (AbstractEntity ae : set) {
			assertTrue(topics.contains(ae));
		}
		
		set = sut.getReferences();
		for (AbstractEntity ae : set) {
			assertTrue(refs.contains(ae));
		}
	}
	
	@Test
	public void testEntityDescription() {
		Question sut = new Question();
		
		assertTrue(sut.getEntityDescription().equals("question"));
	}
	
	@Test
	public void testToJSON() {
		Question sut = new Question();
		
		long id = Long.MAX_VALUE - 1;
		String description = "description";
		String text = "<p>Some [[dynamic]] question text</p>";
		String notMarkedUpText = "Some dynamic question text";
		Difficulty difficulty = new Difficulty(DifficultyConstants.GURU_STR, DifficultyConstants.GURU);
		QuestionType qType = new QuestionType(TypeConstants.PHRASE, TypeConstants.PHRASE_STR);
		long entityStatus = EntityStatusConstants.ACTIVATED;
		User user = new User(); user.setId(1); user.setUsername("username");
		Set<Choice> choices = getSetOfChoices();
		Set<Topic> topics = getSetOfTopics();
		Set<Reference> refs = getSetOfReferences();
		
		sut.setId(id);
		sut.setDescription(description);
		sut.setText(text);
		sut.setDifficulty(difficulty);
		sut.setQuestionType(qType);
		sut.setEntityStatus(entityStatus);
		sut.setUser(user);
		sut.setChoices(choices);
		sut.setTopics(topics);
		sut.setReferences(refs);
		
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
		assertTrue(StringUtil.equals(jobj.get("description"), description));
		assertTrue(StringUtil.equals(jobj.get("text"), text));
		assertTrue(StringUtil.equals(jobj.get("textWithoutHTML"), notMarkedUpText));
		assertTrue(StringUtil.equals(jobj.get("difficulty_id"), difficulty.getId()));
		assertTrue(StringUtil.equals(jobj.get("difficulty_text"), difficulty.getText()));
		assertTrue(StringUtil.equals(jobj.get("type_id"), qType.getId()));
		assertTrue(StringUtil.equals(jobj.get("type_text"), qType.getText()));
		assertTrue(StringUtil.equals(jobj.get("entityStatus"), entityStatus));
		assertTrue(StringUtil.equals(jobj.get("user_id"), user.getId()));
		assertTrue(StringUtil.equals(jobj.get("user_name"), user.getUsername()));
		
		// test JSON
		JSONArray ddFieldNames = (JSONArray)jobj.get("dynamicDataFieldNames");
		assertTrue(ddFieldNames.size() == 2);
		assertTrue(StringUtil.equals(ddFieldNames.get(0), dynamicDataKey1));
		assertTrue(StringUtil.equals(ddFieldNames.get(1), dynamicDataKey2));
		
		assertTrue(StringUtil.equals(jobj.get(dynamicDataKey1), dynamicDataValue1));
		assertTrue(StringUtil.equals(jobj.get(dynamicDataKey2), dynamicDataValue2));
	}
	
	@Test
	public void testCompareMethod() {
		Question sut1 = new Question();
		Question sut2 = new Question();
		
		long id = Long.MAX_VALUE - 1;
		String description = "description";
		String text1 = "<p>1 This question is first.</p>";
		String text2 = "<p>2 This question is second.</p>";
		
		sut1.setId(id);
		sut1.setDescription(description);
		sut1.setText(text1);

		sut2.setId(id);
		sut2.setDescription(description);
		sut2.setText(text2);
		
		assertTrue(sut1.compareTo(sut2) == -1);
		assertTrue(sut2.compareTo(sut1) == 1);
	}
	
	private Set<Choice> getSetOfChoices() {
		Set<Choice> set = new HashSet<>();
		
		set.add(new Choice(1L, "choice1", Choice.CORRECT));
		set.add(new Choice(2L, "choice1", Choice.NOT_CORRECT));
		set.add(new Choice(3L, "choice1", Choice.NOT_CORRECT));
		
		return set;
	}
	
	private Set<Topic> getSetOfTopics() {
		Set<Topic> set = new HashSet<>();
		
		set.add(new Topic("topic1"));
		set.add(new Topic("topic2"));
		
		return set;
	}

	private Set<Reference> getSetOfReferences() {
		Set<Reference> set = new HashSet<>();
		
		set.add(new Reference("reference1"));
		set.add(new Reference("reference2"));
		
		return set;
	}
}
