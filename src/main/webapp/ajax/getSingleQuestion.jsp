<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="java.util.logging.Logger"/>
	<jsp:directive.page import="java.util.logging.Level"/>
	<jsp:directive.page import="com.haxwell.apps.questions.managers.QuestionManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.managers.AJAXReturnData"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.AbstractEntity"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.Exam"/>	
	<jsp:directive.page import="com.haxwell.apps.questions.utils.CollectionUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.StringUtil"/>	
	<jsp:directive.page import="com.haxwell.apps.questions.utils.FilterCollection"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.DifficultyConstants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.TypeConstants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.FilterConstants"/>
	<jsp:directive.page import="java.util.List"/>
	<jsp:directive.page import="java.util.Set"/>
	<jsp:directive.page import="java.util.HashSet"/>	
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

// expects fc.ENTITY_ID_FILTER to be set, at a minimum


java.util.logging.Logger log = Logger.getLogger(this.getClass().getName());

java.io.PrintWriter writer = response.getWriter();

FilterCollection fc = new FilterCollection();

User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);

fc.add(FilterConstants.USER_ID_ENTITY, user);

if (user != null)
	fc.add(FilterConstants.USER_ID_FILTER, user.getId() + "");

String entityId = request.getParameter(FilterConstants.ENTITY_ID_FILTER);

fc.add(FilterConstants.ENTITY_ID_FILTER, entityId);
fc.add(FilterConstants.MAX_ENTITY_COUNT_FILTER, 1);
fc.add(FilterConstants.RANGE_OF_ENTITIES_FILTER, 0);
fc.add(FilterConstants.OFFSET_FILTER, 0);

log.log(Level.SEVERE, fc.toString() + "\n\n");

AJAXReturnData rtnData = null;

rtnData = QuestionManager.getAJAXReturnObject(fc, null);

writer.print(rtnData.toJSON());

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>