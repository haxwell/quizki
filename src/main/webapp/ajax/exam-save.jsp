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
	<jsp:directive.page import="com.haxwell.apps.questions.managers.ExamManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.Exam"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.EventConstants"/>	
	<jsp:directive.page import="com.haxwell.apps.questions.utils.ExamUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.StringUtil"/>
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
Exam exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);

String rtn = "";

if (exam != null) {
	String title = request.getParameter("examTitle");
	String message = request.getParameter("examMessage");
	
	exam.setTitle(title);
	exam.setMessage(message);
	
	rtn = ExamUtil.persist(exam);
	
	if (rtn.contains("successes")) {
		EventDispatcher.getInstance().fireEvent(request, EventConstants.EXAM_WAS_PERSISTED);
	}
}
else {
	rtn = "{\"examValidationWarnings\":[\"There's nothing to save! Try selecting some questions!\"]}";
}

log.log(Level.SEVERE, "exam-save.jsp is going to return: " + rtn);

writer.print(rtn);

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>