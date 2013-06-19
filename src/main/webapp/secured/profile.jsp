<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<title>Profile - Quizki</title>

		<link href="../bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="../css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki.css" rel="stylesheet" type="text/css"/>
		<link href="../css/questions.css" rel="stylesheet" type="text/css"/>
		<link href="../css/styles.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" href="../images/favicon.ico" />

		<jsp:text>
			<![CDATA[ <script src="../js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/bootstrap.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/profile-questions.js" type="text/javascript"></script> ]]>

			<![CDATA[<script type="text/javascript">]]>
			<c:choose>
				<!-- In case you ever want to change this, and use javascript to get the tabIndex, -->
				<!-- http://stackoverflow.com/questions/1403888/get-url-parameter-with-jquery -->
				
				<c:when test="${not empty sessionScope.tabIndex}">
					<![CDATA[ var tabIndex = ${tabIndex}; ]]>
				</c:when>
				<c:otherwise>
					<![CDATA[ var tabIndex = undefined; ]]>
				</c:otherwise>
			</c:choose>
			<![CDATA[</script>]]>

			<![CDATA[
			<script type="text/javascript">
			
				    $( "#open-event" ).tooltip({
				      show: null,
				      position: {
				        my: "left top",
				        at: "left bottom"
				      },
				      open: function( event, ui ) {
				        ui.tooltip.animate({ top: ui.tooltip.position().top + 10 }, "slow" );
				      }
				    });
				

					function getDeleteConfirmationDialogOptions(profileFormName) {
						var options = { 
								autoOpen: false, resizable: false, modal: true,
							      buttons: {
							        "Delete this item": function() {
							          document.getElementById(profileFormName).submit();
							        },
							        Cancel: function() {
							          $( this ).dialog( "close" );
							        }
							      }
						};
						
						return options;
					}

					function setLastPressedButtonName(obj, fieldId) {
						var buttonName = obj.attr("name");
						$('#' + fieldId).attr("value", buttonName);
					}

					function setLastPressedButtonValue(obj, fieldId) {
						var buttonValue = obj.attr("value");
						$('#' + fieldId).attr("value", buttonValue);
					}

					$(document).ready(function() { 
						var num = 1;
	
			            $('.questionButtonDiv').each(function() {
	
			              		$('#delete_button_' + num).click(function() {
									var dlg = $('#dialogText').dialog(getDeleteConfirmationDialogOptions("profileQuestionForm"));

									setLastPressedButtonName($(this), "nameOfLastPressedButton");
									setLastPressedButtonValue($(this), "valueOfLastPressedButton");
		
			              			dlg.dialog("open");
			              			return false;
			              		});
			              		
								$('#edit_button_' + num).click(function() { 
									setLastPressedButtonName($(this), "nameOfLastPressedButton");
									setLastPressedButtonValue($(this), "valueOfLastPressedButton");
									
									document.getElementById("profileQuestionForm").submit();
								});				    
			              		
			              		num = num + 1;
			              });
	
						num = 1;
			              
			          	$('.examButtonDiv').each(function() {
			              		$('#exam_delete_button_' + num).click(function() {
									var dlg2 = $('#dialogText').dialog(getDeleteConfirmationDialogOptions("profileExamForm"));

									setLastPressedButtonName($(this), "exam_nameOfLastPressedButton");
									setLastPressedButtonValue($(this), "exam_valueOfLastPressedButton");
	
			              			dlg2.dialog("open");
			              			return false;
			              		});
			              		
								$('#exam_edit_button_' + num).click(function() { 
									setLastPressedButtonName($(this), "exam_nameOfLastPressedButton");
									setLastPressedButtonValue($(this), "exam_valueOfLastPressedButton");
									
									document.getElementById("profileExamForm").submit();
								});
								
								$('#exam_take_button_' + num).click(function () {
									setLastPressedButtonName($(this), "exam_nameOfLastPressedButton");
									setLastPressedButtonValue($(this), "exam_valueOfLastPressedButton");
									
									document.getElementById("profileExamForm").submit();
								});
			              		
								$('#exam_detail_button_' + num).click(function () {
									setLastPressedButtonName($(this), "exam_nameOfLastPressedButton");
									setLastPressedButtonValue($(this), "exam_valueOfLastPressedButton");
									
									document.getElementById("profileExamForm").submit();
								});
			              		
			              		num = num + 1;
			          	});
		              });

		     </script>
				]]>			

		</jsp:text>
				
	</head>
