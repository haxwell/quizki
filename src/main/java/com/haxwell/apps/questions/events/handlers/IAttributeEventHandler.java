package com.haxwell.apps.questions.events.handlers;

import javax.servlet.http.HttpServletRequest;

public interface IAttributeEventHandler extends IEventHandler {

	public void setAttribute(String attr);
	public void execute(HttpServletRequest req);
	
	boolean equals(Object o);
}
