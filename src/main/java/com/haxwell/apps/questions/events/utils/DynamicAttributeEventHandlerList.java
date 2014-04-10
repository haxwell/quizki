package com.haxwell.apps.questions.events.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.events.handlers.IDynamicAttributeEventHandler;

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