<body>

	<div class="container">
		<jsp:include page="../header.jsp"></jsp:include>
		<div class="content">

	<br/>

      <c:choose>
      <c:when test="${empty sessionScope.currentUserEntity}">
      	<br/><br/>
		Oops! Something went wrong! You should <a href="/index.jsp">go back to the beginning</a>.
      </c:when>
      <c:otherwise>

	<div class="tabbable">
	  <ul class="nav nav-tabs" id="tabsUl">
	    <li class="active"><a href="#tabs-1" data-toggle="tab">Summary</a></li>
	    <li><a href="#tabs-2" data-toggle="tab">Questions</a></li>
	    <li><a href="#tabs-3" data-toggle="tab">Exams</a></li>
	    <li><a href="#tabs-4" data-toggle="tab">Account Status</a></li>
	  </ul>
	  <div class="tab-content">
		  <div class="tab-pane active" id="tabs-1">
		    <jsp:include page="profile-summary.jsp"></jsp:include>
		  </div>
		  <div class="tab-pane" id="tabs-2">
    		<jsp:include page="profile-questions.jsp"></jsp:include>
		  </div>
		  <div class="tab-pane" id="tabs-3">
		    	<div id="exams" class="listOfQuestions" style="overflow:auto;"><jsp:include page="profile-exams.jsp"></jsp:include></div>
		    	<br/>
		  </div>
		  <div class="tab-pane" id	="tabs-4">
		    	<div id="account" class="listOfQuestions" style="overflow:auto;"><jsp:include page="profile-account.jsp"></jsp:include></div>
		  </div>
	  </div>
	</div>
	
	</c:otherwise>
	</c:choose>
	
	<br/>
	
	<div class="hidden" id="dialogText">Are you SURE you want to delete?</div>  

	<input style="display:none;" id="field_true" type="text" name="field_true" value="true"/>
	<input style="display:none;" id="field_false" type="text" name="field_false" value="false"/>
	
	<input style="display:none;" id="offset" type="text" name="offset"/>
	<input style="display:none;" id="maxEntityCountFilter" type="text" name="mcf"/>
	
	<input style="display:none;" id="Questions-tab-data-url" type="text" name="question-tab-data-url" value="/getQuestions.jsp"/>
	<input style="display:none;" id="Questions-entity-table-id" type="text" name="Questions-entity-table-id" value="#questionEntityTable"/>
	<input style="display:none;" id="Exams-tab-data-url" type="text" name="exam-tab-data-url" value="/getExams.jsp"/>
	<input style="display:none;" id="Exams-entity-table-id" type="text" name="Exams-entity-table-id" value="#examEntityTable"/>
	<input style="display:none;" id="prefix-to-current-tab-hidden-fields" type="text" name="prefix-to-current-tab-hidden-fields" value=""/>
	
	<input style="display:none;" id="Questions-data-object-definition" type="text" name="Questions-data-object-definition" value=""/>

