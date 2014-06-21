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
		
		<title>Generate Exam</title>

		<link href="../pkgs/Flat-UI-master/bootstrap/css/bootstrap.css" rel="stylesheet"/>
		<link href="../pkgs/Flat-UI-master/css/flat-ui.css" rel="stylesheet"/>
		<link href="../pkgs/font-awesome/css/font-awesome.css" rel="stylesheet"/>
		<link href="../pkgs/jquery-ui/jquery-ui-1.10.4/css/ui-lightness/jquery-ui-1.10.4.css" rel="stylesheet" type="text/css"/>

		<link href="../css/quizki-sitewide.css" rel="stylesheet" type="text/css"/> 
		<link href="../css/quizki-buttons.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-switch.css" rel="stylesheet" type="text/css"/>		
		<link href="../css/quizki-header-elements.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-select-multiselect-generateExam.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-text-input-fields-generateExam.css" rel="stylesheet" type="text/css"/>
		
		<link href="../css/Question.css" rel="stylesheet" type="text/css"/>

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

			<![CDATA[ <script src="../js/views/views.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/choice.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/topic.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/reference.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/keyValuePair.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/question.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/exam.js" type="text/javascript" ></script> ]]>
			

			<![CDATA[ <script src="../js/generateExam.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/views/generateExam-views.js" type="text/javascript" ></script> ]]>

			<![CDATA[
			
			<script type="text/javascript">
				
					function setDisplayDimensionsAccordingToCurrentWindowHeight() {
						// set the height of the content area according to the browser height
						var bottomBufferHeight = 175;
						var windowHeight = $(window).height();
						
						$('#tabs').height(windowHeight - bottomBufferHeight);
						$('#availableExamsDiv').height(windowHeight - bottomBufferHeight);
					}


			    $(document).ready(function() {
					event_intermediary.initialize();
					MatchingExamsListGetter.initialize();
					
					model_constructor_factory.put("currentListOfTopics", function() { var c = new Backbone.Collection([], { model: Topic }); c.comparator = function(a) { return a.get('text').toLowerCase(); }; return c; });
					model_constructor_factory.put("selectedListOfTopics", function() { var c = new Backbone.Collection([], { model: Topic }); c.comparator = function(a) { return a.get('text').toLowerCase(); }; return c; });
					model_constructor_factory.put("listOfMatchingExams", function() { var c = new Backbone.Collection([], { model: Exam }); c.comparator = 'title'; return c; });
					model_constructor_factory.put("matchingExamsMustContainAllTopics", function() { return { val:false }; });
					model_constructor_factory.put("numberOfQuestions", function() { return { val:5 }; });
					model_constructor_factory.put("difficultyObj", function() { return new Difficulty().initialize(); });
					model_constructor_factory.put("userObj", function() { return { isLoggedIn:${not empty sessionScope.currentUserEntity} }; });					

					FilteredTopicListGetter.get(false, '', model_factory.get("currentListOfTopics"));
					
					MatchingExamsListGetter.listenFor(
						'selectedListOfTopicsChanged', 
						function() { return model_factory.get("listOfMatchingExams"); }, 
						function() { return model_factory.get("selectedListOfTopics"); },
						function() { return false; },
						function() { return model_factory.get("matchingExamsMustContainAllTopics").val; }
					);
					
					var bv_headerTextForGenerateExam = new Quizki.HeaderTextForGenerateExam({ el: $("#divHeaderText") });
					
					var bv_optionsView = new Quizki.GenerateExamOptions({ el: $("#divOptionsView") });
					
					var bv_allTopicsListView = new Quizki.AllTopicsListView({ el: $("#divAllTopicsListView") });
					var bv_allTopicsListFilterView = new Quizki.AllTopicsListFilterView({ el: $("#divAllTopicsListFilterView") });
					var bv_selectedTopicsListView = new Quizki.SelectedTopicsListView({ el: $("#divSelectedTopicsListView") });
					var bv_arrowView = new Quizki.ArrowView({ el: $("#divArrowView") });
					
					//var bv_matchingExamsListFilterView = new Quizki.MatchingExamsFilterView({ el: $("#divMatchingExamsFilterView") });
					//var bv_matchingExamsListView = new Quizki.MatchingExamsView({ el: $("#divMatchingExamsView") });
					
					var bv_maxQuestionsView = new Quizki.MaxQuestionsView({ el: $("#divMaxQuestionsView") });
					var bv_takeGeneratedExamBtnView = new Quizki.TakeGeneratedExamButtonView({ el: $("#takeGeneratedExamBtnView") });
					//var bv_takeSelectedExamBtnView = new Quizki.TakeSelectedExamButtonView({ el: $("#takeSelectedExamBtnView") });
					
					var bv_difficultyChooser = new Quizki.DifficultyChooserView({ el: $("#difficultyChooserElement"), id:1, getModelNameKey:"difficultyObj" });
			    });
			    
			</script>]]>
		</jsp:text>
	</head>
	<body>
	
		<div class="container">
			<jsp:include page="header.jsp"></jsp:include>
			<div class="content">
			
				<div id="divHeaderText">..</div>
				<br/>
				
				<table>
					<tr>
					<td style="vertical-align:top;">
						<div id="divOptionsView">..</div>
					</td>
					<td style="vertical-align:top;">
						<div id="divAllTopicsListView">..</div>
					</td>
					<td style="vertical-align:top;">
						<div id="divArrowView">..</div>
					</td>
					<td>
						<div id="divSelectedTopicsListView">..</div>
						<div id="takeGeneratedExamBtnView">..</div>
					</td>
					<td>
						<!--  <div id="divMatchingExamsFilterView">..</div>  -->
						
						<!--  <div id="divMatchingExamsView" style="margin-left:20px;">..</div> -->
						<!--  <div id="takeSelectedExamBtnView">..</div> -->
					</td>
					</tr>
				</table>
			</div>
		</div>	
	
		<br/><br/>
	
	</body>

</html>
</jsp:root>
