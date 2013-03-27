package com.haxwell.apps.questions;

import org.junit.*;			

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
    
    public void testAutoExamClickLoadsBeginExamNormally()
    {
    	// assume we're already at home page
    	
    	assertLinkPresent("autoExamLink_1");
    	clickLink("autoExamLink_1");
    	
    	assertFormElementPresent("button"); // if the button is there, that means the page loaded normally
    }
    
    @Test
    public void testMainLoginLink() {
    	beginAtHomePageTest();

    	assertLinkPresent("login");
    	clickLink("login");
    	
    	assertFormElementPresent("username");
    	assertFormElementPresent("password");
    	assertFormElementPresent("button");
    	
    	setTextField("username", "johnathan");
    	setTextField("password", "password");
    	
    	submit();
    	
    	assertLinkPresent("logoutLink");
    }

    
    @Test
    public void testHeaderLoginLink() {
    	beginAtHomePageTest();

    	assertLinkPresent("login");
    	clickLink("login");
    	
    	assertFormElementPresent("username");
    	assertFormElementPresent("password");
    	assertFormElementPresent("button");
    	
    	setTextField("username", "johnathan");
    	setTextField("password", "password");
    	
    	submit();
    	
    	assertLinkPresent("logoutLink");
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
    	
    	testAutoExamClickLoadsBeginExamNormally();
    	
    	assertLinkPresent("homeLink");
    	clickLink("homeLink");
    	
    	testAutoExamClickLoadsBeginExamNormally();
    }
}
