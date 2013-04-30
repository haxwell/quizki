package com.haxwell.apps.questions.events.utils;

import com.haxwell.apps.questions.events.handlers.IAttributeEventHandler;

public class AttributeEventHandlerBean {
	String attr;
	String event;
	IAttributeEventHandler handler;
	
	public AttributeEventHandlerBean() { }
	
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public IAttributeEventHandler getHandler() {
		return handler;
	}

	public void setHandler(IAttributeEventHandler handler) {
		this.handler = handler;
		
		this.handler.setAttribute(attr);
	}
	
	public String toString() {
		return "attr: " + attr + " |event: " + event + " |handler: " + handler.getClass().toString();
	}
}
