package com.haxwell.apps.questions;

import static net.sourceforge.jwebunit.junit.JWebUnit.assertLinkPresent;
import static net.sourceforge.jwebunit.junit.JWebUnit.assertTitleEquals;
import static net.sourceforge.jwebunit.junit.JWebUnit.beginAt;
import static net.sourceforge.jwebunit.junit.JWebUnit.clickLink;
import static net.sourceforge.jwebunit.junit.JWebUnit.setBaseUrl;

import org.junit.Before;


public class BaseJWebUnitTest {

	@Before
	public void prepare() {
        setBaseUrl("http://localhost:8080/");
    }
	
	public void beginAtHomePageTest()
    {
        beginAt("index.jsp");
        assertTitleEquals("Home Page - Quizki");
    }
	
	protected void assertAndClickOnLink(String linkId) {
		assertLinkPresent(linkId);
    	clickLink(linkId);
	}

}
