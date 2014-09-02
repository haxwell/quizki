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
<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		
		<title>Exam Report Card Graded - Quizki</title>

		<link href="pkgs/Flat-UI-master/bootstrap/css/bootstrap.css" rel="stylesheet"/>
		<link href="pkgs/Flat-UI-master/css/flat-ui.css" rel="stylesheet"/>
		<link href="pkgs/font-awesome/css/font-awesome.css" rel="stylesheet"/>
		<link href="pkgs/jquery-ui/jquery-ui-1.10.4/css/ui-lightness/jquery-ui-1.10.4.css" rel="stylesheet" type="text/css"/>

		<link href="css/quizki-sitewide.css" rel="stylesheet" type="text/css"/> 
		<link href="css/quizki-buttons.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki-text-input-fields.css" rel="stylesheet" type="text/css"/>

		<link href="images/favicon.ico" rel="shortcut icon"/>

		<jsp:text>
			<![CDATA[ <script src="pkgs/jquery/jquery-1.11.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="pkgs/jquery-ui/jquery-ui-1.10.4/js/jquery-ui-1.10.4.min.js" type="text/javascript"></script> ]]>			
			
			<![CDATA[
			<script type="text/javascript">
					$(document).ready(function(){
						$("#btnSendFeedback").click(function(){
							//alert("btn pushed!");
							$.post("/examReportCard-sendFeedback.jsp",
							{
								examFeedbackTextarea: $('#id_examFeedbackTextarea').attr('value')
							},
							function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									$('#divFeedbackMsgToUser').html('Your feedback has been saved!');
									$('#id_examFeedbackTextarea').attr('disabled','true');
									$('#btnSendFeedback').attr('disabled','true');
								}
							});
						});
						
						$("#btnHome").click(function() {
							window.location.href = "/index.jsp";
						});
					});					
		    </script> ]]>			
		</jsp:text>
				
	</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">


	<jsp:scriptlet>      
	request.getSession().setAttribute(Constants.SHOULD_ALLOW_QUESTION_EDITING, false);
	</jsp:scriptlet>
	
<br/><br/><br/>
	
		<div class="input-prepend">
			<span class="add-on short green"><![CDATA[<i class="icon-ok"></i>]]></span>
			<input class="span1" type="text" value="${numberOfQuestionsAnsweredCorrectly}" size="2" disabled="disabled"/>
		</div>
		
		<div class="input-prepend">
			<span class="add-on short red"><![CDATA[<i class="icon-remove"></i>]]></span>
			<input class="span1" type="text" value="${totalNumberOfQuestions - numberOfQuestionsAnsweredCorrectly}" size="2" disabled="disabled"/>
		</div>
		
	You got <span class="greenText"><strong>${numberOfQuestionsAnsweredCorrectly}</strong></span> questions correct and <span class="redText"><strong>${totalNumberOfQuestions - numberOfQuestionsAnsweredCorrectly}</strong></span> incorrect out of a total of <strong>${totalNumberOfQuestions}</strong>. <br/><br/>Following are the details of the exam. You can click on a question for more information.
	
	<br/><br/>
	<c:forEach var="answeredQuestion" items="${mostRecentExamResults.iterator}">
		<c:choose>
			<c:when test="${answeredQuestion.isCorrect}">
				<strong><![CDATA[<i class="icon-circle-blank greenText span1 examReportCardDetailLine"></i>]]></strong>
				<c:choose>
					<c:when test="${empty answeredQuestion.question.description}">
						<a class="span11 examReportCardDetailLine" style="color:#414151;" href="/displayQuestion.jsp?questionId=${answeredQuestion.question.id}">${answeredQuestion.question.textWithoutHTML}</a>
					</c:when>
					<c:otherwise>
						<a class="span11 examReportCardDetailLine" style="color:#414151;" href="/displayQuestion.jsp?questionId=${answeredQuestion.question.id}">${answeredQuestion.question.description}</a>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<strong><![CDATA[<i class="icon-circle-blank redText span1 examReportCardDetailLine"></i>]]></strong>
				<c:choose>
					<c:when test="${empty answeredQuestion.question.description}">
						<a class="span11 examReportCardDetailLine" style="color:#414151;" href="/displayQuestion.jsp?questionId=${answeredQuestion.question.id}">${answeredQuestion.question.textWithoutHTML}</a>
					</c:when>
					<c:otherwise>
						<a class="span11 examReportCardDetailLine" style="color:#414151;" href="/displayQuestion.jsp?questionId=${answeredQuestion.question.id}">${answeredQuestion.question.description}</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		
		<br/>
		
	</c:forEach>	
	
	<br/><br/>	<br/><br/>
	
	<input type="submit" class="span12 btn" id="btnHome" value="Home"/>
	
	<div id="divFeedbackOverall">
	<div id="divFeedbackMsgToUser"></div>
	<!-- input type="text"
		placeholder="You can leave feedback for the exam creator here...."
		class="span8 hidden" maxlength="255"
		id="id_examMessage" name="examMessage"
		value="${sessionScope.feedbackForCurrentUserAndExam}" / -->
		<!-- input type="submit" class="span3 btn hidden" id="btnSendFeedback" value="Send Feedback" name="button"/  -->		
	</div>
	
</div>
</div>

</body>
</html>
</jsp:root>