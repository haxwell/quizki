package com.haxwell.apps.questions.events.handlers;

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
