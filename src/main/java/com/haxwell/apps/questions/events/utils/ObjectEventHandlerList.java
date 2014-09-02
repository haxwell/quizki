package com.haxwell.apps.questions.events.utils;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

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
