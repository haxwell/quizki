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
		<title>Take Exam - **</title>
		<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="js/createQuestion.js" type="text/javascript" ></script> ]]>
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
						$(document).ready(function() {
			var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
			var values = ${listOfCurrentQuestionsChoicesValues};
			var selected = ${listSayingAnElementIsCheckedOrNot};			
			
			if (${currentQuestion.questionType.id} == 1)
				{
					$('div.choices').html('');
					
					// find the div, and create the html to put in there, for these choices and 
					//  this question type..
					
					for (var counter=0;fieldNames.length>counter;counter++)
					{
						var str = $('#radioButtonExample').html();
						
						str = str.replace('??1', values[counter]);
						str = str.replace('??2', fieldNames[counter]);
						str = str.replace('??2', fieldNames[counter]);
						
						if (selected !== undefined && selected[counter] !== undefined && selected[counter] == 'true')
							str = str.replace("selected=\"\"", 'checked');
						
						if (counter%2 == 0) {
							str = str.replace('??4', 'rowHighlight'); 
						} else {
							str = str.replace('??4', '');
						}

						var previous = $('div.choices').html();
						
						previous += str;
						
						$('div.choices').html(previous);
					}
				}
			else if (${currentQuestion.questionType.id} == 2)
				{
					$('div.choices').html('');
					
					// find the div, and create the html to put in there, for these choices and 
					//  this question type..
					
					for (var counter=0;fieldNames.length>counter;counter++)
					{
						var str = $('#checkboxExample').html();
						
						str = str.replace("??1", values[counter]);
						str = str.replace("??2", fieldNames[counter]);
						str = str.replace("??2", fieldNames[counter]);						
						
						if (selected !== undefined && selected[counter] !== undefined && selected[counter] == 'true')
							str = str.replace("selected=\"\"", 'checked');
						
						if (counter%2 == 0) {
							str = str.replace('??4', 'rowHighlight'); 
						} else {
							str = str.replace('??4', '');
						}

						var previous = $('div.choices').html();
						
						previous += str;
						
						$('div.choices').html(previous);
					}
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

	<form action="/quizki/TakeExamServlet" method="post">
		<br/>
		<div class="examTitle">${currentExam.title}  -- Question ${currentQuestionNumber} of ${totalNumberOfQuestionsInCurrentExam}</div>  
		<br/>
		<div class="examTitle">${currentQuestion.description}</div>
		<br/>		
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
	<a href="/quizki/index.jsp">Quit this exam.</a>
	
	<div class="hidden" id="radioButtonExample"><div class="??3 ??4"><input type="radio" name="group1" value="??2" selected=""/>??1</div></div>	
	<div class="hidden" id="checkboxExample"><div class="??3 ??4"><input type="checkbox" name="??2" value="??2" selected=""/>??1</div></div>
</body>
</html>
</jsp:root>