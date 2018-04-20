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

import javax.persistence.*;

import com.haxwell.apps.questions.utils.StringUtil;

import java.util.Set;


/**
 * The persistent class for the USERS database table.
 * 
 */
@Entity
@Table(name="users")
public class User extends AbstractEntity implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	private String password;

	private String username;

	//bi-directional many-to-one association to Exam
	@OneToMany(mappedBy="user")
	private Set<Exam> exams;

	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="user")
	private Set<Question> questions;

	//uni-directional many-to-many association to UserRole
    @ManyToMany
	@JoinTable(
		name="users_roles_map"
		, joinColumns={
			@JoinColumn(name="user_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="role_id")
			}
		)
	private Set<UserRole> userRoles;

    public User() {
    }

	public User(String username2) {
		this.username = username2;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Exam> getExams() {
		return this.exams;
	}

	public void setExams(Set<Exam> exams) {
		this.exams = exams;
	}
	
	public Set<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}
	
	public Set<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("ID: " + getId());
		sb.append("  |Username: " + getUsername());
		
		return sb.toString();
	}
	
	public boolean equals(Object o) {
		User that;
		boolean rtn = (this == o);
		
		if (!rtn && o instanceof User) {
			that = (User)o;
			
			rtn = this.id == that.id && StringUtil.equals(this.username, that.username);
		}

		return rtn;
	}
}
