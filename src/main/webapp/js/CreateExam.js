$("#containsFilter").change(function() {
	setClonedHeaderInTheGlobalVariables();
});

$("#searchQuestionsBtn").click(function() {
	setClonedHeaderInTheGlobalVariables();
	getQuestions();
});

$("#topicContainsFilter").change(function() {
	setClonedHeaderInTheGlobalVariables();
});

$("#searchTopicsBtn").click(function() {
	setClonedHeaderInTheGlobalVariables();
	getQuestions();
});

$("#questionTypeFilter").change(function() {
	setClonedHeaderInTheGlobalVariables();
	getQuestions();

	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();	
});

$("#difficultyFilter").change(function() {
	setClonedHeaderInTheGlobalVariables();
	getQuestions();

	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();	
});

$("#rangeOfQuestionsFilter").change(function() {
	setClonedHeaderInTheGlobalVariables();
	getQuestions();

	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();	
});

function getQuestions() {
	setRowsOffsetToZero();
	cleanTable();
	displayMoreRows(manageCheckboxesOnMostRecentRow);
}

function cleanTable() {
    $("#examEntityTable tbody tr:not(.filter-row)").remove();
}

function Exams_getNoItemsFoundHTMLString(string) {
	var rtn = "";
	
	rtn += '<tr class="table-status-row"></tr>';
	rtn += '<tr class="table-status-row" id="tableRow_0">';
	rtn += '<td>';
	rtn += '</td><td colspan="6">' + string + '</td>';
	rtn += '</tr>';
	
	return rtn;
}

function Exams_convertToHTMLString(obj, rowNum) {
	var topicsArr = obj.topics;
	
	var rtn = "";
	
	rtn += '<tr id="tableRow_' + rowNum + '">';
	rtn += '<td>';
	
	if (isSelectedEntityId(obj.id) == true) {
		// Checked
		rtn += '<label class="checkbox no-label checked" for="chkbox_' + rowNum + '">';
		rtn += ' <input type="checkbox" class="selectQuestionChkbox" value="" data-toggle="checkbox" id="chkbox_' + rowNum + '"';
		rtn += ' name="selectQuestionChkbox_' + obj.id + '" />';
		rtn += '</label>';
	}
	else {
		// Not checked
		rtn += '<label class="checkbox no-label" for="chkbox_' + rowNum + '">';
		rtn += ' <input type="checkbox" class="selectQuestionChkbox" value="" data-toggle="checkbox" id="chkbox_' + rowNum + '"';
		rtn += ' name="selectQuestionChkbox_' + obj.id + '" />';
		rtn += '</label>';
	}
	
	rtn += '</td><td class="examTableQuestionColumn">';
	
	if (obj.description.length > 0) {
		rtn += '<a href="/displayQuestion.jsp?questionId=' + obj.id + '">' + obj.description + '</a>';
	}
	else {
		rtn += '<a href="/displayQuestion.jsp?questionId=' + obj.id + '">' + obj.textWithoutHTML + '</a>';
	}
	
	rtn += '</td><td class="examTableTopicsColumn">';
	
	if (topicsArr.length > 0) {
		for (var i=0; i<topicsArr.length; i++) {
			rtn += topicsArr[i].text + '<br/>';
		}
	}
	
	rtn += '</td><td>';
	
	rtn += obj.type_text;
	rtn += '</td><td>';
	rtn += obj.difficulty_text;
	rtn += '</td><td>';
	
	// TODO: figure out a way of populating the Vote info.. Probably put it in a JSON str, like [{"objectId":"1","votesUp":"1","votesDown":"0"}]
	//  then create a map of some sort out of it..
	rtn += ' -- ';
	rtn += '</td><td>';
	
	return rtn;
}
