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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
public class Notification extends AbstractTextEntity implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	//bi-directional many-to-one association to User
    @ManyToOne
	private User user;

    @Column(name="NOTIFICATION_ID")
    private long notificationId;
    
    @Temporal(TemporalType.TIMESTAMP) 
    @Column(name = "TIME_STAMP") 
    private java.util.Date time_stamp;    
    
	//uni-directional many-to-one association to QuestionType
    @ManyToOne
	@JoinColumn(name="entity_type_id")
	private EntityType entityType;
    
    @Column(name="entity_id")
    private long entityId;

    private int numOfInstances;
    
    public Notification() {
    }
    
    public long getNotificationId() {
		return this.notificationId;
	}

	public void setNotificationId(long id) {
		this.notificationId = id;
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
	
	public EntityType getEntityType() {
		return this.entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
	
    public long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public int getNumOfInstances() {
		return numOfInstances;
	}

	public void setNumOfInstances(int numOfInstances) {
		this.numOfInstances = numOfInstances;
	}

	public String getPrettyTime_stamp() {
		Date time_stamp2 = getTime_stamp();
		String rtn = null;
		
		if (time_stamp2 != null)
			rtn = (new SimpleDateFormat("MMM d, yyyy hh:mm").format(time_stamp2));
		
		return rtn;
	}
}