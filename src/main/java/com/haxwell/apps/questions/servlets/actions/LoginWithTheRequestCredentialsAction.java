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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.UserManager;
import com.haxwell.apps.questions.utils.StringUtil;

public class LoginWithTheRequestCredentialsAction implements
		AbstractServletAction {

	private void foo(String u, String p, HttpSession session) {
		if (!StringUtil.isNullOrEmpty(u) || !StringUtil.isNullOrEmpty(p)) {
			UsernamePasswordToken token = new UsernamePasswordToken(u, p);
			
			try {
				User user = UserManager.getUser(u);

				if (user != null) {
					Subject currentUser = SecurityUtils.getSubject(); // Each time we need the Shiro subject, we get it this way..
					currentUser.login(token);							
				}

				// we wouldn't get here if the login was not successful... Thanks Shiro!
				session.setAttribute(Constants.CURRENT_USER_ENTITY, user);
			}
			catch (AuthenticationException ae)
			{
//				log.log(Level.FINER, "..OH NO! An AuthenticationException!!");
				ae.printStackTrace();
			}
		}
	}
	
	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

			if (session.getAttribute(Constants.CURRENT_USER_ENTITY) == null) {
			
				String username = request.getParameter("username");
				String password = request.getParameter("password");
	
				if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
					username = (String)request.getAttribute("username");
					password = (String)request.getAttribute("password");
				}
				
				foo(username,password,session);
			}
			else {
				// todo: error: someone is already logged in.. They should log out..
			}
		}

		return 0;
	}

}
