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
	<jsp:directive.page import="com.haxwell.apps.questions.managers.ExamManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.Question"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.Exam"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.servlets.actions.InitializeNewExamInSessionAction"/>
	
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

Exam exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);

if (exam == null) {
	new InitializeNewExamInSessionAction().doAction(request, response);
	exam = (Exam)request.getSession().getAttribute(Constants.CURRENT_EXAM);
}

String id = (String)request.getParameter("chkboxname");
log.log(Level.SEVERE, "chkboxname: " + id);

id = id.substring(id.indexOf('_')+1);

String rowId = (String)request.getParameter("rowid");
log.log(Level.SEVERE, "chkbox rowId: " + rowId);

rowId = rowId.substring(rowId.indexOf('_')+1);

Question q = QuestionManager.getQuestionById(id);

String state = "";

if (exam.getQuestions().contains(q)) {
	ExamManager.removeQuestion(exam, q);
	state = "cleared";
}
else {
	ExamManager.addQuestion(exam, q);
	state = "selected";
}

log.log(Level.SEVERE, "returning: " + state);
log.log(Level.SEVERE, "exam has " + exam.getNumberOfQuestions() + " questions.");

java.io.PrintWriter writer = response.getWriter();
writer.print(rowId+"!"+state+"!");
</jsp:scriptlet>
	
</body>
</html>
</jsp:root>