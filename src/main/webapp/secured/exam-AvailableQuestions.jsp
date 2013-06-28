<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

<!--form id="examAvailableQuestionsForm" action="/secured/ExamServlet"-->
	<table class="table table-striped" id="examEntityTableRows">
		<tbody>

			<!-- Javascript inserts rows here based on AJAX call -->
				
		</tbody>
	</table>

	<input type="hidden" id="valueOfLastPressedButton"
		name="valueOfLastPressedButton"> <input type="hidden"
		id="nameOfLastPressedButton" name="nameOfLastPressedButton">
<!--/form-->

	<!-- table id="header-fixed" class="table table-striped span12 fixedTable" style="position:fixed; top:0px; display:none;"></table  -->
	<div id="header-fixed" class="span12 fillBackgroundColor" style="position:fixed; top:0px; margin-left:0px; padding-top:15px; display:none;"></div>