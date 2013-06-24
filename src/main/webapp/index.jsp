<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" xmlns:qfn="http://quizki.com/tld/qfn" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<title>Home Page - Quizki</title>
		
		<link href="pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="pkgs/jquery-ui/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki.css" rel="stylesheet" type="text/css"/>
		<link href="css/styles.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" href="images/favicon.ico" />
		
		<jsp:text>
			
			<![CDATA[ <script src="pkgs/jquery/jquery-1.10.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="pkgs/jquery-ui/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/jquery.cookie.js" type="text/javascript"></script> ]]>
		
			<![CDATA[
				<script type="text/javascript">

   					//$(document).ready(function() {
					//	var currentSessionCookieVal = $.cookie('quizki.currentSessionCookie'); 
					//
					//	if (currentSessionCookieVal == null)
					//		$("#welcomeToQuizki-dialog").dialog({modal:true,width:530,title:"Welcome to Quizki!"}).dialog();
					//});

					// Handler for the modal dialog CLOSING
				    //$(document).ready(function(){
					//	$('div#welcomeToQuizki-dialog').bind('dialogclose', function(event) {
					//	    //var v = $.cookie('quizki.userHasBeenHereBefore');
					//	    //$.cookie('quizki.userHasBeenHereBefore', v+1, { expires: 30 });

					//	    $.cookie('quizki.currentSessionCookie', 0); // create cookie for current session
					//	});
 				    //});
				    
				</script>
			]]>
		</jsp:text>
		
</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">


<br/>
<div class="mainContentArea">
<br/>
<div class="center"><a href="getQuestionsTestPage.jsp">getQuestionsTestPage</a></div><br/>
<div class="center"><b>Quizki collects questions and answers, allowing you to test yourself with practice exams.</b></div><br/>
<hr style="margin-right:40%; margin-left:40%;"/>

<div class="center">TAKE a practice exam on...</div><br/>
<div class="center"> 

	<c:set var="topicCount" value="${fn:length(fa_listofmajortopics)}" scope="page"/>
	<c:set var="counter" value="0" scope="page"/>
	<c:set var="columnCounter" value="0" scope="page"/>
	<c:set var="maxColumns" value="3" scope="page"/>
	<c:set var="newRowNeeded" value="true" scope="page"/>
	<c:set var="closingNewRowNeeded" value="false" scope="page"/>
	
	<table class="center">
		<c:forEach var="topic" items="${fa_listofmajortopics}">
			<c:if test="${newRowNeeded}">
				<jsp:text><![CDATA[<tr>]]></jsp:text>
				<c:set var="newRowNeeded" value="false" scope="page"/>
				<c:set var="closingNewRowNeeded" value="true" scope="page"/>
			</c:if>
			
			<c:if test="${counter + 1 >= topicCount}">
				<c:if test="${columnCounter == 0}">
					<jsp:text><![CDATA[<td style="padding:3px"></td>]]></jsp:text>
					<c:set var="columnCounter" value="${columnCounter + 1}" scope="page"/>
				</c:if>				
			</c:if>
			
			<td style="padding:3px"><a class="greyLink" id="autoExamLink_${counter}" href="beginExam.jsp?topicId=${topic.id}">${topic.text} </a></td>
			<c:set var="columnCounter" value="${columnCounter + 1}" scope="page"/>
			<c:set var="counter" value="${counter + 1}" scope="page"/>
			
			<c:if test="${columnCounter >= maxColumns}">
				<jsp:text><![CDATA[</tr>]]></jsp:text>
				<c:set var="newRowNeeded" value="true" scope="page"/>
				<c:set var="closingNewRowNeeded" value="false" scope="page"/>
				<c:set var="columnCounter" value="0" scope="page"/>
			</c:if>
		</c:forEach>
		
		<c:if test="${closingNewRowNeeded}">
			<jsp:text><![CDATA[</tr>]]></jsp:text>
			<c:set var="closingNewRowNeeded" value="false" scope="page"/>
		</c:if>
	</table>

	<br/>
	<c:if test="${not empty totalNumberOfTopics}">
		Or one of ${totalNumberOfTopics - topicCount} <a class="greyLink" href="generateExam.jsp">other topics</a>.. 
	</c:if>
		<br/><br/>Or you can take <a class="greyLink" href="listExams.jsp">an exam created by another user</a>..
	
	<br/><br/>
</div>			

<hr style="margin-right:40%; margin-left:40%;"/>

<br/>
<div class="center">CREATE your own unique..</div><br/>

	<table class="center">
		<tr>
			<td style="text-align:left; width:33%;"><a class="greyLink" id="createQuestionLink" href="secured/question.jsp">Question</a></td>
			<td style="width:33%;"></td>
			<td style="text-align:right; width:33%;"><a class="greyLink" id="createExamLink" href="secured/exam.jsp">Exam</a></td>
		</tr>
	</table>

<br/>
<hr style="margin-right:40%; margin-left:40%;"/>

	    <c:choose>
	    	<c:when test="${not empty sessionScope.currentUserEntity}">
				<br/>
				<div class="center">VIEW</div>
				<br/>
				<div class="center"><a class="greyLink" href="secured/profile.jsp">your profile</a></div>
				<br/>

				<hr style="margin-right:40%; margin-left:40%;"/>

			</c:when>
		</c:choose>

<br/><br/>
<div class="center">Information <a href="about.jsp">about Quizki</a>. 

    <c:choose>
     <c:when test="${empty sessionScope.currentUserEntity}">
	You can <a class="greyLink" href="login.jsp" id="login">register, and/or login</a> here.
     </c:when>
     <c:otherwise>
     	You are logged in. <a class="greyLink" href="logout.jsp" id="logout">logout</a>
     </c:otherwise>
    </c:choose>
    
</div>    

</div> <!-- mainContentArea -->

</div>
</div>
</body>
</html>
</jsp:root>
