package com.haxwell.apps.questions.events.listeners;

import java.util.logging.Level;	
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.haxwell.apps.questions.events.utils.AttributeEventHandlerList;

public class AttributeListener implements HttpSessionAttributeListener {

	Logger log = Logger.getLogger(AttributeListener.class.getName());
	
	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
        HttpSession session = arg0.getSession();
        
        //TODO: instead of accessing the context, may want to let EventDispatcher or another class have primary use of AEHL
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        AttributeEventHandlerList aehl = (AttributeEventHandlerList)ctx.getBean("attributeEventHandlerList");
        
        log.log(Level.INFO, "AttributeListener: '" + arg0.getName() + "' was set with the value '" + arg0.getValue() + "'");

        aehl.activateHandlers(arg0.getName());
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub

	}

}
