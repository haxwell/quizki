	function addChoiceInputsForStringQuestionType()
	{
		// find the div, and create the html to put in there
		var str = $('#textboxExample').html();
		
		var previous = $('div.choices').html();
		
		previous += str;
		
		$('div.choices').html(previous);
	}
	
	
	function addChoiceInputsForThisQuestionType(var1)
	{
//		var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
//		var values = ${listOfCurrentQuestionsChoicesValues};
//		var selected = ${listSayingAnElementIsCheckedOrNot};
		
		// find the div, and create the html to put in there, for these choices and 
		//  this question type..
		
		for (var counter=0;fieldNames.length>counter;counter++)
		{
			var str = $('' + var1).html();
			
			str = str.replace("??1", values[counter]);
			str = str.replace("??2", fieldNames[counter]);
			str = str.replace("??2", fieldNames[counter]);						
			
			if (selected !== undefined && selected[counter] !== undefined && selected[counter] == 'true')
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
	
