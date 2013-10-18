package com.haxwell.apps.questions.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.apps.questions.entities.Reference;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.managers.ReferenceManager;

public class ReferenceUtil {
	
	public static Logger log = Logger.getLogger(ReferenceUtil.class.getName());

	public static Set<Reference> getSetFromCSV(String csv) {

		StringTokenizer tokenizer = new StringTokenizer(csv, ",");
		Set<Reference> references = new HashSet<Reference>();
		
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken().trim();
			Reference reference = ReferenceManager.getReference(token);
			
			if (reference == null)
				reference = new Reference(token);

			if (!references.contains(reference))
				references.add(reference);
		}
		
		return references;
	}
	
	public static Set<Reference> getSetFromJsonString(String str) {
		Set<Reference> rtn = new HashSet<Reference>();
		
		JSONValue jValue= new JSONValue();
		JSONArray arr = null;
		
		Object obj = jValue.parse(str);
		
		if (obj instanceof JSONObject)
			arr = (JSONArray)((JSONObject)obj).get("references");
		else
			arr = (JSONArray)obj;
		
		for (int i=0; i < arr.size(); i++) {
			JSONObject o = (JSONObject)arr.get(i);
			
			Reference r = new Reference();
			
			r.setText((String)o.get("text"));
			
			Long id = Long.parseLong((String)o.get("id"));
			if (id != -1)
				r.setId(id);
			
			rtn.add(r);
		}
		
		return rtn;
	}
}
