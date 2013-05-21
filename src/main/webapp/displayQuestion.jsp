<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" xmlns:qfn="http://quizki.com/tld/qfn" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <?xml version="1.0" encoding="UTF-8" ?> ]]>
    </jsp:text>
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Display Question - Quizki</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="js/displayQuestion.js" type="text/javascript" ></script> ]]>			
			<![CDATA[
				<script type="text/javascript">
					tinyMCE.init({
					        theme : "advanced",
					        mode : "textareas",
					        plugins : "autoresize",
					        readonly : 1,
							content_css : "css/custom_content.css"
					});
				</script>
			]]>
			
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>			
			
			<![CDATA[
			<script type="text/javascript">
					$(document).ready(function(){
						$("#btnThumbsUp").click(function(){
							//alert("btn pushed!");
							$.post("/registerVote.jsp",
							{
								voteDirection: "up",
								entityKey: "displayQuestion"
							},
							function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									$('#divVotingButtons').html('Your vote has been saved!');
								}
							});
						});
					});					

					$(document).ready(function(){
						$("#btnThumbsDown").click(function(){
							//alert("btn pushed!");
							$.post("/registerVote.jsp",
							{
								voteDirection: "down",
								entityKey: "displayQuestion"
							},
							function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									$('#divVotingButtons').html('Your vote has been saved!');
								}
							});
						});
					});					
		    </script> ]]>			

			<![CDATA[
			<script type="text/javascript">
			
//		$(function() {
	//	   $( document ).tooltip();
		// });
		 
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
    </script> ]]>
			
			<![CDATA[
				<script type="text/javascript">
			]]>

			// depending on how this page was called, these variables may or may not be present.. so rather than get 
			//  exceptions reporting their absence or malformedness, they are defined as undefined if not present.
			
			<c:choose><c:when test="${not empty sessionScope.userSuppliedAnswerToStringQuestion}">
				<![CDATA[ var userSuppliedAnswerWhenQuestionIsOfTypeString = ${userSuppliedAnswerToStringQuestion}; ]]>
			</c:when>
			<c:otherwise>
				<![CDATA[ var userSuppliedAnswerWhenQuestionIsOfTypeString = undefined; ]]>
			</c:otherwise>
			</c:choose>
			<c:choose><c:when test="${not empty sessionScope.listOfSequenceNumbersTheUserChose}">
				<![CDATA[ var sequenceNumbersTheUserChose = ${listOfSequenceNumbersTheUserChose}; ]]>
			</c:when>
			<c:otherwise>
				<![CDATA[ var sequenceNumbersTheUserChose = undefined; ]]>
			</c:otherwise>
			</c:choose>
			<c:choose><c:when test="${not empty sessionScope.listOfSequenceNumbersForChoices}">
				<![CDATA[ var fieldSequenceNumbers = ${listOfSequenceNumbersForChoices}; ]]>
			</c:when>
			<c:otherwise>
				<![CDATA[ var fieldSequenceNumbers = undefined; ]]>
			</c:otherwise>
			</c:choose>
			<c:choose><c:when test="${not empty sessionScope.listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion}">
				<![CDATA[ var selected = ${listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion}; ]]>
			</c:when>
			<c:otherwise>
				<![CDATA[ var selected = undefined; ]]>
			</c:otherwise>
			</c:choose>
			<c:choose><c:when test="${not empty sessionScope.listOfIndexesToChoiceListBySequenceNumber}">
				<![CDATA[ var indexesBySequenceNumber = ${listOfIndexesToChoiceListBySequenceNumber}; ]]>
			</c:when>
			<c:otherwise>
				<![CDATA[ var indexesBySequenceNumber = undefined; ]]>
			</c:otherwise>
			</c:choose>

			<![CDATA[
				</script>
			]]>
			
			<![CDATA[
				<script type="text/javascript">

					var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
					var fieldValues = ${listOfCurrentQuestionsChoicesValuesForDisplayQuestion}; 
					var isCorrectList = ${listSayingWhichChoicesAreCorrect};
					var examHistoryIsPresent = ${booleanExamHistoryIsPresent};
	

					$(document).ready(function() {
						$('div.choices').html('');
		
						if (${displayQuestion.questionType.id} == 1) {
							addChoiceInputsForThisQuestionType('#radioButtonExample');
						}
						else if (${displayQuestion.questionType.id} == 2) {
							addChoiceInputsForThisQuestionType('#checkboxExample');
						}
						else if (${displayQuestion.questionType.id} == 3) {
							displayStringTypeQuestionChoices('#textExample');
						}
						else if (${displayQuestion.questionType.id} == 4) {
							displaySequenceTypeQuestionChoices('#sequenceExample');
						}
					});
					
				</script>
			]]>
		</jsp:text>

	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<br/>
	<h1 style="display:inline">Display Question  </h1>

	<c:if test="${(sessionScope.shouldAllowQuestionEditing==true)}">
		<a href="/secured/question.jsp?questionId=${displayQuestion.id}">  (edit it)</a>
	</c:if>
	
	<c:if test="${(booleanExamHistoryIsPresent==true)}">
		<div style="display:inline">  <a href="javascript:history.go(-1)">  (Go Back to ${textToDisplayForPrevPage})</a></div>
	</c:if>

		<br/><br/>
		Creator: ${displayQuestion.user.username}<br/>
	<c:choose>
	<c:when test="${not empty displayQuestion.description}">
		Description: ${displayQuestion.description}<br/>
	</c:when>
	</c:choose>
		<br/>
		Text: <textarea name="questionText" cols="50" rows="15">${displayQuestion.text}</textarea><br/>  
		Difficulty: ${displayQuestion.difficulty.text} <br/>
		Type: ${displayQuestion.questionType.text}  <br/>
		<hr/>
		<br/>
		Answers --<br/>

		<div class="choices" style="margin-left:25px">.</div>

		<hr/>
		<br/>
		Topics --<br/>
		<div style="margin-left:25px">
			<table style="width:100%">
							<c:set var="rowNum" value="0"/>
							<c:forEach var="topic" items="${displayQuestion.topics}">
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
							<c:forEach var="reference" items="${displayQuestion.references}">
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
		<br/>
		
		<hr/>
		<br/>
		<c:if test="${not empty sessionScope.currentUserEntity}">
			Vote --<br/>
			<div id="divVotingButtons" style="margin-left:25px">

				<c:choose>
					<c:when test="${qfn:userHasVotedForThisEntity(sessionScope.currentUserEntity, sessionScope.displayQuestion)}">
						You have already voted on this question. You can change your vote.					
					</c:when>
					<c:otherwise>
						Vote for this question
					</c:otherwise>
				</c:choose>
				
				<input type="submit" id="btnThumbsUp" value="Thumbs Up" name="button"/> or <input type="submit" id="btnThumbsDown" value="Thumbs Down" name="button"/> 
			</div>
		</c:if>			

		
		
		<br/><br/>
	<a href="/index.jsp">home</a> -- <a href="javascript:history.go(-1)">Go Back to ${textToDisplayForPrevPage}</a>
	
	<div class="hidden" id="radioButtonExample"><div class="??3 ??4"><input type="radio" disabled="disabled" name="group1" value="??2" selected="" /><div style="display:inline" title="??tooltip">??1</div></div></div>	
	<div class="hidden" id="checkboxExample"><div class="??3 ??4"><input type="checkbox" disabled="disabled" name="??2" value="??2" selected="" /><div style="display:inline" title="??tooltip">??1</div></div></div>
	<div class="hidden" id="textExample"><div class="??4">??1<br/></div></div>
	<div class="hidden" id="youTypedExample"><br/>You typed: <div class="??4">??1<br/></div></div>
	<div class="hidden" id="sequenceExample"><div class="??4">??SEQ - ??1<br/></div></div>

</body>
</html>
</jsp:root>