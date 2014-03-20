<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

	<table class="table table-striped" style="margin-bottom:0px;" id="examEntityTableHeader">
		<thead>
			<tr>
				<!--<th>ID</th>-->
				<th><label class="checkbox no-label toggle-all"
					for="select-all-checkbox"> <input type="checkbox" value=""
						id="select-all-checkbox" data-toggle="checkbox"></label></th>
				<th class="examTableQuestionColumn">Questions</th>
				<th class="examTableTopicsColumn">Topics</th>
				<th>Type</th>
				<th>Difficulty</th>
				<!-- th>Votes</th  -->
				<th></th>
			</tr>
		</thead>
	</table>
