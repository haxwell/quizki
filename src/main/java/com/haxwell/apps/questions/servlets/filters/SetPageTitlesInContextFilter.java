package com.haxwell.apps.questions.servlets.filters;

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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.URLMappedToPageTitleBean;

/**
 * Sets the title of the most recently requested page in the session.
 * 
 */
@WebFilter("/SetPageTitlesInContextFilter")
public class SetPageTitlesInContextFilter extends AbstractFilter {

	private static Logger log = LogManager.getLogger();
    
	public SetPageTitlesInContextFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			
			String url = StringUtil.getShortURL(req.getRequestURL().toString());
			
			if (url.endsWith(".jsp") && !url.startsWith("ajax"))
			{ 
				ApplicationContext appContext = getPageTitlesAppContext(req);
				String pageTitle = "";
				
				try {
					URLMappedToPageTitleBean bean = (URLMappedToPageTitleBean) appContext.getBean(url);
					pageTitle = bean.getTitle();
				}
				catch (NoSuchBeanDefinitionException e) {
					log.info("The page " + url + " does not have a bean which defines its title.");
				}
				
				req.getSession().setAttribute(Constants.PAGE_TITLE, pageTitle);				
			}
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	private ApplicationContext getPageTitlesAppContext(HttpServletRequest req) {
		ApplicationContext rtn = (ApplicationContext) req.getSession().getAttribute(Constants.APPLICATION_PAGE_TITLES_CONTEXT);
		
		if (rtn == null) {
			rtn = new ClassPathXmlApplicationContext("META-INF/applicationPageTitlesContext.xml");
			req.getSession().setAttribute(Constants.APPLICATION_PAGE_TITLES_CONTEXT, rtn);
		}
		
		return rtn;
	}
}
