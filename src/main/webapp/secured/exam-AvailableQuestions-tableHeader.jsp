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
