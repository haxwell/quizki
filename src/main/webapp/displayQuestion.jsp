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

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" xmlns:qfn="http://quizki.com/tld/qfn" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <?xml version="1.0" encoding="UTF-8" ?> ]]>
    </jsp:text>
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<title>Display Question - Quizki</title>

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
		
		<link rel="shortcut icon" href="images/favicon.ico" />
		
		<jsp:text>
						
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
			
			<![CDATA[ <script src="../js/views/question-views.js" type="text/javascript" ></script> ]]>
			
			<![CDATA[ <script src="../js/question-checking.js" type="text/javascript" ></script> ]]>

			<![CDATA[
			<script type="text/javascript">
//					$(document).ready(function(){
//						$("#btnThumbsUp").click(function(){
//							//alert("btn pushed!");
//							$.post("/registerVote.jsp",
//							{
//								voteDirection: "up",
//								entityType: "question",
//								entityId: ${displayQuestion.id}
//							},
//							function(data,status){
//								//alert("Data: " + data + "\nStatus: " + status);
//								
//								if (status == 'success') {
//									$('#divVotingButtons').html('Your vote has been saved!');
//								}
//							});
//						});
//					});					

//					$(document).ready(function(){
//						$("#btnThumbsDown").click(function(){
//						//alert("btn pushed!");
//							$.post("/registerVote.jsp",
//							{
//								voteDirection: "down",
//								entityType: "question",
//								entityId: ${displayQuestion.id}
//							},
//							function(data,status){
//								//alert("Data: " + data + "\nStatus: " + status);
//								
//								if (status == 'success') {
//									$('#divVotingButtons').html('Your vote has been saved!');
//								}
//							});
//						});
//					});					
		    </script> ]]>			

			<![CDATA[
				<script type="text/javascript">

			    $(document).ready(function() {
			    	event_intermediary.initialize();
			    
			    	model_constructor_factory.put("currentQuestion", getFunctionToRetrieveCurrentQuestion);
			    	model_constructor_factory.put("currentUserId", function() { return ${sessionScope.currentUserEntity.id}; });
			    	model_constructor_factory.put("answersToTheMostRecentExam", function() { var ans = '${sessionScope.answersToTheMostRecentExam}'; if (ans.length > 0) return new Backbone.Collection(JSON.parse(ans).answers); else return undefined; });

			    	var currentQuestion = model_factory.get("currentQuestion");
			    	
			    	var _inExamContext = (model_factory.get("answersToTheMostRecentExam") !== undefined);
			    	
			    	if (_inExamContext) {
			    		IsAnsweredCorrectlyThingy.processQuestion();
			    	}
			    	
			    	var bv_questionCreatedByView = new Quizki.CreatedByView({ el: $("#divCreatedBy") });
			    	var bv_questionAndTextView = new Quizki.QuestionTextAndDescriptionView({ el: $("#divTextarea"), readOnly: true });
			    	var bv_questionTypeView = new Quizki.QuestionTypeView({ el: $("#questionTypeView"), readOnly: true });
					var bv_questionChoiceList = new Quizki.ChoiceListView({ el: $("#choiceListDiv"), readOnly: true, inExamContext: _inExamContext });
					
					var bv_difficultyChooser = new Quizki.DifficultyChooserView({ el: $("#difficultyChooserElement"), id:currentQuestion.getDifficultyId(), readOnly: true});
					
					var bv_topicsWell = new Quizki.QuestionAttributeWellView(
						{
							el:$("#topicsWell"), 
							readOnly: true,
							viewKey:'topics', 
							modelToListenTo:'currentQuestion', 
							modelEventToListenFor:'resetQuestion', 
							backboneFunc:function() { return model_factory.get('currentQuestion').getTopics(); }, 
							//modelConstructorFunc:function() { return new Topic(); }, 
							updateModelToListenToFunc:function(modelToListenTo, coll) { modelToListenTo.setTopics(coll); }
						});
						
					var bv_referencesWell = new Quizki.QuestionAttributeWellView(
						{
							el:$("#referencesWell"),
							readOnly: true,
							viewKey:'references', 
							modelToListenTo:'currentQuestion', 
							modelEventToListenFor:'resetQuestion', 
							backboneFunc:function() { return model_factory.get('currentQuestion').getReferences(); },
							//modelConstructorFunc:function() { return new Reference(); }, 
							updateModelToListenToFunc:function(modelToListenTo, coll) { modelToListenTo.setReferences(coll); }
						});

					bv_topicsWell.render();
					bv_referencesWell.render();

					var afFunc = undefined;
					
					if (_inExamContext) {
						afFunc = function () {
							var qcm = model_factory.get("questionCorrectnessModel");
							var msgArr = new Array();

							if (qcm.get('answeredCorrectly') == true) {
								msgArr.push('Great! You answered this question correctly!');

								if (qcm.get('givenAnswer') != undefined) {
									msgArr.push(' You typed <b>' + qcm.get('givenAnswer') + '</b> .');
								}
							}
							else {
								msgArr.push('You missed this question!');
	
								if (qcm.get('correctAndChosen') > 0) {
									var count = qcm.get('correctAndChosen');
									msgArr.push('You made ' + count + ' correct choice' + (count == 1 ? '.' : 's.') );
								}
								
								if (qcm.get('incorrectAndChosen') > 0) {
									var count = qcm.get('incorrectAndChosen');
									msgArr.push(' You made ' + count + ' incorrect choice' + (count == 1 ? '.' : 's.') );
								}

								if (qcm.get('correctButNotChosen') > 0) {
									var count = qcm.get('correctButNotChosen');
									msgArr.push(' There ' + (count < 2 ? 'was ' : 'were ') + count + ' correct choice' + (count == 1 ? ' ' : 's ') + 'that you did not choose.');
								}
								
								if (qcm.get('givenAnswer') != undefined) {
									msgArr.push(' You typed <b>' + qcm.get('givenAnswer') + '</b> .');
								}
							}
							
							populateAlertDiv(msgArr, qcm.get('answeredCorrectly') ? "alert-success" : "alert-error");
						}
					};
					
			    	var bv_header = new Quizki.QuestionHeaderButtonView({ el: $("#divQuestionHeader"), showEditBtn: (!_inExamContext && ${shouldAllowQuestionEditing}), showBackBtn: (_inExamContext), afterDisplayFunction:afFunc });
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
			    
			    //function getEntityId() {
			    //	return $("#idEntityIdField").val();
			    //}
			    
				function populateAlertDiv(msgsArr, alertClassName) {
					var msgs = "";
					
					for (var i=0; i<msgsArr.length; i++) {
						msgs += msgsArr[i] + '<br/>';
					}
					
					var $idAlertDiv = $('div.container').find('#idAlertDiv'); 
											
					$idAlertDiv.html('');
					$idAlertDiv.html(msgs);
					$idAlertDiv.addClass(alertClassName);
					$idAlertDiv.removeClass('hidden');
				}
				
				function clearAlertDiv() {
					$('div.container').find('#idAlertDiv').addClass('hidden');
				}
			    
				</script>
			]]>
		</jsp:text>

	</head>
<body>

	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<div class="content">

		<c:choose>
		    <c:when test="${empty shouldAllowQuestionEditing}">
		      	<br/><br/>
				Oops! Something went wrong! You should <a href="/index.jsp">go back to the beginning.</a>
		    </c:when>
			<c:otherwise>

		<div id="divQuestionHeader" class="row">
		..
		</div>

		<hr style="margin-top:1px; margin-bottom:5px; padding:1px;"/>

		<div id="divCreatedBy">..</div>

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
	
			<div id="idAlertDiv" class="alert hidden">.</div>

			<div id="questionTypeView">..</div>

			<div id="choiceListDiv">
				..
			</div>
		</div>

	<br/>

	<div>
		<input style="display:none;" id="idEntityIdField" type="text" name="entityIdField" value="${sessionScope.entityIdFilter}"/>
	</div>
		</c:otherwise>
	</c:choose>

</div>


</div>

<br/>
<br/>
<br/>

</body>
</html>
</jsp:root>