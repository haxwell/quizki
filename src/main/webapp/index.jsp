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
<title>Home Page - Quizki</title>
		
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/jquery.cookie.js" type="text/javascript"></script> ]]>
		
			<![CDATA[
				<script type="text/javascript">

   					$(document).ready(function() {
						var currentSessionCookieVal = $.cookie('quizki.currentSessionCookie'); 
					
						if (currentSessionCookieVal == null)
							$("#welcomeToQuizki-dialog").dialog({modal:true,width:530,title:"Welcome to Quizki!"}).dialog();
					});

					// Handler for the modal dialog CLOSING
				    $(document).ready(function(){
						$('div#welcomeToQuizki-dialog').bind('dialogclose', function(event) {
						    //var v = $.cookie('quizki.userHasBeenHereBefore');
						    //$.cookie('quizki.userHasBeenHereBefore', v+1, { expires: 30 });

						    $.cookie('quizki.currentSessionCookie', 0); // create cookie for current session
						});
 				    });
				    
				</script>
			]]>
		</jsp:text>
		
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

<br/><br/>
<br/><br/>
TAKE<br/> 
<div class="indentText">
Get a quick exam on ...<br/>   
			<c:forEach var="topic" items="${fa_listofmajortopics}">
				-- <a class="greyLink" href="beginExam.jsp?topicId=${topic.id}">${topic.text} </a><br/>
			</c:forEach>
<br/>
Generate <a class="greyLink" href="generateExam.jsp">a quick exam</a> on another topic.<br/>
Create <a class="greyLink" href="secured/exam.jsp">your own unique exam</a>.<br/><br/>
</div>
<br/>
GIVE<br/>
<div class="indentText">
<a class="greyLink" href="secured/question.jsp">a question of your own</a><br/><br/><br/>
</div>
BROWSE<br/>
<div class="indentText">
<a class="greyLink" href="listExams.jsp">see a list of exams</a><br/>
<a class="greyLink" href="secured/listQuestions.jsp">see a list of questions</a><br/><br/><br/><br/>
</div>

    <c:choose>
     <c:when test="${empty sessionScope.currentUserEntity}">
	You can <a class="greyLink" href="login.jsp">register, and/or login</a> here.<br/>
     </c:when>
     <c:otherwise>
     	You are logged in. <a class="greyLink" href="logout.jsp">logout</a><br/>
     </c:otherwise>
    </c:choose>


<div class="hidden" id="welcomeToQuizki-dialog" title="quizki"> Quizki is a tool to help you remember!<br/><br/>
You create questions and answers in Quizki. When you're ready, Quizki will ask you the questions. You can see which you answered correctly, and which ones you missed.<br/><br/>
BONUS! You can quiz yourself using questions that others have created!<br/><br/>
Click around! You won't break it! (but if you do.. <a href="mailto:johnathan@quizki.com">email me</a>!)<br/> </div>

</body>
</html>
</jsp:root>
