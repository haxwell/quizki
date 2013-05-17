package com.haxwell.apps.questions.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the VOTES database table.
 * 
 */
@Entity
@Table(name="votes")
public class Vote implements EntityWithAnIntegerIDBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	//bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name="userId")
	private User user;

	//uni-directional many-to-one association to QuestionType
    @ManyToOne
	@JoinColumn(name="entityTypeId")
	private EntityType entityType;
    
    @Column(name="entityId")
    private long entityId;
    
    private int thumbsUp; // acts a boolean
    private int thumbsDown; // acts a boolean
    
	public Vote() {
		setThumbsDown(Boolean.FALSE);
		setThumbsUp(Boolean.FALSE);
	}

    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public EntityType getEntityType() {
		return this.entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
	
    public long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
	
	public int getThumbsUp() {
		return this.thumbsUp;
	}

	public void setThumbsUp(boolean b)
	{
		this.thumbsUp = (b ? 1 : 0);
	}
	
	public void setThumbsUp(int val) {
		this.thumbsUp = val;
	}

	public int getThumbsDown() {
		return this.thumbsDown;
	}

	public void setThumbsDown(boolean b)
	{
		this.thumbsDown = (b ? 1 : 0);
	}
	
	public void setThumbsDown(int val) {
		this.thumbsDown = val;
	}
}