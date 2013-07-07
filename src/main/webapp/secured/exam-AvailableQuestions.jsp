<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

	<table class="table table-striped" id="examEntityTableRows">
		<tbody>

			<!-- Javascript inserts rows here based on AJAX call -->
				
		</tbody>
	</table>

	<input type="hidden" id="valueOfLastPressedButton" name="valueOfLastPressedButton">
	<input type="hidden" id="nameOfLastPressedButton" name="nameOfLastPressedButton">

	<div id="header-fixed" class="span12 fillBackgroundColor" style="position:fixed; top:0px; margin-left:0px; padding-top:15px; display:none;"></div>