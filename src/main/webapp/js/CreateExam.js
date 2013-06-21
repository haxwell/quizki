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
	
	displayMoreRows(function () {
        $('[data-toggle="checkbox"]').each(function () {
            var $checkbox = $(this);
            $checkbox.checkbox();
            $checkbox.on('check uncheck toggle', function (e) {
		      var $this = $(this)
		        , check = $this.prop('checked')
		        , toggle = e.type == 'toggle'
		        , checkboxes = $('.table tbody :checkbox')
		        , checkAll = checkboxes.length == checkboxes.filter(':checked').length;
		
		      $this.closest('tr')[check ? 'addClass' : 'removeClass']('selected-row');
		      if (toggle) $this.closest('.table').find('.toggle-all :checkbox').checkbox(checkAll ? 'check' : 'uncheck');
		    });
        });
    });
}

function cleanTable() {
    $("#examEntityTable tbody tr:not(.filter-row)").remove();
}
