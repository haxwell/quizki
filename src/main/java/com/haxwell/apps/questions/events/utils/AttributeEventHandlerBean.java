package com.haxwell.apps.questions.events.utils;

import com.haxwell.apps.questions.events.handlers.IEventHandler;

public class AttributeEventHandlerBean {
	String attr;
	String event;
	IEventHandler handler;
	
	public AttributeEventHandlerBean() { }
	
//		public AttributeEventHandlerBean(String attr, String event, IEventHandler handler) {
//		this.attr = attr;
//		this.event = event;
//		this.handler = handler;
//	}
	
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

	public IEventHandler getHandler() {
		return handler;
	}

	public void setHandler(IEventHandler handler) {
		this.handler = handler;
		
		this.handler.setAttribute(attr);
	}
	
	public String toString() {
		return "attr: " + attr + " |event: " + event + " |handler: " + handler.getClass().toString();
	}
}
