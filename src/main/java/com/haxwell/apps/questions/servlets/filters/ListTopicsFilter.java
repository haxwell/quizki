package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
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
			
			Collection<Topic> coll; 
			
			User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

			if (user != null)
				coll = TopicManager.getAllTopicsForQuestionsCreatedByAGivenUser(user.getId());
			else
				coll = TopicManager.getAllTopics();
			
			req.getSession().setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, coll);
			
			Collection<Topic> coll2 = TopicManager.getAllTopicsWithMoreThanXXQuestions(3);
			
			List<Integer> randomIndexes = RandomIntegerUtil.getRandomListOfNumbers(coll2.size());
			List<Topic> listOfMajorTopics = new ArrayList<Topic>(coll2);

			int count = listOfMajorTopics.size();
			while (count-- > 5) {
				int idx = randomIndexes.remove(0);
				listOfMajorTopics.remove(Math.min(idx, listOfMajorTopics.size()-1));
			}
			
			req.getSession().setAttribute(Constants.LIST_OF_MAJOR_TOPICS, listOfMajorTopics);
			
			req.getSession().setAttribute(Constants.TOTAL_NUMBER_OF_TOPICS, TopicManager.getTotalNumberOfTopics());
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
