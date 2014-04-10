<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn" %>

	
	<div id="questionEntityTableDiv">
	<!-- form id="profileQuestionForm" action="/secured/ProfileQuestionsServlet"  -->	
	<table class="table table-striped" id="questionEntityTable">
		<thead>
			<tr>
				<th></th>
				<th style="width: 400px;">Questions</th>
				<th style="width: 145px;">Topics</th>
				<th style="width: 145px;">Type</th>
				<th style="width: 160px;">Difficulty</th>
				<th style="width: 70px;"><!-- Votes --></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr class="filter-row">
					<td style="width:85px;padding-right:0px;">
						<button type="submit" class="btn btn-secondary btn-small" id="idApplyFilterButton" name="applyFilterButton" title="Apply the filter." value="Apply Filter Button"><i class="icon-magic"></i></button>
						<button type="submit" class="btn btn-secondary btn-small" id="idClearFilterButton" name="clearFilterButton" title="Reset the filter." value="Clear Filter Button"><i class="icon-remove"></i></button>
					</td>
					<td>
						<div class="input-append" style="margin-left: 10px;">
							<input id="containsFilter" type="text" class="flat small"
								placeholder="Search in questions..." style="width: 216px;">
							<button id="searchQuestionsBtn" type="submit" class="btn btn-secondary btn-small">
								<span class="fui-search"></span>
							</button>
						</div>
					</td>					
					<td>
						<div class="input-append">
							<input id="topicContainsFilter" type="text" class="flat small"
								placeholder="Search in topics..." style="width: 96px;">
							<button id="searchTopicsBtn" type="submit" class="btn btn-secondary btn-small">
								<span class="fui-search"></span>
							</button>
						</div>
					</td>
					<td>
						<div class="pull-left" style="width: 87px;">
							<select id="questionTypeFilter" name="small" class="select-block">
								<option value="0">All</option>
								<option value="1">Single</option>
								<option value="2">Multiple</option>
								<option value="3">Phrase</option>
								<option value="4">Sequence</option>
								<option value="5">Set</option>
							</select>
						</div>
					</td>
					<td>
						<div class="pull-left" style="width: 103px;">
							<select id="difficultyFilter" name="small" class="select-block">
								<option value="0">All</option>
								<option value="1">Junior</option>
								<option value="2">Intermediate</option>
								<option value="3">Senior</option>
								<option value="4">Guru</option>
							</select>
						</div>
					</td>
					<td>
						<!-- Votes  -->
					</td>
					<td></td>
				</tr>
		</tbody>
	</table>

	<input type="hidden" id="valueOfLastPressedButton" name="valueOfLastPressedButton">
	<input type="hidden" id="nameOfLastPressedButton" name="nameOfLastPressedButton">
	<input type="hidden" id="Questions-delete-entity-dataObjectDefinition" name="Questions-delete-entity-dataObjectDefinition">
	

	<!-- /form  -->
	</div>	
	
	<table id="header-fixed" class="table table-striped span12 fixedTable" style="position:fixed; top:0px; display:none;"></table>
