<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="java.util.logging.Logger"/>
	<jsp:directive.page import="java.util.logging.Level"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.ExamUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.ExamReportCardData"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="net.minidev.json.JSONObject"/>
	<jsp:directive.page import="net.minidev.json.JSONValue"/>
	
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

// get the json from the client
String qJson = request.getParameter("questions_json");
String aJson = request.getParameter("answers_json");

log.log(Level.SEVERE, qJson + "\n\n");
log.log(Level.SEVERE, aJson + "\n\n");

ExamReportCardData data = ExamUtil.gradeExam(qJson, aJson);

request.getSession().setAttribute("mostRecentExamResults", data);

// examReportCard.jsp needs to be rewritten so that it uses the JSON from the ExamReportCardData.getstate(). But
// for now thats too much work.. Going to incur some technical debt, and pass in an object similar to
// what it was getting before the UI redesign.. this is of course a HACK and should be removed as soon
// as possible.

String ercdJSON = data.getStateAsJSONString();
JSONObject jObj = (JSONObject)JSONValue.parse(ercdJSON);

request.getSession().setAttribute("numberOfQuestionsAnsweredCorrectly", jObj.get("numberCorrect"));
request.getSession().setAttribute("totalNumberOfQuestions", jObj.get("numberTotal"));

request.getSession().setAttribute("questionsOnTheMostRecentExam", qJson);
request.getSession().setAttribute(Constants.ANSWERS_TO_THE_MOST_RECENT_EXAM, aJson);

writer.print(ercdJSON);

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>