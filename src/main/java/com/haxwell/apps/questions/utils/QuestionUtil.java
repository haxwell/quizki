package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.dynamifiers.AbstractDynamifier;
import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.factories.DynamifierFactory;
import com.haxwell.apps.questions.managers.AJAXReturnData;
import com.haxwell.apps.questions.managers.Manager;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.managers.UserManager;

public class QuestionUtil {

	/**
	 * Lists of choices are always ordered by ID. A question could have 20 choices, and
	 * in any list of items related to those 20 choices, the first item in the list relates
	 * to the choice with the highest ID. The second item, the second highest ID, etc.
	 *   
	 * @param currentQuestion
	 * @return
	 */
	public static List<Choice> getChoiceList(Question currentQuestion) {
		Set<Choice> choices = currentQuestion.getChoices();
		
		ArrayList<Choice> list = new ArrayList<Choice>(choices);
		
		Collections.sort(list, Manager.ID_COMPARATOR);
		
		return list;
	}
	
	// ..except for when they're not.. :)
	public static List<Choice> getChoiceListBySequenceNumber(Question currentQuestion) {
		Set<Choice> choices = currentQuestion.getChoices();
		
		ArrayList<Choice> list = new ArrayList<Choice>(choices);
		
		Collections.sort(list, Manager.SEQUENCE_NUMBER_COMPARATOR);
		
		return list;
	}
	
	/**
	 * 
	 */
	public static JSONObject persistQuestionBasedOnHttpServletRequest(HttpServletRequest request) {
		Question question = buildQuestionBasedOnHttpServletRequest(request);
		
		String json = ValidationUtil.validate(question);
		JSONObject jobj = (JSONObject)JSONValue.parse(json);
		
		JSONArray errorsArr = (JSONArray)jobj.get("questionValidationErrors"); // TODO: make hard-coded value a constant instead
		
		if (errorsArr == null)
		{
			if (question.getUser() == null) {
				User user = (User)request.getSession().getAttribute("currentUserEntity");
				question.setUser(user);
			}
			
			long qid = Long.MIN_VALUE;
			
			/**
			 * This commented out code was there because I was having an issue where choice IDs were being corrupted when questions 
			 * were saved. I thought to fix that by persisting new questions, and merging the old ones. Commit 68e4e5b describes that
			 * situation. But afterwards, topics were being duplicated in the topic table, so each new question even though it used the
			 * same topic, would create a new entry in the table, rather than not doing that, and allowing the count in question_topic
			 * to increment. I fixed that by being more complete about the annotations I set on the associated classes of Question.
			 * So I added @manyToMany on topics and references, and @OneToMany on difficulty and question type. Now, when I use just
			 * merge() rather than persist(), it all seems to be working okay.
			 * 
			 * A note, I have read on stackoverflow, that merge() does more work than persist(), and that persist() should be used for
			 * new entities. The problem I have, and am leaving for my future self, is that a Question itself can be new, but contain
			 * topics and other entities that should be merged, not persisted. I don't know, or at least haven't thought about, how 
			 * to break the persistence process up to make that happen. 
			 * 
			 * Anyway, I have left this commented out for history's sake, so that i can remember in the future. If it ever starts 
			 * effin up again.
			 */
			
//			if (question.getId() == -1) {
//				qid = QuestionManager.persistQuestion(question);
//			}
//			else {
				qid = QuestionManager.mergeQuestion(question);				
//			}
			
			jobj.put("newQuestionId", qid);
				
			List<String> successes = new ArrayList<String>();
			
			successes.add("Question '" + StringUtil.getStringWithEllipsis(question.getTextWithoutHTML(), 40) + "' was successfully saved! <a href='/displayQuestion.jsp?questionId=" + qid + "'>(see it)</a>  <a href='/secured/question.jsp?questionId=" + qid + "'>(edit it)</a> <a id='createDupeQuestionLink' href='#'>(duplicate it)</a>");
			
			jobj.put("successes", successes);
			
			request.setAttribute(Constants.CURRENT_QUESTION, null);
			
			request.setAttribute(Constants.SUCCESS_MESSAGES, successes);
			
			EventDispatcher.getInstance().fireEvent(request, EventConstants.QUESTION_WAS_PERSISTED);
		}
		else
		{
			Object o = jobj.remove("questionValidationErrors"); 
			jobj.put("errors", o);
		}

		return jobj;
	}
	
