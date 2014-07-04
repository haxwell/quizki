package com.haxwell.apps.questions.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.haxwell.apps.questions.constants.FilterConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;

/**
 * QuestionAttributeSetterUtil
 * 
 * This class sets attributes on a Question based on given parameters.
 * 
 * ==============
 * HOW IT WORKS
 * ==============
 * The class HandlerFactory() defines a map of parameters (keys) to handlers which can set that parameter on 
 * a question object. When the QuestionAttributeSetterUtil is called, a map of these parameters is passed in. For
 * each of the parameters, QASU calls the HandlerFactory with that key, and an object. The object is what should
 * be set on the question. What that object is, of course, depends on the key. So for QuestionType, the object
 * is the numeric ID from TypeConstants and it is used to get the QuestionType object from TypeUtil, which is 
 * then set on the Question. 
 * 
 * To add another attribute of a Question that can be set by this code, you will need to define a handler, and
 * put it in the map belonging to HandlerFactory. The key you use to put it in that map should be passed in
 * by the client. The HandlerFactory will then find the handler, create an instance of it, and pass in the 
 * Object value the client passed to it.
 * 
 * Note each handler needs to implement the Handler interface.
 * 
 * Question type is interesting, because HandlerFactory has a handler which maps to the key for QuestionType,
 * and in turn it calls another handler which has a map of handlers for each type. In most cases, I expect this
 * map-in-the-second-level-class paradigm will not be necessary, and the handler defined in the HandlerFactory
 * will be able to do the work as necessary.
 * 
 * 
 * @author jjames
 *
 */
public class QuestionAttributeSetterUtil {

	private static HandlerFactory handlerFactory = null;
	
	private static interface Handler {
		public Question set(Object o, Question q);
	}

	public static Question setQuestionAttributes(Map<String, Object> attributes, Question q) {
		if (handlerFactory == null)
			handlerFactory = new HandlerFactory();
		
		if (attributes.containsKey(FilterConstants.QUESTION_TYPE_FILTER)) {
			handlerFactory.executeHandler(FilterConstants.QUESTION_TYPE_FILTER, attributes.get(FilterConstants.QUESTION_TYPE_FILTER), q);
		}
		
		if (attributes.containsKey(FilterConstants.ENTITY_ID_FILTER)) {
			handlerFactory.executeHandler(FilterConstants.ENTITY_ID_FILTER, attributes.get(FilterConstants.ENTITY_ID_FILTER), q);
		}
		
		return q;
	}
	
	private static class HandlerFactory {
		Map<String, Class<? extends Handler>> map;
		
		HandlerFactory() {
			map = new HashMap<>();
			
			map.put(FilterConstants.QUESTION_TYPE_FILTER, QuestionType_HandlerFactory.class);
			map.put(FilterConstants.ENTITY_ID_FILTER, QuestionIDHandler.class);
		}
		
		Question executeHandler(String filterConstant, Object value, Question q) {
			Handler h = null;
			
			if (map.containsKey(filterConstant)) {
				try {
					h = map.get(filterConstant).newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				throw new IllegalArgumentException();
			
			return (h.set(value, q));
		}
	}
	
	//
	// Question ID handler
	//
	private static class QuestionIDHandler implements Handler {
		public QuestionIDHandler() {
			
		}
		
		public Question set(Object o, Question q) {
			Long id = Long.parseLong(o.toString());
			
			q.setId(id);
			
			return q;
		}
	}
	
	//
	// QuestionType Handlers
	//
	
	private static class QuestionType_HandlerFactory implements Handler {
		Map<Object, Class<? extends Handler>> map;
		
		@SuppressWarnings("unused")
		QuestionType_HandlerFactory() {
			map = new HashMap<>();
			
			map.put(TypeConstants.SINGLE, QuestionType_Single_Handler.class);
			map.put(TypeConstants.MULTIPLE, QuestionType_Multiple_Handler.class);
			map.put(TypeConstants.SEQUENCE, QuestionType_Sequence_Handler.class);
		}
		
		public Question set(Object o, Question q) {
			Handler h = null;
			
			try {
				h = map.get(o).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return (h == null) ? null : h.set(o, q);
		}
	}
	
	private static class QuestionType_Single_Handler implements Handler {
		@SuppressWarnings("unused")
		QuestionType_Single_Handler() {
			// do nothing
		}
		
		public Question set(Object o, Question q) {
			q.setQuestionType(TypeUtil.getObjectFromStringTypeId(o.toString()));
			
			Set<Choice> choices = new HashSet<>();
			
			Choice choice = new Choice(1, "Choice 1", Choice.CORRECT, Choice.NO_SEQUENCE);
			choices.add(choice);
			
			choice = new Choice(2, "Choice 2", Choice.NOT_CORRECT, Choice.NO_SEQUENCE);
			choices.add(choice);
			
			choice = new Choice(3, "Choice 3", Choice.NOT_CORRECT, Choice.NO_SEQUENCE);
			choices.add(choice);
			
			q.setChoices(choices);
			
			return q;
		}
	}
	
	private static class QuestionType_Multiple_Handler implements Handler {
		@SuppressWarnings("unused")
		public QuestionType_Multiple_Handler() {
			// do nothing
		}
		
		public Question set(Object o, Question q) {
			q.setQuestionType(TypeUtil.getObjectFromStringTypeId(o.toString()));
			
			Set<Choice> choices = new HashSet<>();
			
			Choice choice = new Choice(1, "Choice 1", Choice.CORRECT, Choice.NO_SEQUENCE);
			choices.add(choice);
			
			choice = new Choice(2, "Choice 2", Choice.CORRECT, Choice.NO_SEQUENCE);
			choices.add(choice);
			
			choice = new Choice(3, "Choice 3", Choice.NOT_CORRECT, Choice.NO_SEQUENCE);
			choices.add(choice);
			
			q.setChoices(choices);
			
			return q;
		}
	}
	
	private static class QuestionType_Sequence_Handler implements Handler {
		@SuppressWarnings("unused")
		public QuestionType_Sequence_Handler() {
			// do nothing
		}
		
		public Question set(Object o, Question q) {
			q.setQuestionType(TypeUtil.getObjectFromStringTypeId(o.toString()));
			
			Set<Choice> choices = new HashSet<>();
			
			Choice choice = new Choice(1, "Choice 1", Choice.CORRECT, 1);
			choices.add(choice);
			
			choice = new Choice(2, "Choice 2", Choice.CORRECT, 2);
			choices.add(choice);
			
			choice = new Choice(3, "Choice 3", Choice.CORRECT, 3);
			choices.add(choice);
			
			choice = new Choice(4, "Choice 4", Choice.CORRECT, 4);
			choices.add(choice);
			
			q.setChoices(choices);
			
			return q;
		}
	}
}
