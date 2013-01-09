package com.haxwell.apps.questions.entities;

import java.io.Serializable;
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

    public QuestionType() {
    }
    
    public QuestionType(String str) {
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