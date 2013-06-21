$("#searchQuestionsBtn").click(function() {
    getQuestions();
});

$("#searchTopicsBtn").click(function() {
    getQuestions();
});

$("#questionTypeFilter").change(function() {
    getQuestions();
});

$("#difficultyFilter").change(function() {
    getQuestions();
});

$("#rangeOfQuestionsFilter").change(function() {
    getQuestions();
});

function getQuestions() {
	setRowsOffsetToZero();
	cleanTable();
	displayMoreRows(manageCheckboxesOnMostRecentRow);
}

function cleanTable() {
    $("#examEntityTable tbody tr:not(.filter-row)").remove();
}
