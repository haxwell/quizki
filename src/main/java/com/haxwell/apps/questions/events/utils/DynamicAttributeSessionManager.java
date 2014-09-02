package com.haxwell.apps.questions.events.utils;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.haxwell.apps.questions.events.handlers.IDynamicAttributeEventHandler;

/**
 * Called when an attribute is set in the session. It gets a list of all the strings defined in beans as the property 'attribute_endsWith'
 * in applicationContext.xml. Then if the attribute to be set ends with one of those attribute_endsWith strings, it gets the list of
 * handlers associated with it, and passes the attribute to each one. 
 * 
 * @author jjames
 *
 */
public class DynamicAttributeSessionManager {

	public static void handleAttributeSet(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		String attr = event.getName();
		
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		DynamicAttributeEventHandlerList daehl = (DynamicAttributeEventHandlerList)ctx.getBean("dynamicAttributeEventHandlerList");

		Set<String> set = daehl.getAttributeEndsWithKeys();
		
		for (String endsWithStr : set) {
			if (!attr.equals(endsWithStr) && attr.endsWith(endsWithStr)) {
				List<IDynamicAttributeEventHandler> list = daehl.getAttributeEndsWithHandlerList(endsWithStr);

				for (IDynamicAttributeEventHandler handler : list)
					handler.execute(endsWithStr, session, attr);
			}
		}
	}
}
