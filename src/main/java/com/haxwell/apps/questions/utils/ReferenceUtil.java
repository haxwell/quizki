package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.haxwell.apps.questions.constants.AutocompletionConstants;
import com.haxwell.apps.questions.entities.EntityWithIDAndTextValuePairBehavior;
import com.haxwell.apps.questions.entities.Reference;
import com.haxwell.apps.questions.managers.EntityWithIDAndTextValuePairManager;
import com.haxwell.apps.questions.managers.ReferenceManager;

public class ReferenceUtil extends AbstractQuestionAttributeUtil {
	
	public static Logger log = Logger.getLogger(ReferenceUtil.class.getName());

	protected static String getKeyToAutocompleteEntriesInTheRequest() {
		return "referencesAutocompleteEntries";
	}

	protected static String getKeyToDeletedAutocompleteEntriesInTheRequest() {
		return "referencesDeletedAutocompleteEntries";
	}
	
	protected static long getAutocompletionConstant() {
		return AutocompletionConstants.REFERENCES;
	}

	protected static EntityWithIDAndTextValuePairManager getManager() {
		return ReferenceManager.getInstance();
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getEntityViaManager(String text) {
		return getManager().getByText(text);
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getEntityViaManager(int id) {
		return getManager().getById(id);
	}
	
	protected static EntityWithIDAndTextValuePairBehavior getNewEntity() {
		return new Reference();
	}

//	public static void persistReferencesForAutocompletion(HttpServletRequest request) {
//		User user = (User)request.getSession().getAttribute("currentUserEntity");
//
//		String userId = (String)request.getParameter("user_id");
//		Long user_id = Long.parseLong((userId == null || userId.equals("-1")) ? user.getId()+"" : userId);
//
//		JSONArray jarr = (JSONArray)JSONValue.parse((String)request.getParameter("referencesAutocompleteEntries"));
//
//		for (int i=0; i<jarr.size(); i++) {	
//			String text = "\"" + jarr.get(i).toString() + "\"";
//			AutocompletionManager.write(user_id, AutocompletionConstants.REFERENCES, text);
//		}
//		
//		jarr = (JSONArray)JSONValue.parse((String)request.getParameter("referencesDeletedAutocompleteEntries"));
//		
//		for (int i=0; i<jarr.size(); i++) {
//			String text = "\"" + jarr.get(i).toString() + "\"";
//			AutocompletionManager.delete(user_id, AutocompletionConstants.REFERENCES, text);
//		}
//	}
//
//	public static String getAutocompleteHistoryForReferences(HttpServletRequest request) {
//		User user = (User)request.getSession().getAttribute("currentUserEntity");
//
//		String userId = (String)request.getParameter("user_id");
//		Long user_id = Long.parseLong(userId == null ? user.getId()+"" : userId);
//
//		String rtn = AutocompletionManager.get(user_id, AutocompletionConstants.REFERENCES);
//		
//		return rtn;
//	}
//
//	public static Set<Reference> getSetFromCSV(String csv) {
//
//		StringTokenizer tokenizer = new StringTokenizer(csv, ",");
//		Set<Reference> references = new HashSet<Reference>();
//		
//		while (tokenizer.hasMoreTokens())
//		{
//			String token = tokenizer.nextToken().trim();
//			Reference reference = ReferenceManager.getReference(token);
//			
//			if (reference == null)
//				reference = new Reference(token);
//
//			if (!references.contains(reference))
//				references.add(reference);
//		}
//		
//		return references;
//	}
//	
//	public static Set<Reference> getSetFromJsonString(String str) {
//		Set<Reference> rtn = new HashSet<Reference>();
//		
//		if (str.length() > 0) {
//			JSONValue jValue= new JSONValue();
//			JSONArray arr = null;
//			
//			Object obj = jValue.parse(str);
//			
//			if (obj instanceof JSONObject)
//				arr = (JSONArray)((JSONObject)obj).get("references");
//			else
//				arr = (JSONArray)obj;
//			
//			long interspersedId = -1;
//			for (int i=0; i < arr.size(); i++) {
//				JSONObject o = (JSONObject)arr.get(i);
//				
//				String text = (String)o.get("text");
//				
//				Reference r = ReferenceManager.getReference(text);
//				
//				if (r == null) {
//					r = new Reference();
//					
//					r.setText(text);
//					
//					Long id = Long.parseLong((String)o.get("id"));
//					if (id == -1)
//						id = interspersedId--; // this is a new reference; give it a different, but negative, id
//
//					r.setId(id);
//				}
//				
//				rtn.add(r);
//			}
//		}
//		
//		return rtn;
//	}

	public static Set<Reference> getReferencesFromJSONString(String str) {
		Collection<EntityWithIDAndTextValuePairBehavior> coll = new ArrayList<>();
		
		getObjectsFromJSONString(str, coll);
		
		Set<Reference> rtn = new HashSet<>();
		
		for (EntityWithIDAndTextValuePairBehavior entity : coll) {
			Reference r = new Reference();
			r.setId(entity.getId());
			r.setText(entity.getText());
			
			rtn.add(r);
		}
		
		return rtn;
	}
}
