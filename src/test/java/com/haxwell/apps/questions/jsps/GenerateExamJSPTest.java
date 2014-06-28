package com.haxwell.apps.questions.jsps;

import org.junit.Test;

import com.haxwell.apps.questions.BaseJWebUnitTest;

public class GenerateExamJSPTest extends BaseJWebUnitTest {

	@Test
	public void testGeneratingExam() {
    	beginAtHomePageTest();

    	assertAndClickOnLink("generateExamLink");
    	
    	// find animals in the listOfTopics list
    	// dblclick or whatever to get it into the other list
    	// click on the Take Generated Exam button
	}
	
}
