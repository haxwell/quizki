package com.haxwell.apps.questions.events.handlers;

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
