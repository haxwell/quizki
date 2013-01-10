package com.haxwell.apps.questions.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
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
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		
		Subject currentUser = SecurityUtils.getSubject(); // Each time we need the Shiro subject, we get it this way..
		
		HttpSession session = request.getSession();
		
		try {
			currentUser.login(token); 
			
			User user = UserManager.getUser(username);
			
			session.setAttribute(Constants.CURRENT_USER_ENTITY, user);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			forwardToJSP(request, response, "/failedLogin.jsp");
		}
		
		String fwdPage = (String)session.getAttribute("originallyRequestedPage");
		
		if (StringUtil.isNullOrEmpty(fwdPage))
		{
			fwdPage = "/index.jsp";
		}
		
		redirectToJSP(request, response, fwdPage);
	}
}
