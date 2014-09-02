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

// ENTITY SCOPE LEVEL.. this type file is included by a Root level file (profile.jsp, for example)

 	$(document).ready(function(){
 		populateTheTable();
 	});

function Exams_getHeadDOMElementInOriginalHeader() {
	return $("#examEntityTable > thead");
}

function Exams_getHeadDOMElementInClonedHeader() {
	return $("#exam-header-fixed");
}

$("#examContainsFilter").change(function() {
	// do nothing
});

$("#searchExamsBtn").click(function() {
	getExams();
});

$("#examTopicContainsFilter").change(function() {
	// do nothing
});

$("#examSearchTopicsBtn").click(function() {
	getExams();
});

$("#examDifficultyFilter").change(function() {
	getExams();
	
	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();
});

$("#rangeOfExamsFilter").change(function() {
	getExams();

	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();	
});

$("#idExamsApplyFilterButton").click(function() {
	getExams();
});

$("#idExamsClearFilterButton").click(function() {
	if (true) { // TODO: set a variable for when the filters have been set, so we don't just call getExams() unnecessarily.
		Exams_resetFilters();
		clearNoMoreItemsToDisplayFlag();
		getExams();
	}
});

function getExams() {
	setRowsOffsetToZero();
	Exams_cleanTable();
	displayMoreRows(setExamsButtonClickHandlersForRow); // when we are inside of profile-exams, we call displayMoreRows this way, passing the method that should be called for each row created as this execution of display more rows executes.
}

// called by smooth-scrolling.js::displayMoreRows
function Exams_thisFunctionCalledForEachRowByDisplayMoreRows(row) {
	setExamsButtonClickHandlersForRow(row);
}

function Exams_getJSONFromServerSuppliedData(parsedJSONObject) {
	return parsedJSONObject.exam;
}

function Exams_cleanTable() {
	$("#examEntityTable tbody tr:not(.filter-row)").remove();
    
    clearNoMoreItemsToDisplayFlag();    
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
	
	rtn += "<tr id=\"tableRow_" + rowNum + "\">";
	rtn += "<td></td>";
	rtn += "<td colspan=\"2\">";
	
	rtn += "<a href=\"/beginExam.jsp?examId=" + obj.id + "\">" + obj.title + "</a>";
	
	rtn += "</td><td>";

	if (topicsArr.length > 0) {
		for (var i=0; i<topicsArr.length; i++) {
			rtn += topicsArr[i].text + "<br/>";
		}
	}
	
	rtn += "</td><td>";
	
	//rtn += obj.type_text;
	//rtn += "</td><td>";
	rtn += obj.difficulty_text;
	rtn += "</td><td>";
	
	// TODO: figure out a way of populating the Vote info.. Probably put it in a JSON str, like [{"objectId":"1","votesUp":"1","votesDown":"0"}]
	//  then create a map of some sort out of it..
	rtn += ""; //" -- ";

	rtn += "</td><td>";
	
//	rtn += "<div class=\"questionButtonDiv\">";
//	rtn += "<button type=\"submit\" class=\"btn btn-secondary btn-small exam_edit_button\" id=\"edit_button_" + rowNum + "\" name=\"examButton_" + obj.id + "\" value=\"Edit Exam\"><i class=\"icon-pencil\"></i></button>";
//	rtn += "<button type=\"submit\" class=\"btn btn-secondary btn-small exam_delete_button\" id=\"delete_button_" + rowNum + "\" name=\"examButton_" + obj.id + "\" value=\"Delete Exam\"><i class=\"icon-remove\"></i></button>";
//	rtn += "</div>";
	
	rtn += "</td></tr>";
	
	return rtn;
}

