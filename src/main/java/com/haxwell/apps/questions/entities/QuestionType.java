package com.haxwell.apps.questions.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;


/**
 * The persistent class for the QUESTION_TYPE database table.
 * 
 */
@Entity
@Table(name="question_type")
public class QuestionType implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String text;

	@OneToMany(mappedBy="questionType")
	Set<Question> questions;

	public QuestionType() {
    }
    
    public QuestionType(long id) {
    	this.id = id;
    }
    
    public QuestionType(String str) {
    	this.text = str;
    }
    
    public QuestionType(long id, String str) {
    	this.id = id;
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

}