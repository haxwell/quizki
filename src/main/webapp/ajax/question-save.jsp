<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="java.util.logging.Logger"/>
	<jsp:directive.page import="java.util.logging.Level"/>
	<jsp:directive.page import="com.haxwell.apps.questions.managers.QuestionManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.Question"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.EventConstants"/>	
	<jsp:directive.page import="com.haxwell.apps.questions.utils.DifficultyUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.ReferenceUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.TopicUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.TypeUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.QuestionUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.events.EventDispatcher"/>
	<jsp:directive.page import="java.util.List"/>
	<jsp:directive.page import="java.util.ArrayList"/>	
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

java.util.logging.Logger log = Logger.getLogger(this.getClass().getName());

java.io.PrintWriter writer = response.getWriter();

// need to get the text data from the javascript, and convert it to a real Question object
Question question = new Question();

User user = (User)request.getSession().getAttribute("currentUserEntity");
Long user_id = Long.parseLong(request.getParameter("user_id"));

if (user.getId() == user_id) {
	question.setId(Long.parseLong(request.getParameter("id")));
	question.setUser(user);

	question.setText((String)request.getParameter("text"));
	question.setDescription((String)request.getParameter("description"));
	question.setDifficulty(DifficultyUtil.getDifficulty(request.getParameter("difficulty_id")));
	question.setQuestionType(TypeUtil.getObjectFromStringTypeId(request.getParameter("type_id")));
	question.setChoices(QuestionUtil.getSetFromAjaxDefinition(request.getParameter("choices")));
	question.setTopics(TopicUtil.getSetFromCSV((String)request.getParameter("topics")));
	question.setReferences(ReferenceUtil.getSetFromCSV((String)request.getParameter("references")));

	log.log(Level.SEVERE, "got: " + question);

	long rtn = QuestionManager.persistQuestion(question);
}


//(rtn > -1) ? writer.print("") : writer.print("");

writer.print("");

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>