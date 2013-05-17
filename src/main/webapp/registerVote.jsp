<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="com.haxwell.apps.questions.managers.VoteManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.AbstractEntity"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
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

String voteDirection = request.getParameter("voteDirection");
String entityKey = request.getParameter("entityKey");
User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

java.io.PrintWriter writer = response.getWriter();

AbstractEntity entity = (AbstractEntity)request.getSession().getAttribute(entityKey);

if (entity != null) {
	if (voteDirection.equals("down"))
		VoteManager.voteDown(user, entity);
	else if (voteDirection.equals("up"))
		VoteManager.voteUp(user, entity);
}

writer.print(voteDirection + " " + entityKey + " ");

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>