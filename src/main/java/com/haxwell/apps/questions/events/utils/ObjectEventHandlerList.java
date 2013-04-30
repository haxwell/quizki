package com.haxwell.apps.questions.events.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.events.handlers.IEventHandler;
import com.haxwell.apps.questions.events.handlers.IObjectEventHandler;

public class ObjectEventHandlerList {

	HashMap<String, List<ObjectEventHandlerBean>> /*event name to list of handlers*/ eventNameToListOfObjectEventHandlersMap = new HashMap<String, List<ObjectEventHandlerBean>>();
//	HashMap<String, List<IObjectEventHandler>> /*event to list of handlers*/ eventNameToActiveIEventHandlerMap = new HashMap<String, List<IObjectEventHandler>>();

	Logger log = Logger.getLogger(ObjectEventHandlerList.class.getName());
	
	public ObjectEventHandlerList() { }

	public void setObjectEventHandlerItem(ObjectEventHandlerBean item) {
		List<ObjectEventHandlerBean> list = eventNameToListOfObjectEventHandlersMap.get(item.event);
		
		if (list == null)
			list = new ArrayList<ObjectEventHandlerBean>();
		
		list.add(item);
		
		eventNameToListOfObjectEventHandlersMap.put(item.event, list);

		log.log(Level.INFO, "Added: " + item.toString());
	}
	
	public void setObjectEventHandlerList(List<ObjectEventHandlerBean> list)
	{
		for (ObjectEventHandlerBean bean : list)
			setObjectEventHandlerItem(bean);
	}

//	public void activateHandlers(String attribute) {
//		List<ObjectEventHandlerBean> aehlBeanlist = eventNameToListOfObjectEventHandlersMap.get(attribute);
//
//		if (aehlBeanlist != null) {
//			log.log(Level.INFO, "Activating '" + attribute + "'....");
//			
//			for (ObjectEventHandlerBean bean : aehlBeanlist) {
//				List<IEventHandler> list = eventNameToActiveIEventHandlerMap.get(bean.event);
//				boolean listChanged = false;
//				
//				if (list == null) {
//					list = new ArrayList<IEventHandler>();
//					listChanged = true;
//					log.log(Level.INFO, "The event '"+ bean.event+"' has no active handlers.");
//				}
//				
//				if (!list.contains(bean.handler)) {
//					list.add(bean.handler);
//					listChanged = true;
//					log.log(Level.INFO, "Added handler for the event (" + bean.event + ") and attr '" + attribute + "' (type: '" + bean.handler.getClass().toString() + "')");
//				}
//				else
//					log.log(Level.INFO, "The list of handlers for the event " + bean.event + " already has a handler associated with the attribute '" + attribute + "' (" + bean.handler.toString() + ")");
//					
//				if (listChanged)
//					eventNameToActiveIEventHandlerMap.put(bean.event, list);
//			}
//		}
//		else {
//			log.log(Level.INFO, "No beans registered for '" + attribute + "'. Nothing to activate!");
//		}
//	}
	
	public List<IObjectEventHandler> getEventHandlerList(String event) {
		List<ObjectEventHandlerBean> list = eventNameToListOfObjectEventHandlersMap.get(event);
		
		List<IObjectEventHandler> rtn = new ArrayList<IObjectEventHandler>();
		
		for (ObjectEventHandlerBean bean : list)
			rtn.add(bean.handler);
		
		return rtn;
	}
}
