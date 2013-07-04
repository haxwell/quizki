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

		<link href="../pkgs/font-awesome/css/font-awesome.css" rel="stylesheet"/>
		
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
			<div id="idAlertDiv" class="alert hidden">.</div>
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
									<button class="btn btn-block pull-right saveChangesBtn" type="submit"
										name="button">Add Exam</button>
								</c:when>
								<c:otherwise>
									<button class="btn btn-block pull-right saveChangesBtn" type="submit"
										name="button">Update Exam</button>
								</c:otherwise>
							</c:choose>
						</div>
					<!--/form-->
				</div>
				<div id="idAvailableQuestionsTableHeader">
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
			</div>

			<div id="examAvailableQuestionTableRowsJSPDiv" style="margin-left:0px;" class="span12">
				<jsp:include page="exam-AvailableQuestions.jsp"></jsp:include>
			</div>
		</div>		
		
	</div>
	
	<input style="display:none;" id="offset" type="text" name="offset"/>
	<input style="display:none;" id="maxEntityCountFilter" type="text" name="mcf"/>
	
	<input style="display:none;" id="prefix-to-current-view-hidden-fields" type="text" name="prefix-to-current-view-hidden-fields" value="Exams"/>
	<input style="display:none;" id="Exams-view-data-url" type="text" name="exam-view-data-url" value="/getQuestions.jsp"/>
	<input style="display:none;" id="Exams-entity-table-id" type="text" name="Exams-entity-table-id" value="#examEntityTableRows"/>
	<input style="display:none;" id="Exams-data-object-definition" type="text" name="Exams-data-object-definition" value=""/>
	<input style="display:none;" id="Exams-persist-entity-dataObjectDefinition" name="Exams-persist-entity-dataObjectDefinition" value=""/>
	<input style="display:none;" id="Exams-last-time-checkbox-handler-called" type="text" name="Exams-last-time-checkbox-handler-called" value="0"/>	
	
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
			
			<![CDATA[ <script src="../js/ajax/ajax-functions.js" type="text/javascript"></script> ]]>			
		</jsp:text>
		
		<jsp:text>
			<![CDATA[
				<script type="text/javascript">
					
					$(document).ready(function() {
						setFunctionCalledForEachRowByDisplayMoreRows(addCheckboxToRow);
						setDataObjectDefinitions();
						displayMoreRows(addCheckboxToRow);
						
						addHandlerToSaveChangesBtn();
						addHandlerToSelectAllCheckbox();
					});
					
					function addHandlerToSelectAllCheckbox() {
						$("#select-all-checkbox").change(handleSelectAllCheckboxChangeFunction);
					}
					
					function addHandlerToSaveChangesBtn() {
						$('.saveChangesBtn').click(function() {
							var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
							var url = '/ajax/exam-save.jsp';
							
							window[prefix+"_setPersistEntityDataObjectDefinition"]();
							
							var data_obj = getDataObjectForAJAX(prefix + "-persist-entity-dataObjectDefinition");

							makeAJAXCall_andWaitForTheResults(url, data_obj, function(data,status) {
								var index = data.indexOf("<!DOCTYPE");
								var jsonExport = data;
								
								if (index != -1) {
									jsonExport = data.substring(0, index);
								}
								
								var obj = undefined;
								
								try {
									obj = jQuery.parseJSON(jsonExport);
								}
								catch (err) {
									// do nothing
								}

								if (obj != undefined) {
									if (obj.examValidationWarnings != undefined) {
										populateAlertDiv(obj.examValidationWarnings, 'alert-info');
									} else if (obj.successes != undefined) {
										alert("success! The Exam was saved!");
										
										window[prefix+"_resetFilters"]();
										window[prefix+"_getEntities"]();
										
										$('#id_examTitle').attr('value', '');
										$('#id_examMessage').attr('value', '');
	
										populateAlertDiv(obj.successes, 'alert-success');
									}
									else {
										alert("errors: " + obj.examValidationErrors[0]);
										
										populateAlertDiv(obj.examValidationErrors, 'alert-error');
									}
								}
							});
						});
					}
					
					function populateAlertDiv(msgsArr, alertClassName) {
						var msgs = "";
						
						for (var i=0; i<msgsArr.length; i++) {
							msgs += msgsArr[i] + '<br/>';
						}
						
						$('#idAlertDiv').html('');
						$('#idAlertDiv').html(msgs);
						$('#idAlertDiv').addClass(alertClassName);
						$('#idAlertDiv').removeClass('hidden');
					}
					
					function hasEnoughTimePassed() {
						var now = new Date().getTime();
						var then = $('#Exams-last-time-checkbox-handler-called').attr('value');
						
						if (then == undefined || then == "")
							then = 0;
						
						return (now - then) > 250;
					}
					
					function rememberLastTimeCheckboxChangeHandlerCalled() {
						$('#Exams-last-time-checkbox-handler-called').attr('value', new Date().getTime());
					}
					
					function handleCheckboxChangeFunction() {
						if (hasEnoughTimePassed()) {
							rememberLastTimeCheckboxChangeHandlerCalled();
							
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
									$('#tableRow_' + rowid).addClass('selectedTableRow');
								}
								else {
									$('#tableRow_' + rowid).removeClass('selectedTableRow');
								}
							});
						}
					}
			
					function handleSelectAllCheckboxChangeFunction() {
						var arr = "";
						var i = 0;
						
						$(".selectQuestionChkbox").each(function() {
							arr += ($(this).attr("name") + ",");
						});
						
						var isSelectAllChecked = $("#select-all-checkbox").attr("checked") == 'checked';
						
						var data_url = "/ajax/exam-questionSelectAllClicked.jsp";
						var data_obj = { 
								chkboxnames: arr,
								isSelectAll: isSelectAllChecked
							};
							
						var returnFunction = function () {
								// for each table row, set/clear class 'selectedTableRow'
								$("#examEntityTableRows > tbody > tr").each(function() {
									if (isSelectAllChecked && !$(this).hasClass('selectedTableRow')) {
										$(this).addClass('selectedTableRow');
										$(this).find('label.checkbox').addClass('checked');									
									}
									else if (!isSelectAllChecked && $(this).hasClass('selectedTableRow')) {
										$(this).removeClass('selectedTableRow');
										$(this).find('label.checkbox').removeClass('checked');										
									}
								});
							};
								
						makeAJAXCall_andDoNotWaitForTheResults(data_url, data_obj, returnFunction);
					}
					
					var selectedEntityIDsArray = undefined;
					
					// called by smoothScrolling::displayMoreRows()
					//  obj is a JSON object based on data from the last AJAX call to getMoreRows()
					function setVarsUsedInProcessingIndividualRows(obj) {
						if (obj.selectedEntityIDsAsCSV != undefined)
							selectedEntityIDsArray = obj.selectedEntityIDsAsCSV.split(',');
					}
					
					// TODO: name this function better.. it is called for each row, and does more than add a checkbox
					function addCheckboxToRow(row) {
						var $checkbox = row.find(':checkbox');
						$checkbox.checkbox();
						$checkbox.change(handleCheckboxChangeFunction);
						
						// from the checkbox on the row, get the name attribute (selectQuestionChkbox_qId)
						// get the id from the name attribute
						
						if (selectedEntityIDsArray != undefined) {
							var name = row.find('input.selectQuestionChkbox').attr('name');
			 				var arr = name.split('_');
			 				var id = arr[1];
							
							// iterate through the array to see if the ID is in there
							//  if so, set the 'selectedTableRow' class on the row, ensure the checkbox is checked
							var found = new Boolean();
							for (var i=0; i<selectedEntityIDsArray.length && found==false; i++) {
								found = (selectedEntityIDsArray[i] == id);
							}
	
							if (found == true) {
								row.addClass('selectedTableRow');
								row.find('label.checkbox').addClass('checked');
							}
						}
					}

		    		var divOffset = $("#belowTheBarPageHeader").offset().top;
					var $header = $("#belowTheBarPageHeader").clone();
					var $fixedHeader = $("#header-fixed").append($header);
					
					$(window).bind("scroll", function() {
					    var offset = $(this).scrollTop();
					
					    if (offset >= divOffset && $fixedHeader.is(":hidden")) {
					        disableHeaderFilterFields();
					        $fixedHeader.show();
					    }
					    else if (offset < divOffset) {
					        $fixedHeader.hide();
					    }
					});

					$(window).scroll(function(){
						
						clearAlertDiv();
						
				        if  ($(window).scrollTop() == $(document).height() - $(window).height()) {
					        if (smoothScrollingEnabledOnCurrentTab()) {
					           displayMoreRows(addCheckboxToRow);
					           unselectTheSelectAllCheckbox();					           
					        }
				        }
					});
					
					function unselectTheSelectAllCheckbox() {
						$("#examEntityTableHeader > thead > tr > th > label.checkbox").removeClass('checked');
					}
					
					function setDataObjectDefinitions() {
						var str = '{"fields": [{"name":"containsFilter","id":"#containsFilter"},{"name":"topicContainsFilter","id":"#topicContainsFilter"},{"name":"questionTypeFilter","id":"#questionTypeFilter"},{"name":"difficultyFilter","id":"#difficultyFilter"},{"name":"maxEntityCountFilter","id":"#maxEntityCountFilter"},{"name":"rangeOfEntitiesFilter","id":"#rangeOfQuestionsFilter"},{"name":"offsetFilter","id":"#offset"}]}';
						$('#Exams-data-object-definition').attr("value",str);
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
						$header = $("#belowTheBarPageHeader").clone();
						
						$("#header-fixed").empty();
						
						$fixedHeader = $("#header-fixed").append($header);
						
						$fixedHeader.find("tr.filter-row").remove();
						
						//disableHeaderFilterFields();
					}
					
					function disableHeaderFilterFields() {
						var v = $("#header-fixed > div > div#idAvailableQuestionsTableHeader > div > div#examAvailableQuestionTableHeaderJSPDiv > table > thead > tr.filter-row > td"); 
						
						v.find("div.btn-group > #rangeOfQuestionsFilter").attr("disabled", true);
						
						v.find("div.input-append > #containsFilter").attr("disabled", true);
						v.find("div.input-append > #searchQuestionsBtn").attr("disabled", true);

						v.find("div.input-append > #topicContainsFilter").attr("disabled", true);
						v.find("div.input-append > #searchTopicsBtn").attr("disabled", true);
						
						v.find("div#idDifficultyFilterDiv > div.btn-group > #difficultyFilter").attr("disabled", true);
						v.find("div#idQuestionTypeFilterDiv > div.btn-group > #questionTypeFilter").attr("disabled", true);
						
						v.find("div.input-append > #containsFilter").attr("placeholder", "");
						v.find("div.input-append > #topicContainsFilter").attr("placeholder", "");						
					}
					
					function clearAlertDiv() {
						//$('#idAlertDiv').html('');
						$('#idAlertDiv').addClass('hidden');
					}
					
			</script>
			]]>

			<![CDATA[ <script src="../js/smooth-scrolling.js" type="text/javascript"></script> ]]>
            <![CDATA[ <script src="../js/CreateExam.js" type="text/javascript"></script> ]]>
		</jsp:text>
		
</body>
	</html>
</jsp:root>
