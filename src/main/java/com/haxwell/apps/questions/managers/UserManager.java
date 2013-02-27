package com.haxwell.apps.questions.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.UserRole;

public class UserManager extends Manager {

	static EntityManager em;
	
	//TODO: what are the consequences of having this be static? Would it be better to just create an instance when necessary?
	static {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(Constants.QUIZKI_PERSISTENCE_UNIT);
		em = emf.createEntityManager();
	}

	private static final Logger log = Logger.getLogger(UserManager.class.getName());
	
	public static User getUser(String username)
	{
		log.log(Level.INFO, "..beginning UserManager::getUser(" + username + ")");
		
		boolean b = em.isOpen();
		Query query = em.createNativeQuery("SELECT * FROM users WHERE username = ?1", User.class);
		
		query.setParameter(1, username);
		
		List<User> list = (List<User>)query.getResultList();
		
		User rtn = (list.size() > 0 ? list.get(0) : null);
		
		log.log(Level.INFO, "Returning from getUser().. no errors() ");

		return rtn;
	}
	
	public static void createUser(String username, String password)
	{
		log.log(Level.INFO, "..beginning Usermanager::createUser("+username+","+password+")");
		
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
		
		log.log(Level.INFO, "Returning from createUser().. no errors() ");
	}
	
	public static User changeUserPassword(User user, String newPassword)
	{
		log.log(Level.INFO, "..beginning UserManager::changeUserPassword()");
		
		user.setPassword(newPassword);
		
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		User rtn = em.merge(user);
		
		em.getTransaction().commit();
		
		em.close();
		
		log.log(Level.INFO, "Returning from getUser().. no errors() ");

		return rtn;
	}
}
