package com.haxwell.apps.questions.jsps;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.BaseJWebUnitTest;

@Category(com.haxwell.apps.questions.testTypes.WebsiteTest.class)
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
