package com.haxwell.apps.questions.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the NOTIFICATION database table.
 * 
 */
@Entity
@Table(name="notification")
public class Notification implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String text;

	//bi-directional many-to-one association to User
    @ManyToOne
	private User user;

    @Column(name="NOTIFICATION_ID")
    private long notificationId;
    
    @Temporal(TemporalType.TIMESTAMP) 
    @Column(name = "TIME_STAMP") 
    private java.util.Date time_stamp;    
    
	public Notification() {
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public long getNotificationId() {
		return this.notificationId;
	}

	public void setNotificationId(long id) {
		this.id = notificationId;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    public java.util.Date getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(java.util.Date time_stamp) {
		this.time_stamp = time_stamp;
	}
	
	public String getPrettyTime_stamp() {
		return (new SimpleDateFormat("MMM d, yyyy hh:mm").format(getTime_stamp()));
	}
}