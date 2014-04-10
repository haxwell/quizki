package com.haxwell.apps.questions.events.handlers;

import javax.servlet.http.HttpSession;

public interface IDynamicAttributeEventHandler extends IEventHandler {

	public void execute(String endsWithStr, HttpSession session);
	public void execute(String endsWithStr, HttpSession session, String attrName);
}
