package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.TopicManager;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;

/**
 * Puts a list of all topics in the request as an attribute.
 */
@WebFilter("/ListTopicsFilter")
public class ListTopicsFilter extends AbstractFilter {

    public ListTopicsFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest)request;
			
			if (req.getSession().getAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS) == null) {
				Collection<Topic> coll = TopicManager.getAllTopics();
				
				req.getSession().setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, coll);
				
				
				Collection<Topic> coll2 = TopicManager.getAllTopicsWithMoreThanXXQuestions(3);
				
				req.getSession().setAttribute(Constants.LIST_OF_MAJOR_TOPICS, coll2);
				
				if (coll.size() > 0)
				{
					int random = RandomIntegerUtil.getRandomInteger(coll.size());
				
					Iterator<Topic> iterator = coll.iterator();
					Topic randomTopic = null;
					for (int i = 0; i < random; i++)
						randomTopic = iterator.next();
					
					req.getSession().setAttribute("randomTopic", randomTopic);
				}
				
				req.getSession().setAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED, new ArrayList<Topic>());			
				req.getSession().setAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED, new ArrayList<Topic>());
			}
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
