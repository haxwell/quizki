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
		
		<title>Begin Exam - Quizki</title>
		
		<link href="bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki.css" rel="stylesheet" type="text/css"/>
		<link href="css/styles.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" href="../images/favicon.ico" />
</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">

      <c:choose>
	      <c:when test="${empty sessionScope.fa_listofexamtopics}">
	      	<br/><br/>
			Oops! Something went wrong! You should <a href="/index.jsp">go back to the beginning</a>.
	      </c:when>
      <c:otherwise>
		<br/><br/><br/>
		
		<c:set var="createdByName" value=""/>
		<c:choose>
		<c:when test="${empty sessionScope.currentExam.user}">
			<c:set var="createdByName" value="quizki.com"/>
		</c:when>
		<c:otherwise>
			<c:set var="createdByName" value="${sessionScope.currentExam.user.username}"/>
		</c:otherwise>
		</c:choose>
		
		You are about to take the exam "${sessionScope.currentExam.title}", created by ${createdByName}<br/><br/> 
		
		This exam's difficulty is ${sessionScope.currentExam.difficulty}. It has questions covering<br/><br/>
		
			<c:forEach var="topicText" items="${fa_listofexamtopics}">
				-- ${topicText}<br/>
			</c:forEach>
		<br/><br/>
		There are ${totalNumberOfQuestionsInCurrentExam} questions in this exam!<br/><br/><br/>

		<c:if test="${not qfn:isNullOrEmpty(sessionScope.currentExam.message)}">
			${sessionScope.currentExam.user.username} 's message to you: <br/>  ${sessionScope.currentExam.message}<br/>
		</c:if>
		
		
		<form action="/BeginExamServlet" method="post">
		
			<hr/>			
			<div class="span2">
				<button class="btn btn-block" id="beginButton" type="submit" name="button">Begin Exam</button>
			</div>
				
		</form>
	</c:otherwise>		
	</c:choose>		

</div> <!-- content -->
</div> <!-- container -->
</body>
</html>
</jsp:root>