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
    
    public void testBeginExamStartsNormallyFromAutoExam0LinkClick()
    {
    	// assume we're already at home page
    	
    	assertAndClickOnLink("autoExamLink_0");
    	
    	assertFormElementPresent("button"); // if the button is there, that means the page loaded normally
    }
    
    @Test
    public void testMainLoginLink() {
    	beginAtHomePageTest();

    	assertAndClickOnLink("login");
    	
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

    	assertAndClickOnLink("headerLoginLink");
    	
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
    	
    	testBeginExamStartsNormallyFromAutoExam0LinkClick();
    	
    	assertAndClickOnLink("homeLink");
    	
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

    	assertAndClickOnLink("homeLink");
    	assertAndClickOnLink("createExamLink");
    	
    	assertTextInElement("id_examTitle", "");
    }
    
	private void assertAndClickOnLink(String linkId) {
		assertLinkPresent(linkId);
    	clickLink(linkId);
	}
	
	private void loginFromHomePage() {
		beginAtHomePageTest();
		testMainLoginLink();
	}
    
}
