package com.haxwell.apps.questions.checkers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionAttributeSetterUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // think this may be a functional test
public class SingleQuestionTypeCheckerTest {

	@Test
	public void testQuestionIsCorrect() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SINGLE);

		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		SingleQuestionTypeChecker sut = new SingleQuestionTypeChecker(q);
		
		assertTrue(sut.questionIsCorrect(getMapRepresentingCorrectChoices(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingASingleIncorrectlyChosenChoice(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingACorrectChoiceAndAnIncorrectChoice(q)));
	}
	
	protected Map<String, String> getMapRepresentingCorrectChoices(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		for (Choice c : set) {
			if (c.getIscorrect() == Choice.CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getText());
			}
		}
		
		return map;
	}
	
	protected Map<String, String> getMapRepresentingASingleIncorrectlyChosenChoice(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		for (Choice c : set) {
			if (map.size() == 0 && c.getIscorrect() == Choice.NOT_CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getText());
			}
		}
		
		return map;
	}
	
	protected Map<String, String> getMapRepresentingACorrectChoiceAndAnIncorrectChoice(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		boolean correctAdded = false;
		boolean incorrectAdded = false;
		
		for (Choice c : set) {
			if (!correctAdded && c.getIscorrect() == Choice.CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getText());
				correctAdded = true;
			}
			
			if (!incorrectAdded && c.getIscorrect() == Choice.NOT_CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getText());
				incorrectAdded = true;
			}
		}
		
		return map;
	}
}
