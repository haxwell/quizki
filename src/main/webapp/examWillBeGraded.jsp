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
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		
		<title>Exam Will Be Graded - Quizki</title>

		<link href="pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="pkgs/jquery-ui/jquery-ui-1.10.4/css/ui-lightness/jquery-ui-1.10.4.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki.css" rel="stylesheet" type="text/css"/>
		<link rel="shortcut icon" href="images/favicon.ico" />

		<jsp:text>
			<![CDATA[ <script src="pkgs/jquery/jquery-1.11.1.min.js" type="text/javascript"></script> ]]>
		</jsp:text>
				
	</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">


	<form action="/TakeExamServlet" method="post">
	<br/><br/><br/><br/>
	<h3>You've completed this exam!</h3><br/><br/>
	You can go back to review/change your answers, or grade the exam to see how you did!<br/><br/>
	
		<div class="span6">
			<div class="span3" style="display:inline">
				<button class="btn btn-block" id="beginButton" type="submit" name="button" value="Review Answers">Review Answers</button>
			</div>
			<div class="span1"></div>
			<div class="span2" style="display:inline">
				<button class="btn btn-block" id="beginButton" type="submit" name="button" value="Grade Exam">Grade Exam</button>
			</div>
		</div>
		
	</form>
	
	<br/><br/>
	
</div> <!-- content -->
</div> <!-- container -->
</body>
</html>
</jsp:root>