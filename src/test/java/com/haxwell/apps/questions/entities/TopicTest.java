package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;

import com.haxwell.apps.questions.utils.StringUtil;

public class TopicTest {

	@Test
	public void testSettersAndGetters() {
		Topic sut = new Topic();
		
		sut.setId(1);
		sut.setText("topic1");
		
		assertTrue(sut.getId() == 1);
		assertTrue(StringUtil.equals(sut.getText(), "topic1"));
	}
	
	@Test
	public void testToJSON() {
		Topic sut = new Topic();
		
		sut.setId(1);
		sut.setText("topic1");
		
		String json = sut.toJSON();
		
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		assertTrue(jobj != null);
		
		assertTrue(StringUtil.equals(jobj.get("id"), "1"));
		assertTrue(StringUtil.equals(jobj.get("text"), "topic1"));
		
		assertTrue(jobj.keySet().size() == 2);
		assertTrue(jobj.values().size() == 2);
	}
	
	@Test
	public void testFromJSON() {
		String json = "{ \"id\":\"1\", \"text\":\"topic1\"}";
		
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		Topic sut = new Topic();
		sut = sut.fromJSON(jobj);
		
		assertTrue(StringUtil.equals(sut.getId(), jobj.get("id")));
		assertTrue(StringUtil.equals(sut.getText(), jobj.get("text")));
	}
}
