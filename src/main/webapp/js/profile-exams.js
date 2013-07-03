
$(document).ready(function() {
	setFunctionCalledForEachRowByDisplayMoreRows(setExamButtonClickHandlersForRow);
});

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
