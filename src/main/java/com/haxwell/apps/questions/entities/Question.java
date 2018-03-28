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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.interfaces.IQuestion;
import com.haxwell.apps.questions.utils.StringUtil;


/**
 * The persistent class for the QUESTION database table.
 * 
 */
@Entity
@Table(name="question")
public class Question extends AbstractEntity implements IQuestion, EntityWithAnIntegerIDBehavior, EntityWithADifficultyObjectBehavior, Serializable, Comparable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String description;
	private String text;
	
	@Column(name="ENTITY_STATUS")
	private long entityStatus = EntityStatusConstants.ACTIVATED;	

	//uni-directional many-to-one association to Difficulty
    @ManyToOne()
	private Difficulty difficulty;

	//uni-directional many-to-one association to QuestionType
    @ManyToOne()
	@JoinColumn(name="type_id")
	private QuestionType questionType;

	//bi-directional many-to-one association to User
    @ManyToOne()
	private User user;

	//uni-directional many-to-many association to Choice
    @ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(
		name="question_choice"
		, joinColumns={
			@JoinColumn(name="question_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="choice_id")
			}
		)
	private Set<Choice> choices;

	//uni-directional many-to-many association to Topic
    @ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(
		name="question_topic"
		, joinColumns={
			@JoinColumn(name="question_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="topic_id")
			}
		)
	private Set<Topic> topics;

	//uni-directional many-to-many association to Reference
    @ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(
		name="question_reference"
		, joinColumns={
			@JoinColumn(name="question_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="reference_id")
			}
		)
	private Set<Reference> references;

    public final static long DEFAULT_ID = -1;
    public final static String DEFAULT_TEXT = "";
    
    public Question() {
    	id = DEFAULT_ID;
    	text = DEFAULT_TEXT;
    }

    @Override
    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	// TODO: rename this.. its removing the dynamic field markers, as well as HTML.
	public String getTextWithoutHTML() {
		String str = this.text.replaceAll("\\<.*?>","");
		
		str = str.replace("[[", "");
		str = str.replace("]]", "");
		
		return str;
	}
	
	@Override
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Difficulty getDifficulty() {
		return this.difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	public QuestionType getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	
	public long getEntityStatus() {
		return this.entityStatus;
	}
	
	public void setEntityStatus(long es) {
		this.entityStatus = es;
	}
	
	@Override
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Set<Choice> getChoices() {
		if (this.choices == null)
			this.choices = new HashSet<Choice>();
		
		return this.choices;
	}

	public void setChoices(Set<Choice> choices) {
		this.choices = choices;
	}
	
	public Set<Reference> getReferences() {
		if (this.references == null)
			this.references = new HashSet<Reference>();
		
		return this.references;
	}

	public void setReferences(Set<Reference> references) {
		this.references = references;
	}

	public Set<Topic> getTopics() {
		if (this.topics == null)
			this.topics = new HashSet<Topic>();
		
		return this.topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	@Override
	public String getEntityDescription() {
		return "question";
	}
	
	@Override
	public String toJSON() {
		JSONObject j = new JSONObject();
		
		j.put("id", getId() + "");
		
		String description = getDescription();
		j.put("description", description == null ? "" : description);
		
		String text = getText();
		j.put("text", text == null ? "" : text);
		
		String textWOHMTL = getTextWithoutHTML();
		j.put("textWithoutHTML", textWOHMTL == null ? "" : textWOHMTL);
		
		Difficulty diff = getDifficulty();
		if (diff == null) diff = new Difficulty(DifficultyConstants.JUNIOR);
		j.put("difficulty_id", diff.getId()+"");
		j.put("difficulty_text", diff.getText());

		QuestionType qt = getQuestionType();
		j.put("type_id", qt == null ? "-1" : qt.getId()+"");
		j.put("type_text", qt == null ? "" : qt.getText());
		j.put("choices", choices == null ? "" : getChoicesAsJSONArray());
		j.put("topics", topics == null ? "" : topics.toArray());
		j.put("references",  references == null ? "" : references.toArray());
		j.put("entityStatus", entityStatus);
		
		User user = getUser();
		j.put("user_id", user == null ? "-1" : user.getId()+"");
		j.put("user_name", user == null ? "" : user.getUsername());
		
		addDynamicDataToJSONObject(j);

		return j.toJSONString();
	}
	
	private ArrayList<JSONObject> getChoicesAsJSONArray() {
		ArrayList<JSONObject> rtn = new ArrayList<>();
		
		for (Choice c : choices) {
			rtn.add((JSONObject)JSONValue.parse(c.toJSON()));
		}
		
		return rtn;
	}

	@Override
	public int hashCode() {
		return this.text.hashCode() + (int)this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean rtn = (this == o);
		
		if (!rtn && o instanceof Question) {
			Question that = (Question)o;
			
			rtn = (this.id == that.id && StringUtil.equals(this.text, that.text)); 
		}
		
		return rtn;
	}
	
//	@Override
	/*
	 * The Comparable interface is removed from the AbstractEntity model in this version so the forced override isn't needed (but it happens)
	 */
	public int compareTo(Object o) {
		int rtn = 0;
		
		if (o instanceof Question){
			Question that = (Question)o;
			
			if (this.id > that.id)
				return -1;
			else if (this.id < that.id)
				return 1;
			
			rtn = this.text.compareTo(that.text); //this line is never reached
		}
		
		return rtn;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("ID: " + getId());
		sb.append("  |Text: " + getText());
		
		return sb.toString();
	}
}