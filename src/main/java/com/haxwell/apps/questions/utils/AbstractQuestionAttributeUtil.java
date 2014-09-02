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

public abstract class AbstractQuestionAttributeUtil {
	
	public static Logger log = Logger.getLogger(AbstractQuestionAttributeUtil.class.getName());

	public void persistEntitiesForAutocompletion(HttpServletRequest request) {
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

	public String getAutocompleteHistory(HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("currentUserEntity");

		String userId = (String)request.getParameter("user_id");
		Long user_id = Long.parseLong(userId == null ? user.getId()+"" : userId);

		String rtn = AutocompletionManager.get(user_id, getAutocompletionConstant());
		
		return rtn;
	}

	public String getJSONOfAllEntitiesForQuestionsCreatedByAGivenUserThatContain(long userId, String containsFilter) {
		Collection coll = getManager().getAllEntitiesForQuestionsCreatedByAGivenUserThatContain(userId, containsFilter);
		
		return CollectionUtil.toJSON(coll);
	}
	
	public String getJSONOfAllEntitiesThatContain(String containsFilter) {
		Collection coll = getManager().getAllEntitiesThatContain(containsFilter);
		
		return CollectionUtil.toJSON(coll);
	}

	/**
	 * Given a string of (at present) Topics or References represented in JSON, this method will return a collection
	 * of those objects based on the values in the JSON string. You must supply the Collection, generified for the type
	 * that you want, ie ArrayList<Topic>, because I haven't figured out how to get this method to determine the type
	 * you want, and then create a collection of that type. TODO, I guess.
	 * 
	 * @param str
	 * @param coll
	 * @return
	 */
	public Collection<EntityWithIDAndTextValuePairBehavior> getObjectsFromJSONString(String str, Collection coll) {
		if (str.length() > 0) {
			JSONValue jValue = new JSONValue();
			JSONArray arr = null;
			
			Object obj = jValue.parse(str);
			
			if (obj instanceof JSONObject)
				arr = (JSONArray)((JSONObject)obj).get(getJSONArrayKey());
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
						id = interspersedId--; // this is a new entity; give it a different, but negative, id

					entity.setId(id);
				}
				
				coll.add(entity);
			}
		}
		
		return coll;
	}
	
	protected abstract Object getJSONArrayKey();

	protected abstract String getKeyToDeletedAutocompleteEntriesInTheRequest();

	protected abstract long getAutocompletionConstant();

	protected abstract String getKeyToAutocompleteEntriesInTheRequest();

	protected abstract EntityWithIDAndTextValuePairManager getManager();
	
	protected abstract EntityWithIDAndTextValuePairBehavior getEntityViaManager(String text);
	
	protected abstract EntityWithIDAndTextValuePairBehavior getEntityViaManager(int id);
	
	protected abstract EntityWithIDAndTextValuePairBehavior getNewEntity();
}
