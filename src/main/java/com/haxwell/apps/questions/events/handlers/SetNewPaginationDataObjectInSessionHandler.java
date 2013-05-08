package com.haxwell.apps.questions.events.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.utils.PaginationData;

public class SetNewPaginationDataObjectInSessionHandler implements IAttributeEventHandler {
	
	Logger log = Logger.getLogger(SetNewPaginationDataObjectInSessionHandler.class.getName());
	
	String attr;
	
	@Override
	public void execute(HttpServletRequest req) {
		req.getSession().setAttribute(attr, new PaginationData());
		
		if (req.getSession().getAttribute(attr) != null)
			log.log(Level.INFO, ":: attr (" + attr + ") was set with a new PaginationData object in the session.");
		else
			log.log(Level.INFO, ":: attr (" + attr + ") was NOT(!!) set with a new PaginationData object in the session.");
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
		if (!(o instanceof SetNewPaginationDataObjectInSessionHandler))
			return false;
		
		return this.attr != null ? this.attr.equals(((SetNewPaginationDataObjectInSessionHandler)o).attr) : false;
	}
	
	public String toString() {
		return "attr: " + this.attr;
	}
}
