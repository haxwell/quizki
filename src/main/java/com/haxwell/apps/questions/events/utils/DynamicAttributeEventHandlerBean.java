package com.haxwell.apps.questions.events.utils;

import com.haxwell.apps.questions.events.handlers.IDynamicAttributeEventHandler;

public class DynamicAttributeEventHandlerBean {

	String event;
	String attribute_endsWith;
	IDynamicAttributeEventHandler eventHandler;
	IDynamicAttributeEventHandler attribute_endsWith_handler;
	
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
	
	public String getAttribute_endsWith() {
		return attribute_endsWith;
	}
	
	public void setAttribute_endsWith(String attribute_endsWith) {
		this.attribute_endsWith = attribute_endsWith;
	}
	
	public IDynamicAttributeEventHandler getEventHandler() {
		return eventHandler;
	}
	
	public void setEventHandler(IDynamicAttributeEventHandler handler) {
		this.eventHandler = handler;
	}

	public IDynamicAttributeEventHandler getAttribute_endsWith_handler() {
		return attribute_endsWith_handler;
	}

	public void setAttribute_endsWith_handler(IDynamicAttributeEventHandler attribute_endsWith_handler) {
		this.attribute_endsWith_handler = attribute_endsWith_handler;
	}
}
