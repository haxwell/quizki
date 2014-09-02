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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.factories.QuestionTypeCheckerFactory;

public class ExamReportCardData {

	JSONArray arr = new JSONArray();
	JSONArray incorrect = new JSONArray();
	
	Map<Integer, AnsweredQuestion> mapOfExistingAnswers = new HashMap<Integer, AnsweredQuestion>();
	
	public void addQuestionAndAnswer(Question q, Map<String, String> a) {
		JSONObject _obj = new JSONObject();
		_obj.put("questionText", QuestionUtil.getDisplayString(q));
		_obj.put("questionId", q.getId()+"");
		arr.add(_obj);

		// pass it to the checker, see if this question is correct
		AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(q);
		boolean isCorrect = checker.questionIsCorrect(a); 

		if (!isCorrect) {
			incorrect.add(q.getId()+"");
		}
		
		AnsweredQuestion aq = new AnsweredQuestion(q, a, isCorrect);
		
		mapOfExistingAnswers.put(arr.size(), aq);
	}
	
	public String getStateAsJSONString() {
		JSONObject jObj = new JSONObject();
		
		jObj.put("numberCorrect", arr.size() - incorrect.size());
		jObj.put("numberTotal", arr.size());
		jObj.put("incorrect_ids", incorrect);
		jObj.put("questionText", arr);
		
		return jObj.toJSONString();
	}

	public class AnsweredQuestion
	{
		public Question question;
		public Map<String, String> answers; /* Map of String to FieldName, see QuestionUtil.getFieldnameForChoice */
		public Map<String, Object> metadata;
		public boolean isCorrect = false;
		
		public AnsweredQuestion(Question q, Map<String, String> answers, boolean b)
		{
			question = q;
			this.answers = answers;
			this.isCorrect = b;
		}

		public Question getQuestion() {
			return question;
		}

		public void setQuestion(Question question) {
			this.question = question;
		}

		public Map<String, String> getAnswers() {
			return answers;
		}

		public void setAnswers(Map<String, String> answers) {
			this.answers = answers;
		}

		public boolean getIsCorrect() {
			return isCorrect;
		}

		public void setIsCorrect(boolean isCorrect) {
			this.isCorrect = isCorrect;
		}
		
		public Map<String, Object> getMetadata() {
			if (metadata == null)
				metadata = new HashMap<String, Object>();
			
			return metadata;
		}

		public void setMetadata(Map<String, Object> metadata) {
			this.metadata = metadata;
		}
	}

	public class ERCDIterator implements Iterator<AnsweredQuestion>
	{
		private int index = 0;
		
		private ERCDIterator() {
			
		}
		
		@Override
		public boolean hasNext() {
			return index < mapOfExistingAnswers.size();
		}

		@Override
		public AnsweredQuestion next() {
			return mapOfExistingAnswers.get(++index);
		}

		@Override
		public void remove() {
			
		}
	}

	public Iterator<ExamReportCardData.AnsweredQuestion> iterator() {
		return new ERCDIterator();
	}
	
	public Iterator<ExamReportCardData.AnsweredQuestion> getIterator()	{
		return iterator();
	}

}