	/**
	 * 
	 */
	public static Question buildQuestionBasedOnHttpServletRequest(HttpServletRequest request) {
		Question question = new Question();
		
		User currentUserEntity = (User)request.getSession().getAttribute("currentUserEntity");

		// if this question does not already have a creator user id (in other words, it appears brand spankin' new)
		String user_id = request.getParameter("user_id"); 
		if (user_id == null) {
			// set the current user as the creator
			question.setUser(currentUserEntity);
		}
		else {
			// set the user who's user ID we have as the creator
			User u = null;
			
			long parsedLong = Long.parseLong(user_id);
			if (currentUserEntity.getId() == parsedLong) {
				u = currentUserEntity;
			}
			else {
				u = UserManager.getUserById(parsedLong);
			}
			
			question.setUser(u);
		}
		
		String id = (String)request.getParameter("id");
		
		if (id != null)
			question.setId(Long.parseLong(id));

		Object obj = request.getSession().getAttribute(Constants.NEXT_SEQUENCE_NUMBER);
		int nextSequenceNumber = Integer.MIN_VALUE;

		if (obj != null) {
			nextSequenceNumber = Integer.parseInt(obj.toString());
		}

		question.setText((String)request.getParameter("text"));
		question.setDescription((String)request.getParameter("description"));
		question.setDifficulty(DifficultyUtil.getDifficulty(request.getParameter("difficulty_id")));
		question.setQuestionType(TypeUtil.getObjectFromStringTypeId(request.getParameter("type_id")));
		question.setChoices(QuestionUtil.getSetFromJsonString(request.getParameter("choices"), nextSequenceNumber));
		question.setTopics(TopicUtil.getInstance().getTopicsFromJSONString((String)request.getParameter("topics")));
		question.setReferences(ReferenceUtil.getInstance().getReferencesFromJSONString(((String)request.getParameter("references"))));
		
		return question;
	}
	
	public static AJAXReturnData getFilteredList(HttpServletRequest request) {
		FilterCollection fc = new FilterCollection();

		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

		fc.add(FilterConstants.USER_ID_ENTITY, user);

		if (user != null)
			fc.add(FilterConstants.USER_ID_FILTER, user.getId() + "");

		String entityId = request.getParameter(FilterConstants.ENTITY_ID_FILTER);
		if (StringUtil.isNullOrEmpty(entityId)) entityId = "";
		
		String difficultyFilterValue = request.getParameter(FilterConstants.DIFFICULTY_FILTER);
		if (StringUtil.isNullOrEmpty(difficultyFilterValue)) difficultyFilterValue = "0";

		String qtf = request.getParameter(FilterConstants.QUESTION_TYPE_FILTER);
		if (StringUtil.isNullOrEmpty(qtf)) qtf = TypeConstants.ALL_TYPES + "";

		String roef = request.getParameter(FilterConstants.RANGE_OF_ENTITIES_FILTER);
		if (StringUtil.isNullOrEmpty(roef)) roef = Constants.ALL_ITEMS + "";
		
		String maxEntityCount = request.getParameter(FilterConstants.MAX_ENTITY_COUNT_FILTER);
		if (StringUtil.isNullOrEmpty(maxEntityCount)) maxEntityCount = "1";
		
		String offset = request.getParameter(FilterConstants.OFFSET_FILTER);
		if (StringUtil.isNullOrEmpty(offset)) offset = "0";

		fc.add(FilterConstants.ENTITY_ID_FILTER, entityId);
		fc.add(FilterConstants.QUESTION_CONTAINS_FILTER, request.getParameter(FilterConstants.QUESTION_CONTAINS_FILTER));
		fc.add(FilterConstants.TOPIC_CONTAINS_FILTER, request.getParameter(FilterConstants.TOPIC_CONTAINS_FILTER));
		fc.add(FilterConstants.QUESTION_TYPE_FILTER, qtf);
		fc.add(FilterConstants.DIFFICULTY_FILTER, difficultyFilterValue);
		fc.add(FilterConstants.AUTHOR_FILTER, request.getParameter(FilterConstants.AUTHOR_FILTER));
		fc.add(FilterConstants.MAX_ENTITY_COUNT_FILTER, maxEntityCount);
		fc.add(FilterConstants.RANGE_OF_ENTITIES_FILTER, roef);
		fc.add(FilterConstants.OFFSET_FILTER, offset);

		AJAXReturnData rtnData = null;

		Exam exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);

		Set selectedQuestions = (exam == null ? new HashSet() : exam.getQuestions());

