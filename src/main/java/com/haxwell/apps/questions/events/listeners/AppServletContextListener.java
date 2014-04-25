package com.haxwell.apps.questions.events.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppServletContextListener implements ServletContextListener {

	ClassPathXmlApplicationContext context;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		context = new ClassPathXmlApplicationContext("META-INF/applicationExecutorTasksContext.xml");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (context != null)
			context.close();
	}

}
