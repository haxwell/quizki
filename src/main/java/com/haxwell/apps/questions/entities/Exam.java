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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.interfaces.IExam;


/**
 * The persistent class for the EXAM database table.
 * 
 */
@Entity
@Table(name="exam")
public class Exam extends AbstractEntity implements IExam, EntityWithAnIntegerIDBehavior, EntityWithADifficultyObjectBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	private String title;
	
	private String message;
	
	@Column(name="ENTITY_STATUS")
	private long entityStatus = EntityStatusConstants.ACTIVATED;	

	//bi-directional many-to-one association to User
    @ManyToOne
	private User user;

	//uni-directional many-to-many association to Question
    @ManyToMany
	@JoinTable(
		name="exam_question"
		, joinColumns={
			@JoinColumn(name="exam_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="question_id")
			}
		)
	private Set<Question> questions;

	@Transient
    private Set<Topic> examTopics;
	
	@Transient
	private Difficulty difficulty;

    public Exam() {
    }

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void addQuestion(Question q) {
		if (this.questions == null)
			this.questions = new HashSet<Question>();
		
		this.questions.add(q);
	}
	
	// NOTE, we use a set because it cannot contain duplicates...
	public Set<Question> getQuestions() {
		if (this.questions == null)
			this.questions = new HashSet<Question>();
		
		return this.questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}
	
	public long getEntityStatus() {
		return this.entityStatus;
	}
	
	public void setEntityStatus(long es) {
		this.entityStatus = es;
	}
	
	public int getNumberOfQuestions()
	{
		return questions.size();
	}
	
	@Transient
	public Set<Topic> getTopics()
	{
		return examTopics;
	}
	
	@Transient
	public void setTopics(Set<Topic> topics)
	{
		this.examTopics = topics;
	}
	
	@Transient
	public Difficulty getDifficulty() {
		return difficulty;
	}

	@Transient
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

//	@Override
	public String getText() {
		return getTitle();
	}
	
	@Override
	public String getEntityDescription() {
		return "exam";
	}
	

	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("ID: " + getId());
		sb.append("  |Title: " + getTitle());
		
		return sb.toString();
	}

    public String toJSON() {
    	JSONObject j = new JSONObject();
    	
    	j.put("id", getId() + "");
    	j.put("title", getText() + "");
    	j.put("message", getMessage() + "");
    	j.put("owningUserId", getUser().getId() + "");
    	j.put("topics", getTopics().toArray());

    	Difficulty diff = getDifficulty();
    	String diffId = (diff == null) ? DifficultyConstants.UNDEFINED+"" : diff.getId()+"";
    	String diffText = (diff == null) ? DifficultyConstants.UNDEFINED_STR : diff.getText();
    	
    	j.put("difficulty", diffId);
    	j.put("difficulty_text", diffText);
    	j.put("entityStatus", getEntityStatus() + "");

    	return j.toJSONString();
    }
}