package com.haxwell.apps.questions.events;

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
