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
		
		<!-- link href="pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" /  -->
		<link href="pkgs/Flat-UI-master/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="pkgs/Flat-UI-master/css/flat-ui.css" rel="stylesheet" />
		<!-- link href="pkgs/jquery-ui/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css"/  -->

		<!-- link href="css/quizki.css" rel="stylesheet" type="text/css"/  -->
		<link href="css/quizki-sitewide.css" rel="stylesheet" type="text/css"/>		
		<link href="css/index.css" rel="stylesheet" type="text/css"/>

		<link href="images/favicon.ico" rel="shortcut icon"/>

<style> 
div.box
{
border:2px solid #a1a1a1;
padding:10px 40px; 
background:#dddddd;
border-radius:25px;
height:100%;
}

div.base
{
 width: auto;
 min-width: 200px;
 padding: 0px;
 display:table;
 height:100%;
}

.base-row {
 Display: table-row;
 height:100%;
 }
.base li {
 display: table-cell;
 width: 33%;
 }
 
</style>
		
</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">


<br/>
<div class="mainContentArea">
<br/>
<!-- div class="center"><a href="getQuestionsTestPage.jsp">getQuestionsTestPage</a></div><br/  -->
<div class="center"><b>Quizki collects questions and answers, allowing you to test yourself with practice exams.</b></div><br/>
<hr style="margin-right:40%; margin-left:40%;"/>

<div class="base center">
	<ul class="base-row">
		<li >
			<div class="box"> Take an exam on..
				<c:set var="topicCount" value="${fn:length(fa_listofmajortopics)}" scope="page"/>
				<c:set var="counter" value="0" scope="page"/>
				<c:set var="columnCounter" value="0" scope="page"/>
				<c:set var="maxColumns" value="1" scope="page"/>
				<c:set var="newRowNeeded" value="true" scope="page"/>
				<c:set var="closingNewRowNeeded" value="false" scope="page"/>
				
				<table class="center" >
					<c:forEach var="topic" items="${fa_listofmajortopics}">
						<c:if test="${newRowNeeded}">
							<jsp:text><![CDATA[<tr>]]></jsp:text>
							<c:set var="newRowNeeded" value="false" scope="page"/>
							<c:set var="closingNewRowNeeded" value="true" scope="page"/>
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
			
			</div>
		</li>
		<li >
			<div class="box"><div> <a class="greyLink" href="generateExam.jsp">Generate an Exam on-the-fly</a></div></div>
		</li>
		<li >
			<div class="box"> <a class="greyLink" href="listExams.jsp">Search through all Exams</a></div>
		</li>
	</ul>	
</div>	

	<br/>

<hr style="margin-right:40%; margin-left:40%;"/>

<br/>
<div class="center">CREATE your own unique..</div><br/>

<div class="base center">
	<ul class="base-row">
		<li >
			<div class="box"> <a class="greyLink" id="createQuestionLink" href="secured/question.jsp">Question</a></div>
		</li>
		<li></li>
		<li >
			<div class="box"> <a class="greyLink" id="createExamLink" href="secured/exam.jsp">Exam</a></div>
		</li>
	</ul>
</div>

<br/>
<hr style="margin-right:40%; margin-left:40%;"/>

	    <c:choose>
	    	<c:when test="${not empty sessionScope.currentUserEntity}">

				<br/>
				<div class="center">VIEW</div>
				<br/>
				
				<div class="base center">
					<ul class="base-row">
						<li></li>
						<li >
							<div class="box"> <a class="greyLink" href="secured/profile.jsp">your profile</a> </div>
						</li>
						<li></li>
					</ul>
				</div>

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

<br/><br/>
</div>    

</div> <!-- mainContentArea -->

</div>
</div>
</body>
</html>
</jsp:root>
