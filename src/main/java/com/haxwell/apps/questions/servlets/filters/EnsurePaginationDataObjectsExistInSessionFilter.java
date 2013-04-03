package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.utils.PaginationData;

/**
 * Checks to see if the pagination data objects are in the session, and if not, creates them.
 * 
 */
@WebFilter("/EnsurePaginationDataObjectsExistInSessionFilter")
public class EnsurePaginationDataObjectsExistInSessionFilter extends AbstractFilter {

	Logger log = Logger.getLogger(EnsurePaginationDataObjectsExistInSessionFilter.class.getName());
	
	public EnsurePaginationDataObjectsExistInSessionFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = ((HttpServletRequest)request);
		HttpSession session = req.getSession();

		ensurePaginationDataIsInSession(Constants.EXAM_PAGINATION_DATA, session);
		ensurePaginationDataIsInSession(Constants.QUESTION_PAGINATION_DATA, session);
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
	
	private void ensurePaginationDataIsInSession(String key, HttpSession session)
	{
		PaginationData pd = (PaginationData)session.getAttribute(key);
		
		if (pd == null) {
			pd = new PaginationData();
			session.setAttribute(key, pd);			
		}
	}
}
