
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

	<div id="examEntityTableDiv">
	<table class="table table-striped" id="examEntityTable">
		<thead>
			<tr>
				<th></th>
				<th style="width: 400px;">Exams</th>
				<th style="width: 145px;"></th>
				<th style="width: 145px;">Topics</th>
				<th style="width: 160px;">Difficulty</th>
				<th style="width: 70px;">Votes</th>
				<th></th>
			</tr>
			<tr class="filter-row">
				<td style="width:85px;padding-right:0px;">
					<button type="submit" class="btn btn-secondary btn-small" id="idExamsApplyFilterButton" name="applyFilterButton" title="Apply the filter." value="Apply Filter Button"><i class="icon-magic"></i></button>
					<button type="submit" class="btn btn-secondary btn-small" id="idExamsClearFilterButton" name="clearFilterButton" title="Reset the filter." value="Clear Filter Button"><i class="icon-remove"></i></button>
				</td>
				<td>
					<div class="input-append">
						<input id="examContainsFilter" type="text" class="flat small"
							placeholder="Search by exam name..." style="width: 96px;">
						<button id="searchExamsBtn" type="submit" class="btn btn-secondary btn-small">
							<span class="fui-search"></span>
						</button>
					</div>
				</td>
				<td>
					<!-- PLACEHOLDER element, because without it, for some reason, the header
							would not expand the full width. -->
				</td>
				<td>
					<div class="input-append">
						<input id="examTopicContainsFilter" type="text" class="flat small"
							placeholder="Search in topics..." style="width: 96px;">
						<button id="examSearchTopicsBtn" type="submit" class="btn btn-secondary btn-small">
							<span class="fui-search"></span>
						</button>
					</div>
				</td>
				<td>
					<div class="pull-left" style="width: 103px;">
						<select id="examDifficultyFilter" name="small" class="select-block">
							<option value="0">All</option>
							<option value="1">Junior</option>
							<option value="2">Intermediate</option>
							<option value="3">Expert</option>
							<option value="4">Guru</option>
						</select>
					</div>
				</td>
				<td></td>
				<td></td>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
				

	<input type="hidden" id="exam_valueOfLastPressedButton" name="exam_valueOfLastPressedButton">
	<input type="hidden" id="exam_nameOfLastPressedButton" name="exam_nameOfLastPressedButton">
	</div>

	<table id="exam-header-fixed" class="table table-striped span12 fixedTable" style="position:fixed; top:0px; display:none;"></table>
