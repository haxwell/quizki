package com.haxwell.apps.questions.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import net.minidev.json.JSONObject;

import com.haxwell.apps.questions.interfaces.ITopic;
import com.haxwell.apps.questions.utils.StringUtil;


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
	private long id = -1;

	@Column(nullable=false,unique=true)
	private String text;
	
	@ManyToMany(mappedBy="topics")
	Set<Question> questions;

    public Topic() {
    }

	public String getEntityDescription()  {
		return "topic";
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
		boolean rtn = (this == o);
		
		if (!rtn && o instanceof Topic)
		{
			Topic that = (Topic)o;
			
			
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
    
    public Topic fromJSON(JSONObject obj) {
    	this.setId(Long.parseLong(obj.get("id")+""));
    	this.setText(obj.get("text")+"");
    	
    	return this;
    }
}