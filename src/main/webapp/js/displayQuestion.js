function addChoiceInputsForThisQuestionType(htmlExampleID)
{
//		var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
//		var values = ${listOfCurrentQuestionsChoicesValues};
//		var selected = ${listSayingAnElementIsCheckedOrNot};
//		var isCorrectList = ${listSayingWhichChoicesAreCorrect};
//		var examHistoryIsPresent = ${booleanExamHistoryIsPresent};

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
