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
		
		<title>Take Exam - ${currentExam.title}</title>

		<link href="pkgs/bootstrap/css/bootstrap.css" rel="stylesheet" />
		<link href="pkgs/jquery-ui/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/quizki.css" rel="stylesheet" type="text/css"/>
		<link rel="shortcut icon" href="images/favicon.ico" />
		
		<jsp:text>
			<![CDATA[ <script src="pkgs/jquery/jquery-1.10.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="pkgs/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="js/choiceFunctions.js" type="text/javascript" ></script> ]]>
			<![CDATA[
			<script type="text/javascript">
				tinyMCE.init({
				        theme : "advanced",
				        mode : "textareas",
				        plugins : "autoresize",
				        readonly : 1,
						content_css : "css/quizki_tinymce_custom_content.css"
				});
			</script>
			]]>


			<![CDATA[
				<script type="text/javascript">
			]]>

			<c:choose>
				<c:when test="${not empty sessionScope.listOfRandomChoiceIndexes}">
					<![CDATA[ var randomChoiceIndexes = ${listOfRandomChoiceIndexes}; ]]>
				</c:when>
				<c:otherwise>
					<![CDATA[ var randomChoiceIndexes = undefined; ]]>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${not empty sessionScope.listOfPreviouslySuppliedAnswers}">
					<![CDATA[ var previouslySuppliedAnswers = ${listOfPreviouslySuppliedAnswers}; ]]>
				</c:when>
				<c:otherwise>
					<![CDATA[ var previouslySuppliedAnswers = undefined; ]]>
				</c:otherwise>
			</c:choose>

			<![CDATA[
					var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
					var fieldValues = ${listOfCurrentQuestionsChoicesValues};
					var selected = ${listOfFieldnamesUserInteractedWithAsAnswersOnCurrentQuestion};

					$(document).ready(function() {
						
						$('div.choices').html('');

						if (${currentQuestion.questionType.id} == 1)
						{
							addChoiceInputsForThisQuestionType('#radioButtonExample');
						}
						else if (${currentQuestion.questionType.id} == 2)
						{
							addChoiceInputsForThisQuestionType('#checkboxExample');
						}
						else if (${currentQuestion.questionType.id} == 3)
						{
							addChoiceInputsForStringQuestionType();
						}
						else if (${currentQuestion.questionType.id} == 4)
						{
							addChoiceInputsForSequenceQuestionType();
						}
					});

				</script>
			]]>
		</jsp:text>
	</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">


      <c:choose>
      <c:when test="${empty currentExam}">
      	<br/><br/>
		Oops! Something went wrong! You should <a href="/index.jsp">go back to the beginning.</a>
      </c:when>
      <c:otherwise>

      <c:if test="${not empty requestScope.validationErrors}">
      	<br/>
      	<c:forEach var="str" items="${validationErrors}">
      		<span class="redText">${str}</span><br/>
      	</c:forEach>
      </c:if>

	<form action="/TakeExamServlet" method="post">
		<br/>
		<div class="examTitle">${currentExam.title}  -- Question ${currentQuestionNumber} of ${totalNumberOfQuestionsInCurrentExam}</div>  
		<br/>
		<c:if test="${not empty currentQuestion.description}">
			<br/>
			<div class="questionDescription">${currentQuestion.description}</div>  
			<br/>
		</c:if>			
		<textarea name="questionText" cols="50" rows="15">${currentQuestion.text}</textarea>
		<br/>
		<div style="float:right;">Submitted by: ${currentQuestion.user.username}</div>
		<hr/>
		<br/>
		<div class="choices">.</div> 
	
	
		<br/><br/>
		<c:if test="${currentExamHistory.allQuestionsHaveBeenAnswered==true}">
		<input type="submit" value="&lt;&lt; FIRST" name="button"/>
		</c:if>

		<input type="submit" value="&lt; PREV" name="button"/>

		<c:if test="${currentExamHistory.allQuestionsHaveBeenAnswered==true}">
		<input type="text" name="jumpToNumber" size="3" maxlength="3" autocomplete="off"/><input type="submit" value="Go To #" name="button"/>
		</c:if>

		<input type="submit" value="NEXT &gt;" name="button"/>
		
		<c:if test="${currentExamHistory.allQuestionsHaveBeenAnswered==true}">
		<input type="submit" value="LAST &gt;&gt;" name="button"/>
		</c:if>
		<br/><br/>
		
	</form>
	
	<br/><br/>
	<a href="/index.jsp">Quit this exam.</a>
	
	</c:otherwise>
	</c:choose>
	
	<div style="display:none;" id="radioButtonExample"><div class="??3 ??4"><input type="radio" name="group1" value="??2" selected=""/>??1</div></div>	
	<div style="display:none;" id="checkboxExample"><div class="??3 ??4"><input type="checkbox" name="??2" value="??2" selected=""/>??1</div></div>
	<div style="display:none;" id="textboxExample"><div class="??3 ??4">Enter your answer:<br/><input type="text" name="stringAnswer" value="??5" autocomplete="off"/></div></div>
	<div style="display:none;" id="sequenceExample"><div class="??3 ??4"><input type="text" name="??2" value="??5" size="2" maxlength="2" autocomplete="off"/> ??1</div></div>

</div>
</div>	
</body>
</html>
</jsp:root>