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
		<title>Question - Quizki</title>
		<link href="../css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="../css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="../js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[
			<script type="text/javascript">
tinyMCE.init({
        mode : "textareas",
		content_css : "../css/custom_content.css",
		theme_advanced_font_sizes: "10px,12px,13px,14px,16px,18px,20px",
		font_size_style_values : "10px,12px,13px,14px,16px,18px,20px",
        theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyfull,|,formatselect",
        theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,|,undo,redo,|,image,|,hr,removeformat,visualaid,|,sub,sup,|,charmap",
        theme_advanced_buttons3 : "",
		theme_advanced_path : false,
		help_shortcut : ""
});

		//$(function() {
		   //$( document ).tooltip();
		 //});
		 
    $( "#open-event" ).tooltip({
      show: null,
      position: {
        my: "left top",
        at: "left bottom"
      },
      open: function( event, ui ) {
        ui.tooltip.animate({ top: ui.tooltip.position().top + 10 }, "slow" );
      }
    });

    $(document).ready(function() {
    	var foo = $("textarea.tinymce").attr("title");
    	$("textarea.tinymce").attr("title", null);
    	foo = $("textarea.tinymce").attr("title");
    });
    
	$(document).ready(function() {
	    $("#questionType").change(function() {
	    	var val = $("#questionType option:selected").text();
	    	
	    	if (val == 'String' || val == 'Sequence')
	    	{
	    		$(".componentSignifiesChoiceCorrectness").attr("disabled", "disabled"); 
	    	}
	    	else
	    	{
	    		$(".componentSignifiesChoiceCorrectness").attr("disabled", null); 
	    	}
	    });    
    });		 
</script>
]]>
		</jsp:text>
				
	</head>
<body>

