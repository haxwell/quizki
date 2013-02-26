package com.haxwell.apps.questions.utils;

public interface ShouldRemoveAnObjectCommand<T> {

	public boolean shouldRemove(T t);
}
