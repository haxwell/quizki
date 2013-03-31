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
	
	<form action="/secured/ExamServlet" method="post">
		Title: <input type="text" id="id_examTitle" name="examTitle" size="35" maxlength="126" value="${currentExam.title}"/>  
		<br/><br/>
		Available Questions
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
					Show <select name="mineOrAll">
				<c:choose><c:when test="${mruMineOrAll == 'mine'}"><option value="mine" selected="selected">my</option></c:when><c:otherwise><option value="mine">my</option></c:otherwise></c:choose>
				<c:choose><c:when test="${mruMineOrAll == 'all'}"><option value="all" selected="selected">all</option></c:when><c:otherwise><option value="all">all</option></c:otherwise></c:choose>
				</select> questions

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
				<br/>

		<div class="listOfQuestions" style="overflow:auto; height:150px; width:100%">
			<table  style="width:100%">

				<c:set var="rowNum" value="0"/>
				<c:choose >
				<c:when test="${empty fa_listofquestionstobedisplayed}">
					<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
					<jsp:text><![CDATA[<td>You have not entered any questions of your own! You can change the filter above to include questions from everyone.]]></jsp:text>
					<jsp:text><![CDATA[</tr>]]></jsp:text>
				</c:when>
				<c:otherwise>
					<c:forEach var="question" items="${fa_listofquestionstobedisplayed}">
						<c:set var="rowNum" value="${rowNum + 1}" />
						<c:choose><c:when test="${rowNum % 2 == 0}">
						<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
						</c:when>
						<c:otherwise>
						<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
						</c:otherwise></c:choose>
			
						<c:choose>
							<!-- TODO: Make use of QuestionUtil.getDisplayString() if possible -->
							<c:when test="${empty question.description}">
								<td><input type="checkbox" name="a_chkbox_${question.id}">	${fn:substring(question.textWithoutHTML, 0, 175)}</input></td>
							</c:when>
							<c:otherwise>
								<td><input type="checkbox" name="a_chkbox_${question.id}">	${fn:substring(question.description, 0, 175)}</input></td>
							</c:otherwise>
						</c:choose>
	
						<jsp:text><![CDATA[</tr>]]></jsp:text>				
					</c:forEach>
				</c:otherwise>
				</c:choose>
			</table>
		</div>
		
		<br/>
		<input type="submit" value="Add Questions" name="button"/>
		<br/>
		<br/><br/>		
		List of questions on this exam:<br/>
		
		<div class="listOfQuestions" style="overflow:auto; height:150px; width:100%">
			<table  style="width:100%">

				<c:set var="rowNum" value="0"/>
				<c:forEach var="question" items="${currentExam.questions}">
					<c:set var="rowNum" value="${rowNum + 1}" />
					<c:choose><c:when test="${rowNum % 2 == 0}">
					<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
					</c:when>
					<c:otherwise>
					<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
					</c:otherwise></c:choose>
		
					<c:choose>
						<!-- TODO: Make use of QuestionUtil.getDisplayString() if possible -->
						<c:when test="${empty question.description}">
							<td><input type="checkbox" name="d_chkbox_${question.id}">	${fn:substring(question.textWithoutHTML, 0, 175)}</input></td>
						</c:when>
						<c:otherwise>
							<td><input type="checkbox" name="d_chkbox_${question.id}">	${fn:substring(question.description, 0, 175)}</input></td>
						</c:otherwise>
					</c:choose>

					<jsp:text><![CDATA[</tr>]]></jsp:text>				
				</c:forEach>
			</table>
		</div>			
		<br/>
		<input type="submit" value="Delete Questions" name="button"/>
				<br/><br/><br/>
				<hr/>
	<c:choose>
	<c:when test="${empty sessionScope.inEditingMode}">
	<input type="submit" value="Add Exam" name="button" style="float:right;"/>
	</c:when>
	<c:otherwise>
	<input type="submit" value="Update Exam" name="button" style="float:right;"/>
	</c:otherwise>
	</c:choose>

	</form>
		</c:when>
		<c:otherwise>
		<br/>
		There was an error loading this page. This entity cannot be edited!<br/>
		</c:otherwise>
		</c:choose>
	
	<br/><br/>
	<a href="/index.jsp">home</a> -- <a href="/listExams.jsp">View All Exams</a> 

</body>
</html>
</jsp:root>