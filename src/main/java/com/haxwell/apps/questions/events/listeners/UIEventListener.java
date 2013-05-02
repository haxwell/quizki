package com.haxwell.apps.questions.events.listeners;

import javax.servlet.http.HttpServletRequest;

public interface UIEventListener extends IEventListener {

	void indexPage(HttpServletRequest req);
}
