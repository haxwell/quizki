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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the ENTITY_STATUS database table.
 * 
 */
@Entity
@Table(name="entity_status")
@Deprecated // Unused... See Issue #68.. the entity status field on Question and Exam takes the place of this.. just didn't delete it for reference sake..
public class EntityStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ENTITY_ID")
	private long entityId;

	@Column(name="ENTITY_TYPE_ID")
	private long entityType;
	
	private int status;

    public EntityStatus() {
    }

    public long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(long id) {
		this.entityId = id;
	}

    public long getEntityType() {
		return this.entityType;
	}

	public void setEntityType(long type) {
		this.entityType = type;
	}

    public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}