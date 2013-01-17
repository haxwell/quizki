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
		<title>Display Question!!!</title>
		<link href="css/smoothness/jquery-ui-1.8.24.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="js/displayQuestion.js" type="text/javascript" ></script> ]]>			
			<![CDATA[
				<script type="text/javascript">
					tinyMCE.init({
					        mode : "textareas",
					        readonly : 1,
							content_css : "css/custom_content.css"
					});
				</script>
			]]>
			<![CDATA[
				<script type="text/javascript">

					var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
					var values = ${listOfCurrentQuestionsChoicesValues};
					var selected = ${listSayingAnElementIsCheckedOrNot};
					var isCorrectList = ${listSayingWhichChoicesAreCorrect};
					var examHistoryIsPresent = ${booleanExamHistoryIsPresent};
	
					$(document).ready(function() {
						$('div.choices').html('');
		
						if (${currentQuestion.questionType.id} == 1) {
							addChoiceInputsForThisQuestionType('#radioButtonExample');
						}
						else if (${currentQuestion.questionType.id} == 2) {
							addChoiceInputsForThisQuestionType('#checkboxExample');
						}
					});
							
				</script>
			]]>
		</jsp:text>

	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<h1>Display Question</h1>

		<form action=".">
		Creator: ${currentQuestion.user.username}<br/>
	<c:choose>
	<c:when test="${not empty currentQuestion.description}">
		Description: ${currentQuestion.description}<br/>
	</c:when>
	</c:choose>
		<br/>
		Text: <textarea name="questionText" cols="50" rows="15">${currentQuestion.text}</textarea><br/>  
		Difficulty: ${currentQuestion.difficulty.text} <br/>
		Type: ${currentQuestion.questionType.text}  <br/>
		<hr/>
		<br/>
		Choices --<br/>

		<div class="choices" style="margin-left:25px">.</div>

		<hr/>
		<br/>
		Topics --<br/>
		<div style="margin-left:25px">
			<table style="width:100%">
							<c:set var="rowNum" value="0"/>
							<c:forEach var="topic" items="${currentQuestion.topics}">
								<c:set var="rowNum" value="${rowNum + 1}" />
								<c:choose><c:when test="${rowNum % 2 == 0}">
								<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
								</c:when>
								<c:otherwise>
								<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
								</c:otherwise></c:choose>
					
									<td>
										${topic.text}
									</td>
									<jsp:text><![CDATA[</tr>]]></jsp:text>				
							</c:forEach>
			</table>
		</div>
		<hr/>
		<br/>
		References --<br/>
		<div style="margin-left:25px">
			<table style="width:100%">
							<c:set var="rowNum" value="0"/>
							<c:forEach var="reference" items="${currentQuestion.references}">
								<c:set var="rowNum" value="${rowNum + 1}" />
								<c:choose><c:when test="${rowNum % 2 == 0}">
								<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
								</c:when>
								<c:otherwise>
								<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
								</c:otherwise></c:choose>
					
									<td>
										${reference.text}
									</td>
									<jsp:text><![CDATA[</tr>]]></jsp:text>				
							</c:forEach>
			</table>
		</div>
		</form>
		
		<!-- TODO: Add ability to store and display references for a question -->
		
		<br/><br/>
	<a href="/index.jsp">home</a> -- <a href="javascript:history.go(-1)">Go Back to ${textToDisplayForPrevPage}</a>
	
	<div class="hidden" id="radioButtonExample"><div class="??3 ??4"><input type="radio" disabled="disabled" name="group1" value="??2" selected=""/>??1</div></div>	
	<div class="hidden" id="checkboxExample"><div class="??3 ??4"><input type="checkbox" disabled="disabled" name="??2" value="??2" selected=""/>??1</div></div>

</body>
</html>
</jsp:root>