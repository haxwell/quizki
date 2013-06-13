<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

<form id="examAvailableQuestionsForm" action="/secured/ExamServlet">
	<table class="table table-striped">
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
			</tr>
		</thead>
		<tbody>
			<tr class="filter-row">
				<td></td>
				<td>
					<div class="row">
						<select name="small" class="span2">
							<option value="0">All</option>
							<option value="1">Selected</option>
							<option value="2">Mine</option>
						</select>
						<div class="input-append" style="margin-left: 10px;">
							<input type="text" class="flat small"
								placeholder="Search in questions..." style="width: 216px;">
							<button type="submit" class="btn btn-secondary btn-small">
								<span class="fui-search"></span>
							</button>
						</div>
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
					<div class="pull-left" style="width: 90px;">
						<select name="small" class="select-block">
							<option value="0">All</option>
							<option value="1">Selected</option>
							<option value="2">Mine</option>
						</select>
					</div>
				</td>
				<td>
					<div class="pull-left" style="width: 87px;">
						<select name="small" class="select-block">
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
			<c:set var="rowNum" value="0" />
			<c:set var="counter" value="0" />
			<c:choose>
				<c:when test="${empty fa_listofquestionstobedisplayed}">
					<jsp:text>
						<![CDATA[<tr></tr>]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[<tr>]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[<tr></tr>]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[<tr>]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[<tr></tr>]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[<tr>]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[<tr></tr>]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[<tr>]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[<td></td><td colspan="6">There are no questions to display! Either adjust the filter above, or add some questions of your own!]]>
					</jsp:text>
					<jsp:text>
						<![CDATA[</tr>]]>
					</jsp:text>
				</c:when>
				<c:otherwise>
					<c:forEach var="question"
						items="${fa_listofquestionstobedisplayed}">
						<c:set var="rowNum" value="${rowNum + 1}" />
						<c:set var="counter" value="${counter + 1}" />
						<tr id="tableRow_${counter}">

							<!--td>${question.id}</td>-->
							<td><c:choose>
									<c:when
										test="${qfn:contains(sessionScope.exam_selectedQuestionIds, question.id)}">
										<label class="checkbox no-label checked"
											for="checkbox-table-2"> <input type="checkbox"
											value="" id="checkbox-table-2" data-toggle="checkbox"
											id="chkbox_${counter}"
											name="selectQuestionChkbox_${question.id}" value="" />
										</label>
									</c:when>
									<c:otherwise>
										<label class="checkbox no-label" for="checkbox-table-2">
											<input type="checkbox" value="" id="checkbox-table-2"
											data-toggle="checkbox" id="chkbox_${counter}"
											name="selectQuestionChkbox_${question.id}" value="" />
										</label>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${empty question.description}">
										<a href="/displayQuestion.jsp?questionId=${question.id}">${question.textWithoutHTML}</a>
									</c:when>
									<c:otherwise>
										<a href="/displayQuestion.jsp?questionId=${question.id}">${question.description}</a>
									</c:otherwise>
								</c:choose></td>
							<td><c:forEach var="topic" items="${question.topics}">
						${topic.text}<br />
								</c:forEach></td>
							<td>${question.questionType.text}</td>
							<td>${question.difficulty.text}</td>
							<td>${qfn:getUpVotesForEntity(question,
								sessionScope.voteDataForListOfQuestionsToBeDisplayed)} /
								${qfn:getDownVotesForEntity(question,
								sessionScope.voteDataForListOfQuestionsToBeDisplayed)}</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>

	<input type="hidden" id="valueOfLastPressedButton"
		name="valueOfLastPressedButton"> <input type="hidden"
		id="nameOfLastPressedButton" name="nameOfLastPressedButton">
</form>
