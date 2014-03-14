package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.servlets.actions.LoginWithTheRequestCredentialsAction;

/**
 * Auto log the user in.. so he doesn't have to keep clicking on the log in button for every little jsp change.
 */
@WebFilter("/AutoAuthenticateUserFilter")
public class AutoAuthenticateUserFilter extends AbstractFilter {

    public AutoAuthenticateUserFilter() { /* Do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest)request).getSession();

		if (session.getAttribute(Constants.IN_PRODUCTION_MODE) == null) {
			// TODO: get these from Spring instead
			request.setAttribute("username", "johnathan");
			request.setAttribute("password", "password");
			
			new LoginWithTheRequestCredentialsAction().doAction(request, response);
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
