<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<form action="/secured/ProfileQuestionsServlet">
	<table class="displayExam">
		<thead>
		<tr>
			<th>ID</th>
			<th></th>
			<th>Question</th>
			<th>Topics</th>
			<th>Type</th>
			<th>Difficulty</th>
			<th>Votes</th>
		</tr>
		</thead>
		<tbody>
				<tr>
					<td></td>
					<td style="text-align:right"><input type="submit" name="runFilter" value="Run Filter -->"/> </td>
					<td><input type="text" name="containsFilter" value="${mruFilterText}" title="Only show questions containing this text..." style="width:100%;"/></td>
					<td><input type="text" name="topicFilter" value="${mruFilterTopicText}" title="Only show questions belonging to topics containing this text.."/></td>
					<td >
						<select name="questionTypeFilter" title="Only include questions of type..">
							<c:choose><c:when test="${mruFilterQuestionType == 0}"><option value="all" selected="selected">All</option></c:when><c:otherwise><option value="all" >All</option></c:otherwise></c:choose>
							<c:choose><c:when test="${mruFilterQuestionType == 1}"><option value="single" selected="selected">Single</option></c:when><c:otherwise><option value="single" >Single</option></c:otherwise></c:choose>
							<c:choose><c:when test="${mruFilterQuestionType == 2}"><option value="multiple" selected="selected">Multiple</option></c:when><c:otherwise><option value="multiple" >Multiple</option></c:otherwise></c:choose>
							<c:choose><c:when test="${mruFilterQuestionType == 3}"><option value="string" selected="selected">String</option></c:when><c:otherwise><option value="string" >String</option></c:otherwise></c:choose>
							<c:choose><c:when test="${mruFilterQuestionType == 4}"><option value="sequence" selected="selected">Sequence</option></c:when><c:otherwise><option value="sequence">Sequence</option></c:otherwise></c:choose>
						</select>
					</td>
					<td >
						<select name="difficultyFilter" title="Do not include any questions more difficult than..">
							<c:choose><c:when test="${mruFilterDifficulty == 1}"><option value="junior" selected="selected">Junior</option></c:when><c:otherwise><option value="junior" >Junior</option></c:otherwise></c:choose>
							<c:choose><c:when test="${mruFilterDifficulty == 2}"><option value="intermediate" selected="selected">Intermediate</option></c:when><c:otherwise><option value="intermediate" >Intermediate</option></c:otherwise></c:choose>
							<c:choose><c:when test="${mruFilterDifficulty == 3}"><option value="wellversed" selected="selected">Well-versed</option></c:when><c:otherwise><option value="wellversed" >Well-versed</option></c:otherwise></c:choose>
							<c:choose><c:when test="${mruFilterDifficulty == 4}"><option value="guru" selected="selected">Guru</option></c:when><c:otherwise><option value="guru">Guru</option></c:otherwise></c:choose>
						</select>
					</td>
					<td></td>
				</tr>
		<c:set var="rowNum" value="0"/>
				<c:choose >
				<c:when test="${empty fa_listoquestionstobedisplayed}">
					<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
					<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
					<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
					<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
					<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
					<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
					<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
					<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
					<jsp:text><![CDATA[<td></td><td colspan="6">There are no questions to display! Either adjust the filter above, or add some questions of your own!]]></jsp:text>
					<jsp:text><![CDATA[</tr>]]></jsp:text>
				</c:when>
				<c:otherwise>
		<c:forEach var="question" items="${fa_listoquestionstobedisplayed}">
			<c:set var="rowNum" value="${rowNum + 1}" />
			<c:choose><c:when test="${rowNum % 2 == 0}">
			<jsp:text><![CDATA[<tr>]]></jsp:text>
			</c:when>
			<c:otherwise>
			<jsp:text><![CDATA[<tr class="rowHighlight">]]></jsp:text>
			</c:otherwise></c:choose>
			
				<td>${question.id}</td>
				<td>
					<c:choose>
					<c:when test="${question.user.id == currentUserEntity.id}">
						<input type="submit" value="Edit Question" name="questionButton_${question.id}"/>
						<input type="submit" value="Delete Question" name="questionButton_${question.id}"/>
					</c:when>
					</c:choose>
				</td>
				<td><c:choose><c:when test="${empty question.description}"><a href="/displayQuestion.jsp?questionId=${question.id}">${question.textWithoutHTML}</a></c:when>
						<c:otherwise><a href="/displayQuestion.jsp?questionId=${question.id}">${question.description}</a></c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:forEach var="topic" items="${question.topics}">
						${topic.text}<br/>
					</c:forEach>
				</td>
				<td>${question.questionType.text}</td>
				<td>${question.difficulty.text}</td>
				<td> + / - </td>
			<jsp:text><![CDATA[</tr>]]></jsp:text>
		</c:forEach>
		</c:otherwise>
		</c:choose>
		</tbody>
	</table>
	</form>
