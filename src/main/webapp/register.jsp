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
	<jsp:directive.page import="net.tanesha.recaptcha.ReCaptcha" />
    <jsp:directive.page import="net.tanesha.recaptcha.ReCaptchaFactory" />
    <jsp:directive.page import="java.util.logging.Logger" />
    <jsp:directive.page import="java.util.logging.Level" />
    
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<title>Register A New User - Quizki</title>

		<link href="pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="pkgs/jquery-ui/jquery-ui-1.10.4/css/ui-lightness/jquery-ui-1.10.4.css" rel="stylesheet" type="text/css"/>

		<link href="css/quizki-sitewide.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki-buttons.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki-text-input-fields.css" rel="stylesheet" type="text/css"/>

		<link rel="shortcut icon" href="images/favicon.ico" />
		
		<jsp:text>
			<![CDATA[ <script src="pkgs/jquery/jquery-1.11.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/createQuestion.js" type="text/javascript" ></script> ]]>
		</jsp:text>
		
	     <script src="https://www.google.com/recaptcha/api.js" async defer></script>
	     <script>
	       function onSubmit(token) {
	         document.getElementById("new-registration-form").submit();
	       }
	     </script>		 
				
	</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">


	<br/>

      <c:if test="${not empty requestScope.successes}">
      	<c:forEach var="str" items="${requestScope.successes}">
      		<span class="greenText">${str}</span><br/>	
      	</c:forEach>
      	<br/>
      </c:if>
      
      <c:if test="${not empty requestScope.validationErrors}">
      	<c:forEach var="str" items="${requestScope.validationErrors}">
      		<span class="redText">${str}</span><br/>
      	</c:forEach>
      	<br/>      	
      </c:if>

	<form id="new-registration-form" action="/RegisterUserServlet" method="post">
		Requested Username: <input type="text" name="username"/>
		<br/><br/>
		Requested Password: <input type="text" name="password"/>
		<br/><br/>
		
		<div class="span2">
			<button class="btn btn-block g-recaptcha" type="submit" name="button" value="Create Log In" data-sitekey="6LfdE90SAAAAACMMvcSYY_eDCB8dhhmY8hemrvKr" data-callback='onSubmit'>Create Log In</button>
		</div>        
	</form>

	
</div>
</div>

</body>
</html>
</jsp:root>