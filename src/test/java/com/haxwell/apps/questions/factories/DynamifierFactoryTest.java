package com.haxwell.apps.questions.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.dynamifiers.AbstractDynamifier;
import com.haxwell.apps.questions.dynamifiers.PhraseQuestionDynamifier;
import com.haxwell.apps.questions.dynamifiers.SetQuestionDynamifier;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionAttributeSetterUtil;
import com.haxwell.apps.questions.utils.TestQuestionUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class DynamifierFactoryTest {

	@Test
	public void testGetDynamifier_forASetQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SET);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertNotEquals(null, ad);
		assertTrue(ad instanceof SetQuestionDynamifier);
	}
	
	@Test
	public void testGetDynamifier_forAPhraseQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_CONTAINS_FILTER, "Phrase Question Text");
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.PHRASE);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertNotEquals(null, ad);
		assertTrue(ad instanceof PhraseQuestionDynamifier);
	}

	@Test
	public void testGetDynamifier_forASingleQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SINGLE);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertEquals(null, ad);
	}

	@Test
	public void testGetDynamifier_forAMultipleQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.MULTIPLE);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertEquals(null, ad);
	}

	@Test
	public void testGetDynamifier_forASequenceQuestion() {
		Map<String, Object> attributes = new HashMap<>();
		
		attributes.put(FilterConstants.QUESTION_TYPE_FILTER, TypeConstants.SEQUENCE);
		
		Question q = new Question();
		q = QuestionAttributeSetterUtil.setQuestionAttributes(attributes, q);
		
		AbstractDynamifier ad = DynamifierFactory.getDynamifier(q);
		
		assertEquals(null, ad);
	}

	@Test
	public void testGetDynamifier_forAnExam() {
		Exam exam = TestQuestionUtil.getExam();
		
		assertEquals(null, DynamifierFactory.getDynamifier(exam));
	}
}
