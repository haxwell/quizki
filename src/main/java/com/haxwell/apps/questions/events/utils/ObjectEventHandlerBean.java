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
