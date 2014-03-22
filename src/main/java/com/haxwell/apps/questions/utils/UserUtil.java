package com.haxwell.apps.questions.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.persistence.exceptions.ValidationException;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.UserRole;
import com.haxwell.apps.questions.managers.UserManager;

public class UserUtil {

	public static boolean canAccessTestingFunctionality(User u) {
		boolean rtn = false;
		
		if (u != null) {
			Iterator<UserRole> iterator = u.getUserRoles().iterator();
			
			while (rtn == false && iterator.hasNext())
			{
				UserRole ur = iterator.next();
				rtn = ur.getText().equals("Administrator") || ur.getText().equals("Beta Tester");
			}
		}
		
		return rtn;
	}

	public static boolean isAdministrator(User u) {
		boolean rtn = false;

		try {
			if (u != null) {
				Iterator<UserRole> iterator = u.getUserRoles().iterator();
				
				while (rtn == false && iterator.hasNext())
				{
					UserRole ur = iterator.next();
					rtn = ur.getText().equals("Administrator");
				}
			}
		} catch (ValidationException ve) {
			// do nothing.. rtn is already false
		}

		return rtn;
	}

	public static String changePassword(HttpServletRequest request) {

		//TODO: very similar code in RegisterUserServlet.java
		
		String rtn = "";
		
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		
		List<String> errors = new ArrayList<String>();
		List<String> successes = new ArrayList<String>();
		boolean blankPasswordEntered = false;
		
		if (StringUtil.isNullOrEmpty(newPassword) || StringUtil.isNullOrEmpty(confirmPassword))
		{
			errors.add("One of the passwords you entered was blank.");
			blankPasswordEntered = true;
		}
		
		if (!newPassword.equals(confirmPassword))
		{
			errors.add("The two passwords did not match.");
		}
		
		if (newPassword.length() < 6 && !blankPasswordEntered)
		{
			errors.add("The new password is too short.. it must be at least 6 characters..");
		}
		
		if (errors.isEmpty())
		{
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			
			User userWithChangedPassword = UserManager.changeUserPassword(user, newPassword);
			
			request.getSession().setAttribute(Constants.CURRENT_USER_ENTITY, userWithChangedPassword);
			
			successes.add("Password successfully changed.");
			
			request.setAttribute(Constants.SUCCESS_MESSAGES, successes);
			
			rtn = "{ " + StringUtil.toJSON("successes", successes) + " }";
		}
		else
		{
			request.setAttribute(Constants.VALIDATION_ERRORS, errors);
			
			rtn = "{ " + StringUtil.toJSON("errors", errors) + " }";
		}
		
		request.getSession().setAttribute("tabIndex", Constants.PROFILE_ACCOUNT_TAB_INDEX);
		
		return rtn;
	}

}
