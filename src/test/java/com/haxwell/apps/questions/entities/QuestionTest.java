package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.utils.QuestionAttributeSetterUtil;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TestQuestionUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
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
		Set<Choice> choices = TestQuestionUtil.getSetOfChoices();
		Set<Topic> topics = TestQuestionUtil.getSetOfTopics();
		Set<Reference> refs = TestQuestionUtil.getSetOfReferences();
		
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
		Set<Choice> choices = TestQuestionUtil.getSetOfChoices();
		Set<Topic> topics = TestQuestionUtil.getSetOfTopics();
		Set<Reference> refs = TestQuestionUtil.getSetOfReferences();
		
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
		
		// test various attributes
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

		// Test the choices
		JSONArray choicesJSON = (JSONArray)jobj.get("choices");
		assertTrue(choicesJSON != null);
		assertTrue(choicesJSON instanceof JSONArray);
		assertTrue(choicesJSON.size() == 3);

		JSONObject choice1 = (JSONObject)choicesJSON.get(0);
		assertTrue(StringUtil.equals(choice1.get("id"), "1"));
		assertTrue(choice1.get("iscorrect") instanceof Integer);
		assertTrue(StringUtil.equals(choice1.get("iscorrect"), 1));
		assertTrue(StringUtil.equals(choice1.get("text"), "choice1"));
		assertTrue(choice1.get("sequence") instanceof Integer);
		assertTrue(StringUtil.equals(choice1.get("sequence"), 0));
		
		JSONObject choice2 = (JSONObject)choicesJSON.get(1);
		assertTrue(StringUtil.equals(choice2.get("id"), "2"));
		assertTrue(choice2.get("iscorrect") instanceof Integer);
		assertTrue(StringUtil.equals(choice2.get("iscorrect"), 0));
		assertTrue(StringUtil.equals(choice2.get("text"), "choice2"));
		assertTrue(choice2.get("sequence") instanceof Integer);
		assertTrue(StringUtil.equals(choice2.get("sequence"), 0));
		
		JSONObject choice3 = (JSONObject)choicesJSON.get(2);
		assertTrue(StringUtil.equals(choice3.get("id"), "3"));
		assertTrue(choice3.get("iscorrect") instanceof Integer);
		assertTrue(StringUtil.equals(choice3.get("iscorrect"), 0));
		assertTrue(StringUtil.equals(choice3.get("text"), "choice3"));
		assertTrue(choice3.get("sequence") instanceof Integer);
		assertTrue(StringUtil.equals(choice3.get("sequence"), 0));

		// TODO: test topics
		// TODO: test references
		
		// test JSON
		JSONArray ddFieldNames = (JSONArray)jobj.get("dynamicDataFieldNames");
		assertTrue(ddFieldNames.size() == 2);
		assertTrue(StringUtil.equals(ddFieldNames.get(0), dynamicDataKey1));
		assertTrue(StringUtil.equals(ddFieldNames.get(1), dynamicDataKey2));
		
		assertTrue(StringUtil.equals(jobj.get(dynamicDataKey1), dynamicDataValue1));
		assertTrue(StringUtil.equals(jobj.get(dynamicDataKey2), dynamicDataValue2));
	}
	
	@Test
	public void testToJSON_defaults() {
		Question sut = new Question();

		String json = sut.toJSON();
		
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		assertTrue(jobj != null);

		assertTrue(StringUtil.equals(jobj.get("description"), ""));
		assertTrue(StringUtil.equals(jobj.get("text"), ""));
		assertTrue(StringUtil.equals(jobj.get("textWithoutHTML"), ""));
		assertTrue(StringUtil.equals(jobj.get("choices"), ""));
		assertTrue(StringUtil.equals(jobj.get("topics"), ""));
		assertTrue(StringUtil.equals(jobj.get("references"), ""));
		assertTrue(StringUtil.equals(jobj.get("user_id"), "-1"));
		assertTrue(StringUtil.equals(jobj.get("user_name"), ""));
	}
	
	@Test
	public void testDefaultSetIsCreatedIfOneHasNotYetBeenSet() {
		Question sut = new Question();
		
		assertTrue(sut.getChoices() != null);
		assertTrue(sut.getChoices().size() == 0);
		
		assertTrue(sut.getTopics() != null);
		assertTrue(sut.getTopics().size() == 0);
		
		assertTrue(sut.getReferences() != null);
		assertTrue(sut.getReferences().size() == 0);
	}
	
	@Test
	public void testCompareMethod_basedOnText() {
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
	
	@Test
	public void testCompareMethod_basedOnID() {
		Question sut1 = new Question();
		Question sut2 = new Question();
		
		long id = Long.MAX_VALUE - 1;
		String description = "description";
		String text1 = "<p>1 This question text is the same.</p>";
		String text2 = "<p>1 This question text is the same.</p>";
		
		sut1.setId(id);
		sut1.setDescription(description);
		sut1.setText(text1);

		sut2.setId(id - 1);
		sut2.setDescription(description);
		sut2.setText(text2);
		
		assertTrue(sut1.compareTo(sut2) == -1);
		assertTrue(sut2.compareTo(sut1) == 1);
	}
	
	@Test
	public void testEquals() {
		Question sut1 = new Question();
		Question sut2 = new Question();
		
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		
		sut1.setId(1);
		
		assertFalse(sut1.equals(sut2));
		
		sut2.setId(1);
		
		sut1.setText("text");
		assertFalse(sut1.equals(sut2));
		
		sut2.setText("text");
	}
	
	@Test
	public void testToString() {
		Question sut = new Question();
		
		sut.setId(1);
		sut.setText("entity");
		
		assertTrue(sut.toString().contains("ID: "));
		assertTrue(sut.toString().contains("1"));
		assertTrue(sut.toString().contains("Text: "));
		assertTrue(sut.toString().contains("entity"));
	}
}
