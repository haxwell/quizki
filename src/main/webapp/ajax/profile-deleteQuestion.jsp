<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="com.haxwell.apps.questions.managers.QuestionManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.StringUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.servlets.actions.SetUserContributedQuestionAndExamCountInSessionAction"/>
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

<jsp:scriptlet>

String str = request.getParameter("nameOfLastPressedButton");
String id = (str == null ? null : str.substring(str.indexOf('_')+1));

java.io.PrintWriter writer = response.getWriter();

if (!StringUtil.isNullOrEmpty(id)) {
	
	String btnValue = request.getParameter("valueOfLastPressedButton");
	
	if (btnValue != null) {
		if (btnValue.equals("Delete Question")) {
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			
			String rtn = QuestionManager.deleteQuestion(user.getId(), id);
			request.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, null);
			
			//new InitializeListOfProfileQuestionsInSessionAction().doAction(request, response);
			new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
			
			//request.getSession().setAttribute(Constants.MRU_FILTER_DIFFICULTY, DifficultyConstants.GURU);
			
			writer.print(rtn);
		}
	}
}

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>