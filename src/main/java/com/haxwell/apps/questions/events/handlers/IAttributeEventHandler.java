package com.haxwell.apps.questions.events.handlers;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines a class which is called when some event happens.
 * 
 * @author jjames
 */
public interface IAttributeEventHandler extends IEventHandler {

	public void setAttribute(String attr);
	public void execute(HttpServletRequest req);
	
	boolean equals(Object o);
}
