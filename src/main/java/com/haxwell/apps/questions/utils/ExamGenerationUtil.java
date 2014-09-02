package com.haxwell.apps.questions.utils;

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

import java.util.ArrayList;	
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.managers.ExamGenerationManager;

public class ExamGenerationUtil {
	private static Logger log = LogManager.getLogger();

	public static void generateExam(HttpServletRequest request, String json) {
		log.entry();
		
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
		
		session.setAttribute(Constants.SHOULD_QUESTIONS_BE_DISPLAYED, Boolean.FALSE);
		
		EventDispatcher.getInstance().fireEvent(request, EventConstants.EXAM_WAS_GENERATED);
		
		log.exit();
	}
}
