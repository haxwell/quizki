package com.haxwell.apps.questions;

import org.junit.*;	
import static org.junit.Assert.assertEquals;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class QuizkiTest {

    @Before
    public void prepare() {
        setBaseUrl("http://localhost:8080/");
    }

    public void beginAtHomePageTest()
    {
        beginAt("index.jsp");
        assertTitleEquals("Home Page - Quizki");
    }
    
    public void testBeginExamStartsNormallyFromAutoExam0LinkClick()
    {
    	// assume we're already at home page
    	
    	assertAndClickOnLink("autoExamLink_0");
    	assertTitleEquals("Begin Exam - Quizki");
    	
    	assertFormElementPresent("button"); // if the button is there, that means the page loaded normally
    }
    
    @Test
    public void testHeaderLoginLink() {
    	beginAtHomePageTest();

    	assertAndClickOnLink("headerLoginLink");
    	
    	assertFormElementPresent("username");
    	assertFormElementPresent("password");
    	assertFormElementPresent("button");
    	
    	assertEquals(getElementAttributeByXPath("//button[@type='submit']", "value"), "Log In");
    	
    	setTextField("username", "johnathan");
    	setTextField("password", "password");
    	
    	clickButton("loginButton");
    	
    	assertLinkPresent("headerLogoutLink");
    }

    /**
     * From the home page, 
     * 1. clicks on one of the Auto Practice Exam links
     * 2. clicks on Home
     * 3. clicks on the same Auto Practice Exam link
     * 
     * Reason: User should be able to go to BeginExam, change their mind, and go back to it with no issues.
     */
    @Test
    public void testAutoExamFromHomePage_A() {
    	beginAtHomePageTest();
    	
    	testBeginExamStartsNormallyFromAutoExam0LinkClick();
    	
    	assertAndClickOnLink("quizkiHomePageLink");
    	
    	testBeginExamStartsNormallyFromAutoExam0LinkClick();
    }

    /**
     * From the home page,
     * 1. Click on an Auto Practice Exam link,
     * 2. Click Home
     * 3. Click on link to create exam
     * 4. Assert nothing is set in the title (title would be set if an exam was set in CurrentExam)
     * 
     * Reason: Anytime you go to Create Exam from the home page, no previous exam should be in the session
     */
    @Test
    public void testNoStaleExamWhenCreateExamIsCalled() {
    	loginFromHomePage();
    	
    	testBeginExamStartsNormallyFromAutoExam0LinkClick();

    	assertAndClickOnLink("quizkiHomePageLink");
    	assertAndClickOnLink("createExamLink");
    	
    	assertTextInElement("id_examTitle", "");
    }
    
    /**
     * From the home page,
     * 1. Click on an Auto Practice Exam link,
     * 2. Click Home
     * 3. Click on link to create question
     * 4. Assert nothing is set in the title (title would be set if a question was set in CurrentQuestion)
     * 
     * Reason: Anytime you go to Create Question from the home page, no previous question should be in the session
     */
    @Test
    public void testNoStaleQuestionWhenCreateQuestionIsCalled() {
    	loginFromHomePage();
    	
    	testBeginExamStartsNormallyFromAutoExam0LinkClick();

    	assertAndClickOnLink("quizkiHomePageLink");
    	assertAndClickOnLink("createQuestionLink");
    	
    	assertTextInElement("id_questionText", "");
    	assertTextInElement("id_questionDescription", "");
    }
     
	private void assertAndClickOnLink(String linkId) {
		assertLinkPresent(linkId);
    	clickLink(linkId);
	}
	
	private void loginFromHomePage() {
		beginAtHomePageTest();
		testHeaderLoginLink();
	}
}
