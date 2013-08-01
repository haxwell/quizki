package com.haxwell.apps.questions.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.haxwell.apps.questions.entities.Reference;
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
}
