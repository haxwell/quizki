<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
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
		<title>Create Exam</title>
		<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
		<link href="../css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/createQuestion.js" type="text/javascript" ></script> ]]>
		</jsp:text>
				
	</head>
<body>

<jsp:include page="../header.jsp"></jsp:include>

	<h1>Create Exam</h1>

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
	
	<form action="/quizki/secured/ExamServlet" method="post">
		Title: <input type="text" name="examTitle" value="${currentExam.title}"/>  
		<br/><br/>
		List of Questions
		<table style="float:right;">
			<tr>
				<td >
					Topic contains <input type="text" name="topicContainsFilter" value="${mruFilterTopicText}"/>
				</td>
				</tr>
				<tr>
				<td >
					Question contains <input type="text" name="containsFilter" value="${mruFilterText}"/>
				</td>
				</tr>
				<tr>
				<td >
					Include difficulties up to: <select name="difficulty">
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
			<c:forEach var="question" items="${fa_listoquestionstobedisplayed}">
				<input type="checkbox" name="a_chkbox_${question.id}">  ${question.text}</input><br/>
			</c:forEach>
		</div>
		
		<br/>
		<input type="submit" value="Add Questions" name="button"/>
		<br/>
		<br/><br/>		
		List of questions on this exam:<br/>
		
		<div class="listOfQuestions" style="overflow:auto; height:150px; width:100%">
			<c:forEach var="question" items="${currentExam.questions}">
				<input type="checkbox" name="d_chkbox_${question.id}">  ${question.text}</input><br/>
			</c:forEach>
		</div>			
		<br/>
		<input type="submit" value="Delete Questions" name="button"/>
				<br/><br/><br/>
				<hr/>
		<input type="submit" value="Add Exam" name="button" style="float:right;"/>
	</form>
		</c:when>
		<c:otherwise>
		<br/>
		There was an error loading this page. This entity cannot be edited!<br/>
		</c:otherwise>
		</c:choose>
	
	<br/><br/>
	<a href="/quizki/index.jsp">home</a> -- <a href="/quizki/listExams.jsp">View All Exams</a> 

</body>
</html>
</jsp:root>