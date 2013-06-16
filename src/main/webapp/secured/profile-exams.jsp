<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn" %>

	<form id="profileExamForm" action="/secured/ProfileExamsServlet">
	<table class="table table-striped">
		<thead>
			<tr>
				<!--<th>ID</th>-->
				<th><label class="checkbox no-label toggle-all"
					for="checkbox-table-1"> <input type="checkbox" value=""
						id="checkbox-table-1" data-toggle="checkbox"></label></th>
				<th style="width: 400px;">Exams</th>
				<th style="width: 130px;">Topics</th>
				<!--  th>Type</th-->
				<th>Difficulty</th>
				<th>Votes</th>
			</tr>
		</thead>
		<tbody>
				<tr class="filter-row">
					<td></td>
					<td>
						<div class="input-append">
							<input type="text" class="flat small"
								placeholder="Search by exam name..." style="width: 96px;">
							<button type="submit" class="btn btn-secondary btn-small">
								<span class="fui-search"></span>
							</button>
						</div>
					</td>
					<td>
						<div class="input-append">
							<input type="text" class="flat small"
								placeholder="Search in topics..." style="width: 96px;">
							<button type="submit" class="btn btn-secondary btn-small">
								<span class="fui-search"></span>
							</button>
						</div>
					</td>
					<td>
						<div class="pull-left" style="width: 103px;">
							<select name="small" class="select-block">
								<option value="0">All</option>
								<option value="1">Junior</option>
								<option value="2">Intermediate</option>
								<option value="3">Expert</option>
								<option value="4">Guru</option>
							</select>
						</div>
					</td>
				</tr>
				<c:set var="rowNum" value="0"/>
		<c:set var="counter" value="0"/>
		<c:choose>
			<c:when test="${empty fa_listofexamstobedisplayed}">
				<jsp:text><![CDATA[<tr></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr></tr>]]></jsp:text>
				<jsp:text><![CDATA[<td></td><td colspan="6">There are no exams to display! Either adjust the filter above, or add some exams of your own!]]></jsp:text>
				<jsp:text><![CDATA[</tr>]]></jsp:text>
			</c:when>
			<c:otherwise>
		<c:forEach var="exam" items="${fa_listofexamstobedisplayed}">
			<c:set var="rowNum" value="${rowNum + 1}" />
				<c:set var="counter" value="${counter + 1}" />
				<tr id="tableRow_${counter}">
					<!-- td>${exam.id}</td -->
					<c:choose><c:when test="${exam.user.id == currentUserEntity.id}">
						<td>
						<div class="examButtonDiv">
							<input type="submit" value="Edit Exam" id="exam_edit_button_${counter}" name="examButton_${exam.id}"/>
							<input type="submit" value="Delete Exam" id="exam_delete_button_${counter}" name="examButton_${exam.id}"/>
						</div>
						</td>
					</c:when>
					<c:otherwise>
						<td><input type="submit" value="Detail Exam" id="exam_detail_button_${counter}" name="examButton_${exam.id}"/></td>
					</c:otherwise>
					</c:choose>
				<td>${exam.title}</td>
				<td>
					<c:forEach var="topic" items="${exam.topics}">
						${topic.text}<br />
					</c:forEach>
				</td>
				<td>${exam.difficulty.text }</td>
				<td>${qfn:getUpVotesForEntity(exam, sessionScope.voteDataForListOfExamsToBeDisplayed)} /  ${qfn:getDownVotesForEntity(exam,	sessionScope.voteDataForListOfExamsToBeDisplayed)}</td>
				<td><input type="submit" value="Take Exam" id="exam_take_button_${counter}" name="examButton_${exam.id}"/></td>
			<jsp:text><![CDATA[</tr>]]></jsp:text>
		</c:forEach>
		</c:otherwise>
		</c:choose>
		</tbody>
	</table>

	<input type="hidden" id="exam_valueOfLastPressedButton" name="exam_valueOfLastPressedButton">
	<input type="hidden" id="exam_nameOfLastPressedButton" name="exam_nameOfLastPressedButton">
	</form>
