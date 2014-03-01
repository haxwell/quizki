package com.haxwell.apps.questions.entities;

import java.io.Serializable;	

import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the USERS database table.
 * 
 */
@Entity
@Table(name="users")
public class User implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private long id;

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

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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
	
}