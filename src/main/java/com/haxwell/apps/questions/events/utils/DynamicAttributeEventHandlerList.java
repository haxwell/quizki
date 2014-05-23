package com.haxwell.apps.questions.events.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.events.handlers.IDynamicAttributeEventHandler;

/**
 * Maintains a map of attributes names to a list of all the possible event handlers for that attribute. Also maintains a map of
 * the event names and all the handlers which are activated for it when an attribute is set in the session, so that when that event happens
 * the handlers can be called.
 * 
 * This class is to directly support dynamic questions. Those questions can be different each time they are presented on an exam,
 * so we need to keep track of how the question was last generated. If a dynamic question had option X out of XYZ the last time, it needs
 * to have option X the next time, too. At least until its no longer needed.
 * 
 * HOW DYNAMIC QUESTIONS WORK IN QUIZKI
 * ------------------------------------
 * 
 * This class is populated as the application starts up. ApplicationContext.xml contains a list dynamification related beans. Spring passes
 * to this class a list of DynamicAttributeEventHandlerBeans. Each bean describes 1) an event it should be activated upon, 2) what should be done
 * when the event happens, 3) the ending of the strings to be set in the session that this bean relates to, and 4) a class to be called when
 * strings like #3 are set in the session. This class takes each DynamicAttributeEventHandlerBean and stores in a map, the #3 string to the
 * #4 handler (attrEndsWithToListOfHandlersMap), and the #1 event name to the entire bean (eventNameToListOfHandlersMap). 
 * 
 * A question is requested by its ID. If that question is dynamic, the DynamifierFactory returns the appropriate Dynamifier. That class
 * then looks for the dynamic data it stored in the session previously. For example, it looks for an id, appended with
 * "_questionTypeDynamifiedString", or something to that effect. If the dynamifier does not find it, the dynamifier picks an id of some
 * part of the question, relevant to its type, as the dynamic attribute of this particular instance of the question. It then saves that id
 * in the session, appended with its specific string. 
 * 
 * When those strings are set in the session, a SessionListener picks it up, and gets a list of the handlers (#4) associated with the
 * given string (#3). It calls each of those handlers, and generally they save the given string to a list keyed by the (#3) string. This
 * is to maintain a list of the specific dynamic data set by the dynamifier (the given strings), keyed by the #3 string. In this way, an
 * exam which has several questions of the same type (#3), can keep an orderly list, with each item in the list representing a specific
 * question.
 * 
 * When the #1 event happens, EventDispatcher calls this class to get a list of the beans associated with that event, and calls them (#2).
 * Generally, this is how the accumulated #3 session strings are removed from the session.
 * 
 * @author jjames
 */
public class DynamicAttributeEventHandlerList {

	HashMap<String, List<DynamicAttributeEventHandlerBean>> /* event name to List of handlers */ eventNameToListOfHandlersMap = new HashMap<>();
	HashMap<String, List<DynamicAttributeEventHandlerBean>> /* attr endsWith to List of handlers */ attrEndsWithToListOfHandlersMap = new HashMap<>();
	
	Logger log = Logger.getLogger(ObjectEventHandlerList.class.getName());
	
	public DynamicAttributeEventHandlerList() { }
	
	public void setDynamicAttributeEventHandlerItem(DynamicAttributeEventHandlerBean item) {
		List<DynamicAttributeEventHandlerBean> list = eventNameToListOfHandlersMap.get(item.event);
		
		if (list == null)
			list = new ArrayList<DynamicAttributeEventHandlerBean>();
		
		list.add(item);
		
		eventNameToListOfHandlersMap.put(item.event, list);
		
		log.log(Level.FINER, "Added: " + item.toString());
	}
	
	public void setDynamicAttributeEndsWithHandlerItem(DynamicAttributeEventHandlerBean item) {
		List<DynamicAttributeEventHandlerBean> list = attrEndsWithToListOfHandlersMap.get(item.attribute_endsWith);
		
		if (list == null)
			list = new ArrayList<DynamicAttributeEventHandlerBean>();
		
		list.add(item);
		
		attrEndsWithToListOfHandlersMap.put(item.attribute_endsWith, list);
		
		log.log(Level.FINER, "Added: " + item.toString());
	}
	
	public void setDynamicAttributeEventHandlerList(List<DynamicAttributeEventHandlerBean> list) {

		for (DynamicAttributeEventHandlerBean bean : list) {
			setDynamicAttributeEventHandlerItem(bean);
			setDynamicAttributeEndsWithHandlerItem(bean);
		}
	}
	
	public List<IDynamicAttributeEventHandler> getEventHandlerList(String event) {
		List<DynamicAttributeEventHandlerBean> list = eventNameToListOfHandlersMap.get(event);
		
		List<IDynamicAttributeEventHandler> rtn = new ArrayList<>();
		
		for (DynamicAttributeEventHandlerBean bean : list)
			rtn.add(bean.eventHandler);
		
		return rtn;
	}
	
	public List<DynamicAttributeEventHandlerBean> getBeansByEventName(String event) {
		return eventNameToListOfHandlersMap.get(event);
	}
	
	public List<IDynamicAttributeEventHandler> getAttributeEndsWithHandlerList(String attributeEndsWith) {
		List<DynamicAttributeEventHandlerBean> list = attrEndsWithToListOfHandlersMap.get(attributeEndsWith);
		
		List<IDynamicAttributeEventHandler> rtn = new ArrayList<>();
		
		for (DynamicAttributeEventHandlerBean bean : list)
			rtn.add(bean.attribute_endsWith_handler);
		
		return rtn;
	}
	
	public Set<String> getAttributeEndsWithKeys() {
		return attrEndsWithToListOfHandlersMap.keySet();
	}
}
