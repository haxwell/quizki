<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="com.haxwell.apps.questions.managers.QuestionManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.AbstractEntity"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.CollectionUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.FilterCollection"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.FilterConstants"/>
	<jsp:directive.page import="java.util.List"/>	
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

java.io.PrintWriter writer = response.getWriter();

User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

FilterCollection fc = new FilterCollection();

if (user != null)
	fc.add(FilterConstants.USER_ID_FILTER, user.getId() + "");

fc.add(FilterConstants.QUESTION_CONTAINS_FILTER, request.getParameter(FilterConstants.QUESTION_CONTAINS_FILTER));
fc.add(FilterConstants.TOPIC_CONTAINS_FILTER, request.getParameter(FilterConstants.TOPIC_CONTAINS_FILTER));
fc.add(FilterConstants.QUESTION_TYPE_FILTER, request.getParameter(FilterConstants.QUESTION_TYPE_FILTER));
fc.add(FilterConstants.DIFFICULTY_FILTER, request.getParameter(FilterConstants.DIFFICULTY_FILTER));
fc.add(FilterConstants.AUTHOR_FILTER, request.getParameter(FilterConstants.AUTHOR_FILTER));
fc.add(FilterConstants.MAX_QUESTION_COUNT_FILTER, request.getParameter(FilterConstants.MAX_QUESTION_COUNT_FILTER));

List list = QuestionManager.getQuestions(fc);

writer.print(CollectionUtil.toJSON(list));

//writer.print(VoteUtil.registerVote(request.getParameter("voteDirection"), request.getParameter("entityType"), request.getParameter("entityId"), (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY)));

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>