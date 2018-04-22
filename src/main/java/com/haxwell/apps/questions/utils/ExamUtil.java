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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


import com.haxwell.apps.questions.constants.DifficultyEnums;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.managers.QuestionManager;

public class ExamUtil {

	public static String getJSONOfExamsWhichContainTheGivenTopics(String tJson) {
		ArrayList<Topic> al = new ArrayList<>();
		
		TopicUtil.getInstance().getObjectsFromJSONString(tJson, al);
		
		return CollectionUtil.toJSON(ExamManager.getExamsById(ExamManager.getExamsWhichContain( al )));
	}
	
	public static ExamReportCardData gradeExam(String qJson, String aJson) {
		//build a collection of question objects
		List<Question> qList = QuestionUtil.getQuestions(qJson);

		// associate them with their answers from the json
		Map<String /* questionId,choiceId */, String /* selectedValue */> answers = QuestionUtil.getAnswers(aJson);

		// for each question answer pair, is it answered correctly?

		Map<String, Map<String, String>> mapOfQuestionNumberToAnswerMap = new HashMap<String, Map<String, String>>();

		// get keys to answers map
		Set<String> answerKeySet = answers.keySet();
		
		// for each key
		for (String key : answerKeySet) {
		// parse it, get the question number
			String qNum = key.substring(0, key.indexOf(','));
			Map<String, String> map = null;
			
			map = mapOfQuestionNumberToAnswerMap.get(qNum);

			// does that number exist in our map of maps?
			if (map == null) {
				// if not, create a map, add it to the map of maps
				map = new HashMap<String, String>();
				mapOfQuestionNumberToAnswerMap.put(qNum, map);
			}
			
		// get the value from the answer map
			String value = answers.get(key);
		// add key for this loop, and its associated value to the map
			map.put(key, value);
			
		// at the end of this should have a map of questionNumber to map of answerkey to selected value
		}				
				
		ExamReportCardData dataObj = new ExamReportCardData();
		
		for (Question q:qList) {
			dataObj.addQuestionAndAnswer(q, mapOfQuestionNumberToAnswerMap.get(q.getId()+""));
		}
		
		return dataObj;
	}
	
	/**
	 * Returns a Set, containing a unique instance of the topic from each of the questions on the exam.
	 * 
	 * @param exam
	 * @return
	 */
	public static Set<Topic> getExamTopics(Exam exam)
	{
		Set<Question> questions = exam.getQuestions();
		
		Set<Topic> rtn = new HashSet<Topic>();
		
		for (Question q : questions)
		{
			rtn.addAll(q.getTopics());
		}
		
		return rtn;
	}
	
	public static Difficulty getExamDifficulty(Exam exam)
	{
		int rtn = (int)DifficultyEnums.JUNIOR.getRank()-1;
		
		// get questions on the exam
		Set<Question> questions = exam.getQuestions();
		int totalQuestionCount = questions.size();
		
		// sum up the difficulties for them all
		int sumOfQuestionDifficulties = 0;
		
		for (Question q : questions)
			sumOfQuestionDifficulties += (q.getDifficulty().getId() - 1);

		////
		if (totalQuestionCount <= 3) {
			rtn = sumOfQuestionDifficulties / totalQuestionCount;
//			int mod = sumOfQuestionDifficulties % totalQuestionCount;
//			rtn += mod;
		}
		else {
			
			int rangeMax = ((int)DifficultyEnums.GURU.getRank()-1) * totalQuestionCount;
			int maxSegmentSize = rangeMax / 4;
			int segmentSizeRemainder = rangeMax % 4;
			int currSegmentSize = 0;
			int segmentIndex = 0;
			
			int[] arr = new int[rangeMax];
			
			for (int i = 0; i < rangeMax; i++) {
				
				if (currSegmentSize < maxSegmentSize)
					arr[i] = segmentIndex;
				else {
					if (segmentSizeRemainder > 0) {
						segmentSizeRemainder--;
					}
					
					segmentIndex++;
					arr[i] = segmentIndex;
					
					currSegmentSize = -1;
				}
				
				currSegmentSize++;
			}
			
			rtn = arr[sumOfQuestionDifficulties];
		}
		
		return DifficultyUtil.getDifficulty(Math.min(rtn + 1, 4));
	}
	
	public static void selectAll(Exam e, String chkboxNames, boolean makeQuestionsSelected) {
		StringTokenizer tokenizer = new StringTokenizer(chkboxNames, ",");
		
		while (tokenizer.hasMoreTokens()) {
			String str = tokenizer.nextToken();
			
			int index = str.indexOf("_");
			String id = str.substring(index+1);
			
			Question q = QuestionManager.getQuestionById(id);
			
			if (makeQuestionsSelected)
				ExamManager.addQuestion(e, q);
			else
				ExamManager.removeQuestion(e, q);
		}
	}
	
	public static String persist(Exam e) {
		String rtn = "";
		String jsonErrors = ValidationUtil.validate(e);
		
		long eid = e.getId();
		
		if (StringUtil.isEmptyJSON(jsonErrors)) {
			if (eid == -1) 
				eid = ExamManager.persistExam(e);
			else
				eid = ExamManager.mergeExam(e);
			
			rtn = "{\"successes\":[\"Exam '" + e.getTitle() + "' was successfully saved! <a href='/secured/exam.jsp?examId=" + eid + "'>(edit it)</a>  <a href='/beginExam.jsp?examId=" + eid + "'>(take it)</a> \"]}";
		}
		else {
			rtn = jsonErrors;
		}
		
		return rtn;
	}
}
