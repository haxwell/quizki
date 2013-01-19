//		var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
//		var values = ${listOfCurrentQuestionsChoicesValuesForDisplayQuestion};
//		var selected = ${listSayingAnElementIsCheckedOrNot};
//		var isCorrectList = ${listSayingWhichChoicesAreCorrect};
//		var examHistoryIsPresent = ${booleanExamHistoryIsPresent};
//		var userSuppliedAnswerWhenQuestionIsOfTypeString = ${userSuppliedAnswerToStringQuestion};

function displaySequenceTypeQuestionChoices(htmlExampleID)
{
	for (var counter=0;fieldNames.length>counter;counter++)
	{
		var str = $(htmlExampleID).html();

		str = str.replace('??SEQ', fieldSequenceNumbers[counter]);
		
		if (fieldSequenceNumbers !== undefined && fieldSequenceNumbers[counter] !== undefined && sequenceNumbersTheUserChose !== undefined && sequenceNumbersTheUserChose[counter] !== undefined)
		{
			if (fieldSequenceNumbers[counter] == sequenceNumbersTheUserChose[counter]) {
				str = str.replace('??4', 'greenText');
				str = str.replace('??1', values[counter]);				
			}
			else {
				str = str.replace('??4', 'redText');
				str = str.replace('??1', values[counter] + ' (you typed: ' + sequenceNumbersTheUserChose[counter] + ')');
			}
		}
		else
			str = str.replace('??1', values[counter]);

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

		str = str.replace('??1', values[counter]);
		
		if (values[counter] == userSuppliedAnswerWhenQuestionIsOfTypeString)
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
		
		str = str.replace('??1', values[counter]);
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

		if (thisChoiceIsCorrect) {
			str = str.replace("selected=\"\"", 'checked');								
		}
		
		if (examHistoryIsPresent == true) {									
			if (thisChoiceIsSelected == true && thisChoiceIsCorrect == true) {
				str = str.replace('??3', 'selectedAndCorrect');
			} else if (thisChoiceIsSelected == true && thisChoiceIsCorrect == false) {
				str = str.replace('??3', 'selectedButNotCorrect');
			} else if (thisChoiceIsSelected == false && thisChoiceIsCorrect == true) {
				str = str.replace('??3', 'correctButNotSelected');
			} 
		}
		else {
			if (thisChoiceIsCorrect == true) {
				str = str.replace('??3', 'greenText');
			}
		}
		
		var previous = $('div.choices').html();
		
		previous += str;
		
		$('div.choices').html(previous);
	}
}
