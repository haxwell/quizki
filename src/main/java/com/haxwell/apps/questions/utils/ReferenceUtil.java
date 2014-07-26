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

	static ReferenceUtil instance = null;
	
	private ReferenceUtil() {
		
	}
	
	public static ReferenceUtil getInstance() {
		if (instance == null)
			instance = new ReferenceUtil();
		
		return instance;
	}
	
	protected String getKeyToAutocompleteEntriesInTheRequest() {
		return "referencesAutocompleteEntries";
	}

	protected String getKeyToDeletedAutocompleteEntriesInTheRequest() {
		return "referencesDeletedAutocompleteEntries";
	}
	
	protected String getJSONArrayKey() {
		return "references";
	}
	
	protected long getAutocompletionConstant() {
		return AutocompletionConstants.REFERENCES;
	}

	protected EntityWithIDAndTextValuePairManager getManager() {
		return ReferenceManager.getInstance();
	}
	
	protected EntityWithIDAndTextValuePairBehavior getEntityViaManager(String text) {
		return getManager().getByText(text);
	}
	
	protected EntityWithIDAndTextValuePairBehavior getEntityViaManager(int id) {
		return getManager().getById(id);
	}
	
	protected EntityWithIDAndTextValuePairBehavior getNewEntity() {
		return new Reference();
	}

	public Set<Reference> getReferencesFromJSONString(String str) {
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