		// TODO: Remove this selectedQuestions functionality to its own space.. See TODO within..
		rtnData = QuestionManager.getAJAXReturnObject(fc, selectedQuestions);

		if (rtnData.getDynamificationStatus() == AJAXReturnData.DynamificationStatus.NEEDED)
			for (AbstractEntity ae : rtnData.entities) {
				AbstractDynamifier ad = DynamifierFactory.getDynamifier(ae);
				
				if (ad != null) 
					ad.dynamify(ae, request);
			}

		return rtnData;
	}
	
	public static String getDisplayString(Question q) {
		return getDisplayString(q, -1);
	}
	
	public static String getDisplayString(Question q, int maxLength) {
		String str = q.getTextWithoutHTML();
		
		if (StringUtil.isNullOrEmpty(str))
			str = q.getDescription();
		
		int strLength = str.length();
		
		if (maxLength <= 0)
			maxLength = strLength;
		
		if (maxLength > strLength)
			maxLength = strLength;
		
		return str.substring(0, maxLength);
	}
	
	public static Set<Choice> getSetFromJsonString(String str, long newChoiceIndexBegin) {
		Set<Choice> rtn = new HashSet<Choice>();
		
		JSONArray arr = null;
		
		Object obj = JSONValue.parse(str);
		
		if (obj instanceof JSONObject)
			arr = (JSONArray)((JSONObject)obj).get("choice");
		else
			arr = (JSONArray)obj;
		
		for (int i=0; i < arr.size(); i++) {
			JSONObject o = (JSONObject)arr.get(i);
			
			Choice c = new Choice();
			
			c.setText((String)o.get("text"));
			
			String seq = (String)o.get("sequence");
			Integer num = -1;
			try {
				num = Integer.parseInt(seq);
			}
			catch (NumberFormatException nfe) {
				
			}
			
			c.setSequence(num);
			
			c.setIscorrect("true".equals((String)(o.get("iscorrect"))));
			
			long id = Long.parseLong((String)o.get("id"));
			if (id != -1)
				c.setId(id);
			
			rtn.add(c);
		}
		
		return rtn;
	}
	
	public static List<Question> getQuestions(String str) {
		JSONObject jObj = (JSONObject)JSONValue.parse(str);

		JSONArray arr = (JSONArray)jObj.get("questions");
		
		LinkedList<Question> ll = new LinkedList<Question>();
		
		for (int i=0; i < arr.size(); i++) {
			JSONObject o = (JSONObject)arr.get(i);
			
			Question q = new Question();
			
			q.setId(Long.parseLong(o.get("id").toString()));
			q.setDescription(o.get("description").toString());
			q.setText(o.get("text").toString());
			q.setDifficulty(DifficultyUtil.getDifficulty(o.get("difficulty_id").toString()));
			q.setQuestionType(TypeUtil.getObjectFromStringTypeId(o.get("type_id").toString()));
			
			q.setChoices(getSetFromJsonString(o.get("choices").toString(), -1));
			q.setTopics(TopicUtil.getInstance().getTopicsFromJSONString(o.get("topics").toString()));
			q.setReferences(ReferenceUtil.getInstance().getReferencesFromJSONString(o.get("references").toString()));
			
			JSONArray dynarr = (JSONArray)o.get("dynamicDataFieldNames");
			
			if (dynarr != null) {
				for (int x=0; x < dynarr.size(); x++) {
					String dynFieldName = (String)dynarr.get(x);
					
					q.setDynamicData(dynFieldName, o.get(dynFieldName));
				}
			}
			
			ll.add(q);
		}
		
		return ll;
	}
	
	/**
	 * Returns a map of field ids (in the form 'question_id[comma]choice_id') to the values for those fields.
	 * 
	 * Example JSON: {"answers": [{ "fieldId": "32,87", "value": "William J. Clinton" }, .... ] }
	 * @param str
	 * @return
	 */
	public static Map<String, String> getAnswers(String str) {
		JSONValue jValue = new JSONValue();
		JSONObject jObj = (JSONObject)jValue.parse(str);
		
		JSONArray arr = (JSONArray)jObj.get("answers");
		
		Map<String, String> rtn = new HashMap<String, String>();
		
		for (int i=0; i < arr.size(); i++) {
			JSONObject o = (JSONObject)arr.get(i);

			String key = o.get("fieldId").toString();
			String value = o.get("value").toString();
			
			rtn.put(key, value);
		}
		
		return rtn;
	}
}
