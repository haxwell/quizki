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
	
	<input style="display:none;" id="offset" type="text" name="offset"/>
	<input style="display:none;" id="maxEntityCountFilter" type="text" name="mcf"/>
	
	<input style="display:none;" id="Exams-view-data-url" type="text" name="exam-view-data-url" value="/getQuestions.jsp"/>
	<input style="display:none;" id="Exams-entity-table-id" type="text" name="Exams-entity-table-id" value="#examEntityTable"/>
	<input style="display:none;" id="prefix-to-current-view-hidden-fields" type="text" name="prefix-to-current-view-hidden-fields" value="Exams"/>
	
	<input style="display:none;" id="Exams-data-object-definition" type="text" name="Exams-data-object-definition" value=""/>
	
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
		
		<jsp:text>
			<![CDATA[
				<script type="text/javascript">
					
					$(document).ready(function() {
						setDataObjectDefinitions();
						displayMoreRows();
					});
					
					$(window).scroll(function(){
				        if  ($(window).scrollTop() == $(document).height() - $(window).height()) {
					        if (smoothScrollingEnabledOnCurrentTab()) {
					           //alert("Hit the bottom!");
					           displayMoreRows();
					        }
				        }
					});
					
					function setDataObjectDefinitions() {
						var str = "{\"fields\": [{\"name\":\"containsFilter\",\"id\":\"#containsFilter\"},{\"name\":\"topicContainsFilter\",\"id\":\"#topicContainsFilter\"},{\"name\":\"questionTypeFilter\",\"id\":\"#questionTypeFilter\"},{\"name\":\"difficultyFilter\",\"id\":\"#difficultyFilter\"},{\"name\":\"maxEntityCountFilter\",\"id\":\"#maxEntityCountFilter\"},{\"name\":\"rangeOfEntitiesFilter\",\"id\":\"#rangeOfQuestionsFilter\"},{\"name\":\"offsetFilter\",\"id\":\"#offset\"}]}";
						$('#Exams-data-object-definition').attr("value",str);
					}
					
					function Exams_convertToHTMLString(obj, rowNum) {
						var choicesArr = obj.choices;
						var topicsArr = obj.topics;
						var referencesArr = obj.references;
						
						var rtn = "";
						
						rtn += "<tr id=\"tableRow_" + rowNum + "\">";
						rtn += "<td>";
						
						if (isSelectedEntityId(obj.id)) {
							// Checked
							rtn += "<label class=\"checkbox no-label checked\" for=\"checkbox-table-2\">";
							rtn += " <input type=\"checkbox\" value=\"\" id=\"checkbox-table-2\" data-toggle=\"checkbox\" id=\"chkbox_" + rowNum + "\"";
							rtn += " name=\"selectQuestionChkbox_" + obj.id + "\" value=\"\" />";
							rtn += "</label>";
						}
						else {
							// Not checked
							rtn += "<label class=\"checkbox no-label\" for=\"checkbox-table-2\">";
							rtn += " <input type=\"checkbox\" value=\"\" id=\"checkbox-table-2\" data-toggle=\"checkbox\" id=\"chkbox_" + rowNum + "\"";
							rtn += " name=\"selectQuestionChkbox_${question.id}\" value=\"\" />";
							rtn += "</label>";
						}
						
						rtn += "</td><td>";
						
						if (obj.description.length > 0) {
							rtn += "<a href=\"/displayQuestion.jsp?questionId=" + obj.id + "\">" + obj.description + "</a>";
						}
						else {
							rtn += "<a href=\"/displayQuestion.jsp?questionId=" + obj.id + "\">" + obj.textWithoutHTML + "</a>";
						}
						
						rtn += "</td><td>";
						
						if (topicsArr.length > 0) {
							for (var i=0; i<topicsArr.length; i++) {
								rtn += topicsArr[i].text + "<br/>";
							}
						}
						
						rtn += "</td><td>";
						
						rtn += obj.type_text;
						rtn += "</td><td>";
						rtn += obj.difficulty_text;
						rtn += "</td><td>";
						
						// TODO: figure out a way of populating the Vote info.. Probably put it in a JSON str, like [{"objectId":"1","votesUp":"1","votesDown":"0"}]
						//  then create a map of some sort out of it..
						rtn += " -- ";
						rtn += "</td></tr>";
						
						return rtn;
					}
					
					function isSelectedEntityId(id) {
						var selectedIds = new Array(${sessionScope.selectedEntityIDs_AsCSV});
						var rtn = new Boolean();
						
						for (var i=0; i<selectedIds.length && rtn==false; i++) {
							rtn = (selectedIds[i] == id);
						}
						
						return rtn;
					}
					
					function currentPageHasAnAJAXDataObjectDefinition() {
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						
						// a list of the name of the field in the data object, and the name of the field with its value
						var dataObjDefinition_json = $("#"+prefix+"-data-object-definition").attr("value");

						return dataObjDefinition_json != undefined;					
					}
					
					function smoothScrollingEnabledOnCurrentTab() {
						return currentPageHasAnAJAXDataObjectDefinition();
					}
					
					function getURLThatProvidesTableData() {
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						
						return $("#"+prefix+"-view-data-url").attr("value");
					}
					
					function getDataObjectForAJAX() {
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						
						// a list of the name of the field in the data object, and the name of the field with its value
						var dataObjDefinition_json = $("#"+prefix+"-data-object-definition").attr("value");
						
						var obj = jQuery.parseJSON(dataObjDefinition_json);
						var arr = obj.fields;
						
						var rtn = { };
						
						for (var i=0; i<arr.length; i++) {
							
							try {
								rtn[arr[i].name] = $(arr[i].id).attr("value");
							}
							catch (err) {
								// skip this field... TODO, handle this better.. an error means the dataObjDefinition is bad..
							}
						}
						
						return rtn;
					}
					
					function displayMoreRows() {
						var data = getMoreRows();
						
						var index = data.indexOf("<!DOCTYPE");
						var jsonExport = data;
						
						if (index != -1) {
							jsonExport = data.substring(0, index);
						}
						
						var obj = jQuery.parseJSON(jsonExport);
						
						var qArr = obj.question;
						
						var str = "";
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						var entityTableId = $("#"+prefix+"-entity-table-id").attr("value");
						
						for (var i=0; i<qArr.length; i++) {
							rowNum = i;
							str = window[prefix+"_convertToHTMLString"](qArr[i], rowNum);
							
							$(entityTableId + " > tbody:last").append(str);
						}
					}		
					
					function getMoreRows() {
						var os = $("#offset").attr("value");
						
						if (os == undefined || os.length == 0) {
							os = 0;
							$("#offset").attr("value", os);
						}

						var mecf = $("#maxEntityCountFilter").attr("value");
						
						if (mecf == undefined || mecf.length == 0) {
							mecf = 10;
							$("#maxEntityCountFilter").attr("value", mecf);
						}
						
						var rtn = "";
						var data_url = getURLThatProvidesTableData();
						var data_obj = getDataObjectForAJAX();

						$.ajax({
							type: "POST",
							url: data_url,
							data: data_obj,
							dataType: "text",
							async: false
						}).done(function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									os = (os*1)+(mecf*1); // force numerical addition
									$("#offset").attr("value", os);
									$("#maxEntityCountFilter").attr("value", mecf);
									
									rtn = data;
								}
							});
						
						return rtn;
					}					
					
			</script>
			]]>
		</jsp:text>
		
</body>
	</html>
</jsp:root>