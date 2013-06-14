package com.haxwell.apps.questions.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.haxwell.apps.questions.interfaces.ITopic;


/**
 * The persistent class for the TOPIC database table.
 * 
 */
@Entity
@Table(name="topic")
public class Topic extends AbstractEntity implements ITopic, EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String text;

    public Topic() {
    }

    public Topic(String str) {
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
		boolean b = false;
		
		if (o instanceof Topic)
		{
			Topic that = (Topic)o;
			
			b = /*(this.id == that.id) && */(this.text.toLowerCase().equals(that.text.toLowerCase())); 
		}
		
		return b;
	}

	@Override
	public String toString()
	{
		return "id: " + this.id + " |text: " + this.text;
	}
	
    public String toJSON() {
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append(getJSONOpening());
    	sb.append(getJSON("id", getId() + "", APPEND_COMMA));
    	sb.append(getJSON("text", getText()));
    	
    	sb.append(getJSONClosing());
    	
    	return sb.toString();
    }
}