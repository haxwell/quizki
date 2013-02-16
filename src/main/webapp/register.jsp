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
		<title>Register A New User - Quizki</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/createQuestion.js" type="text/javascript" ></script> ]]>
		</jsp:text>
				
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<h1>Register</h1>
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

	<form action="/RegisterUserServlet" method="post">
		Requested Username: <input type="text" name="username"/>
		<br/><br/>
		Requested Password: <input type="text" name="password"/>
		<br/><br/>
		
		<jsp:scriptlet>
			ReCaptcha c = ReCaptchaFactory.newReCaptcha("6LdhFt0SAAAAAL2P83cVVXFInUNdpRbJobjstezT", "6LdhFt0SAAAAAHYNTH8dOf7Yb3edDb7K51y5yQ9T", false);
			out.print(c.createRecaptchaHtml(null, null));
		</jsp:scriptlet>
        <br/>

        <input type="submit" value="Create Log In" name="button" />
	</form>

	<br/><br/>
	<a href="/index.jsp">home</a>
	
</body>
</html>
</jsp:root>