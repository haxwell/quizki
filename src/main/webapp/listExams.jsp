<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<title>List Exams - Quizki</title>

		<link href="../css/createExam.css" rel="stylesheet" type="text/css"/>
		
		<link href="../pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="../pkgs/jquery-ui/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki.css" rel="stylesheet" type="text/css"/>
		<link href="../css/displayExam.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" href="images/favicon.ico" />

				
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

	<jsp:scriptlet>      
	request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "List All Questions");
	</jsp:scriptlet>

	<input style="display:none;" id="Exams-offset" type="text" name="offset"/>
	<input style="display:none;" id="maxEntityCountFilter" type="text" name="mcf"/>

	<input style="display:none;" id="Exams-view-data-url" type="text" name="exam-view-data-url" value="/getExams.jsp"/>
	<input style="display:none;" id="Exams-delete-entity-url" type="text" name="exam-delete-entity-url" value="/ajax/profile-deleteExam.jsp"/>
	<input style="display:none;" id="Exams-entity-table-id" type="text" name="Exams-entity-table-id" value="#examEntityTable"/>
	<input style="display:none;" id="Exams-header-div-prefix" type="text" name="Exams-header-div-prefix" value="#belowTheBarPageHeader"/>
	<input style="display:none;" id="Exams-offset" type="text" name="exam-offset"/>
	
	<input style="display:none;" id="Exams-NoMoreItemsToDisplayFlag" type="text" name="noMoreItemsToDisplayFlag"/>
	
	<input style="display:none;" id="Exams-data-object-definition" type="text" name="Exams-data-object-definition" value=""/>
	<input style="display:none;" id="prefix-to-current-view-hidden-fields" type="text" name="prefix-to-current-view-hidden-fields" value="Exams"/>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">

			<jsp:include page="listExamsTable.jsp"></jsp:include>

		</div> <!-- Content -->
	</div> <!-- Container -->

			<![CDATA[
			<script type="text/javascript">
					function getOrigHeaderId() {
						return "#belowTheBarPageHeader";
					}
					
					function getClonedHeaderId() {
						return "div#header-fixed"
					}
					
					function getHeaderOffset() {
						return $("#tabbableDiv").offset().top + $("#tabsUl").height();
					}

					function disableHeaderFilterFields() {
						var $headDOMElementInClonedHeader = getHeadDOMElementInClonedHeader();
					
						$headDOMElementInClonedHeader.find("thead > tr > td > div > #containsFilter").attr("disabled", true);
						$headDOMElementInClonedHeader.find("thead > tr > td > div > #searchQuestionsBtn").attr("disabled", true);

						$headDOMElementInClonedHeader.find("thead > tr > td > div > #topicContainsFilter").attr("disabled", true);
						$headDOMElementInClonedHeader.find("thead > tr > td > div > #searchTopicsBtn").attr("disabled", true);

						$headDOMElementInClonedHeader.find("thead > tr > td > div > div > #difficultyFilter").attr("disabled", true);
						
						$headDOMElementInClonedHeader.find("thead > tr > td > div > div > #questionTypeFilter").attr("disabled", true);
						
						$headDOMElementInClonedHeader.find("thead > tr > td > div > #topicContainsFilter").attr("placeholder", "");
						$headDOMElementInClonedHeader.find("thead > tr > td > div > #containsFilter").attr("placeholder", "");
					}
					
					function setDataObjectDefinitions() {
						str = "{\"fields\": [{\"name\":\"containsFilter\",\"id\":\"#containsFilter\"},{\"name\":\"topicContainsFilter\",\"id\":\"#topicContainsFilter\"},{\"name\":\"difficultyFilter\",\"id\":\"#difficultyFilter\"},{\"name\":\"maxEntityCountFilter\",\"id\":\"#maxEntityCountFilter\"},{\"name\":\"rangeOfEntitiesFilter\",\"id\":\"#field_1\"},{\"name\":\"offsetFilter\",\"id\":\"#Exams-offset\"}]}";
						$('#Exams-data-object-definition').attr("value",str);						
					}
					
					function oneTimeSetupForMethodsCalledByTheFunctionCalledForEachRow(obj) {
						// called from smooth-scrolling.js::displayMoreRows()
						// do nothing
					}					
					
			</script>
			]]>

			<![CDATA[ <script src="../js/smooth-scrolling.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/listExams.js" type="text/javascript"></script> ]]>			

</body>
</html>
</jsp:root>