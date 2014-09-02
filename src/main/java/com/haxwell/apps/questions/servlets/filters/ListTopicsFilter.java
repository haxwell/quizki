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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.annotation.WebFilter;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.EntityWithIDAndTextValuePairManager;
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
			
			Collection coll; 
			
			User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			EntityWithIDAndTextValuePairManager topicManager = TopicManager.getInstance();

			if (user != null)
				coll = topicManager.getAllEntitiesForQuestionsCreatedByAGivenUser(user.getId()); 
			else
				coll = topicManager.getAllEntities();
			
			req.getSession().setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, coll);
			
			List listOfMajorTopics =  topicManager.getAllEntitiesWithMoreThanXXQuestions(3);
			
			List<Integer> randomIndexes = RandomIntegerUtil.getRandomlyOrderedListOfUniqueIntegers(listOfMajorTopics.size());

			int count = listOfMajorTopics.size();
			while (count-- > 5) {
				int idx = randomIndexes.remove(0);
				listOfMajorTopics.remove(Math.min(idx, listOfMajorTopics.size()-1));
			}
			
			req.getSession().setAttribute(Constants.LIST_OF_MAJOR_TOPICS, listOfMajorTopics);
			
			req.getSession().setAttribute(Constants.TOTAL_NUMBER_OF_TOPICS, topicManager.getTotalNumberOfEntities());
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
