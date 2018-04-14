package com.haxwell.apps.questions.servlets;

import java.io.BufferedReader;
import java.io.DataOutputStream;

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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.managers.UserManager;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

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
        reCaptcha.setPrivateKey("6LfdE90SAAAAABhv6mw2hG8zLhes3ioFJoEugBcp");

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
	
	protected boolean verifyCAPTCHA(String secret, String response) {
		final String USER_AGENT = "Mozilla/5.0";
		String urlStr = "https://www.google.com/recaptcha/api/siteverify";
		URL url = null;
		
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException mfe) {
			// TODO: log this highly improbable exception
		}
		
		HttpsURLConnection con = null;
		
		try {
			con = (HttpsURLConnection) url.openConnection();
		} catch (IOException ioe) {
			// TODO: log this slightly improbable exception
		}
		
		try {
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		} catch (ProtocolException pe) {
			// TODO: log this blah blah blah
		}
		
		try {
			con.setDoOutput(true);
			String urlParams = "secret="+secret+"&response="+response;		
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParams);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			
			StringBuffer sb = new StringBuffer();
			
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
		
			br.close();
			
//			if (sb.equals(obj))
		} catch (Exception e) {
			// TODO: log these too..
		}
		
		return true;
	}
}