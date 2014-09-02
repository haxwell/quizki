package com.haxwell.apps.questions.servlets.actions;

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

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Notification;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.NotificationManager;

public class SetListOfUserNotificationsInSessionAction implements
		AbstractServletAction {

	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		HttpServletRequest req = ((HttpServletRequest)request);
		HttpSession session = req.getSession();

		User user = (User)session.getAttribute(Constants.CURRENT_USER_ENTITY);
		
		if (user != null) {
			Collection<Notification> coll = NotificationManager.getAllNotificationsForUser(user.getId());
			session.setAttribute(Constants.LIST_OF_NOTIFICATIONS_TO_BE_DISPLAYED, coll);
		}

		return 0;
	}

}
