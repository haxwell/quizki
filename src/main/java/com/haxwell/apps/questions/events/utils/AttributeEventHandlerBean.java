package com.haxwell.apps.questions.events.utils;

import com.haxwell.apps.questions.events.handlers.IAttributeEventHandler;

/**
 * Spring bean which describes 
 * 		1. an attribute being set in the session,
 * 		2. an event upon which that attribute is to be handled somehow
 * 		3. a handler, a bit of code called when the event happens. 
 * 
 * @author jjames
 */
public class AttributeEventHandlerBean {
	String attr;
	String eventName;
	IAttributeEventHandler handler;
	
	public AttributeEventHandlerBean() { }
	
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getEvent() {
		return eventName;
	}

	public void setEvent(String event) {
		this.eventName = event;
	}

	public IAttributeEventHandler getHandler() {
		return handler;
	}

	public void setHandler(IAttributeEventHandler handler) {
		this.handler = handler;
		
		this.handler.setAttribute(attr);
	}
	
	public String toString() {
		return "attr: " + attr + " |event: " + eventName + " |handler: " + handler.getClass().toString();
	}
}
