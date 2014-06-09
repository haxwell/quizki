package com.haxwell.apps.questions.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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