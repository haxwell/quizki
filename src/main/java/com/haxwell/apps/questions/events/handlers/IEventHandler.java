package com.haxwell.apps.questions.events.handlers;

import javax.servlet.http.HttpServletRequest;

public interface IEventHandler {

	public void setAttribute(String attr);
	public void execute(HttpServletRequest req);
	
	boolean equals(Object o);
}
