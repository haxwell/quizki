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
import javax.persistence.Transient;

import com.haxwell.apps.questions.utils.StringUtil;

/**
 * The persistent class for the VOTES database table.
 *
 * A vote is a per-user indicator of approval or disapproval of an AbstractEntity. A user can only have one vote
 * per entity. They can change that vote as they wish. The vote can be either up or down, or neither, but not both.
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

	public void setThumbsUp(boolean b) {
		this.thumbsUp = (b ? 1 : 0);
		
		if (b && thumbsDown == 1)
			thumbsDown = 0;
	}
	
	public void setThumbsUp(int val) {
		val = Math.max(0, val);
		val = Math.min(1, val);
		
		setThumbsUp(val == 1);
	}
	
	@Transient
	public boolean isThumbsUp() {
		return this.thumbsUp == 1;
	}

	public int getThumbsDown() {
		return this.thumbsDown;
	}

	public void setThumbsDown(boolean b) {
		this.thumbsDown = (b ? 1 : 0);
		
		if (b && thumbsUp == 1)
			thumbsUp = 0;
	}
	
	public void setThumbsDown(int val) {
		val = Math.max(0, val);
		val = Math.min(1, val);
		
		setThumbsDown(val == 1);
	}

	@Transient
	public boolean isThumbsDown() {
		return this.thumbsDown == 1;
	}
	
	public boolean equals(Object o) {
		Vote that;
		boolean rtn = (this == o);
		
		if (!rtn && o instanceof Vote) {
			that = (Vote)o;
			
			rtn = (this.id == that.id) && (this.thumbsDown == that.thumbsDown) && (this.thumbsUp == that.thumbsUp);
		}
		
		return rtn;
	}
}