package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamGenerationManager;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.TopicManager;
import com.haxwell.apps.questions.utils.DifficultyUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Servlet implementation class ExamServlet
 */
@WebServlet("/GenerateExamServlet")
public class GenerateExamServlet extends AbstractHttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenerateExamServlet() {
        super();
    }

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
		
		String fwdPage = "/generateExam.jsp";
		String button = request.getParameter("button");
		HttpSession session = request.getSession();

		List<String> errors = new ArrayList<String>();
		
		if (button.equals("Filter"))
		{
			handleFilterButtonPress(request);
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);
		}
		else if (button.equals("Clear Filter"))
		{
			clearMRUFilterSettings(request);
			refreshListOfQuestionsToBeDisplayed(request);
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);
		}
		else if (button.equals("Select Topics"))
		{
			Collection<Topic> coll = (Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED);
			
			String csvOfTopicIDs = getTopicIDsToBeActedOn(request, coll, "a_chkbox_");
			
			if (!StringUtil.isNullOrEmpty(csvOfTopicIDs)) {
				coll = TopicManager.getTopicsById(csvOfTopicIDs);
			
				String str = request.getParameter("includeExclude");
				
				Collection<Topic> currIncluded = (Collection<Topic>) session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED);
				Collection<Topic> currExcluded = (Collection<Topic>) session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED);				
				
				if (str.equals("include"))
				{
					if (currIncluded == null)
						currIncluded = new ArrayList<Topic>();
					
					currIncluded.addAll(coll);
					
					Collection<Topic> availableTopics = (Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED);
					availableTopics.removeAll(coll);
					
					session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, availableTopics);
				}
				else
				{
					if (currExcluded == null)
						currExcluded = new ArrayList<Topic>();
					
					currExcluded.addAll(coll);
					
					Collection<Topic> availableTopics = (Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED);
					availableTopics.removeAll(coll);
					
					session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, availableTopics);
				}

				session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED, currIncluded);				
				session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED, currExcluded);				
			}
			else
			{
				errors.add("You did not pick any topics!");

				request.setAttribute(Constants.VALIDATION_ERRORS, errors);
			}
			
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);
		}
		else if (button.equals("Clear"))
		{
			session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED, null);				
			session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED, null);
			session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, TopicManager.getAllTopics());
		}
		else if (button.equals("Generate Exam"))
		{
			String numberOfQuestions = request.getParameter("numberOfQuestions");
			Difficulty difficulty = DifficultyUtil.convertToObject(request.getParameter("difficulty"));
			Collection<Topic> includedColl = (Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED);
			
			if (includedColl != null && includedColl.size() >= 1)
			{
				String topicsToInclude = StringUtil.getCSVFromCollection(includedColl); 
				String topicsToExclude = StringUtil.getCSVFromCollection((Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED)); 

				Exam examObj = 
					ExamGenerationManager.generateExam(Long.parseLong(numberOfQuestions), StringUtil.getListOfLongsFromCSV(topicsToInclude), StringUtil.getListOfLongsFromCSV(topicsToExclude), difficulty.getId(), false);
			
				session.setAttribute(Constants.CURRENT_EXAM, examObj);
				
				if (examObj.getQuestions().size() > 0) {
					session.setAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_TAKEN, Boolean.TRUE);
					session.setAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_EDITED, Boolean.TRUE);					
				}
				
				session.setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "Generate Exam");
				session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, difficulty.getId());
				
				session.setAttribute(Constants.SHOULD_QUESTIONS_BE_DISPLAYED, Boolean.FALSE);
	
				fwdPage = "/beginExam.jsp";
				
				session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, null);
			}
			else 
			{
				errors.add("No topics have been picked to include in the exam!");
				
				request.setAttribute(Constants.VALIDATION_ERRORS, errors);
			}
		}
		
		saveTheValueInTheNumberOfQuestionsField(request);

		forwardToJSP(request, response, fwdPage);
	}

	// TODO: Similar code in ExamServlet.. Abstract out to a common class.
	private String getTopicIDsToBeActedOn(HttpServletRequest request, Collection<? extends EntityWithAnIntegerIDBehavior> coll, String fieldName)
	{
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = coll.iterator();
		StringBuffer sb = new StringBuffer();
		
		while (iterator.hasNext())
		{
			EntityWithAnIntegerIDBehavior entity = iterator.next();
			String str = request.getParameter(fieldName + entity.getId());
			
			if (!StringUtil.isNullOrEmpty(str)) {
				if (sb.length() > 0)
					sb.append(",");
				
				sb.append(entity.getId());
			}
		}
		
		return sb.toString();
	}
	
	private void handleFilterButtonPress(HttpServletRequest request) {
		String topicFilterText = request.getParameter("topicContainsFilter");
		int maxDifficulty = DifficultyUtil.convertToInt(request.getParameter("difficulty"));

		Collection<Topic> coll = null;
		
		////
		String mineOrAll = request.getParameter(Constants.SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS);
		if (mineOrAll.equals(Constants.MY_ITEMS_STR))
		{
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

			if (user != null)
				coll = TopicManager.getAllTopicsForQuestionsCreatedByAGivenUserThatContain(user.getId(), topicFilterText);
			else
				coll = TopicManager.getAllTopicsThatContain(topicFilterText);
		}
		else 
		{
			coll = TopicManager.getAllTopicsThatContain(topicFilterText);
		}

		// Remove the topics already on the exam 
		Collection<Topic> currIncluded = (Collection<Topic>)request.getSession().getAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED);				
		Collection<Topic> currExcluded = (Collection<Topic>)request.getSession().getAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED);				
		
		coll.removeAll(currIncluded);
		coll.removeAll(currExcluded);

		request.getSession().setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, coll);

		// store the filter we just used
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
		request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, mineOrAll);
	}

	private void clearMRUFilterSettings(HttpServletRequest request) {
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, null);
		request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, null);
		request.getSession().setAttribute(Constants.MRU_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM, Constants.DEFAULT_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM);
	}
	
	private void refreshListOfQuestionsToBeDisplayed(HttpServletRequest request) {
		String topicFilterText = (String)request.getSession().getAttribute(Constants.MRU_FILTER_TOPIC_TEXT);
		Object o = request.getSession().getAttribute(Constants.MRU_FILTER_DIFFICULTY);
//		String mineOrAll = (String)request.getSession().getAttribute(Constants.MRU_FILTER_MINE_OR_ALL);
		
		int maxDifficulty = DifficultyConstants.GURU;
		
		if (o != null)
			maxDifficulty = Integer.parseInt(o.toString());
		
		Collection<Topic> coll = TopicManager.getAllTopics();

		// Remove the topics already on the exam 
		Collection<Topic> currIncluded = (Collection<Topic>)request.getSession().getAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED);				
		Collection<Topic> currExcluded = (Collection<Topic>)request.getSession().getAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED);				
		
		coll.removeAll(currIncluded);
		coll.removeAll(currExcluded);

		request.getSession().setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, coll);
		request.getSession().setAttribute(Constants.MRU_FILTER_TOPIC_TEXT, topicFilterText);
		request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, maxDifficulty);
//		request.getSession().setAttribute(Constants.MRU_FILTER_MINE_OR_ALL, Constants.MY_ITEMS_STR);		
	}
	
	private void saveTheValueInTheNumberOfQuestionsField(HttpServletRequest request) {
		String numberOfQuestions = request.getParameter("numberOfQuestions");
		request.getSession().setAttribute(Constants.MRU_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM, numberOfQuestions);		
	}
}
