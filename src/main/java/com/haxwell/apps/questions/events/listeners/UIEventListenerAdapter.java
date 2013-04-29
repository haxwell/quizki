package com.haxwell.apps.questions.events.listeners;

import javax.servlet.http.HttpServletRequest;

public class UIEventListenerAdapter implements UIEventListener {

	protected String attr;
	
	public UIEventListenerAdapter(String attr) {
		this.attr = attr;
	}
	
	public void indexPage(HttpServletRequest req) {
		
	}
}
