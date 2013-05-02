package com.haxwell.apps.questions.events.utils;

import com.haxwell.apps.questions.events.handlers.IEventHandler;
import com.haxwell.apps.questions.events.handlers.IObjectEventHandler;

public class ObjectEventHandlerBean {
	String event;
	IObjectEventHandler handler;
	
	public ObjectEventHandlerBean() { }
	
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
		if (handler instanceof IObjectEventHandler)
			this.handler = (IObjectEventHandler)handler;
	}
	
	public String toString() {
		return "event: " + event + " |handler: " + handler.getClass().toString();
	}
}