//function setExamsButtonClickHandlersForRow(row) {
//	
//	if (row != undefined) {
//		row.find('.question_delete_button').click(function() {
//			var dlg = $('#dialogText').dialog(getDeleteConfirmationDialogOptions());
//	
//			// These are used in creating the object which is passed back to the server
//			//  identifying the name of the last pressed button (hint, its the delete
//			//  button that just got clicked) and its value, from which we get the 
//			//  question ID on the server side.. and then the question entity, etc, etc.
//			setLastPressedButtonName($(this), "nameOfLastPressedButton");
//			setLastPressedButtonValue($(this), "valueOfLastPressedButton");
//	
//			dlg.dialog('open');	
//			
//			return false;
//		});
//	
//		row.find('.question_edit_button').click(function() { 
//
//			// These are used in creating the object which is passed back to the server
//			//  identifying the name of the last pressed button (hint, its the 
//			//  button that just got clicked) and its value, from which we get the 
//			//  question ID on the server side.. and then the question entity, etc, etc.
//			setLastPressedButtonName($(this), "nameOfLastPressedButton");
//			setLastPressedButtonValue($(this), "valueOfLastPressedButton");
//			
//			var buttonName = $(this).attr("name");
//			var arr = buttonName.split('_');
//			var id = arr[1];
//			
//			var url="/secured/question.jsp?questionId=" + id;
//			
//			// redirect to question editing page
//			window.location.href = url;
//		});				    
//	}
//	else
//		alert("The row sent to setQuestionsButtonclickedHandlersForRow was undefined!");
//}

function Exams_setDeleteEntityDataObjectDefinition() {
	var str = '{"fields": [{"name":"nameOfLastPressedButton","id":"#nameOfLastPressedButton"},{"name":"valueOfLastPressedButton","id":"#valueOfLastPressedButton"}]}';
	
	$('#Questions-delete-entity-dataObjectDefinition').attr('value', str);
}

function Exams_postDeleteEntityMethod() {
	getQuestions();
}

function Exams_resetFilters() {
	$("#examContainsFilter").attr("value", "");
	$("#examTopicContainsFilter").attr("value", "");

	$("#rangeOfExamsFilter > option[selected='selected']").removeAttr('selected');
	$("#rangeOfExamsFilter > option[value='0']").attr('selected', 'selected');
	$("#rangeOfExamsFilter > span.filter-option").html($("#rangeOfExamsFilter > option[value='0']").html());
	$("button#rangeOfExamsFilter ~ ul > li.selected").removeClass('selected');
	$("button#rangeOfExamsFilter ~ ul > li[rel='0']").addClass('selected');

	$("#examDifficultyFilter > option[selected='selected']").removeAttr('selected');
	$("#examDifficultyFilter > option[value='0']").attr('selected', 'selected');
	$("#examDifficultyFilter > span.filter-option").html($("#difficultyFilter > option[value='0']").html());
	$("button#examDifficultyFilter ~ ul > li.selected").removeClass('selected');
	$("button#examDifficultyFilter ~ ul > li[rel='0']").addClass('selected');
}

function setExamsButtonClickHandlersForRow(row) {
	
	row.find('.exam_delete_button').click(function() {
		var dlg2 = $('#dialogText').dialog(getDeleteConfirmationDialogOptions("profileExamForm"));

		setLastPressedButtonName($(this), "exam_nameOfLastPressedButton");
		setLastPressedButtonValue($(this), "exam_valueOfLastPressedButton");

			dlg2.dialog("open");
			return false;
		});

	row.find('.exam_edit_button').click(function() { 
		setLastPressedButtonName($(this), "exam_nameOfLastPressedButton");
		setLastPressedButtonValue($(this), "exam_valueOfLastPressedButton");
		
		document.getElementById("profileExamForm").submit();
	});

	row.find('.exam_take_button').click(function () {
		setLastPressedButtonName($(this), "exam_nameOfLastPressedButton");
		setLastPressedButtonValue($(this), "exam_valueOfLastPressedButton");
		
		document.getElementById("profileExamForm").submit();
	});
		
	row.find('.exam_detail_button').click(function () {
		setLastPressedButtonName($(this), "exam_nameOfLastPressedButton");
		setLastPressedButtonValue($(this), "exam_valueOfLastPressedButton");
		
		document.getElementById("profileExamForm").submit();
	});
}
