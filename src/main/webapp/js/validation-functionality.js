
function validate_Single (messages) {
	var arr = getChoiceIds();
	var trueChoiceCount = 0;
	
	for (int i=0;i<arr.length;i++) {
		(getChoiceObjectForChoiceId(arr[i]).iscorrect) ? trueChoiceCount++ : null; 
	}
	
	if (trueChoice == 0) {
		// I could see this being a javascript module with internally a method to add a string,
		//  and a method to get an array of all the strings added. it would be passed around to
		//  these validators to build up the list of strings, instead of doing CSVs like this.
		//	Maybe CSVs are fine...
		messages = messages + ",There needs to be at least one question set to correct.";
	}
	
	if (trueChoice > 1) {
		messages = mesages + ",There can only be one correct choice with this question type.";
	}
	
	return messages; 
}

function validate_Multiple (messages) {
	var arr = getChoiceIds();
	
}