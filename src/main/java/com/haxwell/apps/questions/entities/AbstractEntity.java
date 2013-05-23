package com.haxwell.apps.questions.entities;

public class AbstractEntity implements Comparable {

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	public long getId() {
		return Long.MIN_VALUE;
	}
	
	public User getUser() {
		return null;
	}
	
	public String getEntityDescription()  {
		return null;
	}
	
	public String getText() {
		return null;
	}
}