</div>
</div>

			<![CDATA[
			<script type="text/javascript">
			
					$(document).ready(function() {
						setDataObjectDefinitions();
					});
					
					$(window).scroll(function(){
				        if  ($(window).scrollTop() == $(document).height() - $(window).height()) {
					        if (smoothScrollingEnabledOnCurrentTab()) {
					           //alert("Hit the bottom!");
					           displayMoreRows();
					        }
				        }
					});
					
					function setDataObjectDefinitions() {
						var str = "{\"fields\": [{\"name\":\"containsFilter\",\"id\":\"#containsFilter\"},{\"name\":\"topicContainsFilter\",\"id\":\"#topicContainsFilter\"},{\"name\":\"questionTypeFilter\",\"id\":\"#questionTypeFilter\"},{\"name\":\"difficultyFilter\",\"id\":\"#difficultyFilter\"},{\"name\":\"maxEntityCountFilter\",\"id\":\"#maxEntityCountFilter\"},{\"name\":\"includeOnlyUserCreatedEntitiesFilter\",\"id\":\"#field_true\"},{\"name\":\"offsetFilter\",\"id\":\"#offset\"}]}";
					
						$('#Questions-data-object-definition').attr("value",str);
						
						// TODO: define Exam fields						
					}
					
					$('a[data-toggle="tab"]').on('show', function(e) {
						var tab = e.target;
						var prevTab = e.relatedTarget;
						
						// identify the tab
						// figure out its prefix
						var tabText = tab.innerText;
						
						// write that prefix in the hidden prefix field
						$("#prefix-to-current-tab-hidden-fields").attr("value", tabText);
					});
					
					$('a[data-toggle="tab"]').on('shown', function(e) {
						if (currentPageHasAnAJAXDataObjectDefinition()) {
							displayMoreRows();
						}
					});
					
					function currentPageHasAnAJAXDataObjectDefinition() {
						var prefix = $("#prefix-to-current-tab-hidden-fields").attr("value");
						
						// a list of the name of the field in the data object, and the name of the field with its value
						var dataObjDefinition_json = $("#"+prefix+"-data-object-definition").attr("value");

						return dataObjDefinition_json != undefined;					
					}
					
					function smoothScrollingEnabledOnCurrentTab() {
						return currentPageHasAnAJAXDataObjectDefinition();
					}
					
					function getURLThatProvidesTableData() {
						var prefix = $("#prefix-to-current-tab-hidden-fields").attr("value");
						
						return $("#"+prefix+"-tab-data-url").attr("value");
					}
					
					function getDataObjectForAJAX() {
						var prefix = $("#prefix-to-current-tab-hidden-fields").attr("value");
						
						// a list of the name of the field in the data object, and the name of the field with its value
						var dataObjDefinition_json = $("#"+prefix+"-data-object-definition").attr("value");
						
						var obj = jQuery.parseJSON(dataObjDefinition_json);
						var arr = obj.fields;
						
						var rtn = { };
						
						for (var i=0; i<arr.length; i++) {
							
							try {
								rtn[arr[i].name] = $(arr[i].id).attr("value");
							}
							catch (err) {
								// skip this field... TODO, handle this better.. an error means the dataObjDefinition is bad..
							}
						}
						
						return rtn;
					}
					
					function displayMoreRows() {
						var data = getMoreRows();
						
						var index = data.indexOf("<!DOCTYPE");
						var jsonExport = data;
						
						if (index != -1) {
							jsonExport = data.substring(0, index);
						}
						
						var obj = jQuery.parseJSON(jsonExport);
						
						var qArr = obj.question;
						
						var str = "";
						var prefix = $("#prefix-to-current-tab-hidden-fields").attr("value");
						var entityTableId = $("#"+prefix+"-entity-table-id").attr("value");
						
						for (var i=0; i<qArr.length; i++) {
							rowNum = i;
							str = window[prefix+"_convertToHTMLString"](qArr[i], rowNum);
							
							$(entityTableId + " > tbody:last").append(str);
						}
					}		
					
					function getMoreRows() {
						var os = $("#offset").attr("value");
						
						if (os == undefined || os.length == 0) {
							os = 0;
							$("#offset").attr("value", os);
						}

						var mecf = $("#maxEntityCountFilter").attr("value");
						
						if (mecf == undefined || mecf.length == 0) {
							mecf = 10;
							$("#maxEntityCountFilter").attr("value", mecf);
						}
						
						var rtn = "";
						var data_url = getURLThatProvidesTableData();
						var data_obj = getDataObjectForAJAX();

						$.ajax({
							type: "POST",
							url: data_url,
							data: data_obj,
							dataType: "text",
							async: false
						}).done(function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									os = (os*1)+(mecf*1); // force numerical addition
									$("#offset").attr("value", os);
									$("#maxEntityCountFilter").attr("value", mecf);
									
									rtn = data;
								}
							});
						
						return rtn;
					}					
					
			</script>
			]]>

</body>
</html>
</jsp:root>