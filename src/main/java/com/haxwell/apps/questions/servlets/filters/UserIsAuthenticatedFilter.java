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

/**
 * Ensure the user is logged in.. if not, forward to the login page
 */
@WebFilter("/UserIsAuthenticatedFilter")
public class UserIsAuthenticatedFilter extends AbstractFilter {

    public UserIsAuthenticatedFilter() { /* Do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest)request).getSession();
		
		if (session.getAttribute(Constants.CURRENT_USER_ENTITY) == null) {
			((HttpServletResponse)response).sendRedirect(Constants.LOGIN_JSP_URL);
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
