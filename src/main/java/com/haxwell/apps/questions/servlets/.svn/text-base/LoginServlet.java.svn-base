package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.UserManager;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(LoginServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.log(Level.INFO, "Entered LoginServlet::doPost()...");
		
		log.log(Level.INFO, request.getParameterNames().toString());
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		
		Subject currentUser = SecurityUtils.getSubject(); // Each time we need the Shiro subject, we get it this way..
		
		HttpSession session = request.getSession();

		log.log(Level.INFO, "..SecurityUtils.getSubject() completed. (" + currentUser.toString() + ")");
		
		try {
			log.log(Level.INFO, "..about to get user object for [" + username + "]");
			User user = UserManager.getUser(username);
			log.log(Level.INFO, "..got user object for [" + username + "]");
			
			log.log(Level.INFO, String.valueOf(token.getPassword()) + " / " + token.getUsername());
			log.log(Level.INFO, user.getPassword() + " / " + user.getUsername());
			log.log(Level.INFO, "-------=---=---===---------=-");
			
			currentUser.login(token);
			
			session.setAttribute(Constants.CURRENT_USER_ENTITY, user);
		}
		catch (AuthenticationException ae)
		{
			log.log(Level.INFO, "..OH NO! An Exception!!");
			
			ae.printStackTrace();
			
			forwardToJSP(request, response, "/failedLogin.jsp");
		}
		
		String fwdPage = (String)session.getAttribute("originallyRequestedPage");
		
		if (StringUtil.isNullOrEmpty(fwdPage))
		{
			log.log(Level.INFO, "..nothing set for originallyRequestedPage, so fwd to index.jsp");
			fwdPage = "/index.jsp";
		}

		log.log(Level.INFO, "about to redirect to: " + fwdPage);
		redirectToJSP(request, response, fwdPage);
	}
}
