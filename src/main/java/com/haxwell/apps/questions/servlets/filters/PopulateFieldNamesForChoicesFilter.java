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
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * 
 * 
 */
@WebFilter("/PopulateFieldNamesForChoicesFilter")
public class PopulateFieldNamesForChoicesFilter extends AbstractFilter {

    public PopulateFieldNamesForChoicesFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();
			
			Question currentQuestion = getCurrentQuestion(req);
			
			if (currentQuestion != null)
			{
				List<Choice> list = QuestionUtil.getChoiceList(currentQuestion);
			
				StringBuffer choiceFieldNamesJSArray = new StringBuffer(StringUtil.startJavascriptArray());
				StringBuffer choiceFieldValues = new StringBuffer(StringUtil.startJavascriptArray());
				StringBuffer choiceIsCorrect = new StringBuffer(StringUtil.startJavascriptArray());
				StringBuffer choiceSequenceNumbers = new StringBuffer(StringUtil.startJavascriptArray());
				
				for (Choice c : list) {
					StringUtil.addToJavascriptArray(choiceFieldNamesJSArray, QuestionUtil.getFieldnameForChoice(currentQuestion, c));
					StringUtil.addToJavascriptArray(choiceFieldValues, c.getText());
					StringUtil.addToJavascriptArray(choiceIsCorrect, c.getIscorrect() == 1 ? "true" : "");
					StringUtil.addToJavascriptArray(choiceSequenceNumbers, c.getSequence()+"");
				}
				
				StringUtil.closeJavascriptArray(choiceFieldNamesJSArray);
				StringUtil.closeJavascriptArray(choiceFieldValues);
				StringUtil.closeJavascriptArray(choiceIsCorrect);
				StringUtil.closeJavascriptArray(choiceSequenceNumbers);
				
				session.setAttribute(Constants.LIST_OF_FIELD_NAMES_OF_CHOICES, choiceFieldNamesJSArray.toString());
				
				if (currentQuestion.getQuestionType().getId() != TypeConstants.PHRASE)
					session.setAttribute(Constants.LIST_OF_VALUES_OF_CHOICES, choiceFieldValues.toString());
				else 
					session.setAttribute(Constants.LIST_OF_VALUES_OF_CHOICES, "null");
				
				session.setAttribute(Constants.LIST_OF_VALUES_OF_CHOICES_FOR_DISPLAY_QUESTION, choiceFieldValues.toString());
				session.setAttribute(Constants.LIST_SAYING_WHICH_CHOICES_ARE_CORRECT, choiceIsCorrect);
				session.setAttribute(Constants.LIST_OF_SEQUENCE_NUMBERS_FOR_CHOICES, choiceSequenceNumbers);
			}
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
