package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
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
	
	@Test
	public void testEntityDescription() {
		Topic sut = new Topic();
		
		assertTrue(StringUtil.equals(sut.getEntityDescription(), "topic"));
	}
	
	@Test
	public void equals() {
		Topic sut1 = new Topic();
		Topic sut2 = new Topic();
		
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		
		sut1.setId(1);
		
//		assertFalse(sut1.equals(sut2));
		
		sut2.setId(1);
		sut2.setText("fdsaf");
		
		assertFalse(sut1.equals(sut2));
		
		sut1.setText("FDSAf");
		assertTrue(sut1.equals(sut2));
		
		assertFalse(sut1.equals("aString"));
	}
	
	@Test
	public void testToString() {
		Topic sut = new Topic();
		
		sut.setId(1);
		sut.setText("entity");
		
		assertTrue(sut.toString().contains("id: "));
		assertTrue(sut.toString().contains("1"));
		assertTrue(sut.toString().contains("text: "));
		assertTrue(sut.toString().contains("entity"));
	}	
}
