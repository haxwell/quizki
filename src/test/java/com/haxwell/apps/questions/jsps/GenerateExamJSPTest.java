package com.haxwell.apps.questions.jsps;

import static net.sourceforge.jwebunit.junit.JWebUnit.assertLinkPresent;
import static net.sourceforge.jwebunit.junit.JWebUnit.assertTitleEquals;
import static net.sourceforge.jwebunit.junit.JWebUnit.beginAt;
import static net.sourceforge.jwebunit.junit.JWebUnit.clickLink;
import static net.sourceforge.jwebunit.junit.JWebUnit.setBaseUrl;

import org.junit.Before;
import org.junit.Test;

public class GenerateExamJSPTest {

	@Before
	public void prepare() {
        setBaseUrl("http://localhost:8080/");
    }
	
    // TODO: code also in quizkiTest.java
	public void beginAtHomePageTest()
    {
        beginAt("index.jsp");
        assertTitleEquals("Home Page - Quizki");
    }
	
	// TODO: code also in quizkiTest.java
	private void assertAndClickOnLink(String linkId) {
		assertLinkPresent(linkId);
    	clickLink(linkId);
	}

	@Test
	public void testGeneratingExam() {
    	beginAtHomePageTest();

    	assertAndClickOnLink("generateExamLink");
    	
    	// find animals in the listOfTopics list
    	// dblclick or whatever to get it into the other list
    	// click on the Take Generated Exam button
	}
	
}
