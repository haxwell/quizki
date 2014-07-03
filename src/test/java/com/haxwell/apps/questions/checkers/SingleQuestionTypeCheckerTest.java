package com.haxwell.apps.questions.checkers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionGeneratorUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // think this may be a functional test
public class SingleQuestionTypeCheckerTest {

	@Test
	public void testQuestionIsCorrect() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SINGLE);
		
		Question q;
		q = QuestionGeneratorUtil.getQuestion(attributes);
		
		SingleQuestionTypeChecker sut = new SingleQuestionTypeChecker(q);
		
		Set<Choice> choices = q.getChoices();
		
	}
}
