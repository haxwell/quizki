package com.haxwell.apps.questions.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
public class Choice extends AbstractEntity implements IChoice, EntityWithAnIntegerIDBehavior, EntityWithASequenceNumberBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	public static final int CORRECT = 1;
	public static final int NOT_CORRECT = 0;
	
	public static final int NO_SEQUENCE = 0;
	
	public static final int NO_ID = 0;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private int iscorrect;

	private int sequence;

	private String text;

	@ManyToMany(mappedBy="choices")
	Set<Question> questions;

	public Choice() {
    }
    
    public Choice(long id, String text, boolean isCorrect) {
    	this.id = id;
    	this.text = text;
    	this.iscorrect = isCorrect ? 1 : 0;
    	this.sequence = 0;
    }
    
    public Choice(long id, String text, int isCorrect) {
    	this.id = id;
    	this.text = text;
    	this.iscorrect = isCorrect == CORRECT ? 1 : 0;
    	this.sequence = 0;
    }
    
    public Choice(long id, String text, boolean isCorrect, int sequence) {
    	this.id = id;
    	this.text = text;
    	this.iscorrect = isCorrect ? 1 : 0;
    	this.sequence = sequence;
    }
    
    public Choice(long id, String text, int isCorrect, int sequence) {
    	this.id = id;
    	this.text = text;
    	this.iscorrect = isCorrect == CORRECT ? 1 : 0;
    	this.sequence = sequence;
    }
    
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
		iscorrect = Math.min(iscorrect, 1);
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
		return this.text.hashCode() + (int)this.id;
	}

	@Override
	public boolean equals(Object o)
	{
		boolean rtn = false;
		
		if (o instanceof Choice)
		{
			Choice that = (Choice)o;
			
			rtn = (this.id == that.id && this.text.equals(that.text) && this.iscorrect == that.iscorrect && this.sequence == that.sequence);
		}
		
		return rtn;
	}
	
	public String toString()
	{
		return "id: " + this.id + " |text: " + this.text + " |isCorrect: " + this.iscorrect + " |sequence: " + this.sequence;
	}
	
    public String toJSON() {
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append(getJSONOpening());
    	sb.append(getJSON("id", getId() + "", APPEND_COMMA));
    	sb.append(getJSON("text", getText(), APPEND_COMMA));
    	sb.append(getJSON("iscorrect", getIscorrect() == 0 ? "false" : "true", APPEND_COMMA));
    	sb.append(getJSON("sequence", sequence + ""));
    	
    	sb.append(getJSONClosing());
    	
    	return sb.toString();
    }
}