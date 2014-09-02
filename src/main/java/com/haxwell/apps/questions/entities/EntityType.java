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

import javax.persistence.*;

import com.haxwell.apps.questions.utils.StringUtil;


/**
 * The persistent class for the ENTITY_TYPE database table.
 * 
 */
@Entity
@Table(name="entity_type")
public class EntityType implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String text;

    public EntityType() {
    }
    
    public EntityType(long id, String str) {
    	this.id = id;
    	this.text = str;
    }
    
    public EntityType(long id) {
    	this.id = id;
    }
    
    public EntityType(String str) {
    	this.text = str;
    }

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String toString()
	{
		return "id: " + id + " | type: " + text;
	}

	public boolean equals(Object o) {
		EntityType that;
		boolean rtn = (this == o);
		
		if (!rtn && o instanceof EntityType) {
			that = (EntityType)o;
			
			rtn = (this.id == that.id) && (StringUtil.equals(this.text, that.text));
		}
		
		return rtn;
	}
}