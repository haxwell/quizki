package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;	

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Question;

public class AbstractFilter implements Filter {

	FilterConfig filterConfig;
	
    /**
     * Default constructor. 
     */
    public AbstractFilter() { /* do nothing */ }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.filterConfig = fConfig;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
	}
	
	// These get and set methods were necessary because DisplayQuestion, and Create/Edit Question were using the same key. So, when the user Created a question
	// and saved it, it was cleared from the session. They then had the option to Display the question, which added it back to the session. If after displaying 
	// the question, they hit Back in the browser, they were back at the Create question screen with the just-displayed question in their session. If they then
	// began to edit a (supposedly new) question, they had the attributes from the just-displayed question as well. They should be dealing with an entirely new 
	// question at that point. 

	// So these get and set methods were added to easily use different keys for the current question. See Issue #45.
	protected void setCurrentQuestion(HttpServletRequest req, String key, Question q) {
		req.getSession().setAttribute(key, q);
		req.getSession().setAttribute(Constants.CURRENT_QUESTION_KEY, key);
	}
	
	protected Question getCurrentQuestion(HttpServletRequest req) {
		String key = (String)req.getSession().getAttribute(Constants.CURRENT_QUESTION_KEY);
		return (Question)req.getSession().getAttribute((key == null) ? Constants.CURRENT_QUESTION : key);
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		filterConfig = null;
	}
}
