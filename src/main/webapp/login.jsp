<!--
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 -->

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html> ]]>
    </jsp:text>
	<!--html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">-->
	<html lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		
		<title>Login - Quizki</title>

		<link href="pkgs/Flat-UI-master/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="pkgs/Flat-UI-master/css/flat-ui.css" rel="stylesheet" />

		<link href="css/quizki-sitewide.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki-buttons.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki-text-input-fields.css" rel="stylesheet" type="text/css"/>

		<link href="images/favicon.ico" rel="shortcut icon"/>

</head>
<body>
	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">

	<br/><br/>

      <c:if test="${not empty requestScope.successes}">
      	<c:forEach var="str" items="${requestScope.successes}">
      		<span class="greenText">${str}</span><br/>	
      	</c:forEach>
      	<br/>
      </c:if>

      <c:if test="${not empty requestScope.validationErrors}">
      	<c:forEach var="str" items="${validationErrors}">
      		<span class="redText">${str}</span><br/>
      	</c:forEach>
      	<br/>
      </c:if>

	When you log in, Quizki can keep track of the questions and exams you create!<br/><br/>

	If you're not yet a user, <a href="register.jsp">click here</a>!<br/><br/>

	<form action="/LoginServlet" method="post">
		Username: <input type="text" name="username"/>
		<br/><br/>
		Password: <input type="password" name="password"/>
		<br/><br/>
		
		<div class="span2">
			<button class="btn btn-block" type="submit" id="loginButton" name="button" value="Log In">Log In</button>
		</div>		
	</form>

</div> <!-- content -->
</div> <!-- container -->	
</body>
</html>
</jsp:root>