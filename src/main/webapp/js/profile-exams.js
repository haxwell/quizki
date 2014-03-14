// ENTITY SCOPE LEVEL.. this type file is included by a Root level file (profile.jsp, for example)

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
	
	rtn += obj.title;
	
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
	
	rtn += "<div class=\"questionButtonDiv\">";
	rtn += "<button type=\"submit\" class=\"btn btn-secondary btn-small exam_edit_button\" id=\"edit_button_" + rowNum + "\" name=\"examButton_" + obj.id + "\" value=\"Edit Exam\"><i class=\"icon-pencil\"></i></button>";
	rtn += "<button type=\"submit\" class=\"btn btn-secondary btn-small exam_delete_button\" id=\"delete_button_" + rowNum + "\" name=\"examButton_" + obj.id + "\" value=\"Delete Exam\"><i class=\"icon-remove\"></i></button>";
	rtn += "</div>";
	
	rtn += "</td></tr>";
	
	return rtn;
}

function Exams_setDeleteEntityDataObjectDefinition() {
	var str = '{"fields": [{"name":"nameOfLastPressedButton","id":"#nameOfLastPressedButton"},{"name":"valueOfLastPressedButton","id":"#valueOfLastPressedButton"}]}';
	
	$('#Exams-delete-entity-dataObjectDefinition').attr('value', str);
}

function Exams_postDeleteEntityMethod() {
	getExams();
}

function Exams_resetFilters() {
	$("#examContainsFilter").attr("value", "");
	$("#examTopicContainsFilter").attr("value", "");

	$("#examDifficultyFilter > option[selected='selected']").removeAttr('selected');
	$("#examDifficultyFilter > option[value='0']").attr('selected', 'selected');
	$("#examDifficultyFilter > span.filter-option").html($("#difficultyFilter > option[value='0']").html());
	$("button#examDifficultyFilter ~ ul > li.selected").removeClass('selected');
	$("button#examDifficultyFilter ~ ul > li[rel='0']").addClass('selected');
}

function setExamsButtonClickHandlersForRow(row) {
	
	row.find('.exam_delete_button').click(function() {
		// get the id of this row
		var buttonName = $(this).attr("name");
		var arr = buttonName.split('_');
		var id = arr[1];
		
		// pop up dialog, confirm deletion
		var dlg2 = $('#dialogText').dialog({ 
			autoOpen: false, resizable: false, modal: true,
		      buttons: [{
		        text : "No! Don't do it!", 
		        click : function() {
		        	$( this ).dialog( "close" );
		        }},
		        {
		        text : "DELETE IT!",
		        click : function() {
		        	
		    		var json = '';

		    		json += JSONUtility.startJSONString(json);

		    		json += JSONUtility.getJSON('exam_id', ''+id);
		    		
		    		json = JSONUtility.endJSONString(json);
		    		
		    		var data_obj = { data : json };

		    		// pass it to exam-delete.jsp
		        	makeAJAXCall_andWaitForTheResults('/ajax/exam-delete.jsp', data_obj, function(data, status) {
		        		// TODO: Explicitly handle success and failure cases
		        		//window.location.href = '/beginExam.jsp';					        		
		        	});

		        	$( this ).dialog( "close" );
		        	
		    		// refresh, call Exams_postDeleteEntityMethod()
		    		Exams_postDeleteEntityMethod();
		        }}]});
	
			dlg2.dialog('open');
		});

	row.find('.exam_edit_button').click(function() { 
		// just do a window.href and forward to /exam.jsp?examId = idOfThisRow
		
		var buttonName = $(this).attr("name");
		var arr = buttonName.split('_');
		var id = arr[1];
		
		var url="/secured/exam.jsp?examId=" + id;
		
		// redirect to question editing page
		window.location.href = url;

	});
}
