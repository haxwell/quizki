package com.haxwell.apps.questions.events.runnables;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ReloadLoggingPropertiesRunnable implements Runnable {

	Logger log = Logger.getLogger(ReloadLoggingPropertiesRunnable.class.getName());
	
	@Override
	public void run() {
		try {
			LogManager.getLogManager().readConfiguration();
			
			log.log(Level.FINEST, "Reloaded logging configuration.");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
}
