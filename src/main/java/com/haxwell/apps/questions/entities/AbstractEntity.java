package com.haxwell.apps.questions.entities;

public class AbstractEntity implements Comparable {

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	public long getId() {
		return Long.MIN_VALUE;
	}
}
