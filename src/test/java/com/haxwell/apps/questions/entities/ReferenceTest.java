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

import static org.junit.Assert.assertFalse;
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

	@Test
	public void testEntityDescription() {
		Reference sut = new Reference();
		
		assertTrue(StringUtil.equals(sut.getEntityDescription(), "reference"));
	}

	@Test
	public void equals() {
		Reference sut1 = new Reference();
		Reference sut2 = new Reference();
		
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
		Reference sut = new Reference();
		
		sut.setId(1);
		sut.setText("entity");
		
		assertTrue(sut.toString().contains("id: "));
		assertTrue(sut.toString().contains("1"));
		assertTrue(sut.toString().contains("text: "));
		assertTrue(sut.toString().contains("entity"));
	}
}
