<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="java.util.logging.Logger"/>
	<jsp:directive.page import="java.util.logging.Level"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.TopicUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.managers.QuestionManager"/>
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

User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

String filterByUserId = request.getParameter("filterByUserId");
String containsFilter = request.getParameter("containsFilter");

log.log(Level.SEVERE, filterByUserId + "\n\n");
log.log(Level.SEVERE, containsFilter + "\n\n");

String rtn = "";

if (filterByUserId.equals("true")) {
	rtn = TopicUtil.getJSONOfAllTopicsForQuestionsCreatedByAGivenUserThatContain(user.getId(), containsFilter); 
}
else {
	rtn = TopicUtil.getJSONOfAllTopicsThatContain(containsFilter);
}

writer.print(rtn);

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>