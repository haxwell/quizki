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
		<title>List Exams</title>
		<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
				
		<jsp:text>
			<![CDATA[ <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js" type="text/javascript"></script> ]]>
		</jsp:text>
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<h1>Generate Exam</h1>
	<br/><br/>

	<form action="/GenerateExamServlet" method="post">
		
		<table style="width:100%">
			<tr><td>
			Number Of Questions: <input type="text" name="numberOfQuestions" value="10"/>
			<br/>  
			Max. Difficulty: <select name="difficulty">
					<c:choose><c:when test="${mruFilterDifficulty == 1}"><option value="junior" selected="selected">Junior</option></c:when><c:otherwise><option value="junior" >Junior</option></c:otherwise></c:choose>
					<c:choose><c:when test="${mruFilterDifficulty == 2}"><option value="intermediate" selected="selected">Intermediate</option></c:when><c:otherwise><option value="intermediate" >Intermediate</option></c:otherwise></c:choose>
					<c:choose><c:when test="${mruFilterDifficulty == 3}"><option value="wellversed" selected="selected">Well-versed</option></c:when><c:otherwise><option value="wellversed" >Well-versed</option></c:otherwise></c:choose>
					<c:choose><c:when test="${mruFilterDifficulty == 4}"><option value="guru" selected="selected">Guru</option></c:when><c:otherwise><option value="guru">Guru</option></c:otherwise></c:choose>
					</select>
			</td>
			<td style="float:right;">
					<td>
						Selected topics will be <select name="includeExclude">
							<c:choose><c:when test="${mruIncludeExclude == 1}"><option value="include" selected="selected">Included</option></c:when><c:otherwise><option value="include" >Included</option></c:otherwise></c:choose>
							<c:choose><c:when test="${mruIncludeExclude == 2}"><option value="exclude" selected="selected">Excluded</option></c:when><c:otherwise><option value="exclude" >Excluded</option></c:otherwise></c:choose>
						</select>
							
					</td>
					<td >
						Topic contains <input type="text" name="topicContainsFilter" value="${mruFilterTopicText}"/>
						<input type="submit" value="Filter" name="button"/><input type="submit" value="Clear Filter" name="button"/>
					</td>			
			</td>
			</tr>
		</table>				

		<br/>
		List of topics:<br/>
		<div class="listOfQuestions" style="overflow:auto; width:100%">
			<table  style="width:100%">
			<c:set var="rowNum" value="0"/>
			<c:forEach var="topic" items="${fa_listofalltopics}">
				<c:set var="rowNum" value="${rowNum + 1}" />
				<c:choose><c:when test="${rowNum % 2 == 0}">
				<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
				</c:when>
				<c:otherwise>
				<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
				</c:otherwise></c:choose>
	
					<td>
						<input type="checkbox" name="a_chkbox_${topic.id}">  ${topic.text}</input>
					</td>
					<jsp:text><![CDATA[</tr>]]></jsp:text>				
				</c:forEach>
			</table>
		</div>
		<br/>
		<input type="submit" value="Select Topics" name="button" />
		<hr/>
			<table style="width:100%">
			<tr>
				<td style="vertical-align:top">
					<table  style="width:50%">
					<tr>			Included</tr>
					<c:set var="rowNum" value="0"/>
					<c:forEach var="topic" items="${fa_listofincludedtopics}">
						<c:set var="rowNum" value="${rowNum + 1}" />
						<c:choose><c:when test="${rowNum % 2 == 0}">
						<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
						</c:when>
						<c:otherwise>
						<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
						</c:otherwise></c:choose>
			
							<td>
								<input type="checkbox" name="a_chkbox_${topic.id}">  ${topic.text}</input>
							</td>
							<jsp:text><![CDATA[</tr>]]></jsp:text>				
						</c:forEach>
					</table>
		
				</td>
				<td style="vertical-align:top">
					<table  style="width:50%">
					<tr> Excluded </tr>
					<c:set var="rowNum" value="0"/>
					<c:forEach var="topic" items="${fa_listofexcludedtopics}">
						<c:set var="rowNum" value="${rowNum + 1}" />
						<c:choose><c:when test="${rowNum % 2 == 0}">
						<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
						</c:when>
						<c:otherwise>
						<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
						</c:otherwise></c:choose>
			
							<td>
								<input type="checkbox" name="a_chkbox_${topic.id}">  ${topic.text}</input>
							</td>
							<jsp:text><![CDATA[</tr>]]></jsp:text>				
						</c:forEach>
					</table>
				</td>
			</tr>
			</table>
			<br/><br/>
			<input type="submit" name="button" value="Clear"/>
		<hr/>
		<input type="submit" value="Generate Exam" name="button" style="float:right;"/>
	</form>
	
	<br/><br/><br/>
	<a href="/index.jsp">home</a>

</body>
</html>
</jsp:root>