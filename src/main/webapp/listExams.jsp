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
		<title>List Exams - Quizki</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
				
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

		<br/>
		
		<c:choose>
		<c:when test="${empty fa_listofexamstobedisplayed}">
		<br/><br/>You haven't created any exams yet! (<a href="/secured/exam.jsp">Create Exam</a>)
		</c:when>
		<c:otherwise>
		These are the available exams:
		</c:otherwise>
		</c:choose>
		
		<br/><br/>

		<form action="/ListExamsServlet" method="post">
		<table>
			<c:forEach var="exam" items="${fa_listofexamstobedisplayed}">
				<tr>
					<td>${exam.id}</td>   
					<td>${exam.title}</td>					
					<td>--</td>
					<td><input type="submit" value="Take Exam" name="examButton_${exam.id}"/></td>
					<c:choose><c:when test="${exam.user.id == currentUserEntity.id}">
						<td><input type="submit" value="Edit Exam" name="examButton_${exam.id}"/></td>
					</c:when>
					<c:otherwise>
						<td><input type="submit" value="Detail Exam" name="examButton_${exam.id}"/></td>
					</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
		</table>
		</form>
		<br/>		<br/>

		<form action="/ListExamsServlet" method="post">		
		Show Exams for <input type="radio" name="group1" value="everyone" selected=""/>Everyone or <input type="radio" name="group1" value="mine" selected=""/>The ones I created.
		<br/>
		<input type="submit" value="Refresh The List" name="button"/>
		</form>
		
<br/><br/><br/><br/>
<a href="/index.jsp">home</a><br/>


</body>
</html>
</jsp:root>