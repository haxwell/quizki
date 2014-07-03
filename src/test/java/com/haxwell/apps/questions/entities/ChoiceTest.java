package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class ChoiceTest {

	@Test
	public void testObjectDefaultConstructor() {
		Choice sut = new Choice();

		assertTrue(sut.getId() == Choice.NO_ID);
		assertTrue(sut.getText() == null);
		assertTrue(sut.getIscorrect() == Choice.NOT_CORRECT);
		assertTrue(sut.getSequence() == Choice.NO_SEQUENCE);
	}
	
	@Test
	public void testObjectLongStringBooleanConstructor() {
		long choiceId = 1;
		String text = "choiceText";
		boolean isCorrect = true;
		
		Choice sut = new Choice(choiceId, text, isCorrect);

		assertTrue(sut.getId() == choiceId);
		assertTrue(sut.getText().equals(text));
		assertTrue(sut.getIscorrect() == Choice.CORRECT);
		assertTrue(sut.getSequence() == Choice.NO_SEQUENCE);
		
		sut = new Choice(choiceId, text, !isCorrect);
		
		assertTrue(sut.getId() == choiceId);
		assertTrue(sut.getText().equals(text));
		assertTrue(sut.getIscorrect() == Choice.NOT_CORRECT);
		assertTrue(sut.getSequence() == Choice.NO_SEQUENCE);
	}
	
	@Test
	public void testObjectLongStringIntInt() {
		long choiceId = 1;
		String text = "choiceText";
		
		Choice sut = new Choice(choiceId, text, Choice.CORRECT, Choice.NO_SEQUENCE);
		
		assertTrue(sut.getId() == choiceId);
		assertTrue(sut.getText().equals(text));
		assertTrue(sut.getIscorrect() == Choice.CORRECT);
		assertTrue(sut.getSequence() == Choice.NO_SEQUENCE);
		
		sut = new Choice(choiceId, text, Choice.NOT_CORRECT, Choice.NO_SEQUENCE);

		assertTrue(sut.getId() == choiceId);
		assertTrue(sut.getText().equals(text));
		assertTrue(sut.getIscorrect() == Choice.NOT_CORRECT);
		assertTrue(sut.getSequence() == Choice.NO_SEQUENCE);
	}

	@Test
	public void testObjectLongStringBooleanLongConstructor() {
		long choiceId = 1;
		String text = "choiceText";
		boolean isCorrect = true;
		int sequence = 2;
		
		Choice sut = new Choice(choiceId, text, isCorrect, sequence);

		assertTrue(sut.getId() == choiceId);
		assertTrue(sut.getText().equals(text));
		assertTrue(sut.getIscorrect() == Choice.CORRECT);
		assertTrue(sut.getSequence() == sequence);
	}

	@Test
	public void testSettersAndGetters() {
		long choiceId = 1;
		String text = "choiceText";
		String text2 = "choiceText2";
		boolean isCorrect = true;
		int sequence = 2;
		
		Choice sut = new Choice(choiceId, text, isCorrect, sequence);
		
		sut.setId(choiceId+1);
		sut.setText(text2);
		sut.setIscorrect(!isCorrect);
		sut.setSequence(sequence+1);
		
		assertTrue(sut.getId() == (choiceId + 1));
		assertTrue(sut.getText().equals(text2));
		assertTrue(sut.getIscorrect() == Choice.NOT_CORRECT);
		assertTrue(sut.getSequence() == (sequence + 1));
		
		sut.setIscorrect(isCorrect);
		assertTrue(sut.getIscorrect() == Choice.CORRECT);
		
		sut.setIscorrect(Integer.MAX_VALUE -1);
		assertTrue(sut.getIscorrect() == Choice.CORRECT);
		
		sut.setIscorrect(Choice.CORRECT);
		assertTrue(sut.getIsCorrect() == Choice.CORRECT);
		
		sut.setIscorrect(Choice.NOT_CORRECT);
		assertTrue(sut.getIsCorrect() == Choice.NOT_CORRECT);
		
		sut.setIsCorrect(Choice.CORRECT);
		assertTrue(sut.getIsCorrect() == Choice.CORRECT);

		sut.setIsCorrect(Choice.NOT_CORRECT);
		assertTrue(sut.getIsCorrect() == Choice.NOT_CORRECT);
	}
	
	@Test
	public void testEquals() {
		long choiceId = 1;
		String text = "choiceText";
		boolean isCorrect = true;
		int sequence = 2;
		
		Choice sut1 = new Choice(choiceId, text, isCorrect, sequence);
		Choice sut2 = new Choice(choiceId, text, isCorrect, sequence);
		Choice sut3 = new Choice(choiceId+1, text, isCorrect, sequence);
		Choice sut4 = new Choice(choiceId, text+"!", isCorrect, sequence);
		Choice sut5 = new Choice(choiceId, text, !isCorrect, sequence);
		Choice sut6 = new Choice(choiceId, text, isCorrect, sequence+1);
		
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		assertFalse(sut1.equals(sut3));
		assertFalse(sut1.equals(sut4));
		assertFalse(sut1.equals(sut5));
		assertFalse(sut1.equals(sut6));
		assertFalse(sut1.equals("aString"));
	}
	
	@Test
	public void testToJSON_WithSequenceNumberSet() {
		long choiceId = 1;
		String text = "choiceText";
		boolean isCorrect = true;
		int sequence = 2;

		Choice sut1 = new Choice(choiceId, text, isCorrect, sequence);
		
		String json = sut1.toJSON();
		
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		assertTrue(jobj != null);
		
		assertTrue(jobj.get("id").equals(choiceId+""));
		assertTrue(jobj.get("iscorrect").equals(isCorrect+""));
		assertTrue(jobj.get("text").equals(text));
		assertTrue(jobj.get("sequence").equals(sequence+""));
		
		assertTrue(jobj.keySet().size() == 4);
		assertTrue(jobj.values().size() == 4);
	}

	@Test
	public void testToJSON_WithoutSequenceNumberSet() {
		long choiceId = 1;
		String text = "choiceText";
		boolean isCorrect = true;

		Choice sut1 = new Choice(choiceId, text, isCorrect);
		
		String json = sut1.toJSON();
		
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		assertTrue(jobj != null);
		
		assertTrue(jobj.get("id").equals(choiceId+""));
		assertTrue(jobj.get("iscorrect").equals(isCorrect+""));
		assertTrue(jobj.get("text").equals(text));
		assertTrue(jobj.get("sequence").equals("0"));

		assertTrue(jobj.keySet().size() == 4);
		assertTrue(jobj.values().size() == 4);
	}
	
	@Test
	public void testToString() {
		long choiceId = 1;
		String text = "choiceText";
		boolean isCorrect = true;

		Choice sut1 = new Choice(choiceId, text, isCorrect);
		
		String toString = sut1.toString();
		
		assertTrue(toString.contains("id: "));
		assertTrue(toString.contains("text: "));
		assertTrue(toString.contains("isCorrect: "));
		assertTrue(toString.contains("sequence: "));
	}
}
