<!--
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 -->

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
	<jsp:directive.page import="com.haxwell.apps.questions.utils.QuestionUtil"/>	
	<jsp:directive.page import="com.haxwell.apps.questions.utils.FilterCollection"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.DifficultyConstants"/>
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

java.util.logging.Logger log = Logger.getLogger(this.getClass().getName());

java.io.PrintWriter writer = response.getWriter();

AJAXReturnData rtnData = QuestionUtil.getFilteredList(request);

writer.print(rtnData.toJSON());

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>