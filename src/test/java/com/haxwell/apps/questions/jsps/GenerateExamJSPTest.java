package com.haxwell.apps.questions.jsps;

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
