<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Begin Exam - Quizki</title>
		
		<link href="/css/questions.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<jsp:include page="/header.jsp"></jsp:include>

      <c:choose>
      <c:when test="${empty sessionScope.fa_listofexamtopics}">
      	<br/><br/>
		Oops! Something went wrong! You should <a href="/index.jsp">go back to the beginning</a>.
      </c:when>
      <c:otherwise>

		<br/><br/><br/>
		You are about to begin an exam with questions covering<br/><br/>
		
			<c:forEach var="topicText" items="${fa_listofexamtopics}">
				-- ${topicText}<br/>
			</c:forEach>
<br/><br/>
		There are ${totalNumberOfQuestionsInCurrentExam} questions in this exam!<br/><br/>
		
		<form action="/BeginExamServlet" method="post">
		
			<div class="hidden" id="radioButtonExample"><input type="text" name="examId" value="${requestScope.examId}"/><input type="text" name="topicId" value="${requestScope.topicId}"/></div> 
			<hr/>			
			Press <input type="submit" value="BEGIN!" name="button"/> to get to it!<br/>
				
		</form>
	</c:otherwise>		
	</c:choose>		
		
<br/><br/><br/><br/>
<a href="/index.jsp">home</a> <br/>


</body>
</html>
</jsp:root>