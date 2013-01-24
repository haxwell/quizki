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
		<title>Login - Quizki</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<h1>Login</h1>
	<br/><br/>

      <c:if test="${not empty requestScope.validationErrors}">
      	<c:forEach var="str" items="${validationErrors}">
      		<span class="redText">${str}</span><br/>
      	</c:forEach>
      	<br/>
      </c:if>

	When you log in, Quizki can tell which questions and exams are yours!<br/><br/>

	If you're not yet a user, <a href="register.jsp">click here</a>!<br/><br/>

	<form action="/LoginServlet" method="post">
		Username: <input type="text" name="username"/>
		<br/><br/>
		Password: <input type="password" name="password"/>
		<br/><br/>
		
		<input type="submit" value="Log In" name="button" />
	</form>

	<br/><br/>
	<a href="/index.jsp">home</a>
	
</body>
</html>
</jsp:root>