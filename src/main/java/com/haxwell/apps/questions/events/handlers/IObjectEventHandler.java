package com.haxwell.apps.questions.events.handlers;

import javax.servlet.http.HttpServletRequest;

public interface IObjectEventHandler extends IEventHandler {

	public void setObject(Object o);
	public void execute(HttpServletRequest req, Object o);
	
	boolean equals(Object o);
}