<jsp:include page="../header.jsp"></jsp:include>

	<c:choose>
	<c:when test="${empty sessionScope.inEditingMode}">
	<h1>Create Question</h1>
	</c:when>
	<c:otherwise>
	<h1>Edit Question</h1>
	</c:otherwise>
	</c:choose>

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

	<c:choose>
		<c:when test="${empty requestScope.doNotAllowEntityEditing}">
	
		<div >
		<form action="/secured/QuestionServlet" method="post">
			<div >
			<textarea name="questionText" cols="50" rows="15" value="Enter a question...?" class="tinymce">${currentQuestion.text}</textarea><br/>  
			Difficulty: <select name="difficulty" id="questionDifficulty" title="How hard is this question?">
				<c:choose><c:when test="${currentQuestion.difficulty.id == 1}"><option value="junior" selected="selected">Junior</option></c:when><c:otherwise><option value="junior" >Junior</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.difficulty.id == 2}"><option value="intermediate" selected="selected">Intermediate</option></c:when><c:otherwise><option value="intermediate" >Intermediate</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.difficulty.id == 3}"><option value="wellversed" selected="selected">Well-versed</option></c:when><c:otherwise><option value="wellversed" >Well-versed</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.difficulty.id == 4}"><option value="guru" selected="selected">Guru</option></c:when><c:otherwise><option value="guru" >Guru</option></c:otherwise></c:choose>
				</select> | Type: <select name="type" id="questionType" title="What form do the answers come in?">
				<c:choose><c:when test="${currentQuestion.questionType.id == 1}"><option value="Single" selected="selected">Single</option></c:when><c:otherwise><option value="Single">Single</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 2}"><option value="Multiple" selected="selected">Multiple</option></c:when><c:otherwise><option value="Multiple">Multiple</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 3}"><option value="String" selected="selected">String</option></c:when><c:otherwise><option value="String">String</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 4}"><option value="Sequence" selected="selected">Sequence</option></c:when><c:otherwise><option value="Sequence">Sequence</option></c:otherwise></c:choose>
				</select>
				 | Description: <input type="text" size="45" name="questionDescription" value="${currentQuestion.description}" title="Optional. A few words describing the question."/>	
			</div>
			
			<hr/>
			<br/>
			<div >
			Answers --<br/>
			Text: <input type="text" name="choiceText" size="35" title="A potential answer.. What should this choice say?"/>  Is Correct?: 
			<c:choose>
				<c:when test="${currentQuestion.questionType.id > 2}">
					<select name="isCorrect" class="componentSignifiesChoiceCorrectness" title="Is this a valid answer?" disabled="disabled"><option value="no">No</option><option value="yes">Yes</option></select> 
				</c:when>
				<c:otherwise>
					<select name="isCorrect" class="componentSignifiesChoiceCorrectness" title="Is this a valid answer?"><option value="no">No</option><option value="yes">Yes</option></select> 
				</c:otherwise>
			</c:choose>
			
			<input type="submit" value="Add Choice" name="button"/> 
			<c:choose>
				<c:when test="${currentQuestion.questionType.id > 2}">
					<input type="submit" class="componentSignifiesChoiceCorrectness" value="Add True/False" name="button" disabled="disabled"/>
				</c:when>
				<c:otherwise>
					<input type="submit" class="componentSignifiesChoiceCorrectness" value="Add True/False" name="button"/>
				</c:otherwise>
			</c:choose>
			<br/>

			<table>
				<c:forEach var="choice" items="${currentQuestion.choices}">
					<tr>
						<c:if test="${currentQuestion.questionType.id == 4}"><td><input type="text" name="sequenceNum_${choice.id}" value="${choice.sequence}" size="2" maxlength="2" title="Position # in the sequence"/> </td></c:if>
						<td><input type="text" name="choiceText_${choice.id}" value="${choice.text}" title="Make a change, then press Update."/></td>
						<td>Is Correct? <c:if test="${choice.iscorrect == 1}"><c:choose><c:when test="${currentQuestion.questionType.id > 2}"><input disabled="disabled" type="radio" class="componentSignifiesChoiceCorrectness" name="group1_${choice.id}" value="Yes" checked="checked" title="This choice is a correct answer."/>Yes </c:when><c:otherwise><input type="radio" class="componentSignifiesChoiceCorrectness" name="group1_${choice.id}" value="Yes" checked="checked" title="This choice is a correct answer."/>Yes</c:otherwise></c:choose></c:if> 
										<c:if test="${choice.iscorrect == 0}"><c:choose><c:when test="${currentQuestion.questionType.id > 2}"><input disabled="disabled" type="radio" class="componentSignifiesChoiceCorrectness" name="group1_${choice.id}" value="Yes" title="This choice is marked incorrect."/>Yes </c:when><c:otherwise><input type="radio" class="componentSignifiesChoiceCorrectness" name="group1_${choice.id}" value="Yes" title="This choice is marked incorrect."/>Yes </c:otherwise></c:choose></c:if>
										<c:if test="${choice.iscorrect == 1}"><c:choose><c:when test="${currentQuestion.questionType.id > 2}"><input disabled="disabled" type="radio" class="componentSignifiesChoiceCorrectness" name="group1_${choice.id}" value="No" title="This choice is a correct answer."/>No </c:when><c:otherwise><input type="radio" class="componentSignifiesChoiceCorrectness" name="group1_${choice.id}" value="No" title="This choice is a correct answer."/>No </c:otherwise></c:choose></c:if>
										<c:if test="${choice.iscorrect == 0}"><c:choose><c:when test="${currentQuestion.questionType.id > 2}"><input disabled="disabled" type="radio" class="componentSignifiesChoiceCorrectness" name="group1_${choice.id}" value="No" title="This choice is marked incorrect." checked="checked" />No </c:when><c:otherwise><input type="radio" class="componentSignifiesChoiceCorrectness" name="group1_${choice.id}" value="No" title="This choice is marked incorrect." checked="checked" />No </c:otherwise></c:choose></c:if>
						</td>
						<td><input type="submit" value="Update" name="choiceButton_${choice.id}" title="Save this choice's changes."/></td>
						<td><input type="submit" value="Delete" name="choiceButton_${choice.id}" title="Obliterate this choice."/></td>
					</tr>
				</c:forEach>
			</table>
			</div>
			<br/>
			<hr/>
			<div >
			Topics --<br/>
			Text: <input type="text" name="topicText" size="35" title="Whats this question about? (try commas!)"/>  <input type="submit" value="Add Topic" name="button"/>
			<br/>		
			<table>
				<c:forEach var="topic" items="${currentQuestion.topics}">
					<tr>
						<td><input type="text" name="topicText_${topic.id}" value="${topic.text}" readonly="readonly"/></td>
						<td><input type="submit" value="Delete" name="topicButton_${topic.id}" title="Efface this topic."/></td>
					</tr>
				</c:forEach>
			</table>
			</div>

			<br/>
			<hr/>
			<div >
			References --<br/>
			Text: <input type="text" name="referenceText" title="Back it up with facts and stuff.." size="35" />  <input type="submit" value="Add Reference" name="button"/>
			<br/>		
			<table>
				<c:forEach var="ref" items="${currentQuestion.references}">
					<tr>
						<td><input type="text" name="referenceText_${ref.id}" value="${ref.text}" size="35" readonly="readonly"/></td>
						<td><input type="submit" value="Delete" name="referenceButton_${ref.id}" title="Expunge this reference."/></td>
					</tr>
				</c:forEach>
			</table>
			</div>
			
			<br/>
			<br/>
			<hr/>
	<c:choose>
	<c:when test="${empty sessionScope.inEditingMode}">
	<input type="submit" value="Add Question" name="button" style="float:right;"/>
	</c:when>
	<c:otherwise>
	<input type="submit" value="Update Question" name="button" style="float:right;"/>
	</c:otherwise>
	</c:choose>
				
		</form>
		</div>
		
		</c:when>
		<c:otherwise>
			<br/>
			There was an error loading this page. This entity cannot be edited!<br/>
		</c:otherwise>
	</c:choose>
	
	<br/><br/>
	<a href="/index.jsp">home</a>  ---  <a href="listQuestions.jsp">List All Questions</a>
	

</body>
</html>
</jsp:root>