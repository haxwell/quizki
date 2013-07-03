<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="java.util.logging.Logger"/>
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
	<jsp:directive.page import="java.util.logging.Level"/>	
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

FilterCollection fc = new FilterCollection();

log.log(Level.SEVERE, "just created FilterCollection");

if (user != null)
	fc.add(FilterConstants.USER_ID_FILTER, user.getId() + "");

String difficultyFilterValue = request.getParameter(FilterConstants.DIFFICULTY_FILTER);

log.log(Level.SEVERE, "difficultyFilterValue " + difficultyFilterValue);

if (difficultyFilterValue.equals("0") || StringUtil.isNullOrEmpty(difficultyFilterValue)) difficultyFilterValue = DifficultyConstants.GURU+"";

String qtf = request.getParameter(FilterConstants.QUESTION_TYPE_FILTER);

if (StringUtil.isNullOrEmpty(qtf)) qtf = TypeConstants.ALL_TYPES + "";

String roef = request.getParameter(FilterConstants.RANGE_OF_ENTITIES_FILTER);

if (StringUtil.isNullOrEmpty(roef)) roef = Constants.ALL_ITEMS + "";

log.log(Level.SEVERE, "FilterConstants.OFFSET_FILTER " + FilterConstants.OFFSET_FILTER);
log.log(Level.SEVERE, "offset value " + request.getParameter(FilterConstants.OFFSET_FILTER));

fc.add(FilterConstants.QUESTION_CONTAINS_FILTER, request.getParameter(FilterConstants.QUESTION_CONTAINS_FILTER));
fc.add(FilterConstants.TOPIC_CONTAINS_FILTER, request.getParameter(FilterConstants.TOPIC_CONTAINS_FILTER));
fc.add(FilterConstants.QUESTION_TYPE_FILTER, qtf);
fc.add(FilterConstants.DIFFICULTY_FILTER, difficultyFilterValue);
fc.add(FilterConstants.AUTHOR_FILTER, request.getParameter(FilterConstants.AUTHOR_FILTER));
fc.add(FilterConstants.MAX_ENTITY_COUNT_FILTER, request.getParameter(FilterConstants.MAX_ENTITY_COUNT_FILTER));
fc.add(FilterConstants.RANGE_OF_ENTITIES_FILTER, roef);
fc.add(FilterConstants.OFFSET_FILTER, request.getParameter(FilterConstants.OFFSET_FILTER));

AJAXReturnData rtnData = null;

Exam exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);

Set selectedQuestions = (exam == null ? new HashSet() : exam.getQuestions());

rtnData = QuestionManager.getAJAXReturnObject(fc, selectedQuestions);

writer.print(rtnData.toJSON());

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>