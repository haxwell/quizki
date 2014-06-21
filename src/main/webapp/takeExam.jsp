<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		
		<title>Take Exam - ${currentExam.title}</title>

		<link href="../pkgs/Flat-UI-master/bootstrap/css/bootstrap.css" rel="stylesheet"/>
		<link href="../pkgs/Flat-UI-master/css/flat-ui.css" rel="stylesheet"/>
		<link href="../pkgs/font-awesome/css/font-awesome.css" rel="stylesheet"/>
		<link href="../pkgs/jquery-ui/jquery-ui-1.10.4/css/ui-lightness/jquery-ui-1.10.4.css" rel="stylesheet" type="text/css"/>

		<link href="../css/quizki-sitewide.css" rel="stylesheet" type="text/css"/> 
		<link href="../css/quizki-buttons.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-switch.css" rel="stylesheet" type="text/css"/>		
		<link href="../css/quizki-text-input-fields.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-text-input-fields-question.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-header-elements.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-attribute-wells.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-dialogs.css" rel="stylesheet" type="text/css"/>
		
		<link href="../css/Question.css" rel="stylesheet" type="text/css"/>
		<!-- link href="../css/takeExam.css" rel="stylesheet" type="text/css"/  -->

		<link rel="shortcut icon" href="images/favicon.ico" />
				
		<jsp:text>
			<![CDATA[ <script data-main="../js/quizki.js" src="../js/require.js"></script> ]]>
						
			<![CDATA[ <script src="../pkgs/jquery/jquery-1.11.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/jquery-ui/jquery-ui-1.10.4/js/jquery-ui-1.10.4.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/tiny_mce/tinymce.min.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/Flat-UI-master/js/bootstrap.min.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/bootstrap-switch-master/js/bootstrapSwitch.js" type="text/javascript" ></script> ]]>
			
			<![CDATA[ <script src="../pkgs/underscore.js/underscore.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/backbone.js/backbone.js" type="text/javascript" ></script> ]]>

			<![CDATA[ <script src="../js/stringUtil.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/backbone-quizki.js" type="text/javascript"></script> ]]>
			
			<![CDATA[ <script src="../js/ajax/ajax-functions.js" type="text/javascript"></script> ]]>

			<![CDATA[ <script src="../js/models/choice.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/reference.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/topic.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/keyValuePair.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/question.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/takeExam.js" type="text/javascript" ></script> ]]>

			<![CDATA[ <script src="../js/views/views.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/views/takeExam-views.js" type="text/javascript" ></script> ]]>
			

			<![CDATA[
				<script type="text/javascript">
			]]>

			<![CDATA[
			    $(document).ready(function() {

					var answers = '${sessionScope.answersToTheMostRecentExam}';
					
					if (answers.length > 0) {
						var dlg2 = $('#examAlreadyCompletedDialogText').dialog({
							autoOpen: false, resizable: false, modal: true,
							dialogClass:'dialog_stylee', width:500,
						      buttons: [{
							        text : "< Go HOME", 
							        click : function() {
							        	window.location.href = "/index.jsp";
						        }},
						        {
							        text : "Exam Report Card >",
							        click : function() {
										window.history.go(+1);					        		
						        } 
						      }]
						});
					
						dlg2.dialog('open');
					}
					
					var questionTextFormatterFunc = function(question) {
						var text = question.get('text');
						
						if (question.getTypeId() == QUESTION_TYPE_PHRASE) {
							var work = _.filter(question.get('dynamicData').models, function(model) { 
								return model.get('key') == 'dynamicFieldToBeBlankedOut'; 
							});
							
							if (work.length > 0) {
								var textArr = getBeginningAndEndingTextForSetQuestion(work[0].get('value'), text);
								
								text = textArr[0] + "_______________" + textArr[1];
								
								text = removeAllOccurrences(']]', text);
								text = removeAllOccurrences('[[', text);
							}
						}
						
						return text;
					};
						
					event_intermediary.initialize();

			    	model_constructor_factory.put("currentQuestion", getFunctionToRetrieveCurrentQuestion);
			    	model_constructor_factory.put("currentExamQuestionIdsSortedByTheirID", function() { return '${sessionScope.currentExamQuestionIdsSortedByTheirID}'; });
			    	model_constructor_factory.put("answersMap", function() { return new KeyValueMap(); });
			    	model_constructor_factory.put("answersToTheMostRecentExam", function() { var ans = '${sessionScope.answersToTheMostRecentExam}'; if (ans.length > 0) return new Backbone.Collection(JSON.parse(ans).answers); else return undefined; });
			    	
					ExamEngine.initialize();
			    
			    	var bv_header = new Quizki.QuitThisExamView({ el: $("#divQuestionHeaderWithQuitButtons") });

					var bv_questionAndTextView = new Quizki.QuestionTextAndDescriptionView({ el: $("#divTextarea"), readOnly: true, modelToListenTo:'ExamEngine', modelEventToListenFor:'examEngineSetNewCurrentQuestion', questionTextFormatter:questionTextFormatterFunc });
					
					var bv_choiceListView = new Quizki.ExamChoiceListView({ el: $("#divQuestionChoices") });
					
					var bv_navigationButtons = new Quizki.ExamNavigationButtons({ el: $("#divExamNavigationButtons") });
			    });
			]]>

			<![CDATA[
				</script>
			]]>
		</jsp:text>
	</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">

    <c:choose>
	    <c:when test="${empty currentExam}">
	      	<br/><br/>
			Oops! Something went wrong! You should <a href="/index.jsp">go back to the beginning.</a>
	    </c:when>
	    <c:otherwise>
			<div id="idAlertDiv" class="alert hidden">.</div>
	
			<div id="divQuestionHeaderWithQuitButtons" class="row">
			..
			</div>
		
			<hr style="margin-top:1px; margin-bottom:5px; padding:1px;"/>
			
			<div >
				<div id="divTextarea">
				..
				</div>
				
				<div class="row">
					<div class="span3">
						<h1 class="questionPageSectionHeader">Answer</h1>
					</div>
				</div> 
		
				<div id="divQuestionChoices">
				..
				</div>		
		
				<br/>
				<div id="divExamNavigationButtons">
				..
				</div>
			</div>
		</c:otherwise>
	</c:choose>
	
	<input style="display:none;" id="idCurrentQuestionAsJson" type="text" value="${sessionScope.currentQuestionAsJson}"/>
	
	<input style="display:none;" id="idCurrentExamQuestionIdsSortedByTheirID" type="text" value="${sessionScope.currentExamQuestionIdsSortedByTheirID}"/>
	
	<div style="display:none;" id="dialogText">You're at the end of the exam!</div>
	<div style="display:none;" id="examAlreadyCompletedDialogText">You've already completed this exam!</div>
	
	<div style="display:none;" id="radioButtonExample"><div class="??3 ??4"><input type="radio" name="group1" value="??2" selected=""/>??1</div></div>	
	<div style="display:none;" id="checkboxExample"><div class="??3 ??4"><input type="checkbox" name="??2" value="??2" selected=""/>??1</div></div>
	<div style="display:none;" id="textboxExample"><div class="??3 ??4">Enter your answer:<br/><input type="text" name="stringAnswer" value="??5" autocomplete="off"/></div></div>
	<div style="display:none;" id="sequenceExample"><div class="??3 ??4"><input type="text" name="??2" value="??5" size="2" maxlength="2" autocomplete="off"/> ??1</div></div>

</div>
</div>	

<br/><br/>

</body>
</html>
</jsp:root>
