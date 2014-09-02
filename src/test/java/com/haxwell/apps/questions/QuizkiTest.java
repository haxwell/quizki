package com.haxwell.apps.questions;

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

import org.junit.*;		
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;

@Category(com.haxwell.apps.questions.testTypes.WebsiteTest.class)
public class QuizkiTest extends BaseJWebUnitTest {

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
     
	private void loginFromHomePage() {
		beginAtHomePageTest();
		testHeaderLoginLink();
	}
}
