package com.haxwell.apps.questions.utils;

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

		AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(q);
		boolean isCorrect = checker.questionIsCorrect(a); 

		// pass it to the checker, see if this question is correct
		if (!isCorrect) {
			incorrect.add(q.getId()+"");
		}
		
		AnsweredQuestion aq = new AnsweredQuestion(q, a, isCorrect);
		
		mapOfExistingAnswers.put(arr.size(), aq);
	}
	
	public String getStateAsJSONString() {
		// WILO: This last little section, needs to be in a new implementation of ExamHistory. We need
		//  that class now on the server side to take a question and the answers associated with it, 
		//  one at a time, and keep track of whether it was answered correctly, and the incorrect
		//  question ids. It should expose an iterator which returns AnsweredQuestion objects like
		//  examReportCard.jsp would expect (for now). It should return the JSON returned from this method
		//  as its state.
		
		// might want to create this as the class ExamReportCardData, and use it, and remove ExamHistory
		//  server side as appropriate.
		
		// to be decided.. write an object to the session with the number correct, number total, and a collection of the QA pairs
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
