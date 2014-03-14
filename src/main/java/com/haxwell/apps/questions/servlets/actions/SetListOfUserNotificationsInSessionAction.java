package com.haxwell.apps.questions.servlets.actions;

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
