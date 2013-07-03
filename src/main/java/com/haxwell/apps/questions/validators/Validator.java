package com.haxwell.apps.questions.validators;

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.AbstractEntity;

public interface Validator {

	public boolean validate(AbstractEntity e, Map<String, List<String>> errors);
}
