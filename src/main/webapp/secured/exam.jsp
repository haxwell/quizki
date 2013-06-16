<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions" version="2.0">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<jsp:text>
		<![CDATA[ <!DOCTYPE html> ]]>
	</jsp:text>
	<!--html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">-->
	<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Quizki - Create Exam</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="../bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="../css/quizki.css" rel="stylesheet" />
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../images/favicon.ico" />
</head>
<body>
	<div class="container">
		<jsp:include page="../header.jsp"></jsp:include>
		<div class="content">
			<div class="row">
				<form action="/secured/ExamServlet" method="post"
					id="titleAndSubmitButtonForm">
					<div class="span3">

						<input type="text" placeholder="Enter a title for your exam..."
							class="flat input-block-level" maxlength="128" id="id_examTitle"
							name="examTitle" value="${currentExam.title}"
							title="A name for this exam." />
					</div>
					<div class="span7">
						<input type="text"
							placeholder="Enter a message for users who will take your exam..."
							class="flat input-block-level" size="45" maxlength="255"
							id="id_examMessage" name="examMessage"
							value="${currentExam.message}"
							title="A message for folks who take this exam." />
					</div>
					<div class="span2">
						<c:choose>
							<c:when test="${empty sessionScope.inEditingMode}">
								<button class="btn btn-block pull-right" type="submit"
									name="button">Add Exam</button>
							</c:when>
							<c:otherwise>
								<button class="btn btn-block pull-right" type="submit"
									name="button">Update Exam</button>
							</c:otherwise>
						</c:choose>
					</div>
				</form>
			</div>
			<div class="row">
				<div class="span12 divider">
					<h2>Select questions for your exam</h2>
				</div>
			</div>
			<div class="row">
				<div class="span12"><jsp:include
						page="exam-AvailableQuestions.jsp"></jsp:include></div>
			</div>
		</div>
	</div>
	
		<jsp:text>
			<![CDATA[ <script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/jquery-ui-1.10.3.custom.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/jquery.ui.touch-punch.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/bootstrap.min.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/bootstrap-select.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/bootstrap-switch.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/flatui-checkbox.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/flatui-radio.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/jquery.tagsinput.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/jquery.placeholder.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/jquery.stacktable.js"></script> ]]>
			<![CDATA[ <script type="text/javascript" src="../js/quizki.js"></script> ]]>
		</jsp:text>
</body>
	</html>
</jsp:root>