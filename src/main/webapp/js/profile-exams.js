// ENTITY SCOPE LEVEL.. this type file is included by a Root level file (profile.jsp, for example)

function Exams_getHeadDOMElementInOriginalHeader() {
	return $("#TO_BE_SET_LATER > thead");
}

function Exams_getHeadDOMElementInClonedHeader() {
	return $("#header-fixed");
}

// called by smooth-scrolling.js::displayMoreRows
function Exams_thisFunctionCalledForEachRowByDisplayMoreRows(row) {
	setExamsButtonClickHandlersForRow(row);
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
