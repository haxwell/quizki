package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.StringUtil;

@WebFilter("/AddRandomIndexToChoiceArrayFilter")
public class AddRandomIndexToChoiceArrayFilter extends AbstractFilter {

    public AddRandomIndexToChoiceArrayFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			boolean doGeneration = true;
			
			if (req.getSession().getAttribute(Constants.SHOULD_GENERATE_NEW_RANDOM_CHOICE_INDEXES) != null)
				doGeneration = req.getSession().getAttribute(Constants.SHOULD_GENERATE_NEW_RANDOM_CHOICE_INDEXES).equals(Boolean.TRUE);
			
			if (doGeneration) {
				Question q = getCurrentQuestion(req); 
				
				if (q == null) // try other means
				{
					Object questionIdRequestParameter = req.getParameter(Constants.QUESTION_ID);
					
					if (questionIdRequestParameter != null)
					{
						q = QuestionManager.getQuestionById(questionIdRequestParameter.toString());
					}
				}
				
				if (q != null)
				{
					List<Integer> list = RandomIntegerUtil.getRandomListOfNumbers(q.getChoices().size());
					StringBuffer randomChoiceIndexes = new StringBuffer(StringUtil.startJavascriptArray());
					
					for (Integer i : list)
					{
						StringUtil.addToJavascriptArray(randomChoiceIndexes, i.toString());
					}
	
					StringUtil.closeJavascriptArray(randomChoiceIndexes);
					
					req.getSession().setAttribute(Constants.LIST_OF_RANDOM_CHOICE_INDEXES, randomChoiceIndexes.toString());
				}
			}
			
			EventDispatcher.getInstance().fireEvent(req, EventConstants.RANDOM_INDEXES_ADDED_TO_CHOICE_ARRAY);
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
