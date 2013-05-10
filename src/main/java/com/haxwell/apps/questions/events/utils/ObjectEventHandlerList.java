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

	Logger log = Logger.getLogger(ObjectEventHandlerList.class.getName());
	
	public ObjectEventHandlerList() { }

	public void setObjectEventHandlerItem(ObjectEventHandlerBean item) {
		List<ObjectEventHandlerBean> list = eventNameToListOfObjectEventHandlersMap.get(item.event);
		
		if (list == null)
			list = new ArrayList<ObjectEventHandlerBean>();
		
		list.add(item);
		
		eventNameToListOfObjectEventHandlersMap.put(item.event, list);

		log.log(Level.FINER, "Added: " + item.toString());
	}
	
	public void setObjectEventHandlerList(List<ObjectEventHandlerBean> list)
	{
		for (ObjectEventHandlerBean bean : list)
			setObjectEventHandlerItem(bean);
	}

	public List<IObjectEventHandler> getEventHandlerList(String event) {
		List<ObjectEventHandlerBean> list = eventNameToListOfObjectEventHandlersMap.get(event);
		
		List<IObjectEventHandler> rtn = new ArrayList<IObjectEventHandler>();
		
		for (ObjectEventHandlerBean bean : list)
			rtn.add(bean.handler);
		
		return rtn;
	}
}
