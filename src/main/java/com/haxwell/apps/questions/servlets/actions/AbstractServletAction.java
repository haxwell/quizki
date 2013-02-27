package com.haxwell.apps.questions.servlets.actions;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface AbstractServletAction {

	int doAction(ServletRequest request, ServletResponse response);
}
