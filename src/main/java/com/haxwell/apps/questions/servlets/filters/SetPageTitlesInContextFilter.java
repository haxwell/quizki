package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

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

    Logger log = Logger.getLogger(SetPageTitlesInContextFilter.class.getName());
	
	public SetPageTitlesInContextFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			
			String url = StringUtil.getShortURL(req.getRequestURL().toString());
			
			if (url.endsWith(".jsp"))
			{ 
				ApplicationContext appContext = getPageTitlesAppContext(req);
				String pageTitle = "";
				
				try {
					URLMappedToPageTitleBean bean = (URLMappedToPageTitleBean) appContext.getBean(url);
					pageTitle = bean.getTitle();
				}
				catch (NoSuchBeanDefinitionException e) {
					log.log(Level.INFO, "The page " + url + " does not have a bean which defines its title.");
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
