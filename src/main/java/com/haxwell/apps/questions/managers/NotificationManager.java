package com.haxwell.apps.questions.managers;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Notification;


public class NotificationManager extends Manager {

	public static Notification newNotification()
	{
		Notification notification = new Notification();
		return notification;
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
	
	public static long persistNotification(Notification notification)
	{
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		Notification rtn = em.merge(notification);
		
		em.getTransaction().commit();
		
		return rtn.getId();
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
