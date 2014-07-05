package com.haxwell.apps.questions.checkers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.haxwell.apps.questions.utils.RandomIntegerUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class) // think this may be a functional test
public class SequenceQuestionTypeCheckerTest {

	@Test
	public void testQuestionIsCorrect() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SEQUENCE);
		attributes.put(FilterConstants.ENTITY_ID_FILTER, RandomIntegerUtil.getRandomInteger(1000));
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);

		SequenceQuestionTypeChecker sut = new SequenceQuestionTypeChecker(q);
		
		// asserts
		assertTrue(sut.questionIsCorrect(getMapRepresentingCorrectChoices(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingAllIncorrectlySequencedChoices(q)));
		assertFalse(sut.questionIsCorrect(getMapRepresentingOneIncorrectSequence(q)));
		assertFalse(sut.questionIsCorrect(new HashMap<String, String>()));
	}

	protected Map<String, String> getMapRepresentingCorrectChoices(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		for (Choice c : set) {
			if (c.getIscorrect() == Choice.CORRECT) {
				map.put(q.getId() + "," + c.getId(), c.getSequence()+"");
			}
		}
		
		return map;
	}
	
	protected Map<String, String> getMapRepresentingAllIncorrectlySequencedChoices(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		int[] intArray = {4,3,2,1};
		
		for (Choice c : set) {
			if (c.getIscorrect() == Choice.CORRECT) {
				int choiceId = Integer.parseInt(c.getId()+"");
				map.put(q.getId() + "," + choiceId, intArray[choiceId-1]+"");
			}
		}
		
		return map;
	}
	
	protected Map<String, String> getMapRepresentingOneIncorrectSequence(Question q) {
		Map<String, String> map = new HashMap<>();
		
		Set<Choice> set = q.getChoices();
		
		int[] intArray = {1,2,3,5};
		
		for (Choice c : set) {
			if (c.getIscorrect() == Choice.CORRECT) {
				int choiceId = Integer.parseInt(c.getId()+"");
				map.put(q.getId() + "," + choiceId, intArray[choiceId-1]+"");
			}
		}
		
		return map;
	}
}
