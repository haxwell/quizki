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

		<!--  link href="../pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" /  -->
		<!--  link href="../css/quizki.css" rel="stylesheet" /  -->

		<link href="../pkgs/Flat-UI-master/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="../pkgs/Flat-UI-master/css/flat-ui.css" rel="stylesheet" />
		
		<link href="../pkgs/font-awesome/css/font-awesome.css" rel="stylesheet"/>
		
		<link href="../pkgs/jquery-ui/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.css" rel="stylesheet"/>

		<link href="../css/quizki-sitewide.css" rel="stylesheet" />
		
		<link href="../css/quizki-buttons.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-checkbox-radio-btn.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-select-dropdowns.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-tables.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-text-input-fields.css" rel="stylesheet" type="text/css"/>
		
		<link href="../css/profile.css" rel="stylesheet" type="text/css" />
		
		<link href="../images/favicon.ico" rel="shortcut icon"  />

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

		<jsp:text>
			<![CDATA[ <script type="text/javascript" src="../pkgs/Flat-UI-master/js/jquery-1.8.3.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../pkgs/jquery-ui/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.js"></script> ]]>
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

			<![CDATA[
			<script type="text/javascript">

					function getDeleteConfirmationDialogOptions(profileFormName) {
						var options = { 
								autoOpen: false, resizable: false, modal: true,
							      buttons: [{
							        text : "Delete this item", 
							        click : function() {
										var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
										var url = $("#"+prefix+"-delete-entity-url").attr("value");
										
										window[prefix+"_setDeleteEntityDataObjectDefinition"]();
										
										var data_obj = getDataObjectForAJAX(prefix + "-delete-entity-dataObjectDefinition");
										
										makeAJAXCall_andWaitForTheResults(url, data_obj, function (data,status) {
											window[prefix+"_postDeleteEntityMethod"]();
										});
										
										$( this ).dialog( "close" );
							        } },
							        {
							        text : "Cancel",
							        click : function() {
							        	$( this ).dialog( "close" );
							        } 
							      }]
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
					
		     </script>
				]]>			

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

	<div id="tabbableDiv" class="tabbable">
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
	
	<div style="display:none;" id="dialogText">Are you SURE you want to delete?</div>  

	<input style="display:none;" id="field_1" type="text" name="field_1" value="1"/>
	<input style="display:none;" id="field_true" type="text" name="field_true" value="true"/>
	<input style="display:none;" id="field_false" type="text" name="field_false" value="false"/>
	
	<input style="display:none;" id="offset" type="text" name="offset"/>
	<input style="display:none;" id="maxEntityCountFilter" type="text" name="mcf"/>
	
	<input style="display:none;" id="Questions-view-data-url" type="text" name="question-view-data-url" value="/getQuestions.jsp"/>
	<input style="display:none;" id="Questions-delete-entity-url" type="text" name="question-delete-entity-url" value="/ajax/profile-deleteQuestion.jsp"/>
	<input style="display:none;" id="Questions-entity-table-id" type="text" name="Questions-entity-table-id" value="#questionEntityTable"/>
	<input style="display:none;" id="Exams-view-data-url" type="text" name="exam-view-data-url" value="/getExams.jsp"/>
	<input style="display:none;" id="Exams-delete-entity-url" type="text" name="exam-delete-entity-url" value="/ajax/profile-deleteExam.jsp"/>
	<input style="display:none;" id="Exams-entity-table-id" type="text" name="Exams-entity-table-id" value="#examEntityTable"/>
	<input style="display:none;" id="prefix-to-current-view-hidden-fields" type="text" name="prefix-to-current-view-hidden-fields" value=""/>
	
	<input style="display:none;" id="idNoMoreItemsToDisplayFlag" type="text" name="noMoreItemsToDisplayFlag"/>
	
	<input style="display:none;" id="Questions-data-object-definition" type="text" name="Questions-data-object-definition" value=""/>
	<input style="display:none;" id="Questions-data-object-definition" type="text" name="Questions-data-object-definition" value=""/>

</div>
</div>

			<![CDATA[
			<script type="text/javascript">
			
					var tableOffset = $("#tabbableDiv").offset().top + $("#tabsUl").height();
					var $header = $("#questionEntityTable > thead").clone();
					var $fixedHeader = $("#header-fixed").append($header);
					
					$(window).scroll(function() {
					    var offset = $(this).scrollTop();
					
					    if (offset >= tableOffset && $fixedHeader.is(":hidden")) {
					        $fixedHeader.show();
					    }
					    else if (offset < tableOffset) {
					        $fixedHeader.hide();
					    }
					});

					function setClonedHeaderInTheGlobalVariables() {
						$header = $("#questionEntityTable > thead").clone();
						
						$("#header-fixed").empty();
						
						$fixedHeader = $("#header-fixed").append($header);
						
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
					}
					
					function setDataObjectDefinitions() {
						var str = "{\"fields\": [{\"name\":\"containsFilter\",\"id\":\"#containsFilter\"},{\"name\":\"topicContainsFilter\",\"id\":\"#topicContainsFilter\"},{\"name\":\"questionTypeFilter\",\"id\":\"#questionTypeFilter\"},{\"name\":\"difficultyFilter\",\"id\":\"#difficultyFilter\"},{\"name\":\"maxEntityCountFilter\",\"id\":\"#maxEntityCountFilter\"},{\"name\":\"rangeOfEntitiesFilter\",\"id\":\"#field_1\"},{\"name\":\"offsetFilter\",\"id\":\"#offset\"}]}";
					
						$('#Questions-data-object-definition').attr("value",str);
						
						// TODO: define Exam fields						
					}
					
					function setVarsUsedInProcessingIndividualRows(obj) {
						// called from smooth-scrolling.js::displayMoreRows()
						
						// do nothing
					}					
					
			</script>
			]]>
			

			<![CDATA[ <script src="../js/smooth-scrolling.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/profile-questions.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/profile-exams.js" type="text/javascript"></script> ]]>			
</body>
</html>
</jsp:root>