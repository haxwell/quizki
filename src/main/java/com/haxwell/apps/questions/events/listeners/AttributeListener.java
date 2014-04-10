package com.haxwell.apps.questions.events.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.haxwell.apps.questions.events.utils.AttributeEventHandlerList;
import com.haxwell.apps.questions.events.utils.DynamicAttributeSessionManager;

public class AttributeListener implements HttpSessionAttributeListener {

	Logger log = Logger.getLogger(AttributeListener.class.getName());
	
	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
        HttpSession session = arg0.getSession();
        
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        AttributeEventHandlerList aehl = (AttributeEventHandlerList)ctx.getBean("attributeEventHandlerList");
        
        log.log(Level.FINER, "AttributeListener: '" + arg0.getName() + "' was set with the value '" + arg0.getValue() + "'");

        aehl.activateHandlers(arg0.getName());
        
        // For attributes like '89_dynamification', we need to store them in a list, and then store the attribute itself.
        // This is so that the event listeners can quickly find all that are in the session to be removed, and remove them.
        
        // a dynamifiedAttributeListKeeper.. takes the attribute name, compares it with a list of beans that have an ends-with
        //  attribute. If they match that bean has a handler which is called, which sets the attribute in the session, and in a list.
        DynamicAttributeSessionManager.handleAttributeSet(arg0);

        // Then when the event is thrown, the AEHL should activate a handler which will get the list from the session, remove each
        //  attribute with that name from the list, and remove the list.
        
        
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {	}
}
