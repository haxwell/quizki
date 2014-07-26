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

		<title>Question - Quizki</title>

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
		
		<link href="../css/Question.css" rel="stylesheet" type="text/css"/>

		<link rel="shortcut icon" href="../images/favicon.ico" />
		
		<jsp:text>
						
			<![CDATA[ <script src="../pkgs/jquery/jquery-1.11.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/jquery-ui/jquery-ui-1.10.4/js/jquery-ui-1.10.4.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/tiny_mce/tinymce.min.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/Flat-UI-master/js/bootstrap.min.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/bootstrap-switch-master/js/bootstrapSwitch.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/complete.ly/complete.ly.1.0.1.js" type="text/javascript" ></script> ]]>
			
			<![CDATA[ <script src="../pkgs/underscore.js/underscore.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/backbone.js/backbone.js" type="text/javascript" ></script> ]]>

			<![CDATA[ <script src="../js/stringUtil.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/backbone-quizki.js" type="text/javascript"></script> ]]>
			
			<![CDATA[ <script src="../js/ajax/ajax-functions.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/autocomplete.js" type="text/javascript" ></script> ]]>

			<![CDATA[ <script src="../js/views/views.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/choice.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/topic.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/reference.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/keyValuePair.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/models/question.js" type="text/javascript" ></script> ]]>
						
			<![CDATA[ <script src="../js/views/question-views.js" type="text/javascript" ></script> ]]>
			
			<![CDATA[
			
			<script type="text/javascript">
				function questionTinyMCEBlurHandler(text) {
			        var currentQuestion = model_factory.get("currentQuestion");
			        currentQuestion.setText(text, false);
			        
			        // do custom stuff
			        var func = PostQuestionTextChangedEventFactory.getHandler(currentQuestion);
			        if (func != undefined)
			        	func(currentQuestion);
				};
				
			    $(document).ready(function() {
			    	var topicsViewKey = 'topics';
			    	var referencesViewKey = 'references';
			    	
			    	event_intermediary.initialize();
			    	
			    	model_constructor_factory.put("currentQuestion", getFunctionToRetrieveCurrentQuestion);
			    	model_constructor_factory.put("topicsAutocompleteHistory", function() {
			    		return getAutocompleteHistoryForEnvironment(topicsViewKey);
			    	});
			    	model_constructor_factory.put("referencesAutocompleteHistory", function() {
			    		return getAutocompleteHistoryForEnvironment(referencesViewKey);
			    	});
			    	
			    	ReadOnlyManager.addHandler(QUESTION_TYPE_PHRASE, function(question) {
				    	return getCountOfDynamicFields(question.getText()) > 0;
			    	});
			    		
			    	var currentQuestion = model_factory.get("currentQuestion");
			    	var bv_questionAndTextView = new Quizki.QuestionTextAndDescriptionView({ el: $("#divTextarea") });
			    	
			    	var bv_questionTypeView = new Quizki.QuestionTypeView({ el: $("#questionTypeView") });
			    	var bv_enterNewChoiceView = new Quizki.EnterNewChoiceView({ el: $("#enterNewChoiceContainerDiv") });
					var bv_questionChoiceList = new Quizki.ChoiceListView({ el: $("#choiceListDiv") });
					
					var bv_difficultyChooser = new Quizki.DifficultyChooserView({ el: $("#difficultyChooserElement"), id:currentQuestion.getDifficultyId(), getModelNameKey:"currentQuestion" });
					
					
					var bv_topicsWell = new Quizki.QuestionAttributeWellView(
						{
							el:$("#topicsWell"), 
							viewKey:topicsViewKey, 
							KeyTo_modelToListenTo:'event_intermediary', 
							modelEventToListenFor:'currentQuestion::put::model_factory', 
							backboneFunc:function() { return model_factory.get('currentQuestion').getTopics(); }, 
							updateModelToListenToFunc:function(modelToListenTo, coll) { modelToListenTo.setTopics(coll); }
						});
						
					var bv_referencesWell = new Quizki.QuestionAttributeWellView(
						{
							el:$("#referencesWell"), 
							viewKey:referencesViewKey, 
							KeyTo_modelToListenTo:'event_intermediary', 
							modelEventToListenFor:'currentQuestion::put::model_factory', 
							backboneFunc:function() { return model_factory.get('currentQuestion').getReferences(); },
							updateModelToListenToFunc:function(modelToListenTo, coll) { modelToListenTo.setReferences(coll); }
						});

					
					bv_topicsWell.render();
					bv_referencesWell.render();

					var func = function () {
						var rtn = model_factory.get("currentQuestion").getDataObject();

						var topicsAutocompleteEntries = model_factory.get(topicsViewKey + "AutocompleteEntries").pluck('value');
						rtn.topicsAutocompleteEntries = JSON.stringify(topicsAutocompleteEntries);
						
						var referencesAutocompleteEntries = model_factory.get(referencesViewKey + "AutocompleteEntries").pluck('value');
						rtn.referencesAutocompleteEntries = JSON.stringify(referencesAutocompleteEntries);
						
						var deletedAutocompleteEntries = model_factory.get(topicsViewKey + "DeletedAutocompleteEntries").pluck('value');
						rtn.topicsDeletedAutocompleteEntries = JSON.stringify(deletedAutocompleteEntries);
						
						deletedAutocompleteEntries = model_factory.get(referencesViewKey + "DeletedAutocompleteEntries").pluck('value');
						rtn.referencesDeletedAutocompleteEntries = JSON.stringify(deletedAutocompleteEntries);						
						
						return rtn;
					};
					
			    	var bv_header = new Quizki.SaveButtonView({ el: $("#divQuestionHeaderWithSaveButtons"), getDataObjectFunc:func});
			    });
			    
				// this same code is in displayQuestion.jsp.. extract it somewhere
			    function addCSVItemsToWell(view, backboneListOfItems) {
					var items = '';
					var arr = new Array();
				    var collection = model_factory.get(view.getModelKey());
				    						
					if (backboneListOfItems.length > 0) {
						items = backboneListOfItems.pluck('text');

						for (var i=0; i<items.length; i++) {
							arr.push(items[i]);
						}
				    	
				    	arr = method_utility.giveAttributeNamesToElementsOfAnArray("text",arr);
			    	}

			    	collection.addArray(arr);
			    }
			    
			    function getEntityId() {
			    	// this field is set as a hidden field. Its value comes from the Request 
			    	return $("#idEntityIdField").val();
			    }
			    
			</script>]]>
		</jsp:text>
				
	</head>
