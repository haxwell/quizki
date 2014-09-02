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

import java.util.Date;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.EntityTypeConstants;
import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class NotificationTest {

	@Test
	public void testDefaultConstructor() {
		Notification sut = new Notification();
		
		assertTrue(sut.getId() == 0);
		assertTrue(sut.getEntityId() == 0);
		assertTrue(sut.getEntityType() == null);
		assertTrue(sut.getNotificationId() == 0);
		assertTrue(StringUtil.isNullOrEmpty(sut.getPrettyTime_stamp()));
		assertTrue(sut.getNumOfInstances() == 0);
		assertTrue(StringUtil.isNullOrEmpty(sut.getText()));
		assertTrue(sut.getTime_stamp() == null);
		assertTrue(sut.getUser() == null);
	}
	
	@Test
	public void testSettersAndGetters() {
		Notification sut = new Notification();
		
		long id = 1;
		long entityId = 23;
		EntityType entityType = new EntityType(EntityTypeConstants.EXAM, EntityTypeConstants.EXAM_STR);
		long notificationId = 34;
		int numOfInstances = 5;
		String text = "notificationText";
		User user = new User(); user.setId(1); user.setUsername("username");
		Date date = new Date(114, 6, 2, 18, 15, 15);
		
		sut.setId(id);
		sut.setEntityId(entityId);
		sut.setEntityType(entityType);
		sut.setNotificationId(notificationId);
		sut.setNumOfInstances(numOfInstances);
		sut.setText(text);
		sut.setUser(user);
		sut.setTime_stamp(date);
		
		assertTrue(sut.getId() == id);
		assertTrue(sut.getEntityId() == entityId);
		assertTrue(sut.getEntityType().equals(entityType));
		assertTrue(sut.getNotificationId() == notificationId);
		assertTrue(sut.getNumOfInstances() == numOfInstances);
		assertTrue(StringUtil.equals(sut.getText(), text));
		assertTrue(sut.getUser().equals(user));
		assertTrue(StringUtil.equals(sut.getPrettyTime_stamp(), "Jul 2, 2014 06:15"));
	}
	
	@Test
	public void testGetPrettyTime_stamp() {
		Notification sut = new Notification();
		
		assertTrue(sut.getPrettyTime_stamp() == null);
	}
}
