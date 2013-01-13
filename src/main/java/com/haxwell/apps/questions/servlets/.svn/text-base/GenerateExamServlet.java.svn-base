package com.haxwell.apps.questions.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.ExamGenerationManager;
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
		
		if (button.equals("Filter"))
		{
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, Boolean.TRUE);
		}
		else if (button.equals("Clear Filter"))
		{
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
					currIncluded.addAll(coll);
					
					Collection<Topic> availableTopics = (Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED);
					availableTopics.removeAll(coll);
					
					session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, availableTopics);
				}
				else
				{
					currExcluded.addAll(coll);
					
					Collection<Topic> availableTopics = (Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED);
					availableTopics.removeAll(coll);
					
					session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_DISPLAYED, availableTopics);
				}

				session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED, currIncluded);				
				session.setAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED, currExcluded);				
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
			String topicsToInclude = getCSVFromCollection((Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_INCLUDED)); //request.getParameter("topicsToInclude");
			String topicsToExclude = getCSVFromCollection((Collection<Topic>)session.getAttribute(Constants.LIST_OF_TOPICS_TO_BE_EXCLUDED)); //request.getParameter("topicsToInclude");
			
			Exam examObj = 
					ExamGenerationManager.generateExam(Long.parseLong(numberOfQuestions), StringUtil.getListOfLongsFromCSV(topicsToInclude), StringUtil.getListOfLongsFromCSV(topicsToExclude), difficulty.getId(), false);
			
			session.setAttribute(Constants.CURRENT_EXAM, examObj);
			
			if (examObj.getQuestions().size() > 0)
				session.setAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_TAKEN, Boolean.TRUE);
			
			session.setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "Generate Exam");
			session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, difficulty.getId());

			fwdPage = "/displayExam.jsp";
			
			session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, null);
		}

		redirectToJSP(request, response, fwdPage);
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

	// TODO: Move this to a common class.
	private String getCSVFromCollection(Collection<? extends EntityWithAnIntegerIDBehavior> coll)
	{
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = coll.iterator();
		StringBuffer sb = new StringBuffer();
		
		while (iterator.hasNext())
		{
			EntityWithAnIntegerIDBehavior entity = iterator.next();

			sb.append(entity.getId());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}
	
}
