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
<title>Index - Questions JEE</title>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

<br/><br/>
<br/><br/>
TAKE<br/> 
Get a quick exam on ...<br/>   
			<c:forEach var="topic" items="${fa_listofmajortopics}">
				-- <a class="greyLink" href="beginExam.jsp?topicId=${topic.id}">${topic.text} </a><br/>
			</c:forEach>
<br/>
Generate <a class="greyLink" href="generateExam.jsp">a quick exam</a> on another topic.<br/>
Create <a class="greyLink" href="secured/createExam.jsp">your own unique exam</a>.<br/><br/>
<br/>
GIVE<br/>
<a class="greyLink" href="secured/createQuestion.jsp">a question of your own</a><br/><br/><br/>
BROWSE<br/>
<a class="greyLink" href="listExams.jsp">see a list of exams</a><br/>
<a class="greyLink" href="secured/listQuestions.jsp">see a list of questions</a><br/><br/><br/><br/>


    <c:choose>
     <c:when test="${empty sessionScope.currentUserEntity}">
	You can <a class="greyLink" href="login.jsp">register, and/or login</a> here.<br/>
     </c:when>
     <c:otherwise>
     	You are logged in. <a class="greyLink" href="logout.jsp">logout</a><br/>
     </c:otherwise>
    </c:choose>

</body>
</html>
</jsp:root>
