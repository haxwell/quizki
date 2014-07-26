package com.haxwell.apps.questions.utils;

import java.util.Collection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.apps.questions.entities.EntityWithIDAndTextValuePairBehavior;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.AutocompletionManager;
import com.haxwell.apps.questions.managers.EntityWithIDAndTextValuePairManager;
import com.haxwell.apps.questions.managers.Manager;

public abstract class AbstractQuestionAttributeUtil {
	
	public static Logger log = Logger.getLogger(AbstractQuestionAttributeUtil.class.getName());

	public static void persistTopicsForAutocompletion(HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("currentUserEntity");

		String userId = (String)request.getParameter("user_id");
		Long user_id = Long.parseLong((userId == null || userId.equals("-1")) ? user.getId()+"" : userId);

		JSONArray jarr = (JSONArray)JSONValue.parse((String)request.getParameter(getKeyToAutocompleteEntriesInTheRequest()));

		for (int i=0; i<jarr.size(); i++) {	
			String text = "\"" + jarr.get(i).toString() + "\"";
			AutocompletionManager.write(user_id, getAutocompletionConstant(), text);
		}
		
		jarr = (JSONArray)JSONValue.parse((String)request.getParameter(getKeyToDeletedAutocompleteEntriesInTheRequest()));
		
		for (int i=0; i<jarr.size(); i++) {
			String text = "\"" + jarr.get(i).toString() + "\"";
			AutocompletionManager.delete(user_id, getAutocompletionConstant(), text);
		}
	}

	protected static String getAutocompleteHistory(HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("currentUserEntity");

		String userId = (String)request.getParameter("user_id");
		Long user_id = Long.parseLong(userId == null ? user.getId()+"" : userId);

		String rtn = AutocompletionManager.get(user_id, getAutocompletionConstant());
		
		return rtn;
	}

	public static Collection<EntityWithIDAndTextValuePairBehavior> getObjectsFromJSONString(String str, Collection<EntityWithIDAndTextValuePairBehavior> coll) {
		if (str.length() > 0) {
			JSONValue jValue = new JSONValue();
			JSONArray arr = null;
			
			Object obj = jValue.parse(str);
			
			if (obj instanceof JSONObject)
				arr = (JSONArray)((JSONObject)obj).get("topics");
			else
				arr = (JSONArray)obj;
			
			long interspersedId = -1;
			for (int i=0; i < arr.size(); i++) {
				JSONObject o = (JSONObject)arr.get(i);
				
				String textFromJSON = o.get("text").toString();
				EntityWithIDAndTextValuePairBehavior entity = getEntityViaManager(textFromJSON);
				
				if (entity == null) {
					entity = getNewEntity();
					
					entity.setText(textFromJSON);
					
					Long id = Long.parseLong((String)o.get("id"));
					if (id == -1)
						id = interspersedId--; // this is a new topic; give it a different, but negative, id

					entity.setId(id);
				}
				
				coll.add(entity);
			}
		}
		
		return coll;
	}
	
	
	protected static String getKeyToDeletedAutocompleteEntriesInTheRequest() {
		return null;
	}

	protected static long getAutocompletionConstant() {
		return 0;
	}

	protected static String getKeyToAutocompleteEntriesInTheRequest() {
		return null;
	}

	protected static EntityWithIDAndTextValuePairManager getManager() {
		return null;
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getEntityViaManager(String text) {
		return null;
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getEntityViaManager(int id) {
		return null;
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getNewEntity() {
		return null;
	}

	public static String getJSONOfAllEntitiesForQuestionsCreatedByAGivenUserThatContain(long userId, String containsFilter) {
		Collection coll = getManager().getAllEntitiesForQuestionsCreatedByAGivenUserThatContain(userId, containsFilter);
		
		return CollectionUtil.toJSON(coll);
	}
	
	public static String getJSONOfAllEntitiesThatContain(String containsFilter) {
		Collection coll = getManager().getAllEntitiesThatContain(containsFilter);
		
		return CollectionUtil.toJSON(coll);
	}
}
