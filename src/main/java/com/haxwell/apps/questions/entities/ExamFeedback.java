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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the EXAM_FEEDBACK database table.
 * 
 */
@Entity
@Table(name="exam_feedback")
public class ExamFeedback extends AbstractEntity implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1264851209L;

	//bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name="commentingUserId")    
	private User commentingUser;

	//bi-directional many-to-one association to Exam
    @ManyToOne
    @JoinColumn(name="examId")    
	private Exam exam;

    private String comment;
	
	public ExamFeedback() {
    }
	
	public ExamFeedback(Exam e, User u, String comment) {
		this.exam = e;
		this.comment = comment;
		this.commentingUser = u;
	}

	public User getCommentingUser() {
		return this.commentingUser;
	}

	public void setCommentingUser(User user) {
		this.commentingUser = user;
	}

//	public User getCommentingUserId() {
//		return this.commentingUser;
//	}
//
//	public void setCommentingUserId(User user) {
//		this.commentingUser = user;
//	}
//
	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

//	public Exam getExamId() {
//		return exam;
//	}
//
//	public void setExamId(Exam exam) {
//		this.exam = exam;
//	}
//
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}