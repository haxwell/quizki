package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class ReferenceTest {

	@Test
	public void testSettersAndGetters() {
		Reference sut = new Reference();
		
		sut.setId(1);
		sut.setText("reference1");
		
		assertTrue(sut.getId() == 1);
		assertTrue(StringUtil.equals(sut.getText(), "reference1"));
	}
	
	@Test
	public void testToJSON() {
		Reference sut = new Reference();
		
		sut.setId(1);
		sut.setText("reference1");
		
		String json = sut.toJSON();
		
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		assertTrue(jobj != null);
		
		assertTrue(StringUtil.equals(jobj.get("id"), "1"));
		assertTrue(StringUtil.equals(jobj.get("text"), "reference1"));
		
		assertTrue(jobj.keySet().size() == 2);
		assertTrue(jobj.values().size() == 2);
	}
}
