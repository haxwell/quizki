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
				<th style="width: 70px;"><!-- Votes  --></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr class="filter-row">
				<td style="width:85px;padding-right:0px;">
					<button type="submit" class="btn btn-secondary btn-small" id="idExamsApplyFilterButton" name="applyFilterButton" title="Apply the filter." value="Apply Filter Button"><i class="icon-magic"></i></button>
					<button type="submit" class="btn btn-secondary btn-small" id="idExamsClearFilterButton" name="clearFilterButton" title="Reset the filter." value="Clear Filter Button"><i class="icon-remove"></i></button>
				</td>
				<td>
					<c:choose>
						<c:when test="${not empty sessionScope.currentUserEntity}">
							<select id="rangeOfExamsFilter" name="small" class="span1">
								<option value="0">All</option>
								<option value="1">Mine</option>
							</select>
						</c:when>
						<c:otherwise>
							<!-- add a hidden field for the dataObjectDefinition to refer to, since our original field is not displayed as there is no user logged in -->
							<input style="display:none;" id="rangeOfExamsFilter" type="text" value="0"/>
						</c:otherwise>
					</c:choose>
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
							<option value="3">Senior</option>
							<option value="4">Guru</option>
						</select>
					</div>
				</td>
				<td></td>
				<td></td>
			</tr>
		</tbody>
	</table>
				

	<input type="hidden" id="exam_valueOfLastPressedButton" name="exam_valueOfLastPressedButton">
	<input type="hidden" id="exam_nameOfLastPressedButton" name="exam_nameOfLastPressedButton">
	</div>

	<table id="exam-header-fixed" class="table table-striped fixedTable" style="position:fixed; top:0px; display:none;"></table>
