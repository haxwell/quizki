package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.managers.UserManager;

/**
 * Servlet implementation class LoginServlet
 */
//@WebServlet("/RegisterUserServlet")
public class RegisterUserServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUserServlet() {
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

		ArrayList<String> errors = new ArrayList<String>();
		ArrayList<String> successes = new ArrayList<String>();		

        String remoteAddr = request.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6LcZhfASAAAAAC5oKnOSx7dxaQhDIS_Nz5vxuwFg");

        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        ReCaptchaResponse reCaptchaResponse = null;
        
        try {
        	reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
        }
        catch (Exception e) {
        	errors.add("Caught Exception trying to verify the Captcha. Sorry about that.");
        }

        if (reCaptchaResponse != null) {
	        if (reCaptchaResponse.isValid()) {
	            if (username.length() < 5)
	            	errors.add("The username '" + username + "' is too short. It must be at least 5 characters long.");
	            
	        	if (errors.size() == 0 && UserManager.getUser(username) != null)
	    			errors.add("The username '" + username + "' already exists....");
	    	}
	        else
	        {
	        	errors.add("The text you entered for the CAPTCHA was wrong....");
	        }
        }
        
        if (password.length() < 6)
        {
        	errors.add("The password must be at least 6 characters.");
        }
        	
		String fwdPage = null;        
        if (errors.size() == 0)	{
			UserManager.createUser(username, password);
			successes.add("User <b>" + username + "</b> created.");

			request.setAttribute(Constants.SUCCESS_MESSAGES, successes);
			fwdPage = "/login.jsp";
		}
		else {
			request.setAttribute(Constants.VALIDATION_ERRORS, errors);
			fwdPage = "/register.jsp";
		}
        
		forwardToJSP(request, response, fwdPage);
	}
}