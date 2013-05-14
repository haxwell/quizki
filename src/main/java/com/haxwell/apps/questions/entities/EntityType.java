package com.haxwell.apps.questions.entities;

import java.io.Serializable;
import javax.persistence.*;


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

}