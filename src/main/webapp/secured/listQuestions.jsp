<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <?xml version="1.0" encoding="UTF-8" ?> ]]>
    </jsp:text>
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>List Exams</title>
		<link href="../css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="../css/questions.css" rel="stylesheet" type="text/css"/>
				
		<jsp:text>
			<![CDATA[ <script src="../js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>			
			<![CDATA[ <script src="../js/listQuestions.js" type="text/javascript" ></script> ]]>
			<![CDATA[
			<script type="text/javascript">
			
			
		$(function() {
		   $( document ).tooltip();
		 });
		 
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
    </script> ]]>
			
		</jsp:text>
	</head>
<body>

	<jsp:scriptlet>      
	request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "List All Questions");
	</jsp:scriptlet>


<jsp:include page="../header.jsp"></jsp:include>

		<form action="/ListQuestionsServlet" method="post">
				<table style="float:right;">
					<tr>
						<td >
							Topic contains <input type="text" name="topicContainsFilter" value="${mruFilterTopicText}" title="Only show questions belonging to topics containing this text.."/>
						</td>
						</tr>
						<tr>
						<td >
							Question contains <input type="text" name="containsFilter" value="${mruFilterText}" title="Only show questions containing this text..."/>
						</td>
						</tr>
						<tr>
						<td >
							Include difficulties up to: <select name="difficulty" title="Do not include any questions more difficult than..">
						<c:choose><c:when test="${mruFilterDifficulty == 1}"><option value="junior" selected="selected">Junior</option></c:when><c:otherwise><option value="junior" >Junior</option></c:otherwise></c:choose>
						<c:choose><c:when test="${mruFilterDifficulty == 2}"><option value="intermediate" selected="selected">Intermediate</option></c:when><c:otherwise><option value="intermediate" >Intermediate</option></c:otherwise></c:choose>
						<c:choose><c:when test="${mruFilterDifficulty == 3}"><option value="wellversed" selected="selected">Well-versed</option></c:when><c:otherwise><option value="wellversed" >Well-versed</option></c:otherwise></c:choose>
						<c:choose><c:when test="${mruFilterDifficulty == 4}"><option value="guru" selected="selected">Guru</option></c:when><c:otherwise><option value="guru">Guru</option></c:otherwise></c:choose>
						</select>
		
						</td>
						</tr>
						<tr>
						<td>
						<input type="submit" value="Filter" name="button"/><input type="submit" value="Clear Filter" name="button"/>
						</td>				</tr>
				</table>
		</form>

		<h1>List All Questions</h1>


		<form action="/ListQuestionsServlet" method="post">
		<br/><br/>
			<div id="center" class="listOfQuestions"  style="overflow:auto; height:225px; width:100%">
			
		<br/>
	<table class="displayExam">
		<thead>
			<th>ID</th>
			<th></th>
			<th>Question</th>
			<th>Topics</th>
			<th>Difficulty</th>
		</thead>
		<tbody>
		<c:set var="rowNum" value="0"/>
		<c:forEach var="question" items="${fa_listoquestionstobedisplayed}">
			<c:set var="rowNum" value="${rowNum + 1}" />
			<c:choose><c:when test="${rowNum % 2 == 0}">
			<jsp:text><![CDATA[<tr>]]></jsp:text>
			</c:when>
			<c:otherwise>
			<jsp:text><![CDATA[<tr class="rowHighlight">]]></jsp:text>
			</c:otherwise></c:choose>
			
				<td>${question.id}</td>
				<td>
									<c:choose><c:when test="${question.user.id == currentUserEntity.id}">
						<input type="submit" value="Edit Question" name="questionButton_${question.id}"/>
					</c:when>
					</c:choose>
				</td>   
				<td><c:choose><c:when test="${empty question.description}"><a href="/displayQuestion.jsp?questionId=${question.id}">${question.textWithoutHTML}</a></c:when>
						<c:otherwise><a href="/displayQuestion.jsp?questionId=${question.id}">${question.description}</a></c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:forEach var="topic" items="${question.topics}">
						${topic.text}<br/>
					</c:forEach>
				</td>
				<td>${question.difficulty.text}</td>
			<jsp:text><![CDATA[</tr>]]></jsp:text>
		</c:forEach>
		</tbody>
	</table>
	</div>
		
		<br/>

		</form>
		

<br/>
<a href="/index.jsp">home</a> <br/>


</body>
</html>
</jsp:root>