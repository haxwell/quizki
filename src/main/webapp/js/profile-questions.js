// This file is a collection of methods that focus on the components in the UI. You will find methods which 
//  set event handers on specific UI components, and change the state of specific UI components, perhaps by 
//  removing a row, or changing its text. Also, not only setting the event handlers to be called at a future 
//  time, but event handlers called by a component are here.  See *_convertToHTML(). 

function Questions_getHeadDOMElementInOriginalHeader() {
	return $("#questionEntityTable > thead");
}
function Questions_getHeadDOMElementInClonedHeader() {
	return $("#header-fixed");
}

$("#containsFilter").change(function() {
	// do nothing
});

$("#searchQuestionsBtn").click(function() {
	getQuestions();
});

$("#topicContainsFilter").change(function() {
	// do nothing
});

$("#searchTopicsBtn").click(function() {
	getQuestions();
});

$("#questionTypeFilter").change(function() {
	getQuestions();
	
	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();	
});

$("#difficultyFilter").change(function() {
	getQuestions();
	
	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();
});

$("#idApplyFilterButton").click(function() {
	getQuestions();
});

$("#idClearFilterButton").click(function() {
	Questions_resetFilters();
	clearNoMoreItemsToDisplayFlag();
	getQuestions();
});

function getQuestions() {
	setRowsOffsetToZero();
	Questions_cleanTable();
	displayMoreRows(setQuestionsButtonClickHandlersForRow); // when we are inside of profile-questions, we call displayMoreRows this way, passing the method that should be called for each row created as this execution of display more rows executes.
}

// This is how 'the method that should be called for each row created as this execution of display more rows executes' from when we don't initiate the call to displayMoreRows, rather an event handled in common code fires, and needs the displayMoreRows behavior as it applies to the current tab. This is how we execute this method for each row. 
function Questions_thisFunctionCalledForEachRowByDisplayMoreRows(row) {
	setQuestionsButtonClickHandlersForRow(row);
}

function Questions_getJSONFromServerSuppliedData(parsedJSONObject) {
	return parsedJSONObject.question;
}

function Questions_cleanTable() {
	$("#questionEntityTable tbody tr:not(.filter-row)").remove();
    
    clearNoMoreItemsToDisplayFlag();    
}

function Questions_getNoItemsFoundHTMLString(string) {
	var rtn = "";
	
	rtn += '<tr class="table-status-row"></tr>';
	rtn += '<tr class="table-status-row" id="tableRow_0">';
	rtn += '<td>';
	rtn += '</td><td colspan="6">' + string + '</td>';
	rtn += '</tr>';
	
	return rtn;
}

function Questions_convertToHTMLString(obj, rowNum) {
	var topicsArr = obj.topics;
	var rtn = "";
	
	rtn += "<tr id=\"tableRow_" + rowNum + "\">";
	rtn += "<td colspan=\"2\">";
	
	if (obj.description.length > 0) {
		rtn += "<a href=\"/displayQuestion.jsp?questionId=" + obj.id + "\">" + obj.description + "</a>";
	}
	else {
		rtn += "<a href=\"/displayQuestion.jsp?questionId=" + obj.id + "\">" + obj.textWithoutHTML + "</a>";
	}
	
	rtn += "</td><td>";

	if (topicsArr.length > 0) {
		for (var i=0; i<topicsArr.length; i++) {
			rtn += topicsArr[i].text + "<br/>";
		}
	}
	
	rtn += "</td><td>";
	
	rtn += obj.type_text;
	rtn += "</td><td>";
	rtn += obj.difficulty_text;
	rtn += "</td><td>";
	
	// TODO: figure out a way of populating the Vote info.. Probably put it in a JSON str, like [{"objectId":"1","votesUp":"1","votesDown":"0"}]
	//  then create a map of some sort out of it..
	rtn += ""; //" -- ";

	rtn += "</td><td>";
	
	rtn += "<div class=\"questionButtonDiv\">";
	rtn += "<button type=\"submit\" class=\"btn btn-secondary btn-small question_edit_button\" id=\"edit_button_" + rowNum + "\" name=\"questionButton_" + obj.id + "\" value=\"Edit Question\"><i class=\"icon-pencil\"></i></button>";
	rtn += "<button type=\"submit\" class=\"btn btn-secondary btn-small question_delete_button\" id=\"delete_button_" + rowNum + "\" name=\"questionButton_" + obj.id + "\" value=\"Delete Question\"><i class=\"icon-remove\"></i></button>";
	rtn += "</div>";
	
	rtn += "</td></tr>";
	
	return rtn;
}

function setQuestionsButtonClickHandlersForRow(row) {
	
	if (row != undefined) {
		row.find('.question_delete_button').click(function() {
			var dlg = $('#dialogText').dialog(getDeleteConfirmationDialogOptions());
	
			// These are used in creating the object which is passed back to the server
			//  identifying the name of the last pressed button (hint, its the delete
			//  button that just got clicked) and its value, from which we get the 
			//  question ID on the server side.. and then the question entity, etc, etc.
			setLastPressedButtonName($(this), "nameOfLastPressedButton");
			setLastPressedButtonValue($(this), "valueOfLastPressedButton");
	
			dlg.dialog('open');	
			
			return false;
		});
	
		row.find('.question_edit_button').click(function() { 

			// These are used in creating the object which is passed back to the server
			//  identifying the name of the last pressed button (hint, its the 
			//  button that just got clicked) and its value, from which we get the 
			//  question ID on the server side.. and then the question entity, etc, etc.
			setLastPressedButtonName($(this), "nameOfLastPressedButton");
			setLastPressedButtonValue($(this), "valueOfLastPressedButton");
			
			var buttonName = $(this).attr("name");
			var arr = buttonName.split('_');
			var id = arr[1];
			
			var url="/secured/question.jsp?questionId=" + id;
			
			// redirect to question editing page
			window.location.href = url;
		});				    
	}
	else
		alert("The row sent to setQuestionsButtonclickedHandlersForRow was undefined!");
}

function Questions_setDeleteEntityDataObjectDefinition() {
	var str = '{"fields": [{"name":"nameOfLastPressedButton","id":"#nameOfLastPressedButton"},{"name":"valueOfLastPressedButton","id":"#valueOfLastPressedButton"}]}';
	
	$('#Questions-delete-entity-dataObjectDefinition').attr('value', str);
}

function Questions_postDeleteEntityMethod() {
	getQuestions();
}

function Questions_resetFilters() {
	$("#containsFilter").attr("value", "");
	$("#topicContainsFilter").attr("value", "");

	$("#questionTypeFilter > option[selected='selected']").removeAttr('selected');
	$("#questionTypeFilter > option[value='0']").attr('selected', 'selected');
	$("#questionTypeFilter > span.filter-option").html($("#questionTypeFilter > option[value='0']").html());
	$("button#questionTypeFilter ~ ul > li.selected").removeClass('selected');
	$("button#questionTypeFilter ~ ul > li[rel='0']").addClass('selected');

	$("#difficultyFilter > option[selected='selected']").removeAttr('selected');
	$("#difficultyFilter > option[value='0']").attr('selected', 'selected');
	$("#difficultyFilter > span.filter-option").html($("#difficultyFilter > option[value='0']").html());
	$("button#difficultyFilter ~ ul > li.selected").removeClass('selected');
	$("button#difficultyFilter ~ ul > li[rel='0']").addClass('selected');
}
