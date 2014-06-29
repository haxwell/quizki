package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;

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
		
		sut.setId(id);
		sut.setEntityId(entityId);
		sut.setEntityType(entityType);
		sut.setNotificationId(notificationId);
		sut.setNumOfInstances(numOfInstances);
		sut.setText(text);
		sut.setUser(user);
		
		assertTrue(sut.getId() == id);
		assertTrue(sut.getEntityId() == entityId);
		assertTrue(sut.getEntityType().equals(entityType));
		assertTrue(sut.getNotificationId() == notificationId);
		assertTrue(sut.getNumOfInstances() == numOfInstances);
		assertTrue(StringUtil.equals(sut.getText(), text));
		assertTrue(sut.getUser().equals(user));
	}
}
