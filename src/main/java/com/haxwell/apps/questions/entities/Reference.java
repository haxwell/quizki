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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import net.minidev.json.JSONObject;

import com.haxwell.apps.questions.utils.StringUtil;


/**
 * The persistent class for the REFERENCE database table.
 * 
 */
@Entity
@Table(name="reference")
public class Reference extends AbstractEntity implements EntityWithIDAndTextValuePairBehavior, Serializable {
	private static final long serialVersionUID = 4623732L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String text;

	@ManyToMany(mappedBy="references")
	Set<Question> questions;
	
	public Reference() {
    }

	public String getEntityDescription()  {
		return "reference";
	}
	
    public Reference(String str) {
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
	
	@Override
	public int hashCode()
	{
		return this.text.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		boolean rtn = (this == o);
		
		if (!rtn && o instanceof Reference)
		{
			Reference that = (Reference)o;
			
			rtn = /*(this.id == that.id) && */(StringUtil.equalsCaseInsensitive(this.text, that.text)); 
		}
		
		return rtn;
	}

	@Override
	public String toString()
	{
		return "id: " + this.id + " |text: " + this.text;
	}
	
    public String toJSON() {
    	JSONObject j = new JSONObject();
    	
    	j.put("id", getId());
    	j.put("text", getText());
    	
    	return j.toJSONString();
    }
}