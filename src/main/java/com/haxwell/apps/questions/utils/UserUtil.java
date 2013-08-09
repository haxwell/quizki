package com.haxwell.apps.questions.utils;

import java.util.Iterator;

import org.eclipse.persistence.exceptions.ValidationException;

import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.UserRole;

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
}
