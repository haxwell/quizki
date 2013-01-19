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
		<title>Take Exam - ${currentExam.title}</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="js/choiceFunctions.js" type="text/javascript" ></script> ]]>
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

<jsp:include page="header.jsp"></jsp:include>

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
		<input type="submit" value="&lt; PREV" name="button"/>
		<input type="submit" value="NEXT &gt;" name="button"/>
	</form>
	
	<br/><br/>
	<a href="/index.jsp">Quit this exam.</a>
	
	<div class="hidden" id="radioButtonExample"><div class="??3 ??4"><input type="radio" name="group1" value="??2" selected=""/>??1</div></div>	
	<div class="hidden" id="checkboxExample"><div class="??3 ??4"><input type="checkbox" name="??2" value="??2" selected=""/>??1</div></div>
	<div class="hidden" id="textboxExample"><div class="??3 ??4">Enter your answer:<br/><input type="text" name="stringAnswer" autocomplete="off"/></div></div>
	<div class="hidden" id="sequenceExample"><div class="??3 ??4"><input type="text" name="??2" size="2" autocomplete="off"/> ??1</div></div>
	
</body>
</html>
</jsp:root>