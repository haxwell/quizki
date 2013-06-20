$("#getQuestionsBtn").click(function() {
    $.post("/getQuestions.jsp", {
        containsFilter : $("#containsFilter").attr("value"),
        topicContainsFilter : "",
        questionTypeFilter : "0",
        difficultyFilter : "4",
        //authorFilter : "",
        //maxQuestionCountFilter : "3",
        maxEntityCountFilter : "0",
        rangeOfEntitiesFilter : "0",
        offsetFilter : "0"
    }, function(data, status) {

        if (status == 'success') {
            try {
                var obj = $.parseJSON(data);
                $.each(obj, function() {
                    var questions = $(this);
                    var tbl_row = "";
                    var counter = 0;

                    $.each(questions, function(k, v) {
                        counter++;
                        tbl_row = '';
                        
                        if(isSelectedEntityId(parseInt(v["id"]))) {
                            tbl_row +=  '<td>' + '<label class="checkbox no-label checked" for="chkbox_$' + counter + '">'
                            + '<input type="checkbox" value="" data-toggle="checkbox" id="chkbox_$'
                            + counter
                            + '" name="selectQuestionChkbox_$' + v["id"] + '" /></label>'
                            + '</td>';
                            
                        }
                        else {
                            tbl_row +=  '<td>' + '<label class="checkbox no-label" for="chkbox_$' + counter + '">'
                            + '<input type="checkbox" value="" data-toggle="checkbox" id="chkbox_$'
                            + counter
                            + '" name="selectQuestionChkbox_$' + v["id"] + '" /></label>'
                            + '</td>';
                        }

                        tbl_row += '<td>' + (v["description"] == "" ? v["textWithoutHTML"] : v["description"]) + '</td>';
                        
                        tbl_row += '<td>';
                        $.each(v["topics"], function(index, topic){
                            tbl_row += topic["text"] + '<br />';
                        });
                        tbl_row += '</td>';

                        tbl_row += '<td>Johnathan</td>'; //hardcoded author as placeholder till we have the correct author
                        tbl_row += '<td>' + v["type_text"] + '</td>';
                        tbl_row += '<td>' + v["difficulty_text"] + '</td>';

                        $('#entityTable').find('tbody:last').append("<tr>" + tbl_row + "</tr>");
                    });
                    
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
            catch (err) {
                alert("invalid json");
            }
        }
    });
});