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
	public static Map<String, List<String>> persistQuestionBasedOnHttpServletRequest(HttpServletRequest request) {
		Question question = buildQuestionBasedOnHttpServletRequest(request);
		
		Map<String, List<String>> rtn = new HashMap<String, List<String>>();
		
		List<String> errors = QuestionManager.validate(question);
		
		if (errors.size() == 0)
		{
			if (question.getUser() == null) {
				User user = (User)request.getSession().getAttribute("currentUserEntity");
				question.setUser(user);
			}
			
			long qid = Long.MIN_VALUE;
			if (question.getId() == -1) {
				qid = QuestionManager.persistQuestion(question);
			}
			else {
				qid = QuestionManager.mergeQuestion(question);				
			}
			
			List<String> successes = new ArrayList<String>();
			
			successes.add("Question was successfully saved! <a href='/displayQuestion.jsp?questionId=" + qid + "'>(see it)</a>  <a href='/secured/question.jsp?questionId=" + qid + "'>(edit it)</a>");
			
			rtn.put("successes", successes);			

			request.setAttribute(Constants.CURRENT_QUESTION, null);
			
			request.setAttribute(Constants.SUCCESS_MESSAGES, successes);
			
			EventDispatcher.getInstance().fireEvent(request, EventConstants.QUESTION_WAS_PERSISTED);
		}
		else
		{
			rtn.put("errors", errors);
		}

		return rtn;
	}
	
	/**
	 * 
	 */
	public static Question buildQuestionBasedOnHttpServletRequest(HttpServletRequest request) {
		Question question = new Question();
		
		User user = (User)request.getSession().getAttribute("currentUserEntity");

		String questionUserId = (String)request.getParameter("user_id");
		Long user_id = Long.parseLong(questionUserId == null ? user.getId()+"" : questionUserId);

		Object obj = request.getSession().getAttribute(Constants.NEXT_SEQUENCE_NUMBER);
		int nextSequenceNumber = Integer.MIN_VALUE;

		if (obj != null) {
			nextSequenceNumber = Integer.parseInt(obj.toString());
		}

		if (user.getId() == user_id) {
			String id = (String)request.getParameter("id");
			
			if (id != null)
				question.setId(Long.parseLong(id));
			
			question.setUser(user);
		}

		question.setText((String)request.getParameter("text"));
		question.setDescription((String)request.getParameter("description"));
		question.setDifficulty(DifficultyUtil.getDifficulty(request.getParameter("difficulty_id")));
		question.setQuestionType(TypeUtil.getObjectFromStringTypeId(request.getParameter("type_id")));
		question.setChoices(QuestionUtil.getSetFromJsonString(request.getParameter("choices"), nextSequenceNumber));
		question.setTopics(TopicUtil.getSetFromJsonString((String)request.getParameter("topics")));
		question.setReferences(ReferenceUtil.getSetFromJsonString((String)request.getParameter("references")));
		
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
			q.setTopics(TopicUtil.getSetFromJsonString(o.get("topics").toString()));
			q.setReferences(ReferenceUtil.getSetFromJsonString(o.get("references").toString()));
			
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
