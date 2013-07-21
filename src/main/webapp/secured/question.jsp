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
		<link href="../pkgs/jquery-ui/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css"/>

		<link href="../css/quizki-sitewide.css" rel="stylesheet" type="text/css"/> 
		<link href="../css/quizki-buttons.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-switch.css" rel="stylesheet" type="text/css"/>		
		<link href="../css/quizki-text-input-fields.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-text-input-fields-question.css" rel="stylesheet" type="text/css"/>
		<link href="../css/quizki-header-elements.css" rel="stylesheet" type="text/css"/>
		
		<link href="../css/Question.css" rel="stylesheet" type="text/css"/>

		<link rel="shortcut icon" href="../images/favicon.ico" />
		
		<jsp:text>
			<![CDATA[ <script data-main="../js/quizki.js" src="../js/require.js"></script> ]]>
						
			<![CDATA[ <script src="../js/backbone-quizki.js" type="text/javascript"></script> ]]>
			
			<![CDATA[ <script src="../pkgs/jquery/jquery-1.10.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/jquery-ui/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/Flat-UI-master/js/bootstrap.min.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/bootstrap-switch-master/js/bootstrapSwitch.js" type="text/javascript" ></script> ]]>
			
			<![CDATA[ <script src="../pkgs/underscore.js/underscore.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/backbone.js/backbone.js" type="text/javascript" ></script> ]]>

			<![CDATA[ <script src="../js/ajax/ajax-functions.js" type="text/javascript"></script> ]]>

			<![CDATA[ <script src="../js/question.js" type="text/javascript" ></script> ]]>
			
			<![CDATA[ <script src="../js/models/question-models.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/views/question-views.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/collections/question-collections.js" type="text/javascript" ></script> ]]>
			
			<![CDATA[
			
			<script type="text/javascript">
				tinyMCE.init({
				        theme : "advanced",
				        mode : "textareas",
				        plugins : "autoresize",
						content_css : "../css/quizki_tinymce_custom_content.css",
						theme_advanced_font_sizes: "10px,12px,13px,14px,16px,18px,20px",
						font_size_style_values : "10px,12px,13px,14px,16px,18px,20px",
				        theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyfull,|,formatselect",
				        theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,|,undo,redo,|,image,|,hr,removeformat,visualaid,|,sub,sup,|,charmap",
				        theme_advanced_buttons3 : "",
						theme_advanced_path : false,
						theme_advanced_statusbar_location : 0,
						help_shortcut : ""
				});

			    $(document).ready(function() {
			    	Quizki.loadTemplates(["EnterNewChoiceView","QuestionChoiceCollectionView"],
			    		function() {  });
			    		
			    	populateQuestionFields();
			    	
			    	var questionChoiceCollection =
			    			model_factory.get(	"questionChoiceCollection", 
			    								function() { return new Quizki.QuestionChoiceCollection(); }
			    							);
			    	
			    	var currentQuestion = model_factory.get(	"currentQuestion",
																getFunctionToRetrieveCurrentQuestion
			    											);
			    	
			    	questionChoiceCollection.add(currentQuestion.choices);
			    	
			    	var bv_enterNewChoiceView = new Quizki.EnterNewChoiceView({ el: $("#enterNewChoiceContainerDiv") });
					var bv_questionChoiceList = new Quizki.ChoiceListView({ el: $("#fooDiv") });
					
					bv_questionChoiceList.render();
			    });
			    
			    function persistTheQuestion() {
			    	var data_url = "/ajax/question-save.jsp";
			    	var data_obj = { 	text : $("#id_questionText").val(),
			    						description : $("#id_questionDescription").val(),
			    						difficulty : $("#difficultyBtnGroup > .active").attr("value"),
			    						topics : $("#topicsCSV").val(),
			    						references: $("#referencesCSV").val()};
			    						//choices : $
			    }
			    
			    function populateQuestionFields() {
			    	
			    	// this method executes if the entity id hidden field is set
					var qArr = getFunctionToRetrieveCurrentQuestion();
					
					if (qArr != undefined) {

						// set the choices from this question
	    				if (qArr.choices != undefined && qArr.choices.length > 0) {
							var questionChoiceCollection = 
										    			model_factory.get(	"questionChoiceCollection", 
		    								function() { return new Quizki.QuestionChoiceCollection(); }
		    							);

		    				for (var x=0; x<qArr.choices.length; x++) {
		    					questionChoiceCollection.add(qArr.choices[x]);
		    					// consider a way to tell it, don't throw "add" event until ... function()
		    					// also keep in mind, this is prime "refactor to a common method" material
		    				}
	    				}
						
						// - ----= - ---==- --- --==
						// set difficulty radio buttons
						// TODO: make this a view
						var difficultyId = qArr.difficulty_id;
						
						for (var i=0; i<4; i++) {
							$("#difficultyBtn"+i).removeClass('active');
							
							if (difficultyId == i)
								$("#difficultyBtn"+i).addClass('active');
						}								
						
						
						// add topics
						
						// add references
					}
					else {
						// there's no question by that entity ID!
					}
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

		<div class="content">
		<div id="idAlertDiv" class="alert hidden">.</div>

		<div class="row">
			<div class="span3">
				<h1 class="questionPageSectionHeader">Question</h1>
			</div> 
			<div class="span3 offset5">
				<button class="btn btn-block" type="submit" name="button" style="margin-top:25px;">Save and Add Another</button>
			</div>
			<div class="span1">
				<button class="btn btn-block" type="submit" name="button" style="margin-top:25px;">Save</button>
			</div>	
		</div>
	
		<hr style="margin-top:1px; margin-bottom:5px; padding:1px;"/>

	<c:choose>
		<c:when test="${empty requestScope.doNotAllowEntityEditing}">
	
		<div >
			<textarea id="id_questionText" name="questionText" rows="8" class="span12 tinymce">${currentQuestion.text}</textarea><br/>  
			<input class="span12" type="text" maxlength="998" id="id_questionDescription" name="questionDescription" value="${currentQuestion.description}" placeholder="Optional. A few words describing the question."/>
			
			<table class="span12" style="margin-left:0px">
				<tr>
					<td style="width:25%; vertical-align:top;">
						<div class="entityAttributeHeaderName">Difficulty <br/></div>
							<div id="difficultyBtnGroup" class="btn-group" data-toggle="buttons-radio">
								<button id="difficultyBtn1" value="1" type="button" class="btn btn-small btn-primary active">Junior</button>
								<button id="difficultyBtn2" value="2" type="button" class="btn btn-small btn-primary">Intermediate</button>
								<button id="difficultyBtn3" value="3" type="button" class="btn btn-small btn-primary">Well Versed</button>
								<button id="difficultyBtn4" value="4" type="button" class="btn btn-small btn-primary">Guru</button>
							</div>					
					</td>
					<td style="width:33%">
						<div class="entityAttributeHeaderName">Topics<br/> </div>
						<div class="well"></div>
					</td>
					<td style="width:41%">
						<div class="entityAttributeHeaderName">References<br/> </div>
						<div class="well"></div>
					</td>
				</tr>
			</table>

	<div class="row">
		<div class="span3">
			<h1 class="questionPageSectionHeader">Answer</h1>
		</div>
	</div> 
	<hr style="margin-top:1px; margin-bottom:5px; padding:1px;"/>

				<select name="type" id="questionType" title="What form do the answers come in?">
				<c:choose><c:when test="${currentQuestion.questionType.id == 1}"><option value="Single" selected="selected">Single</option></c:when><c:otherwise><option value="Single">Single</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 2}"><option value="Multiple" selected="selected">Multiple</option></c:when><c:otherwise><option value="Multiple">Multiple</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 3}"><option value="String" selected="selected">String</option></c:when><c:otherwise><option value="String">String</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 4}"><option value="Sequence" selected="selected">Sequence</option></c:when><c:otherwise><option value="Sequence">Sequence</option></c:otherwise></c:choose>
				</select>
			
			<br/>
			
			<div id="enterNewChoiceContainerDiv">
				..
			</div>
			
			<div id="fooDiv">
				..
			</div>
		</div>
		
		</c:when>
		<c:otherwise>
			<br/>
			There was an error loading this page. This entity cannot be edited!<br/>
		</c:otherwise>
	</c:choose>
	
	<div>
		<input style="display:none;" id="idEntityIdField" type="text" name="entityIdField" value="${sessionScope.entityIdFilter}"/>
		<input style="display:none;" id="idChoiceIDList" type="text" name="choiceIdList" value=""/>
		
		
		<div id="hiddenDiv1" class="hidden">
					<![CDATA[
				<table id="switchContainerTable">
				<tr id="switchContainerXXX"><div class="span5">
					
					<td>
					<div id="switchXXX" class="inline span1 switch-square" data-on-label="<i class='icon-ok greenText' style='font-size:1.5em;'></i>" data-off-label="<i style='font-size:1.5em;' class='icon-remove redText'></i>">
						<input type="checkbox" checked/>
					</div>
					</td>
					<td>
					<div id="switchTextDivXXX" class="inline span4">
					..
					</div>
					</td>
					<td>
						<div id="choiceBtnDiv">
						<button type="submit" class="btn btn-secondary btn-small choice_edit_button" id="choice_edit_button_ROWNUM" name="choiceButton_OBJID" value="Edit Choice"><i class="icon-pencil"></i></button>
						<button type="submit" class="btn btn-secondary btn-small choice_delete_button" id="choice_delete_button_ROWNUM" name="choiceButton_OBJID" value="Delete Choice"><i class="icon-remove"></i></button>						
						</div>
					</td>
				</div></tr>
				<tr id="switchContainerYYY"><div class="span5">
					
					<td>
					<div id="switchYYY" class="inline span1 switch-square" data-on-label="<i class='icon-ok greenText' style='font-size:1.5em;'></i>" data-off-label="<i style='font-size:1.5em;' class='icon-remove redText'></i>">
						<input type="checkbox" />
					</div>
					</td>
					<td>
					<div id="switchTextDivYYY" class="inline span4">
					..
					</div>
					</td>
					<td>
						<div id="choiceBtnDiv">
						<button type="submit" class="btn btn-secondary btn-small choice_edit_button" id="choice_edit_button_ROWNUM" name="choiceButton_OBJID" value="Edit Choice"><i class="icon-pencil"></i></button>
						<button type="submit" class="btn btn-secondary btn-small choice_delete_button" id="choice_delete_button_ROWNUM" name="choiceButton_OBJID" value="Delete Choice"><i class="icon-remove"></i></button>						
						</div>
					</td>
				</div></tr>

				</table>
				]]>
		</div>
	</div>
	
</div>
</div>

<script type="text/javascript">
	
	var questionChoiceCollection = new Quizki.QuestionChoiceCollection();
	
</script>

<br/>
<br/>
<br/>

</body>
</html>
</jsp:root>