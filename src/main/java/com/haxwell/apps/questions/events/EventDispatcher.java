package com.haxwell.apps.questions.events;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.haxwell.apps.questions.events.handlers.IEventHandler;
import com.haxwell.apps.questions.events.utils.AttributeEventHandlerList;

/**
 * HOW EVENTS WORK IN QUIZKI:
 * 	We discover something that needs to be done, when something else happens. So, we define a handler to do that thing.
 * 	The handler is a type of IEventHandler. Define the handler as a bean in Spring. That handler is associated with an
 * 	actual event in another bean. This bean, is an AttributeEventHandlerBean, and it is also defined in Spring. The 
 * 	something that needs to be done, at press time, is resetting an attribute in the session to null, once an event 
 * 	happens. So, its been designed for that case. It should likely be expanded for other cases. But anyway, here we go.
 * 
 *  On AttributeEventHandlerBean, the means of associating an event with its handler,
 * 
 * 	We set
 * 		- an Attribute (attr), the key, which says execute this handler, when this event happens, for this attribute.
 * 		- an Event, a key defined in EventConstants, to identify a unique event
 * 		- a Handler, an IEventHandler object which is called when the associated event is fired.
 * 
 *   This AttributeEventHandlerBean is set on the AttributeEventHandlerList.
 *   
 *   The AEHL sets the AEHBean in a map with the key as the attribute, and the bean itself as the value. The beans are
 *   only collected here, but if the event were to happen, no handlers would be called. To make the handlers active,
 *   an HttpSessionAttributeListener is used. This listener catches each setting of an attributer on the session. It
 *   notices the Attribute being set, and calls the AEHL to say, activate the events associated with this attribute.
 *   At that point, the AEHL gets the list of handlers associated with attribute, and adds each of those handlers to
 *   a list of handlers associated with the event name.
 *   
 *   When the event happens, the EventDispatcher is called on to tell all the interested parties, "Hey, this event just
 *   happened". It does this via the fireEvent() method. FireEvent() calls the AEHL, and gets the list of handlers
 *   associated with the event. It then executes each handler in the list. At this point, AEHL no longer has handlers 
 *   associated with that event. That attribute has to be set in the session again in order to have the handlers made 
 *   active for that event again.
 * 
 * @author johnathanj
 *
 */
public class EventDispatcher {

	Logger log = Logger.getLogger(EventDispatcher.class.getName());
	
	protected static EventDispatcher instance; /* TODO: Replace this with the cool enum way of doing Singletons */
	
	protected EventDispatcher() { }
	
	public static EventDispatcher getInstance() {
		if (instance == null)
			instance = new EventDispatcher();
		
		return instance;
	}

	public void fireEvent(HttpServletRequest req, String eventName) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
		AttributeEventHandlerList aehl = (AttributeEventHandlerList)ctx.getBean("attributeEventHandlerList");
		
		List<IEventHandler> list = aehl.getEventHandlerList(eventName);
		
		log.log(Level.INFO, "EventDisptacher: called to fire the event (" + eventName + ")");
		
		if (list != null) {
			for (IEventHandler handler : list) {
				
				log.log(Level.INFO, "EventDispatcher: calling the handler (" + handler.toString() + ")");
				
				handler.execute(req);
			}
		}
		else
			log.log(Level.INFO, "No event handlers found associated with '" + eventName + "'");
	}
	
}
