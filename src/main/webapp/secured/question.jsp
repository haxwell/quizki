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
			<![CDATA[ <script src="../pkgs/jquery/jquery-1.10.1.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/jquery-ui/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../pkgs/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/Flat-UI-master/js/bootstrap.min.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../pkgs/bootstrap-switch-master/js/bootstrapSwitch.js" type="text/javascript" ></script> ]]>
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

		//$(function() {
		   //$( document ).tooltip();
		 //});
		 
    $( "#open-event" ).tooltip({
      show: null,
      position: {
        my: "left top",
        at: "left bottom"
      },
      open: function( event, ui ) {
        ui.tooltip.animate({ top: ui.tooltip.position().top + 10 }, "slow" );
      }
    });

	$(document).ready(function() {
	    $("#questionType").change(function() {
	    	var val = $("#questionType option:selected").text();
	    	
	    	if (val == 'String' || val == 'Sequence')
	    	{
	    		$(".componentSignifiesChoiceCorrectness").attr("disabled", "disabled"); 
	    	}
	    	else
	    	{
	    		$(".componentSignifiesChoiceCorrectness").attr("disabled", null); 
	    	}
	    });    
    });		 
</script>
]]>
		</jsp:text>
				
	</head>
<body>

	<div class="container">
		<jsp:include page="../header.jsp"></jsp:include>
		<div>


      <c:if test="${not empty requestScope.successes}">
      	<c:forEach var="str" items="${requestScope.successes}">
      		<span class="greenText">${str}</span><br/>	
      	</c:forEach>
      	<br/>
      </c:if>
      
      <c:if test="${not empty requestScope.validationErrors}">
      	<c:forEach var="str" items="${requestScope.validationErrors}">
      		<span class="redText">${str}</span><br/>
      	</c:forEach>
      	<br/>      	
      </c:if>

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
		<form action="/secured/QuestionServlet" method="post">
			<textarea id="id_questionText" name="questionText" rows="8" class="span12 tinymce">${currentQuestion.text}</textarea><br/>  
			<input class="span12" type="text" maxlength="998" id="id_questionDescription" name="questionDescription" value="${currentQuestion.description}" placeholder="Optional. A few words describing the question."/>
			
			<table class="span12" style="margin-left:0px">
				<tr>
					<td style="width:25%; vertical-align:top;">
						<div class="entityAttributeHeaderName">Difficulty <br/></div>
							<div class="btn-group" data-toggle="buttons-radio">
								<button type="button" class="btn btn-small btn-primary active">Junior</button>
								<button type="button" class="btn btn-small btn-primary">Intermediate</button>
								<button type="button" class="btn btn-small btn-primary">Well Versed</button>
								<button type="button" class="btn btn-small btn-primary">Guru</button>
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
			
			<input class="span6" type="text" name="choiceText" size="35" maxlength="998" placeholder="Enter answer.."/>
			<![CDATA[
			<div class="switch switch-square" data-on-label="<i class='icon-ok greenText' style='font-size:1.5em;'></i>" data-off-label="<i style='font-size:1.5em;' class='icon-remove redText'></i>">
				<input type="checkbox" checked/>
			</div>

			<button class="btn submitAnswer" type="submit" name="button">
				<i class="icon-plus icon-white"></i>
			</button>
			]]>
						
			<br/>
				
		</form>
		</div>
		
		</c:when>
		<c:otherwise>
			<br/>
			There was an error loading this page. This entity cannot be edited!<br/>
		</c:otherwise>
	</c:choose>
	
	
</div>
</div>

</body>
</html>
</jsp:root>