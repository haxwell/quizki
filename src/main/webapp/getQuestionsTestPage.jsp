<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" xmlns:qfn="http://quizki.com/tld/qfn" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Home Page - Quizki</title>
		
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>
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
				    
					$(document).ready(function(){
						$("#getQuestionsBtn").click(function(){
							//alert("btn pushed!");
							
							$.post("/getQuestions.jsp",
							{
								containsFilter: $("#idContainsFilter").attr("value"),
								topicContainsFilter: $("#idTopicContainsFilter").attr("value"),
								questionTypeFilter: $("#idQuestionTypeFilter").attr("value"),
								difficultyFilter: $("#idDifficultyFilter").attr("value"),
								authorFilter: $("#idAuthorFilter").attr("value"),
								maxQuestionCountFilter: $("#idMaxQuestionCountFilter").attr("value"),
								offsetFilter: $("#idOffsetFilter").attr("value")
							},
							function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									$('#resultsTextarea').attr("value", data);
								}
							});
						});
					});					

				</script>
			]]>
		</jsp:text>
		
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>	
	
	<br/><br/>
	Question Contains: <input id="idContainsFilter" type="text" name="containsFilter" value="animal"/>
	Topic Contains: <input type="text" name="topicContainsFilter" id="idTopicContainsFilter" />
	Type text: <input type="text" name="questionTypeFilter" id="idQuestionTypeFilter" value="0" />
	Difficulty text: <input type="text" name="difficultyFilter" id="idDifficultyFilter" value="4" />
	Author text: <input type="text" name="authorFilter" id="idAuthorFilter" />	
	maxQuestionCount: <input id="idMaxQuestionCountFilter" type="text" name="maxQuestionCountFilter" value="10"/>
	offset: <input id="idOffsetFilter" type="text" name="offsetFilter" />

	<input id="getQuestionsBtn" type="submit" name="button" value="Get Questions"/>
	
	<br/><br/>
	<textarea id="resultsTextarea" rows="7" cols="90"></textarea>

</body>
</html>
</jsp:root>
