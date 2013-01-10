package com.haxwell.apps.questions.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the DIFFICULTY database table.
 * 
 */
@Entity
@Table(name="difficulty")
public class Difficulty implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String text;

    public Difficulty() {
    }
    
    public Difficulty(String str) {
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

}