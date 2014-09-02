package com.haxwell.apps.questions.events;

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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.mock.web.MockHttpServletRequest;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class EventDispatcherTest {

	@Test
	public void testGetInstance() {
		EventDispatcher ed = EventDispatcher.getInstance();
		
		assertTrue(ed == EventDispatcher.getInstance());
	}
	
	@Test
	public void testFireEvent() {
//	TODO: understand mocking better.. need to mock the HttpServletRequest, which returns a context, 
		//  from which Spring gets the beans which manage the events.. Lots to be done there, but
		//  there is lower hanging fruit at the moment.
		
//		EventDispatcher ed = EventDispatcher.getInstance();
//		
//		MockHttpServletRequest req = new MockHttpServletRequest();
//		ed.fireEvent(req, "indexPage");
	}
}
