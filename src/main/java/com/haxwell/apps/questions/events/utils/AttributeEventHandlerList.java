package com.haxwell.apps.questions.events.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.events.handlers.IAttributeEventHandler;

/**
 * Maintains a map of attributes names to a list of all the possible event handlers for that attribute. Also maintains a map of
 * the event names and all the handlers which are activated for it when an attribute is set in the session, so that when that event happens
 * the handlers can be called.
 * 
 * So you have an attribute X, and a list of handlers for Event 1, Event 2, Event 3. The handlers describe the event name and the method
 * to be called when that named event happens. This makes up the first map.
 * 
 * When X is set in the session, the name of the handlers associated with it in the first list (each in turn) is mapped to a list of 
 * handlers to be called when that event happens. So that list could contain a handler which deals with attribute X, attribute Y and
 * attribute RX493-A. When the event happens, each of those attribute handlers would be called.
 * 
 * So we're building 1) a list of attributes to events, and 2) events to handlers. When an attribute is set each event associated with it
 * gets their own list, and we add the attribute's handler to that list. In this way, when the event happens, all the handlers for the 
 * various attributes that have been set in the meanwhile can be called.
 * 
 * @author jjames
 */
public class AttributeEventHandlerList {

	HashMap<String, Set<AttributeEventHandlerBean>> /*attr to list of handlers*/ attrToRegisteredAEHLBeanMap = new HashMap<String, Set<AttributeEventHandlerBean>>();
	HashMap<String, List<IAttributeEventHandler>> /*event to list of handlers*/ eventNameToActiveIEventHandlerMap = new HashMap<String, List<IAttributeEventHandler>>();

	Logger log = Logger.getLogger(AttributeEventHandlerList.class.getName());
	
	public AttributeEventHandlerList() { }

	public void setAttributeEventHandlerItem(AttributeEventHandlerBean item) {
		addItemToMap(attrToRegisteredAEHLBeanMap, item.attr, item);
		
		log.log(Level.FINER, "Added: " + item.toString());
	}
	
	public void setAttributeEventHandlerList(List<AttributeEventHandlerBean> list)
	{
		for (AttributeEventHandlerBean bean : list)
			setAttributeEventHandlerItem(bean);
	}

	public void activateHandlers(String attribute) {
		Set<AttributeEventHandlerBean> aehlBeanSet = attrToRegisteredAEHLBeanMap.get(attribute);

		if (aehlBeanSet != null) {
			log.log(Level.FINER, "Activating '" + attribute + "'....");
			
			for (AttributeEventHandlerBean bean : aehlBeanSet) {
				List<IAttributeEventHandler> list = eventNameToActiveIEventHandlerMap.get(bean.eventName);
				boolean listChanged = false;
				
				if (list == null) {
					list = new ArrayList<IAttributeEventHandler>();
					listChanged = true;
					log.log(Level.FINER, "The event '"+ bean.eventName+"' has no active handlers.");
				}
				
				if (!list.contains(bean.handler)) {
					list.add(bean.handler);
					listChanged = true;
					log.log(Level.FINER, "Activated handler for the event (" + bean.eventName + ") and attr '" + attribute + "' (type: '" + bean.handler.getClass().toString() + "')");
				}
				else
					log.log(Level.FINER, "The list of handlers for the event " + bean.eventName + " already has a handler associated with the attribute '" + attribute + "' (" + bean.handler.toString() + ")");
					
				if (listChanged)
					eventNameToActiveIEventHandlerMap.put(bean.eventName, list);
			}
		}
		else {
			log.log(Level.FINER, "No beans registered for '" + attribute + "'. Nothing to activate!");
		}
	}
	
	public List<IAttributeEventHandler> getEventHandlerList(String event) {
		List<IAttributeEventHandler> rtn = eventNameToActiveIEventHandlerMap.get(event);
		
		// We null the list so that we only return handlers which have been registered since the last use,
		//  and we don't build up a list in between events.. When an event happens, all the active listeners are called,
		//  and then cleared.
		eventNameToActiveIEventHandlerMap.put(event, null);
		
		return rtn;
	}
	
	private void addItemToMap(HashMap<String, Set<AttributeEventHandlerBean>> map, String key, AttributeEventHandlerBean item) {
		Set<AttributeEventHandlerBean> set = map.get(key);
		
		if (set == null)
			set = new HashSet<AttributeEventHandlerBean>();
		
		set.add(item);
		
		map.put(key, set);
	}
}
