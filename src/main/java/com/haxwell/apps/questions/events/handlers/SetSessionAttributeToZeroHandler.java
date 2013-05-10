package com.haxwell.apps.questions.events.handlers;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class SetSessionAttributeToZeroHandler implements IAttributeEventHandler {
	
	Logger log = Logger.getLogger(SetSessionAttributeToZeroHandler.class.getName());
	
	String attr;
	
	@Override
	public void execute(HttpServletRequest req) {
		req.getSession().setAttribute(attr, BigInteger.ZERO);
		
		if (req.getSession().getAttribute(attr) == null)
			log.log(Level.FINER, ":: attr (" + attr + ") was set to null in the session.");
		else
			log.log(Level.FINER, ":: attr (" + attr + ") was NOT(!!) set to null in the session.");
	}

	@Override
	public void setAttribute(String attr) {
		this.attr = attr;
	}
	
	@Override
	public int hashCode() {
		return attr != null ? attr.hashCode() : -1;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SetSessionAttributeToZeroHandler))
			return false;
		
		return this.attr != null ? this.attr.equals(((SetSessionAttributeToZeroHandler)o).attr) : false;
	}
	
	public String toString() {
		return "attr: " + this.attr;
	}
}
