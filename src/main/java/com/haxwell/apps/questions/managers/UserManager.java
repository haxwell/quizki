package com.haxwell.apps.questions.managers;

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

import java.util.HashSet;	
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.UserRole;

public class UserManager extends Manager {

	private static final Logger log = Logger.getLogger(UserManager.class.getName());
	
	public static User getUser(String username)
	{
		log.log(Level.FINE, "..beginning UserManager::getUser(" + username + ")");

		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT * FROM users WHERE username = ?1", User.class);
		
		query.setParameter(1, username);
		
		List<User> list = (List<User>)query.getResultList();
		
		User rtn = (list.size() > 0 ? list.get(0) : null);

		em.close();
		
		log.log(Level.FINE, "Returning from getUser().. no errors.. ");

		return rtn;
	}
	
	public static User getUserById(long id) {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT * FROM users where id = ?1", User.class);
		
		query.setParameter(1, id);
		
		List<User> list = (List<User>)query.getResultList();
		
		User rtn = (list.size() > 0 ? list.get(0) : null);

		em.close();
		
		return rtn;
	}
	
	public static void createUser(String username, String password)
	{
		log.log(Level.FINE, "..beginning Usermanager::createUser("+username+","+password+")");
		
		User user = new User();
		
		user.setUsername(username);
		user.setPassword(password);
		
		Set<UserRole> roles = new HashSet<UserRole>();
		
		UserRole ur = new UserRole();
		ur.setId(2);
		ur.setText("General User");
		
		roles.add(ur);
		
		user.setUserRoles(roles);
		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		
		em.persist(user);
		
		em.getTransaction().commit();
		
		em.close();
		
		log.log(Level.FINE, "Returning from createUser().. no errors() ");
	}
	
	public static User changeUserPassword(User user, String newPassword)
	{
		log.log(Level.FINE, "..beginning UserManager::changeUserPassword()");
		
		user.setPassword(newPassword);
		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		User rtn = em.merge(user);
		
		em.getTransaction().commit();
		
		em.close();
		
		log.log(Level.FINE, "Returning from getUser().. no errors() ");

		return rtn;
	}
}
