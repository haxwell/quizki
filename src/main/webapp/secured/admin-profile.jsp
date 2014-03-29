<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" xmlns:qfn="http://quizki.com/tld/qfn" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<title>Admin Profile - Quizki</title>

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
			
			<![CDATA[ <script src="../js/backbone-quizki.js" type="text/javascript"></script> ]]>
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
										var prefix = getPrefix();
										var url = $("#"+prefix+"-delete-entity-url").attr("value");
										
										window[prefix+"_setDeleteEntityDataObjectDefinition"]();
										
										var data_obj = getDataObjectForAJAX(prefix + "-delete-entity-dataObjectDefinition");
										
										makeAJAXCall_andWaitForTheResults(url, data_obj, function (data,status) {
											window[prefix+"_postDeleteEntityMethod"]();
											
											// get from data the text of the question 
											var text = data;
											
											// display it in alert div
											var str = 'Your question has been deleted! ( "' + text + '" )';
											var obj = { arr : [str] };
											populateAlertDiv(obj.arr, 'alert-info');
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
		<div id="belowTheBarPageHeader" class="fillBackgroundColor">
			<br/>
			<div id="idAlertDiv" class="alert hidden">.</div>

      <c:choose>
      <c:when test="${empty sessionScope.currentUserEntity || !qfn:isAdministrator(sessionScope.currentUserEntity)}">
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
				    <jsp:include page="admin-profile-summary.jsp"></jsp:include>
				  </div>
				  <div class="tab-pane" id="tabs-2">
		    		<jsp:include page="admin-profile-questions.jsp"></jsp:include>
				  </div>
				  <div class="tab-pane" id="tabs-3">
				    <jsp:include page="admin-profile-exams.jsp"></jsp:include>
				  </div>
				  <div class="tab-pane" id	="tabs-4">
				    	<div id="account" class="listOfQuestions" style="overflow:auto;"><jsp:include page="admin-profile-account.jsp"></jsp:include></div>
				  </div>
			  </div>
			</div>
	
		</c:otherwise>
	</c:choose>
	
	<br/>
	
	<div style="display:none;" id="dialogText">Are you SURE you want to delete?</div>  

	<input style="display:none;" id="field_0" type="text" name="field_0" value="0"/>
	
	<input style="display:none;" id="maxEntityCountFilter" type="text" name="mcf"/>
	
	<input style="display:none;" id="Questions-view-data-url" type="text" name="question-view-data-url" value="/getQuestions.jsp"/>
	<input style="display:none;" id="Questions-delete-entity-url" type="text" name="question-delete-entity-url" value="/ajax/profile-deleteQuestion.jsp"/>
	<input style="display:none;" id="Questions-entity-table-id" type="text" name="Questions-entity-table-id" value="#questionEntityTable"/>
	<input style="display:none;" id="Questions-header-div-prefix" type="text" name="Questions-header-div-prefix" value="#belowTheBarPageHeader"/>
	<input style="display:none;" id="Questions-offset" type="text" name="question-offset"/>
	<input style="display:none;" id="Exams-view-data-url" type="text" name="exam-view-data-url" value="/ajax/exam-getFilteredList.jsp"/>
	<input style="display:none;" id="Exams-delete-entity-url" type="text" name="exam-delete-entity-url" value="/ajax/profile-deleteExam.jsp"/>
	<input style="display:none;" id="Exams-entity-table-id" type="text" name="Exams-entity-table-id" value="#examEntityTable"/>
	<input style="display:none;" id="Exams-header-div-prefix" type="text" name="Exams-header-div-prefix" value="#belowTheBarPageHeader"/>
	<input style="display:none;" id="Exams-offset" type="text" name="exam-offset"/>
	<!-- input style="display:none;" id="prefix-to-current-view-hidden-fields" type="text" name="prefix-to-current-view-hidden-fields" value=""/  -->
	
	
	<input style="display:none;" id="Exams-NoMoreItemsToDisplayFlag" type="text" name="noMoreItemsToDisplayFlag"/>
	<input style="display:none;" id="Questions-NoMoreItemsToDisplayFlag" type="text" name="noMoreItemsToDisplayFlag"/>
	
	<input style="display:none;" id="Questions-data-object-definition" type="text" name="Questions-data-object-definition" value=""/>
	<input style="display:none;" id="Exams-data-object-definition" type="text" name="Exams-data-object-definition" value=""/>
	<input style="display:none;" id="prefix-to-current-view-hidden-fields" type="text" name="prefix-to-current-view-hidden-fields" value="Exams"/>
</div>
</div>
</div>

			<![CDATA[
			<script type="text/javascript">
			
					function getOrigHeaderId() {
						return "#belowTheBarPageHeader";
					}
					
					function getClonedHeaderId() {
						return "div#header-fixed"
					}
					
					function getHeaderOffset() {
						return $("#tabbableDiv").offset().top + $("#tabsUl").height() + $("#questionEntityTable > thead").height();
					}
					
					function onScroll_beforeClonedHeaderShows($obj) {
						// temp code used to set the background of the cloned header to aid in figuring out why
						//  the header appears in the items table when scrolling up in profile-questions..
						
						//$obj.removeClass("fillBackgroundColor");
						//$obj.removeClass("table");
						//$obj.addClass("table2");
					}

					function disableHeaderFilterFields() {
						
					}
					
					function setDataObjectDefinitions() {
						// define a JSON string which has the name of the attribute to be sent to the server, along with the ID of the field from which the value sent to the server
						//   should be pulled from. Later we iterate over each element and build the data object sent to the server when getExams() or getQuestions() or whatever
						//   is called.
					
						var str = "{\"fields\": [{\"name\":\"containsFilter\",\"id\":\"#containsFilter\"},{\"name\":\"topicContainsFilter\",\"id\":\"#topicContainsFilter\"},{\"name\":\"questionTypeFilter\",\"id\":\"#questionTypeFilter\"},{\"name\":\"difficultyFilter\",\"id\":\"#difficultyFilter\"},{\"name\":\"maxEntityCountFilter\",\"id\":\"#maxEntityCountFilter\"},{\"name\":\"rangeOfEntitiesFilter\",\"id\":\"#field_0\"},{\"name\":\"offsetFilter\",\"id\":\"#Questions-offset\"}]}";
						$('#Questions-data-object-definition').attr("value",str);

						str = "{\"fields\": [{\"name\":\"containsFilter\",\"id\":\"#examContainsFilter\"},{\"name\":\"topicContainsFilter\",\"id\":\"#examTopicContainsFilter\"},{\"name\":\"difficultyFilter\",\"id\":\"#examDifficultyFilter\"},{\"name\":\"maxEntityCountFilter\",\"id\":\"#maxEntityCountFilter\"},{\"name\":\"rangeOfEntitiesFilter\",\"id\":\"#field_0\"},{\"name\":\"offsetFilter\",\"id\":\"#Exams-offset\"}]}";
						$('#Exams-data-object-definition').attr("value",str);						
					}
					
					function oneTimeSetupForMethodsCalledByTheFunctionCalledForEachRow(obj) {
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