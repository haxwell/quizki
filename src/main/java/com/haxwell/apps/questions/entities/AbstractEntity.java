package com.haxwell.apps.questions.entities;

import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Transient;



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


@MappedSuperclass
public abstract class AbstractEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id; //can this be set to a default?

	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
		
	@Transient	
	public User getUser() {
		return null;
	}
	@Transient
	public String getEntityDescription()  {
		return null;
	}
	
	//In Java version 1.8 this can be moved to a default method in a QuizkiSerializable interface
	public String toJSON() {
		return null;
	}

	
}
