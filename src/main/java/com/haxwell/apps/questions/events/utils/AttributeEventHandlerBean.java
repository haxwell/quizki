package com.haxwell.apps.questions.events.utils;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

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
