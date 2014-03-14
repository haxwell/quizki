<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<title>Login Failed - Quizki</title>
		
		<link href="pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="pkgs/jquery-ui/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki.css" rel="stylesheet" type="text/css"/>
		<link rel="shortcut icon" href="images/favicon.ico" />

		<jsp:text>
			<![CDATA[ <script src="pkgs/jquery/jquery-1.10.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/createQuestion.js" type="text/javascript" ></script> ]]>
		</jsp:text>
				
	</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">


	<h1>Login</h1>
	<br/><br/>

	Login failed!!
	
	<br/><br/>
	<a href="index.jsp">home</a> --	<a href="login.jsp">login</a><br/>

</div>
</div>
	
</body>
</html>
</jsp:root>