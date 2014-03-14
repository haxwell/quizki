package com.haxwell.apps.questions.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.AbstractEntity;
import com.haxwell.apps.questions.factories.ValidatorFactory;
import com.haxwell.apps.questions.validators.Validator;

public class ValidationUtil {

	public static String validate(AbstractEntity e) {
		
		String rtn = "";
		Map<String, List<String>> errors = new HashMap<String, List<String>>();
		
		Validator validator = ValidatorFactory.getValidator(e);
		validator.validate(e, errors);

		rtn = CollectionUtil.toJSON(errors);
		
		return rtn;
	}
}
