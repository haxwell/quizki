<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Profile - Quizki</title>
		<link href="../css/custom-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="../css/questions.css" rel="stylesheet" type="text/css"/>
		<link href="../css/profile.css" rel="stylesheet" type="text/css"/>
				
		<jsp:text>
			<![CDATA[ <script src="../js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>

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
			
					$(function() {
						$( "#tabs" ).tabs();
					   
						if (tabIndex !== undefined)
					   		$( "#tabs" ).tabs("option","active", tabIndex);
					});			
			
					//$(function() {
					//   $( document ).tooltip();
					// });
					 
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
				
					function setDisplayDimensionsAccordingToCurrentWindowHeight() {
						// set the height of the content area according to the browser height
						var bottomBufferHeight = 110;
						var questionsBufferHeight = 77;
						var windowHeight = $(window).height();
						
						$('#tabs').height(windowHeight - bottomBufferHeight);
						$('#questions').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
						$('#exams').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
					}

					$(document).ready(function(){
				 		setDisplayDimensionsAccordingToCurrentWindowHeight();
					});
				 
					$(document).ready(function(){
					     $(window).resize(function() {
					 		setDisplayDimensionsAccordingToCurrentWindowHeight();
						});
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
			              		
			              		num = num + 1;
			          	});
		              });

		     </script>
				]]>			

		</jsp:text>
				
	</head>
<body>

<jsp:include page="../header.jsp"></jsp:include>

	<br/>

      <c:choose>
      <c:when test="${empty sessionScope.currentUserEntity}">
      	<br/><br/>
		Oops! Something went wrong! You should <a href="/index.jsp">go back to the beginning</a>.
      </c:when>
      <c:otherwise>

	<div id="tabs" class="mainContentArea">
	  <ul>
	    <li><a href="#tabs-1">Summary</a></li>
	    <li><a href="#tabs-2">Questions</a></li>
	    <li><a href="#tabs-3">Exams</a></li>
	    <li><a href="#tabs-4">Account Status</a></li>
	  </ul>
	  <div id="tabs-1">
	    <jsp:include page="profile-summary.jsp"></jsp:include>
	  </div>
	  <div id="tabs-2">
	    	<div id="questions" class="listOfQuestions" style="overflow:auto; height:95%; width:98%"><jsp:include page="profile-questions.jsp"></jsp:include></div>
	    	<br/>
		<form id="profileQuestionNavigationForm" action="/secured/ProfileQuestionsServlet">
		<div id="paginationDiv" class="center">
			Showing questions ${sessionScope.questionPaginationData.beginIndex} - ${sessionScope.questionPaginationData.endIndex} of ${sessionScope.questionPaginationData.totalItemCount} 
			<input type="submit" value="&lt;&lt; FIRST" name="button"/>
			<input type="submit" value="&lt; PREV" name="button"/>
			<input type="submit" value="NEXT &gt;" name="button"/>
			<input type="submit" value="LAST &gt;&gt;" name="button"/>
			- Max. List Size 
			<select name="quantity">
			<c:choose><c:when test="${mruFilterPaginationQuantity == 10}"><option value="quantity_10" selected="selected">10</option></c:when><c:otherwise><option value="quantity_10" >10</option></c:otherwise></c:choose>
			<c:choose><c:when test="${mruFilterPaginationQuantity == 25}"><option value="quantity_25" selected="selected">25</option></c:when><c:otherwise><option value="quantity_25" >25</option></c:otherwise></c:choose>
			<c:choose><c:when test="${mruFilterPaginationQuantity == 50}"><option value="quantity_50" selected="selected">50</option></c:when><c:otherwise><option value="quantity_50" >50</option></c:otherwise></c:choose>
			<c:choose><c:when test="${mruFilterPaginationQuantity == 75}"><option value="quantity_75" selected="selected">75</option></c:when><c:otherwise><option value="quantity_75" >75</option></c:otherwise></c:choose>					
			<c:choose><c:when test="${mruFilterPaginationQuantity == 100}"><option value="quantity_100" selected="selected">100</option></c:when><c:otherwise><option value="quantity_100" >100</option></c:otherwise></c:choose>
			</select>
			<input type="submit" value="REFRESH" name="button"/>
		</div>
		</form>
	
	  </div>
	  <div id="tabs-3">
	    	<div id="exams" class="listOfQuestions" style="overflow:auto; height:95%; width:98%"><jsp:include page="profile-exams.jsp"></jsp:include></div>
	    	<br/>
			${fn:length(fa_listofexamstobedisplayed)} exams.	
	  </div>
	  <div id="tabs-4">
	    	<div id="account" class="listOfQuestions" style="overflow:auto; height:95%; width:98%"><jsp:include page="profile-account.jsp"></jsp:include></div>
	  </div>
	</div>
	
	</c:otherwise>
	</c:choose>
	
	<br/>
	<a href="/index.jsp">home</a>
	
	<div class="hidden" id="dialogText">Are you SURE you want to delete?</div>  

</body>
</html>
</jsp:root>