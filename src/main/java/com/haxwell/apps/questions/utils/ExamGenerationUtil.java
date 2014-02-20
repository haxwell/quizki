package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.managers.ExamGenerationManager;

public class ExamGenerationUtil {
	public static Logger log = Logger.getLogger(ExamGenerationUtil.class.getName());

	public static void generateExam(HttpServletRequest request, String json) {
		HttpSession session = request.getSession();
		
		JSONValue jValue= new JSONValue();
		JSONObject jObj = (JSONObject)jValue.parse(json);

		JSONArray arr = (JSONArray)jObj.get("selectedListOfTopics");
		List<Long> selectedTopicsList = new ArrayList<Long>();
		
		for (int i=0; i < arr.size(); i++) {
			JSONObject jObj2 = (JSONObject)arr.get(i);
			selectedTopicsList.add(Long.parseLong(jObj2.get("id")+""));
		}
		
		arr = (JSONArray)jObj.get("excludedListOfTopics");
		List<Long> excludedTopicsList = new ArrayList<Long>();
		
		for (int i=0; i < arr.size(); i++) {
			JSONObject jObj2 = (JSONObject)arr.get(i);
			excludedTopicsList.add(Long.parseLong(jObj2.get("id")+""));
		}

		long difficultyId = Long.parseLong(jObj.get("difficulty_id")+"");
		long numberOfQuestions = Long.parseLong(jObj.get("numberOfQuestions")+"");
		Exam exam = ExamGenerationManager.generateExam(numberOfQuestions, selectedTopicsList, excludedTopicsList, difficultyId, false);
		
		session.setAttribute(Constants.CURRENT_EXAM, exam);
		
		if (exam.getQuestions().size() > 0) {
			session.setAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_TAKEN, Boolean.TRUE);
			session.setAttribute(Constants.ALLOW_GENERATED_EXAM_TO_BE_EDITED, Boolean.TRUE);					
		}
		
		session.setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "Generate Exam");
		session.setAttribute(Constants.MRU_FILTER_DIFFICULTY, difficultyId);
		
		session.setAttribute(Constants.SHOULD_QUESTIONS_BE_DISPLAYED, Boolean.FALSE);

//		fwdPage = "/beginExam.jsp";
		
		//session.setAttribute(Constants.EXAM_GENERATION_IS_IN_PROGRESS, null);
		
		EventDispatcher.getInstance().fireEvent(request, EventConstants.EXAM_WAS_GENERATED);
	}
}
