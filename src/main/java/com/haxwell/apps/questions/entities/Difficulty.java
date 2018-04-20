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
import java.util.Set;

import javax.persistence.Entity;
//import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//This class did not implement javax.persistance.GenerationType using it now with ATE might cause conflicts in database


import com.haxwell.apps.questions.utils.StringUtil;


/**
 * The persistent class for the DIFFICULTY database table.
 * 
 */
@Entity
@Table(name="difficulty")
public class Difficulty extends AbstractTextEntity implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;
/*
	@Id
	private long id;

	private String text;
*/
	@OneToMany(mappedBy="difficulty")
	Set<Question> questions;
	
	public Difficulty() {
    }
    
    public Difficulty(String str) {
    	this.text = str;
    }
    
    public Difficulty(long id) {
    	this.id = id;
    }
    
    public Difficulty(String str, long id) {
    	this.text = str;
    	this.id = id;
    }
/*
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
*/	
	@Override
	public boolean equals(Object o) {
		boolean rtn = (this == o);
		
		if (!rtn && o instanceof Difficulty) {
			Difficulty that = (Difficulty)o;
			
			rtn = this.id == that.id && StringUtil.equals(this.text, that.text);
		}
		
		return rtn;
	}

}
