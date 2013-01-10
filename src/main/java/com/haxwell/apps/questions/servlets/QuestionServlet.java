package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ChoiceManager;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.managers.TopicManager;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.StringUtil;
import com.haxwell.apps.questions.utils.TypeUtil;

/**
 * Servlet implementation class QuestionServlet
 */
@WebServlet("/secured/QuestionServlet")
public class QuestionServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public QuestionServlet() {   }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean entityWasPersisted = false;
		
		String button = request.getParameter("button");
		String fwdPage = "/secured/createQuestion.jsp";
		
		Question questionObj = getQuestionBean(request);
		
		// in case they press a button for a choice or a topic.. this will be null..
		if (button == null) button = "";
		
		if (button.equals("Add Question")) {
			addQuestion(request, questionObj);
			entityWasPersisted = true;
		}
		else if (button.equals("Add Choice")) {
			addChoice(request, questionObj);
			setQuestionText(request, questionObj);
		}
		else if (button.equals("Add True/False")) {
			addChoice(questionObj, "True", getIsCorrectParameter(request));
			addChoice(questionObj, "False", !getIsCorrectParameter(request));
			setQuestionText(request, questionObj);
		}
		else if (button.equals("Add Topic")) {
			addTopic(request, questionObj);
		}
		else
		{
			Set<Choice> choices = questionObj.getChoices();
			Iterator<Choice> iterator = choices.iterator();
			String action = null;
			Choice c = null;
			
			while (iterator.hasNext() && action == null)
			{
				c = iterator.next();
				action = request.getParameter("choiceButton_" + c.getId());
			}
			
			if (action != null) {
				if (action.equals("Update"))
					ChoiceManager.update(request.getParameter("choiceText_" + c.getId()), (request.getParameter("group1_" + c.getId()).equals("Yes")), choices, c);
				else if (action.equals("Delete"))
					ChoiceManager.delete(choices, c);
				
				setQuestionText(request, questionObj);
			}
			
			// they clicked a topic specific button..
			if (action == null)
			{
				Iterator<Topic> topicIterator = questionObj.getTopics().iterator();
				Topic t = null;
				
				while (topicIterator.hasNext() && action == null)
				{
					t = topicIterator.next();
					action = request.getParameter("topicButton_" + t.getId());
				}
				
				if (action != null) {
					TopicManager.delete(questionObj.getTopics(), t);
				}
			}
		}

		if (!entityWasPersisted)
			request.setAttribute(Constants.CURRENT_QUESTION, questionObj);

		forwardToJSP(request, response, fwdPage);
	}

	private void setQuestionText(HttpServletRequest request,
			Question questionObj) {
		String text = request.getParameter("questionText");
		String desc = request.getParameter("questionDescription");

		questionObj.setText(text);
		questionObj.setDescription(desc);
	}

	private void addTopic(HttpServletRequest request, Question questionObj) {
		String text = request.getParameter("topicText");
		
		if (!StringUtil.isNullOrEmpty(text))
		{
			StringTokenizer tokenizer = new StringTokenizer(text, ",");
			Set<Topic> topics = questionObj.getTopics();
			
			while (tokenizer.hasMoreTokens())
			{
				String token = tokenizer.nextToken();
				Topic topic = TopicManager.getTopic(token);
				
				if (topic == null)
					topic = new Topic(token);

				if (!topics.contains(topic))
					topics.add(topic);
			}

			questionObj.setTopics(topics);
		}
	}

	private void addQuestion(HttpServletRequest request, Question questionObj) {
		setQuestionText(request, questionObj);
		questionObj.setDifficulty(DifficultyUtil.convertToObject(request.getParameter("difficulty")));
		questionObj.setQuestionType(TypeUtil.convertToObject(request.getParameter("type")));
		
		List<String> errors = QuestionManager.validate(questionObj);
		
		if (errors.size() == 0)
		{
			long id = QuestionManager.persistQuestion(questionObj);
			List<String> successes = new ArrayList<String>();
			
			successes.add("Question was successfully saved! <a href=\"/quizki/displayQuestion.jsp?questionId=" + id + "\">(see it)</a>, <a href=\"/quizki/secured/createQuestion.jsp?questionId=" + id + "\">(edit it)</a>");

			request.getSession().setAttribute(Constants.CURRENT_QUESTION, null);
			request.setAttribute(Constants.CURRENT_QUESTION, null);
			
			request.getSession().setAttribute(Constants.CURRENT_QUESTION_HAS_BEEN_PERSISTED, Boolean.TRUE);
			request.setAttribute(Constants.SUCCESS_MESSAGES, successes);
		}
		else
		{
			request.setAttribute(Constants.VALIDATION_ERRORS, errors);
		}
	}

	private void addChoice(HttpServletRequest request, Question questionObj) {
		String text = request.getParameter("choiceText");

		if (!StringUtil.isNullOrEmpty(text))
		{
			addChoice(questionObj, text, getIsCorrectParameter(request));
		}
	}
	
	private boolean getIsCorrectParameter(HttpServletRequest request)
	{
		return request.getParameter("isCorrect").toLowerCase().equals("yes");
	}
	
	private void addChoice(Question questionObj, String text, boolean isCorrect)
	{
		Set<Choice> choices = questionObj.getChoices();
		Choice choice = ChoiceManager.newChoice(text, isCorrect);
		
		choice.setId(choices.size());
		choices.add(choice);
		questionObj.setChoices(choices);
	}
	
	private Question getQuestionBean(HttpServletRequest request)
	{
		Question question = (Question)request.getSession().getAttribute(Constants.CURRENT_QUESTION);
		
		if (question == null)
		{
			question = QuestionManager.newQuestion();
			
			// TODO: need some way of throwing an object up in the air saying "Hey! Just created this!"
			//  so that anyone who cares can set attributes on it. JMS message, or some other event listening/handler
			
			//  ..because I'm pretty sure I don't like doing this here..
			User user = (User)request.getSession().getAttribute("currentUserEntity");
			question.setUser(user);
			
			request.getSession().setAttribute(Constants.CURRENT_QUESTION, question);
		}
		
		return question;
	}
}
