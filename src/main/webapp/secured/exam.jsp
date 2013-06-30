<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions" version="2.0">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<jsp:text>
		<![CDATA[ <!DOCTYPE html> ]]>
	</jsp:text>
	<!--html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">-->
	<html lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

		<title>Quizki - Create Exam</title>

		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<!-- link href="../pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" /  -->
		<!--  <link href="../css/quizki.css" rel="stylesheet" /  -->

		<link href="../pkgs/Flat-UI-master/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="../pkgs/Flat-UI-master/css/flat-ui.css" rel="stylesheet" />

		<link href="../css/quizki-sitewide.css" rel="stylesheet" />
		
		<link href="../css/quizki-buttons.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-checkbox-radio-btn.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-select-dropdowns.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-tables.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-text-input-fields.css" rel="stylesheet" type="text/css"/>
		
		<link href="../css/quizki-tables-exam.css" rel="stylesheet" type="text/css"/>
		
		<link rel="shortcut icon" href="../images/favicon.ico" />
	
	</head>

<body>
	<div class="container">
		<jsp:include page="../header.jsp"></jsp:include>
		<div class="content" style="padding:20px 0;">
			<div id="belowTheBarPageHeader" class="fillBackgroundColor"> 
				<div class="row">
					<!--form action="/secured/ExamServlet" method="post" id="titleAndSubmitButtonForm"-->
						<div class="span3">
	
							<input type="text" placeholder="Enter a title for your exam..."
								class="flat input-block-level" maxlength="128" id="id_examTitle"
								name="examTitle" value="${currentExam.title}"
								title="A name for this exam." />
						</div>
						<div class="span7">
							<input type="text"
								placeholder="Enter a message for users who will take your exam..."
								class="flat input-block-level" size="45" maxlength="255"
								id="id_examMessage" name="examMessage"
								value="${currentExam.message}"
								title="A message for folks who take this exam." />
						</div>
						<div class="span2">
							<c:choose>
								<c:when test="${empty sessionScope.inEditingMode}">
									<button class="btn btn-block pull-right" type="submit"
										name="button">Add Exam</button>
								</c:when>
								<c:otherwise>
									<button class="btn btn-block pull-right" type="submit"
										name="button">Update Exam</button>
								</c:otherwise>
							</c:choose>
						</div>
					<!--/form-->
				</div>
				<div class="row">
					<div class="span12 horizontal-rule">
						<h2>Select questions for your exam</h2>
					</div>
				</div>
				<div class="row">
					<div id="examAvailableQuestionTableHeaderJSPDiv" class="span12">
						<jsp:include page="exam-AvailableQuestions-tableHeader.jsp"></jsp:include>
					</div>
				</div>
			</div>

			<div id="examAvailableQuestionTableRowsJSPDiv" style="margin-left:0px;" class="span12">
				<jsp:include page="exam-AvailableQuestions.jsp"></jsp:include>
			</div>
		</div>		
		
	</div>
	
	<input style="display:none;" id="offset" type="text" name="offset"/>
	<input style="display:none;" id="maxEntityCountFilter" type="text" name="mcf"/>
	
	<input style="display:none;" id="Exams-view-data-url" type="text" name="exam-view-data-url" value="/getQuestions.jsp"/>
	<input style="display:none;" id="Exams-entity-table-id" type="text" name="Exams-entity-table-id" value="#examEntityTableRows"/>
	<input style="display:none;" id="prefix-to-current-view-hidden-fields" type="text" name="prefix-to-current-view-hidden-fields" value="Exams"/>
	
	<input style="display:none;" id="Exams-data-object-definition" type="text" name="Exams-data-object-definition" value=""/>
	
		<jsp:text>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/jquery-1.8.3.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/jquery-ui/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/jquery.ui.touch-punch.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/bootstrap.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/bootstrap-select.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/bootstrap-switch.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/flatui-checkbox.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/flatui-radio.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/jquery.tagsinput.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/jquery.placeholder.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/jquery.stacktable.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/quizki_custom_application.js"></script> ]]>
		</jsp:text>
		
		<jsp:text>
			<![CDATA[
				<script type="text/javascript">
					
					$(document).ready(function() {
						//setFunctionCalledForEachRowByDisplayMoreRows(manageCheckboxesOnMostRecentRow);
						setFunctionCalledForEachRowByDisplayMoreRows(addCheckboxToRow);
						
						setDataObjectDefinitions();
						//displayMoreRows(manageCheckboxesOnMostRecentRow);
						displayMoreRows(addCheckboxToRow);
					});
					
					// when the document loads, any rows that are selected, highlight them
					$(document).ready(function(){
						$('.selectQuestionChkbox').each(function() {
				 			var v = $(this).attr('checked');
				 			
				 			if (v !== undefined) {
				 				var id = $(this).attr('id');
				 				var arr = id.split('_');
				 				
				 				$('#tableRow_' + arr[1]).addClass('selectedTableRow');
				 			}
				 		});
					});
					
					$(document).ready(function(){
						$(".selectQuestionChkbox").change(function(){
							$.post("/secured/exam-questionChkboxClicked.jsp",
							{
								chkboxname: $(this).attr("name"),
								rowid: $(this).attr("id")
							},
							function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								var arr = data.split('!');
								
								var rowid = arr[0];
								var state = arr[1];
								
								if (state == 'selected') {
									//$('#tableRow_' + rowid).css('background', '#E6FFCC');
									$('#tableRow_' + rowid).addClass('selectedTableRow');
								}
								else {
									$('#tableRow_' + rowid).removeClass('selectedTableRow');
									
									//var klass = $('#tableRow_' + rowid).attr('class');
									
									//if (klass == 'rowHighlight')
										//$('#tableRow_' + rowid).css('background-color', '#F0F0EE');
									//else 
										//$('#tableRow_' + rowid).css('background-color', '#FFFFFF');
								}
							});
						});
					});					
		    
		    
		    		var divOffset = $("#belowTheBarPageHeader").offset().top;
					var $header = $("#belowTheBarPageHeader").clone();
					var $fixedHeader = $("#header-fixed").append($header);
					
					$(window).bind("scroll", function() {
					    var offset = $(this).scrollTop();
					
					    if (offset >= divOffset && $fixedHeader.is(":hidden")) {
					        $fixedHeader.show();
					    }
					    else if (offset < divOffset) {
					        $fixedHeader.hide();
					    }
					});

					$(window).scroll(function(){
				        if  ($(window).scrollTop() == $(document).height() - $(window).height()) {
					        if (smoothScrollingEnabledOnCurrentTab()) {
					           //alert("Hit the bottom!");
					           displayMoreRows(addCheckboxToRow);
					        }
				        }
					});
					
					function setDataObjectDefinitions() {
						var str = '{"fields": [{"name":"containsFilter","id":"#containsFilter"},{"name":"topicContainsFilter","id":"#topicContainsFilter"},{"name":"questionTypeFilter","id":"#questionTypeFilter"},{"name":"difficultyFilter","id":"#difficultyFilter"},{"name":"maxEntityCountFilter","id":"#maxEntityCountFilter"},{"name":"rangeOfEntitiesFilter","id":"#rangeOfQuestionsFilter"},{"name":"offsetFilter","id":"#offset"}]}';
						$('#Exams-data-object-definition').attr("value",str);
					}
					
					function addCheckboxToRow(row) {
						var $checkbox = row.find(':checkbox');
						$checkbox.checkbox();
						
//			            $checkbox.on('check uncheck toggle', function (e) {
	///				      var $this = $(this)
		//			        , check = $this.prop('checked')
			///		        , toggle = e.type == 'toggle'
				//	        , checkboxes = $('.table tbody :checkbox')
					//        , checkAll = checkboxes.length == checkboxes.filter(':checked').length;
					
					  //    $this.closest('tr')[check ? 'addClass' : 'removeClass']('selected-row');
					    //  if (toggle) $this.closest('.table').find('.toggle-all :checkbox').checkbox(checkAll ? 'check' : 'uncheck');
					    //});
					}

					function isSelectedEntityId(id) {
						var selectedIds = new Array(${sessionScope.selectedEntityIDs_AsCSV});
						var rtn = new Boolean();
						
						for (var i=0; i<selectedIds.length && rtn==false; i++) {
							rtn = (selectedIds[i] == id);
						}
						
						return rtn;
					}
					
					function setClonedHeaderInTheGlobalVariables() {
						//$header = $("#belowTheBarPageHeader").clone();
						
						//$("#header-fixed").empty();
						
						//$fixedHeader = $("#header-fixed").append($header);
						
						disableHeaderFilterFields();
					}
					
					function disableHeaderFilterFields() {
						$("#header-fixed > thead > tr > td > div > #containsFilter").attr("disabled", true);
						$("#header-fixed > thead > tr > td > div > #searchQuestionsBtn").attr("disabled", true);

						$("#header-fixed > thead > tr > td > div > #topicContainsFilter").attr("disabled", true);
						$("#header-fixed > thead > tr > td > div > #searchTopicsBtn").attr("disabled", true);

						$("#header-fixed > thead > tr > td > div > div > #difficultyFilter").attr("disabled", true);
						
						$("#header-fixed > thead > tr > td > div > div > #questionTypeFilter").attr("disabled", true);
						
						$("#header-fixed > thead > tr > td > div > #topicContainsFilter").attr("placeholder", "");
						$("#header-fixed > thead > tr > td > div > #containsFilter").attr("placeholder", "");
						
						// TODO: add missing fields
					}
					
			</script>
			]]>

			<![CDATA[ <script src="../js/smooth-scrolling.js" type="text/javascript"></script> ]]>
            <![CDATA[ <script src="../js/CreateExam.js" type="text/javascript"></script> ]]>
		</jsp:text>
		
</body>
	</html>
</jsp:root>
