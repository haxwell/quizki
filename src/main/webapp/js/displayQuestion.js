// REMEMBER, these are just comments, used for reference.. the actual definition could have changed, and is in the 
//  JSP referencing this JS file. #voiceOfExperience

//		var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
//		var fieldValues = ${listOfCurrentQuestionsChoicesValuesForDisplayQuestion};
//		var selected = ${listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion};
//		var isCorrectList = ${listSayingWhichChoicesAreCorrect};
//		var examHistoryIsPresent = ${booleanExamHistoryIsPresent};
//		var userSuppliedAnswerWhenQuestionIsOfTypeString = ${userSuppliedAnswerToStringQuestion};

function displaySequenceTypeQuestionChoices(htmlExampleID)
{
	for (var counter=0;fieldNames.length>counter;counter++)
	{
		var str = $(htmlExampleID).html();
		
		var pos = counter;
		
		if (indexesBySequenceNumber !== undefined)
			pos = indexesBySequenceNumber[counter];

		str = str.replace('??SEQ', fieldSequenceNumbers[pos]);
		
		if (fieldSequenceNumbers !== undefined && fieldSequenceNumbers[pos] !== undefined && sequenceNumbersTheUserChose !== undefined && sequenceNumbersTheUserChose[pos] !== undefined)
		{
			if (fieldSequenceNumbers[pos] == sequenceNumbersTheUserChose[pos]) {
				str = str.replace('??4', 'greenText');
				str = str.replace('??1', fieldValues[pos]);				
			}
			else {
				str = str.replace('??4', 'redText');
				str = str.replace('??1', fieldValues[pos] + ' (you typed: ' + sequenceNumbersTheUserChose[pos] + ')');
			}
		}
		else
			str = str.replace('??1', fieldValues[pos]);

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

function displayStringTypeQuestionChoices(htmlExampleID)
{
	var userSuppliedTextIsCorrect = false;
	
	for (var counter=0;fieldNames.length>counter;counter++)
	{
		var str = $(htmlExampleID).html();

		str = str.replace('??1', fieldValues[counter]);
		
		if (fieldValues[counter] == userSuppliedAnswerWhenQuestionIsOfTypeString)
			userSuppliedTextIsCorrect = true;
		
		if (counter%2 == 0) {
			str = str.replace('??4', 'rowHighlight'); 
		} else {
			str = str.replace('??4', '');
		}
		
		var previous = $('div.choices').html();
		
		previous += str;
		
		$('div.choices').html(previous);
	}
	
	if (userSuppliedAnswerWhenQuestionIsOfTypeString !== undefined) {
		var previous = $('div.choices').html();
		var str = $('#youTypedExample').html();
		
		if (userSuppliedTextIsCorrect == true) {
			str = str.replace('??4', 'greenText');
		}
		else {
			str = str.replace('??4', 'redText');
		}
	
		str = str.replace('??1', userSuppliedAnswerWhenQuestionIsOfTypeString);
		
		previous += str;
		
		$('div.choices').html(previous);
	}
}

function addChoiceInputsForThisQuestionType(htmlExampleID)
{
	// find the div, and create the html to put in there, for these choices and 
	//  this question type..
	
	for (var counter=0;fieldNames.length>counter;counter++)
	{
		var str = $(htmlExampleID).html();

		var thisChoiceIsSelected = false;
		var thisChoiceIsCorrect = false;
		
		str = str.replace('??1', fieldValues[counter]);
		str = str.replace('??2', fieldNames[counter]);
		str = str.replace('??2', fieldNames[counter]);
		
		if (selected !== undefined && selected[counter] !== undefined && selected[counter] == 'true')
		{
			str = str.replace("selected=\"\"", 'checked');
			thisChoiceIsSelected = true;
		}
		
		if (isCorrectList !== undefined && isCorrectList[counter] !== undefined && isCorrectList[counter] == 'true') 
			thisChoiceIsCorrect = true;

		if (counter%2 == 0) {
			str = str.replace('??4', 'rowHighlight'); 
		} else {
			str = str.replace('??4', '');
		}

		if (thisChoiceIsSelected && thisChoiceIsCorrect) {
			str = str.replace("selected=\"\"", 'checked');								
		}
		
		if (examHistoryIsPresent == true) {									
			if (thisChoiceIsSelected == true && thisChoiceIsCorrect == true) {
				str = str.replace('??3', 'selectedAndCorrect');
				str = str.replace('??tooltip', 'You selected this choice, correctly.');
			} else if (thisChoiceIsSelected == true && thisChoiceIsCorrect == false) {
				str = str.replace('??3', 'selectedButNotCorrect');
				str = str.replace('??tooltip', 'You selected this choice, but incorrectly.');				
			} else if (thisChoiceIsSelected == false && thisChoiceIsCorrect == true) {
				str = str.replace('??3', 'correctButNotSelected');
				str = str.replace('??tooltip', 'This choice is correct, but you did not select it.');				
			} else
				str = str.replace('??tooltip', '');
		}
		else {
			if (thisChoiceIsCorrect == true) {
				str = str.replace('??3', 'greenText');
			}

			str = str.replace('??tooltip', '');			
		}
		
		var previous = $('div.choices').html();
		
		previous += str;
		
		$('div.choices').html(previous);
	}
}
