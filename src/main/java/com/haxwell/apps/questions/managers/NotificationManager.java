package com.haxwell.apps.questions.managers;

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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EntityTypeConstants;
import com.haxwell.apps.questions.entities.AbstractTextEntity;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Notification;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.factories.EntityTypeFactory;
import com.haxwell.apps.questions.utils.QuestionUtil;


public class NotificationManager extends Manager {

	public static Notification newNotification()
	{
		Notification notification = new Notification();
		return notification;
	}

	public static void issueNotification_questionDeletedAndRemovedFromExam(
			long examOwnerId, long questionId, long examId) {
		Exam exam = ExamManager.getExam(examId);
		Question q = QuestionManager.getQuestionById(questionId);
		
		Notification notification = newNotification();
		
		notification.setUser(exam.getUser());
		notification.setNotificationId(Constants.NOTIFICATION_ID_QUESTION_DELETED);
		notification.setText("Question '" + QuestionUtil.getDisplayString(q, 65) + "' was deleted by its owner. Quizki automatically removed it from Exam '" + exam.getTitle() + "'.");
		notification.setTime_stamp(new java.util.Date());
		
		persistNotification(notification);
	}

	public static void issueNotification_emptyExamWasDeleted(long examId)
	{
		Exam exam = ExamManager.getExam(examId);
		
		Notification notification = newNotification();

		notification.setUser(exam.getUser());
		notification.setNotificationId(Constants.NOTIFICATION_ID_EXAM_DELETED);
		notification.setText("Exam '" + exam.getTitle() + "' only had one question, and that question was deleted by its owner. Since your exam no longer had any questions associated with it, Quizki automatically deleted it.");
		notification.setTime_stamp(new java.util.Date());
		
		persistNotification(notification);
	}
	
	public static void issueNotification_feedbackLeftForExam(long examId, String commentingUsername)
	{
		Exam exam = ExamManager.getExam(examId);
		
		Notification notification = newNotification();
		
		notification.setUser(exam.getUser());
		notification.setNotificationId(Constants.NOTIFICATION_ID_FEEDBACK_LEFT_FOR_EXAM);
		notification.setText(commentingUsername + " left feedback on your exam '" + exam.getTitle() + "'.");
		notification.setTime_stamp(new java.util.Date());
		
		persistNotification(notification);
	}
	
	// TODO, change EntityTypeId to an enum
	public static void  issueNotification_entityWasVotedOn(long entityId, int entityTypeId)
	{
		AbstractTextEntity e = getAbstractTextEntity(entityId, entityTypeId);
		
		Notification notification = getNotification(entityId, entityTypeId, e.getUser().getId());

		int count = 0;
		Date date = null;
		boolean isFirstNotification = false;
		
		if (notification == null) {
			isFirstNotification = true;
			notification = newNotification();
			
			date = new java.util.Date();
			
			notification.setUser(e.getUser());
			notification.setNotificationId(getVotedOnNotificationIdByEntityTypeId(entityTypeId));
			notification.setTime_stamp(date);
			notification.setEntityId(entityId);
			notification.setEntityType(EntityTypeFactory.getEntityTypeFor(e));
		}
		else {
			count = notification.getNumOfInstances();
			date = notification.getTime_stamp();
		}
		
		count++;

		notification.setNumOfInstances(count);
		notification.setText("Your " + e.getEntityDescription() + " '" + e.getText() + "' was voted on " + count + " times on " + date + ".");
		
		if (isFirstNotification)
			persistNotification(notification);
		else
			updateNotification(notification);
	}
	
	private static AbstractTextEntity getAbstractTextEntity(long entityId, int entityTypeId) 
	{
		AbstractTextEntity e = null;
		
		if (entityTypeId == EntityTypeConstants.EXAM)
			e = ExamManager.getExam(entityId);
		else if (entityTypeId == EntityTypeConstants.QUESTION)
			e = QuestionManager.getQuestionById(entityId);
		
		return e;
	}
	
	private static long getVotedOnNotificationIdByEntityTypeId(int entityTypeId)
	{
		long rtn = -1;
		
		if (entityTypeId == EntityTypeConstants.EXAM)
			rtn = Constants.NOTIFICATION_ID_VOTED_ON_EXAM;
		else if (entityTypeId == EntityTypeConstants.QUESTION)
			rtn = Constants.NOTIFICATION_ID_VOTED_ON_QUESTION;
		
		return rtn;
	}
	
	private static Notification getNotification(long entityId, long entityTypeId, long userId) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.entityType.id = :entityTypeId AND n.entityId = :entityId AND n.time_stamp > :midnightToday AND n.time_stamp < :elevenFiftyNineTonight");
		
		query.setParameter("userId", userId);
		query.setParameter("entityId", entityId);
		query.setParameter("entityTypeId", entityTypeId);
		
		Calendar tempDate = Calendar.getInstance();
		tempDate.set(Calendar.HOUR_OF_DAY, 0);
		tempDate.set(Calendar.MINUTE, 0);
		tempDate.set(Calendar.SECOND, 0);
		
		query.setParameter("midnightToday", tempDate.getTime());
		
		tempDate.set(Calendar.HOUR_OF_DAY, 23);
		tempDate.set(Calendar.MINUTE, 59);
		tempDate.set(Calendar.SECOND, 59);
		
		query.setParameter("elevenFiftyNineTonight", tempDate.getTime());

		Notification rtn = null;
		
		try {
			rtn = (Notification)query.getSingleResult();
		}
		catch (javax.persistence.NoResultException nre)
		{
			// do nothing.. assuming our query is correct, this means there is no Notification today for this entity, entityType and userId
		}
		
		em.close();
		
		return rtn;
	}
	
	public static void updateNotification(Notification notification)
	{
		EntityManager em = emf.createEntityManager();
		
		String queryStr = "UPDATE Notification n SET n.numOfInstances=:numOfInstances, n.text=:text WHERE n.id=:notificationId";
		
		Query query = em.createQuery(queryStr);
		
		query.setParameter("numOfInstances", notification.getNumOfInstances());
		query.setParameter("text", notification.getText());
		query.setParameter("notificationId", notification.getId());
		
		em.getTransaction().begin();
		
		query.executeUpdate();
		
		em.getTransaction().commit();
		
		em.close();
	}
	
	public static long persistNotification(Notification notification)
	{
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		em.persist(notification);
		
		em.getTransaction().commit();
		
		return notification.getId();
	}
	
	public static Collection<Notification> getAllNotificationsForUser(long userId)
	{
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT n FROM Notification n, User u WHERE n.user.id = u.id AND u.id = ?1", Notification.class);		
		
		query.setParameter(1, userId);
		
		Collection<Notification> rtn = query.getResultList();
		
		em.close();
		
		return rtn;
	}

	public static void clearAllUserNotifications(long id) {
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();

		Query query = em.createNativeQuery("DELETE FROM notification WHERE user_id = ?1");
		query.setParameter(1, id);
		
		query.executeUpdate();
		
		em.getTransaction().commit();
		
		em.close();
	}
}
