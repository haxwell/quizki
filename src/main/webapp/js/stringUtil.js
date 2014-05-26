//
// TODO, refactor this, such that any usage of a method from here indicates 'stringUtil' as the source.
// ie, stringUtil.getBeginningAndEnding......
//

// First, it [[analyzes]] the [[market environment]].
// First, it analyzes the market environment.
var getBeginningAndEndingTextForSetQuestion = function(fieldId, text) {

	var openingMarkerIndex = 0;
	var counter = 0;
	var b = true;
	
	if (fieldId == -1) {
		// there is no beforeTheField and afterTheField text to get
		return undefined; 
	}
	
	while (counter < fieldId && b) {
		
		openingMarkerIndex = text.indexOf('[[', openingMarkerIndex + 1);
		
		b = openingMarkerIndex > -1;
		
		if (b) {
			counter++;
		}
	}
	
	var closingMarkerIndex = -1;
	if (openingMarkerIndex > -1) {
		closingMarkerIndex = text.indexOf(']]', openingMarkerIndex);
	}
	
	var rtn = new Array();
	if (openingMarkerIndex > -1 && closingMarkerIndex > -1) {
		rtn.push(text.substring(0, openingMarkerIndex));
		rtn.push(text.substring(closingMarkerIndex + 2, text.length));
		
		rtn[0].replace('[[', '');
		rtn[1].replace(']]', '');
	}
	else {
		rtn.push(text);
	}

	return rtn;
};

var getCountOfDynamicFields = function(text) {
	var count = 0;
	var bindex = -1;
	var eindex = -1;
	
	do {
		bindex = text.indexOf('[[', bindex+1);
		
		if (bindex != -1) {
			eindex = text.indexOf(']]', bindex+1);
			
			if (eindex != -1) {
				bindex = eindex;
				count++;
			}
		}
	} while (bindex != -1);
	
	return count;
};

var getTextOfAllDynamicFields = function(text) {

	var count = getCountOfDynamicFields(text);
	var rtn = new Array();
	
	for (var i = 0; i < count; i++) {
		rtn.push(getTextOfGivenFieldForSetQuestion(i+1, text));
	}
	
	console.log("returning with the text of " + rtn.length + " fields");
	
	return rtn;
};

// TODO: Rename to getTextOfGivenDynamicField
var getTextOfGivenFieldForSetQuestion = function(fieldId, textFromWhichToExtractTheGivenField) {
	
	if (fieldId == -1) {
		return textFromWhichToExtractTheGivenField;
	}
	else {
		var openingMarkerIndex = -1;
		var counter = 0;
		var b = true;

		while (counter < fieldId && b) {
			
			openingMarkerIndex = textFromWhichToExtractTheGivenField.indexOf('[[', openingMarkerIndex + 1);
			
			b = openingMarkerIndex > -1;
			
			if (b) {
				counter++;
			}
		}
		
		var closingMarkerIndex = -1;
		if (openingMarkerIndex > -1) {
			closingMarkerIndex = textFromWhichToExtractTheGivenField.indexOf(']]', openingMarkerIndex);
		}
		
		var rtn = undefined;
		if (openingMarkerIndex > -1 && closingMarkerIndex > -1) {
			rtn = textFromWhichToExtractTheGivenField.substring(openingMarkerIndex + 2, closingMarkerIndex);
		}
		
		return rtn;
	}
};

var removeAllOccurrences = function(pattern, str) {
	
	// usage: _str = removeAllOccurrences(pattern, _str);
	
	while (str.indexOf(pattern) > -1)
		str = str.replace(pattern, '');
	
	return str;
};