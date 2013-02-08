package com.haxwell.apps.questions.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.haxwell.apps.questions.entities.EntityWithASequenceNumberBehavior;
import com.haxwell.apps.questions.interfaces.IChoice;


/**
 * The persistent class for the CHOICE database table.
 * 
 */
@Entity
@Table(name="choice")
public class Choice implements IChoice, EntityWithAnIntegerIDBehavior, EntityWithASequenceNumberBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private int iscorrect;

	private int sequence;

	private String text;

    public Choice() {
    }
    
    public Choice(long id, String text, boolean isCorrect) {
    	this.id = id;
    	this.text = text;
    	this.iscorrect = isCorrect ? 1 : 0;
    	this.sequence = 0;
    }
    
    public Choice(long id, String text, boolean isCorrect, int sequence) {
    	this.id = id;
    	this.text = text;
    	this.iscorrect = isCorrect ? 1 : 0;
    	this.sequence = sequence;
    }
    
    @Transient
    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIscorrect() {
		return this.iscorrect;
	}

	public void setIscorrect(boolean b)
	{
		this.iscorrect = (b ? 1 : 0);
	}
	
	public void setIscorrect(int iscorrect) {
		this.iscorrect = iscorrect;
	}

	public int getSequence() {
		return this.sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int getIsCorrect() {
		return getIscorrect(); // this is the generated name, and we're just going with it..
	}

	@Override
	public void setIsCorrect(int b) {
		setIscorrect(b);
	}
	
	@Override
	public int hashCode() 
	{
		return this.text.hashCode() + (int)this.id; // TODO: Find a better way
	}

	@Override
	public boolean equals(Object o)
	{
		boolean rtn = false;
		
		if (o instanceof Choice)
		{
			Choice that = (Choice)o;
			
			rtn = (this.id == that.id && this.text.equals(that.text));
		}
		
		return rtn;
	}
	
	public String toString()
	{
		return "id: " + this.id + " |text: " + this.text + " |isCorrect: " + this.iscorrect + " |sequence: " + this.sequence;
	}
}