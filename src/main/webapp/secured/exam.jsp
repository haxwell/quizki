<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Exam - Quizki</title>
		<link href="../css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="../css/questions.css" rel="stylesheet" type="text/css"/>
		<link href="../css/createExam.css" rel="stylesheet" type="text/css"/>
		
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
			    
					$(function() {
						$( "#tabs" ).tabs();
					   
						if (tabIndex !== undefined)
					   		$( "#tabs" ).tabs("option","active", tabIndex);
					});			
			    
					function setDisplayDimensionsAccordingToCurrentWindowHeight() {
						// set the height of the content area according to the browser height
						var bottomBufferHeight = 150;
						var questionsBufferHeight = 97;
						var windowHeight = $(window).height();
						
						$('#tabs').height(windowHeight - bottomBufferHeight);
						$('#availableQuestionsDiv').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
						$('#selectedQuestionsDiv').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
					}

					$(document).ready(function(){
				 		setDisplayDimensionsAccordingToCurrentWindowHeight();
					});
				 
					$(document).ready(function(){
					     $(window).resize(function() {
					 		setDisplayDimensionsAccordingToCurrentWindowHeight();
						});
					});
		    
		    </script> ]]>
			
		</jsp:text>
				
	</head>
<body>

<jsp:include page="../header.jsp"></jsp:include>

	<c:choose>
	<c:when test="${empty sessionScope.inEditingMode}">
	<h1>Create Exam</h1>
	</c:when>
	<c:otherwise>
	<h1>Edit Exam</h1>
	</c:otherwise>
	</c:choose>

      <c:if test="${not empty requestScope.validationErrors}">
      	<c:forEach var="str" items="${validationErrors}">
      		<span class="redText">${str}</span><br/>
      	</c:forEach>
      	<br/>
      </c:if>

      <c:if test="${not empty requestScope.successes}">
      	<c:forEach var="str" items="${successes}">
      		<span class="greenText">${str}</span><br/>
      	</c:forEach>
      	<br/>
      </c:if>

	<c:choose><c:when test="${empty requestScope.doNotAllowEntityEditing}">
	
	<div id="tabs" class="mainContentArea">
	  <ul>
	    <li><a href="#tabs-1">Available Questions</a></li>
	    <li><a href="#tabs-2">Selected Questions</a></li>
	  </ul>
	  <div id="tabs-1">
	    	<div id="availableQuestionsDiv" class="listOfQuestions" style="overflow:auto; height:95%; width:98%"><jsp:include page="exam-AvailableQuestions.jsp"></jsp:include></div>
	    	<br/>
	    	<form action="/secured/ExamServlet" method="post" id="paginationForm">
		    	<div id="paginationDiv" class="center">
		    	Showing questions ${sessionScope.questionPaginationData.beginIndex} - ${sessionScope.questionPaginationData.endIndex} of ${sessionScope.questionPaginationData.totalItemCount} 
		    	<input type="submit" value="&lt;&lt; FIRST" name="button"/>
		    	<input type="submit" value="&lt; PREV" name="button"/>
		    	<input type="submit" value="NEXT &gt;" name="button"/>
		    	<input type="submit" value="LAST &gt;&gt;" name="button"/>
		    	- Max. List Size 
		    	<select name="quantity">
		    		<option value="quantity_25">25</option>
		    		<option value="quantity_25">50</option>
		    		<option value="quantity_25">75</option>
		    		<option value="quantity_25">100</option>
		    	</select>
		    	<input type="submit" value="REFRESH" name="button"/>
		    	</div>
	    	</form>
	  </div>
	  <div id="tabs-2">
	    	<div id="selectedQuestionsDiv" class="listOfQuestions" style="overflow:auto; height:95%; width:98%"><jsp:include page="exam-SelectedQuestions.jsp"></jsp:include></div>
	    	<br/>
	  </div>
	</div>
	
		</c:when>
		<c:otherwise>
		<br/>
		There was an error loading this page. This entity cannot be edited!<br/>
		</c:otherwise>
		</c:choose>
	
	<br/> 

</body>
</html>
</jsp:root>