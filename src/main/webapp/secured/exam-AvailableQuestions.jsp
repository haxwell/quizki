<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

<!--form id="examAvailableQuestionsForm" action="/secured/ExamServlet"-->
	<table class="table table-striped" id="examEntityTable">
		<thead>
			<tr>
				<!--<th>ID</th>-->
				<th><label class="checkbox no-label toggle-all"
					for="checkbox-table-1"> <input type="checkbox" value=""
						id="checkbox-table-1" data-toggle="checkbox"></label></th>
				<th style="width: 400px;">Questions</th>
				<th style="width: 130px;">Topics</th>
				<th>Author</th>
				<th>Type</th>
				<th>Difficulty</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr class="filter-row">
				<td></td>
				<td>
					<div class="row">
						<select id="rangeOfQuestionsFilter" name="small" class="span1">
							<option value="0">All</option>
							<option value="1">Mine</option>
							<option value="2">Selected</option>
						</select>
						<div class="input-append" style="margin-left: 10px;">
							<input id="containsFilter" type="text" class="flat small"
								placeholder="Search in questions..." style="width: 216px;">
							<button type="submit" class="btn btn-secondary btn-small" id="searchQuestionsBtn">
								<span class="fui-search"></span>
							</button>
						</div>
					</div>
				</td>
				<td>
					<div class="input-append">
						<input id="topicContainsFilter" type="text" class="flat small"
							placeholder="Search in topics..." style="width: 96px;">
						<button type="submit" class="btn btn-secondary btn-small" id="searchTopicsBtn">
							<span class="fui-search"></span>
						</button>
					</div>
				</td>
				<td>
					<div class="pull-left" style="width: 90px;">
						<select name="small" class="select-block">
							<option value="0">All</option>
							<option value="1">Mine</option>
							<option value="2">Selected</option>
						</select>
					</div>
				</td>
				<td>
					<div class="pull-left" style="width: 87px;">
						<select id="questionTypeFilter" name="small" class="select-block">
							<option value="0">All</option>
							<option value="1">Single</option>
							<option value="2">Multiple</option>
							<option value="3">String</option>
							<option value="4">Sequence</option>
						</select>
					</div>
				</td>
				<td>
					<div class="pull-left" style="width: 103px;">
						<select id="difficultyFilter" name="small" class="select-block">
							<option value="0">All</option>
							<option value="1">Junior</option>
							<option value="2">Intermediate</option>
							<option value="3">Expert</option>
							<option value="4">Guru</option>
						</select>
					</div>
				</td>
				<td></td>
			</tr>

			<!-- Javascript inserts rows here based on AJAX call -->
				
		</tbody>
	</table>

	<input type="hidden" id="valueOfLastPressedButton"
		name="valueOfLastPressedButton"> <input type="hidden"
		id="nameOfLastPressedButton" name="nameOfLastPressedButton">
<!--/form-->
