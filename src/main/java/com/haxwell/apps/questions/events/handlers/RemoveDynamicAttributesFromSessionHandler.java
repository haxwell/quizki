package com.haxwell.apps.questions.events.handlers;

import java.util.List;

import javax.servlet.http.HttpSession;

public class RemoveDynamicAttributesFromSessionHandler implements IDynamicAttributeEventHandler {

	@Override
	public void execute(String endsWithStr, HttpSession session) {

		List<String> list = (List<String>)session.getAttribute(endsWithStr);
		
		if (list != null) {
			for (String str : list) 
				session.removeAttribute(str);
			
			session.removeAttribute(endsWithStr);
		}
	}

	@Override
	public void execute(String endsWithStr, HttpSession session, String attrName) {

	}
}
