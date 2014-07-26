package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.haxwell.apps.questions.constants.AutocompletionConstants;
import com.haxwell.apps.questions.entities.EntityWithIDAndTextValuePairBehavior;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.EntityWithIDAndTextValuePairManager;
import com.haxwell.apps.questions.managers.TopicManager;

public class TopicUtil extends AbstractQuestionAttributeUtil {
	
	public static Logger log = Logger.getLogger(TopicUtil.class.getName());

	protected static String getKeyToAutocompleteEntriesInTheRequest() {
		return "topicsAutocompleteEntries";
	}

	protected static String getKeyToDeletedAutocompleteEntriesInTheRequest() {
		return "topicsDeletedAutocompleteEntries";
	}
	
	protected static long getAutocompletionConstant() {
		return AutocompletionConstants.TOPICS;
	}

	protected static EntityWithIDAndTextValuePairManager getManager() {
		return TopicManager.getInstance();
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getEntityViaManager(String text) {
		return getManager().getByText(text);
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getEntityViaManager(int id) {
		return getManager().getById(id);
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getNewEntity() {
		return new Topic();
	}

//	public static void persistTopicsForAutocompletion(HttpServletRequest request) {
//		User user = (User)request.getSession().getAttribute("currentUserEntity");
//
//		String userId = (String)request.getParameter("user_id");
//		Long user_id = Long.parseLong((userId == null || userId.equals("-1")) ? user.getId()+"" : userId);
//
//		JSONArray jarr = (JSONArray)JSONValue.parse((String)request.getParameter("topicsAutocompleteEntries"));
//
//		for (int i=0; i<jarr.size(); i++) {	
//			String text = "\"" + jarr.get(i).toString() + "\"";
//			AutocompletionManager.write(user_id, AutocompletionConstants.TOPICS, text);
//		}
//		
//		jarr = (JSONArray)JSONValue.parse((String)request.getParameter("topicsDeletedAutocompleteEntries"));
//		
//		for (int i=0; i<jarr.size(); i++) {
//			String text = "\"" + jarr.get(i).toString() + "\"";
//			AutocompletionManager.delete(user_id, AutocompletionConstants.TOPICS, text);
//		}
//	}
//
//	public static String getAutocompleteHistoryForTopics(HttpServletRequest request) {
//		User user = (User)request.getSession().getAttribute("currentUserEntity");
//
//		String userId = (String)request.getParameter("user_id");
//		Long user_id = Long.parseLong(userId == null ? user.getId()+"" : userId);
//
//		String rtn = AutocompletionManager.get(user_id, AutocompletionConstants.TOPICS);
//		
//		return rtn;
//	}
//	
//	// Takes a string like "topicA,topicB,topicC" and returns a collection of the three Topic objects which match
//	public static Collection<Topic> getTopicsFromCSV(String csv) {
//
//		Set<String> topics = CollectionUtil.getSetFromCSV(csv);
//		Collection<Topic> coll = TopicManager.getTopics(topics);
//		
//		return coll;
//	}
//	
//	// Using a JSON string as a base, creates instances of Topic to match, returning them in a List.
//	public static List<Topic> getListFromJsonString(String str) {
//		List<Topic> rtn = new ArrayList<Topic>();
//		
//		if (str.length() > 0) {
//			JSONValue jValue = new JSONValue();
//			JSONArray arr = null;
//			
//			Object obj = jValue.parse(str);
//			
//			if (obj instanceof JSONObject)
//				arr = (JSONArray)((JSONObject)obj).get("topics");
//			else
//				arr = (JSONArray)obj;
//			
//			for (int i=0; i < arr.size(); i++) {
//				JSONObject o = (JSONObject)arr.get(i);
//				
//				Topic t = new Topic();
//				
//				t.setText(o.get("text").toString());
//				
//				Long id = Long.parseLong(o.get("id").toString());
//				if (id != -1)
//					t.setId(id);
//				
//				rtn.add(t);
//			}
//		}
//		
//		return rtn;
//	}
//	
//	public static Set<Topic> getSetFromJsonString(String str) {
//		Set<Topic> rtn = new HashSet<Topic>();
//		
//		JSONValue jValue= new JSONValue();
//		JSONArray arr = null;
//		
//		Object obj = jValue.parse(str);
//		
//		if (obj instanceof JSONObject)
//			arr = (JSONArray)((JSONObject)obj).get("topics");
//		else
//			arr = (JSONArray)obj;
//		
//		long interspersedId = -1;
//		for (int i=0; i < arr.size(); i++) {
//			JSONObject o = (JSONObject)arr.get(i);
//			
//			String text = (String)o.get("text");
//			
//			Topic t = TopicManager.getTopic(text);
//					
//			if (t == null){
//				t = new Topic();
//				t.setText(text);
//
//				Long id = Long.parseLong((String)o.get("id"));
//				if (id == -1)
//					id = interspersedId--; // this is a new topic; give it a different, but negative, id
//
//				t.setId(id);
//			}
//			
//			rtn.add(t);
//		}
//		
//		return rtn;
//	}
//	
//	public static String getJSONOfAllTopicsForQuestionsCreatedByAGivenUserThatContain(long userId, String containsFilter) {
//		return getJSONOfAllEntitiesForQuestionsCreatedByAGivenUserThatContain(userId, containsFilter);
//	}
//	
//	public static String getJSONOfAllTopicsThatContain(String containsFilter) {
//		return getJSONOfAllEntitiesThatContain(containsFilter);
//	}
	
	public static Set<Topic> getTopicsFromJSONString(String str) {
		Collection<EntityWithIDAndTextValuePairBehavior> coll = new ArrayList<>();
		
		getObjectsFromJSONString(str, coll);
		
		Set<Topic> rtn = new HashSet<>();
		
		for (EntityWithIDAndTextValuePairBehavior entity : coll) {
			Topic t = new Topic();
			t.setId(entity.getId());
			t.setText(entity.getText());
			
			rtn.add(t);
		}
		
		return rtn;
	}
}
