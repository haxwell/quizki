package com.haxwell.apps.questions.factories;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.MultiQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.PhraseQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.SequenceQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.SetQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.SingleQuestionTypeChecker;
import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionAttributeSetterUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class QuestionTypeCheckerFactoryTest {

	@Test
	public void testGetQuestionTypeChecker_forASingleQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SINGLE);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractQuestionTypeChecker qtc = QuestionTypeCheckerFactory.getChecker(q);
		
		assertNotEquals(null, qtc);
		assertTrue(qtc instanceof SingleQuestionTypeChecker);
	}
	
	@Test
	public void testGetQuestionTypeChecker_forAMultipleQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.MULTIPLE);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractQuestionTypeChecker qtc = QuestionTypeCheckerFactory.getChecker(q);
		
		assertNotEquals(null, qtc);
		assertTrue(qtc instanceof MultiQuestionTypeChecker);
	}

	@Test
	public void testGetQuestionTypeChecker_forASequenceQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SEQUENCE);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractQuestionTypeChecker qtc = QuestionTypeCheckerFactory.getChecker(q);
		
		assertNotEquals(null, qtc);
		assertTrue(qtc instanceof SequenceQuestionTypeChecker);
	}

	@Test
	public void testGetQuestionTypeChecker_forAPhraseQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_CONTAINS_FILTER, "Phrase Question Text");
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.PHRASE);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractQuestionTypeChecker qtc = QuestionTypeCheckerFactory.getChecker(q);
		
		assertNotEquals(null, qtc);
		assertTrue(qtc instanceof PhraseQuestionTypeChecker);
	}

	@Test
	public void testGetQuestionTypeChecker_forASetQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SET);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractQuestionTypeChecker qtc = QuestionTypeCheckerFactory.getChecker(q);
		
		assertNotEquals(null, qtc);
		assertTrue(qtc instanceof SetQuestionTypeChecker);
	}
	
	@Test
	public void testGetQuestionTypeChecker_forAQuestionWithoutAType() {
		Question q = new Question();
		
		AbstractQuestionTypeChecker qtc = QuestionTypeCheckerFactory.getChecker(q);
		
		assertEquals(null, qtc);
	}
}
