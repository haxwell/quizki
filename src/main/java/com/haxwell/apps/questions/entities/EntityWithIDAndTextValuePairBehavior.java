package com.haxwell.apps.questions.entities;

public interface EntityWithIDAndTextValuePairBehavior extends EntityWithAnIntegerIDBehavior {

	public long getId();
	public void setId(long id);
	
	public void setText(String str);
	public String getText();
}