<body>

	<div class="container">
		<jsp:include page="../header.jsp"></jsp:include>

	<c:choose>
		<c:when test="${not empty requestScope.doNotAllowEntityEditing}">
			<br/>
			2 There was an error loading this page. This entity cannot be edited! You should <a href="/index.jsp">go back to the beginning.</a><br/>
		</c:when>
		<c:otherwise>
		
			<div class="content">
				<div id="idAlertDiv" class="alert hidden">.</div>
	
				<div id="divQuestionHeaderWithSaveButtons" class="row">
				..
				</div>
		
				<hr style="margin-top:1px; margin-bottom:5px; padding:1px;"/>
		
				<div >
					<div id="divTextarea">
					..
					</div>
					
					<table class="span12" style="margin-left:0px">
						<tr>
							<td style="width:25%; vertical-align:top;">
								<div class="entityAttributeHeaderName">Difficulty <br/></div>
								<div id="difficultyChooserElement"></div>
							</td>
							<td style="width:33%; vertical-align:top;">
								<div class="entityAttributeHeaderName">Topics<br/> </div>
								<div id="topicsWell"></div>
							</td>
							<td style="width:41%; vertical-align:top;">
								<div class="entityAttributeHeaderName">References<br/> </div>
								<div id="referencesWell"></div>
							</td>
						</tr>
					</table>
		
					<div class="row">
						<div class="span3">
							<h1 class="questionPageSectionHeader">Answer</h1>
						</div>
					</div> 
		
					<hr style="margin-top:1px; margin-bottom:5px; padding:1px;"/>
		
					<div id="questionTypeView">..</div>
								
					<div id="enterNewChoiceContainerDiv">
					..
					</div>
					
					<div id="choiceListDiv">
					..
					</div>
				</div>
			
			</div> <!-- div: content -->
		
		</c:otherwise>
	</c:choose>
	
	<div>
		<input style="display:none;" id="idEntityIdField" type="text" name="entityIdField" value="${sessionScope.entityIdFilter}"/>
		<input style="display:none;" id="idChoiceIDList" type="text" name="choiceIdList" value=""/>
	</div>
	
</div>
	
<br/>
<br/>
<br/>

</body>
</html>
</jsp:root>