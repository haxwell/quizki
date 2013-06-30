package com.haxwell.apps.questions.managers;

import java.util.Collection;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.utils.CollectionUtil;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Used to collect information from a Manager to return to a JSP that has been called by AJAX
 * 
 * @author johnathanj
 */
public class AJAXReturnData {
	
	public Collection<? extends AbstractEntity> entities = null;
	public int additionalItemCount = -1;
	public int additionalInfoCode = -1;
	
	public String toJSON() {
		String rtn = "{ \"addlItemCount\":\"" + additionalItemCount + "\", \"addlInfoCode\":\"" + additionalInfoCode + "\" ";
		
		String collJSON = CollectionUtil.toJSON(entities, CollectionUtil.DONT_ADD_OPENING_CLOSING_CURLY_BRACES);
		
		if (!StringUtil.isNullOrEmpty(collJSON))
			rtn += ", " + collJSON;
		
		rtn += " }";
		
		return rtn;
	}
}
