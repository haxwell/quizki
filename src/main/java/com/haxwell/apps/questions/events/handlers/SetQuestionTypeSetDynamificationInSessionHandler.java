package com.haxwell.apps.questions.events.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

public class SetQuestionTypeSetDynamificationInSessionHandler implements IDynamicAttributeEventHandler {

	@Override
	public void execute(String endsWithStr, HttpSession session) {

	}

	@Override
	public void execute(String endsWithStr, HttpSession session, String attrName) {
		List<String> list = (List<String>)session.getAttribute(endsWithStr);

		if (list == null)
			list = new ArrayList<String>();

		list.add(attrName);

		session.setAttribute(endsWithStr, list);
	}
}
