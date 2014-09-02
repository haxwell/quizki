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

// REMEMBER, these are just comments, used for reference.. the actual definition could have changed, and is in the 
//  JSP referencing this JS file. #voiceOfExperience

//		var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
//		var fieldValues = ${listOfCurrentQuestionsChoicesValues};
//		var selected = ${listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion};
//		var randomChoiceIndexes = ${listOfRandomChoiceIndexes}
// 		var previouslySuppliedAnswers = ${listOfPreviouslySuppliedAnswers};

	function addChoiceInputsForSequenceQuestionType()
	{
		for (var counter=0;fieldNames.length>counter;counter++)
		{
			var str = $('#sequenceExample').html();
			
			var pos = counter;
			
			if (randomChoiceIndexes !== undefined)
				pos = randomChoiceIndexes[counter];
			
			str = str.replace('??1', fieldValues[pos]);
			str = str.replace('??2', fieldNames[pos]);
			
			if (previouslySuppliedAnswers !== undefined) {
				str = str.replace('??5', previouslySuppliedAnswers[pos]);
			} else {
				str = str.replace('??5', '');
			}

			if (counter%2 == 0) {
				str = str.replace('??4', 'rowHighlight'); 
			} else {
				str = str.replace('??4', '');
			}
			
			var previous = $('div.choices').html();
			
			previous += str;
			
			$('div.choices').html(previous);
		}
	}

	function addChoiceInputsForStringQuestionType()
	{
		// find the div, and create the html to put in there
		var str = $('#textboxExample').html();
		
		if (previouslySuppliedAnswers !== undefined)
		{
			str = str.replace("??5", previouslySuppliedAnswers[0]);
		}
		else
			str = str.replace("??5", '');

		var previous = $('div.choices').html();
		
		previous += str;
		
		$('div.choices').html(previous);
	}
	
	function addChoiceInputsForThisQuestionType(var1)
	{
		// find the div, and create the html to put in there, for these choices and 
		//  this question type..
		for (var counter=0;fieldNames.length>counter;counter++)
		{
			var str = $('' + var1).html();
			
			var pos = counter;
			
			if (randomChoiceIndexes !== undefined)
				pos = randomChoiceIndexes[counter];
			
			str = str.replace("??1", fieldValues[pos]);
			str = str.replace("??2", fieldNames[pos]);
			str = str.replace("??2", fieldNames[pos]);						
			
			if (selected !== undefined && selected[pos] !== undefined && selected[pos] == 'true')
				str = str.replace("selected=\"\"", 'checked');
			
			if (counter%2 == 0) {
				str = str.replace('??4', 'rowHighlight'); 
			} else {
				str = str.replace('??4', '');
			}
	
			var previous = $('div.choices').html();
			
			previous += str;
			
			$('div.choices').html(previous);
		}
	}
	
