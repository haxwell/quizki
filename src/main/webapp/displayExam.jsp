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
		<title>Display Exam</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		<link href="css/displayExam.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ 
			 			<script type="text/javascript">
			 $(document).ready(function(){
	// set the height of the content area according to the browser height
	var bottomBufferHeight = 250;
	var windowHeight = $(window).height();
	
	$('#center').height(windowHeight - bottomBufferHeight);
});
 
 $(document).ready(function(){
     $(window).resize(function() {
             var bottomBufferHeight = 250;
             var windowHeight = $(window).height();

             $('#center').height(windowHeight - bottomBufferHeight);
     })});
     </script>
]]>     
			
		</jsp:text>
				
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<h1>Display Exam</h1>
	<br/>

	Exam Title: "${currentExam.title}", ${currentExam.numberOfQuestions} questions.
	
	<c:if test="${not empty sessionScope.allowGeneratedExamToBeTaken}">
		-- <a href="beginExam.jsp">Take this exam</a><br/>
	</c:if>
	
	<br/><br/>

			<div id="center" class="listOfQuestions" style="overflow:auto; height:150px; width:100%">
	<table class="displayExam">
		<thead>
			<th>ID</th>
			<th>Question</th>
			<th>Topics</th>
			<th>Difficulty</th>
		</thead>
		<tbody>
		<c:set var="rowNum" value="0"/>
		<c:forEach var="question" items="${currentExam.questions}">
			<c:set var="rowNum" value="${rowNum + 1}" />
			<c:choose><c:when test="${rowNum % 2 == 0}">
			<jsp:text><![CDATA[<tr>]]></jsp:text>
			</c:when>
			<c:otherwise>
			<jsp:text><![CDATA[<tr class="rowHighlight">]]></jsp:text>
			</c:otherwise></c:choose>
			
				<td>${question.id}</td>   
				<c:choose>
				<c:when test="${empty question.description}">
					<td>
					<c:choose>
						<c:when test="${empty sessionScope.shouldQuestionsBeDisplayed}">
							<a href="/displayQuestion.jsp?questionId=${question.id}">${question.textWithoutHTML}</a>
						</c:when>
						<c:otherwise>
							${question.textWithoutHTML}
						</c:otherwise>
					</c:choose>
					</td>
				</c:when>
				<c:otherwise>
					<td>
					<c:choose>
						<c:when test="${empty sessionScope.shouldQuestionsBeDisplayed}">
							<a href="/displayQuestion.jsp?questionId=${question.id}">${question.description}</a>
						</c:when>
						<c:otherwise>
							${question.description}
						</c:otherwise>
					</c:choose>
					</td>
				</c:otherwise>
				</c:choose>
				<td>
					<c:forEach var="topic" items="${question.topics}">
						${topic.text}<br/>
					</c:forEach>
				</td>
				<td>${question.difficulty.text}</td>
			<jsp:text><![CDATA[</tr>]]></jsp:text>
		</c:forEach>
		</tbody>
	</table>
	
	</div>
	<br/><br/>

	<a href="/index.jsp">home</a> -- <a href="javascript:history.go(-1)">Go Back to ${textToDisplayForPrevPage}</a> 

</body>
</html>
</jsp:root>