<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="org.apache.logging.log4j.LogManager"/>
	<jsp:directive.page import="org.apache.logging.log4j.Logger"/>
	<jsp:directive.page import="com.haxwell.apps.questions.managers.QuestionManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.Question"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.EventConstants"/>	
	<jsp:directive.page import="com.haxwell.apps.questions.utils.StringUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.TopicUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.ReferenceUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.QuestionUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.events.EventDispatcher"/>
	<jsp:directive.page import="java.util.List"/>
	<jsp:directive.page import="java.util.Map"/>
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

Logger log = LogManager.getLogger();

java.io.PrintWriter writer = response.getWriter();

// need to get the text data from the javascript, and convert it to a real Question object
String jsonRtn = StringUtil.toJSON(QuestionUtil.persistQuestionBasedOnHttpServletRequest(request));

// only persist topics and references if there are no validation errors from the persist question operation
if (!jsonRtn.contains("\"errors\"")) {
	TopicUtil.persistTopicsForAutocompletion(request);
	ReferenceUtil.persistReferencesForAutocompletion(request);
}

log.trace(jsonRtn);

writer.print(jsonRtn);

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>