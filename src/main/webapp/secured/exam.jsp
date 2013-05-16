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
			    
//					$(function() {
//						$( "#tabs" ).tabs();
//					   
//						if (tabIndex !== undefined)
//					   		$( "#tabs" ).tabs("option","active", tabIndex);
//					});			

					function setDisplayDimensionsAccordingToCurrentWindowHeight() {
						// set the height of the content area according to the browser height
						var bottomBufferHeight = 125;
						var questionsBufferHeight = 97;
						var windowHeight = $(window).height();
						
						$('#tabs').height(windowHeight - bottomBufferHeight);
						$('#availableQuestionsDiv').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
						$('#selectedQuestionsDiv').height(windowHeight - bottomBufferHeight - questionsBufferHeight);
					}

					$(document).ready(function(){
				 		setDisplayDimensionsAccordingToCurrentWindowHeight();
				 		
				 		$('.selectQuestionChkbox').each(function() {
				 			var v = $(this).attr('checked');
				 			
				 			if (v !== undefined) {
				 				var id = $(this).attr('id');
				 				var arr = id.split('_');
				 				
				 				$('#tableRow_' + arr[1]).css('background-color', '#E6FFCC');
				 			}
				 		});
					});
				 
					$(document).ready(function(){
					     $(window).resize(function() {
					 		setDisplayDimensionsAccordingToCurrentWindowHeight();
						});
					});
					
					$(document).ready(function(){
						$(".selectQuestionChkbox").click(function(){
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
								
								if (state == 'selected')
									$('#tableRow_' + rowid).css('background-color', '#E6FFCC');
								else {
									var klass = $('#tableRow_' + rowid).attr('class');
									
									if (klass == 'rowHighlight')
										$('#tableRow_' + rowid).css('background-color', '#F0F0EE');
									else 
										$('#tableRow_' + rowid).css('background-color', '#FFFFFF');
								}
							});
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
      
		<form action="/secured/ExamServlet" method="post" id="titleAndSubmitButtonForm">
		<table style="float:right; width:100%">
	      	<tr>
	      		<td>
	      			Title: <input type="text" size="45" maxlength="128" id="id_examTitle" name="examTitle" value="${currentExam.title}" title="A name for this exam."/>
	      		</td>
	      		<td>
	      			Message: <input type="text" size="45" maxlength="255" id="id_examMessage" name="examMessage" value="${currentExam.message}" title="A message for folks who take this exam."/>
	      		</td>
				<c:choose>
					<c:when test="${empty sessionScope.inEditingMode}">
						<td style="float:right;"><input type="submit" value="Add Exam" name="button" style="float:right;"/></td>
					</c:when>
					<c:otherwise>
						<td style="float:right;"><input type="submit" value="Update Exam" name="button" style="float:right;"/></td>
					</c:otherwise>
				</c:choose>
	      	</tr>
		</table>
		</form>
	<!--  fhdsjfkdshkjds -->

	<c:choose><c:when test="${empty requestScope.doNotAllowEntityEditing}">
	
		<div id="tabs" class="mainContentArea">
		  <br/>
	    	<div id="availableQuestionsDiv" class="listOfQuestions" style="overflow:auto; height:95%;">
	    		<jsp:include page="exam-AvailableQuestions.jsp"></jsp:include>
	    	</div>
	    	<br/>
	    	<div id="paginationDiv" class="center">
		    	<form action="/secured/ExamServlet" method="post" id="paginationForm">
		    	<c:choose>
			    	<c:when test="${sessionScope.questionPaginationData.totalItemCount > 0}">
				    	Showing questions ${sessionScope.questionPaginationData.beginIndex} - ${sessionScope.questionPaginationData.endIndex} of ${sessionScope.questionPaginationData.totalItemCount}
					</c:when>
					<c:otherwise>
						No questions to show! 					
					</c:otherwise>
				</c:choose>
					 
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
		    	</form>
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