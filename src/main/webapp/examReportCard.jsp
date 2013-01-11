<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
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
		<title>Exam Report Card Graded</title>
		<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/createQuestion.js" type="text/javascript" ></script> ]]>
		</jsp:text>
				
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<jsp:scriptlet>      
	request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "Exam Report Card");
	</jsp:scriptlet>
	
<br/><br/><br/>
	Okay, you correctly answered ${numberOfQuestionsAnsweredCorrectly} out of ${totalNumberOfQuestions} questions.<br/><br/>
	
	Questions in <span class="greenText">green</span> are correct, <span class="redText">red</span> is one you missed!<br/><br/>
	

	<c:forEach var="answeredQuestion" items="${currentExamHistory.iterator}">
		<div class="qIsCorrect_${answeredQuestion.isCorrect}"><a class="q_aIsCorrect_${answeredQuestion.isCorrect}" href="/displayQuestion.jsp?questionId=${answeredQuestion.question.id}">${answeredQuestion.question.text}</a></div>
	</c:forEach>
	
	<br/><br/><br/>
	<a href="/index.jsp">home</a>
	
</body>
</html>
</jsp:root>