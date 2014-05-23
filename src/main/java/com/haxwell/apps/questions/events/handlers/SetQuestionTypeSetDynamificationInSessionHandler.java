package com.haxwell.apps.questions.events.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * This is a handler, called when an attribute is set in the session which matches an 'attribute_endsWith' string
 * set in the bean's Spring definition. This class is responsible for adding the newly set attribute to a list of 
 * strings stored in the session, and keyed by that 'attribute_endsWith' string. 
 * 
 * @author jjames
 */
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
