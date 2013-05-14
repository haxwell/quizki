package com.haxwell.apps.questions.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the EXAM_FEEDBACK database table.
 * 
 */
@Entity
@Table(name="exam_feedback")
public class ExamFeedback implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1264851209L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

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

    @Override
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getCommentingUser() {
		return this.commentingUser;
	}

	public void setCommentingUser(User user) {
		this.commentingUser = user;
	}

	public User getCommentingUserId() {
		return this.commentingUser;
	}

	public void setCommentingUserId(User user) {
		this.commentingUser = user;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Exam getExamId() {
		return exam;
	}

	public void setExamId(Exam exam) {
		this.exam = exam;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}