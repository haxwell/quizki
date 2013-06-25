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
			<![CDATA[ <script src="../pkgs/jquery/jquery-1.10.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/jquery-ui/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script> ]]>			
			<![CDATA[ <script src="../js/listQuestions.js" type="text/javascript" ></script> ]]>
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
			    
					function setDisplayDimensionsAccordingToCurrentWindowHeight() {
						// set the height of the content area according to the browser height
						var bottomBufferHeight = 175;
						var windowHeight = $(window).height();
						
						$('#tabs').height(windowHeight - bottomBufferHeight);
						$('#availableExamsDiv').height(windowHeight - bottomBufferHeight);
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
			    

		    </script> ]]>
			
		</jsp:text>
	</head>
<body>

	<jsp:scriptlet>      
	request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "List All Questions");
	</jsp:scriptlet>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">

		<div id="tabs" class="mainContentArea">
		  <div id="tabs-1">
		  	<form id="listExamsForm" action="/ListExamsServlet">
		    	<div id="availableExamsDiv" class="listOfQuestions" style="overflow:auto; height:95%;">

					<table class="displayExam">
						<thead>
						<tr>
							<th>ID</th>
							<th></th>
							<th>Exam Name</th>
							<th>Topic</th>
							<th></th>
							<th></th>
						</tr>
						</thead>
						<tbody>
							<tr>
								<td></td>
								<td style="text-align:right"><input type="submit" name="button" value="Apply Filter -->"/> </td>
								<td>
									<c:if test="${not empty sessionScope.currentUserEntity}">
									Show <select name="mineOrAll" title="..">
										<c:choose><c:when test="${mruMineOrAll == 1}"><option value="mine" selected="selected">My</option></c:when><c:otherwise><option value="mine">My</option></c:otherwise></c:choose>
										<c:choose><c:when test="${mruMineOrAll == 2}"><option value="all" selected="selected">All</option></c:when><c:otherwise><option value="all">All</option></c:otherwise></c:choose>
									</select> Exams
									</c:if>
									<input type="text" name="containsFilter" value="${mruFilterText}" title="Only show exams with titles that contain this text..." style="width:75%%;"/>
								</td>
								<td><input type="text" name="topicContainsFilter" value="${mruFilterTopicText}" title="Only show questions belonging to topics containing this text.." style="width:100%;"/></td>
							</tr>
						<c:set var="rowNum" value="0"/>
						<c:set var="counter" value="0"/>
						<c:choose>
							<c:when test="${empty fa_listofexamstobedisplayed}">
								<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
								<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
								<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
								<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
								<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
								<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
								<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
								<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
								<jsp:text><![CDATA[<td></td><td colspan="6">There are no exams to display! Either adjust the filter above, or add some exams of your own!]]></jsp:text>
								<jsp:text><![CDATA[</tr>]]></jsp:text>
							</c:when>
							<c:otherwise>
						<c:forEach var="exam" items="${fa_listofexamstobedisplayed}">
							<c:set var="rowNum" value="${rowNum + 1}" />
							<c:choose><c:when test="${rowNum % 2 == 0}">
							<jsp:text><![CDATA[<tr>]]></jsp:text>
							</c:when>
							<c:otherwise>
							<jsp:text><![CDATA[<tr class="rowHighlight">]]></jsp:text>
							</c:otherwise></c:choose>
							
								<c:set var="counter" value="${counter + 1}" />
								<td>${exam.id}</td>
								<td><input type="submit" value="Detail Exam" name="examButton_${exam.id}"/></td>
								<td style="width:80%;">${exam.title}</td>
								<td>
									<c:forEach var="topic" items="${exam.topics}">
										${topic.text}<br/>
									</c:forEach>
								</td>
								<td><input type="submit" value="Take Exam" id="exam_take_button_${counter}" name="examButton_${exam.id}"/></td>
				
							<jsp:text><![CDATA[</tr>]]></jsp:text>
						</c:forEach>
						</c:otherwise>
						</c:choose>
						</tbody>
					</table>
				
					<input type="hidden" id="exam_valueOfLastPressedButton" name="exam_valueOfLastPressedButton"></input>
					<input type="hidden" id="exam_nameOfLastPressedButton" name="exam_nameOfLastPressedButton"></input>
					
					
		    	</div>
		    	<br/>
			    	<div id="paginationDiv" class="center">
			    	<c:choose>
			    	<c:when test="${sessionScope.examPaginationData.totalItemCount > 0}">
				    	Showing exams ${sessionScope.examPaginationData.beginIndex} - ${sessionScope.examPaginationData.endIndex} of ${sessionScope.examPaginationData.totalItemCount}
					</c:when>
					<c:otherwise>
						No exams to show! 					
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
			    	</div>
			</form>			    	
		  </div>
		</div>

</div> <!-- Content -->
</div> <!-- Container -->

</body>
</html>
</jsp:root>