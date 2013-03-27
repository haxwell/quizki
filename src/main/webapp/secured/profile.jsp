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

				 $(document).ready(function(){
					// set the height of the content area according to the browser height
					var bottomBufferHeight = 110;
					var questionsBufferHeight = 77;
					var windowHeight = $(window).height();
					
					$('#tabs').height(windowHeight - bottomBufferHeight);
					$('#questions').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
					$('#exams').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
				});
				 
				 $(document).ready(function(){
				     $(window).resize(function() {
				             var bottomBufferHeight = 110;
				             var questionsBufferHeight = 77;
				             var windowHeight = $(window).height();
				
					$('#tabs').height(windowHeight - bottomBufferHeight);
					$('#questions').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
					$('#exams').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
				})});

				$(document).ready(function() { 
	              $('#delete_button').click(function() { 
					var buttonValue = $( this ).attr("value");
					var buttonName = $( this ).attr("name");
					// set that buttonId on hidden field on form
					$('#nameOfLastPressedButton').attr("value", buttonName);
					$('#valueOfLastPressedButton').attr("value", buttonValue);
					
					$( "#dialogText" ).dialog({
						  autoOpen: true,
					      resizable: false,
					      modal: true,
					      buttons: {
					        "Delete this item": function() {
					          document.getElementById("profileQuestionForm").submit();
					        },
					        Cancel: function() {
					          $( this ).dialog( "close" );
					        }
					      }
					    });
					    return false;
				    });
				   });

				$(document).ready(function() { 
	              $('#edit_button').click(function() { 
					var buttonValue = $( this ).attr("value");
					var buttonName = $( this ).attr("name");
					// set that buttonId on hidden field on form
					$('#nameOfLastPressedButton').attr("value", buttonName);
					$('#valueOfLastPressedButton').attr("value", buttonValue);
					
					document.getElementById("profileQuestionForm").submit();
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
			${fn:length(fa_listofquestionstobedisplayed)} questions.	
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