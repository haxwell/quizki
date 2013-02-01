//		var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
//		var values = ${listOfCurrentQuestionsChoicesValues};
//		var selected = ${listSayingAnElementIsCheckedOrNot};
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
			
			str = str.replace('??1', values[pos]);
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
			
			str = str.replace("??1", values[pos]);
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
	
