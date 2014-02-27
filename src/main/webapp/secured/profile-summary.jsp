<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div>
	<h1>${sessionScope.currentUserEntity.username}</h1>
	<br/>
	<c:choose>
		<c:when test="${sessionScope.numberOfQuestionsUserContributed == 0}">
			You have not created any questions.
		</c:when>
		<c:otherwise>
			You have created ${sessionScope.numberOfQuestionsUserContributed} questions.
		</c:otherwise>
	</c:choose>

	<br/><br/>
	<c:choose>
		<c:when test="${sessionScope.numberOfExamsUserContributed == 0}">
			You have not created any exams.
		</c:when>
		<c:otherwise>
			You have created ${sessionScope.numberOfExamsUserContributed} exams.
		</c:otherwise>
	</c:choose>
	<br/><br/>
	<!--  c:choose>
		<!--  c:when test="${not empty sessionScope.questionVoteCounts}">
			Your questions have received ${sessionScope.questionVoteCounts.positiveCount} positive votes, and ${sessionScope.questionVoteCounts.negativeCount} negative votes. 
		<!--  c:when>
		<!--  c:otherwise>
			<!--  c:if test="${sessionScope.numberOfQuestionsUserContributed > 0}">
				Your questions have not received any votes.
			<!--  c:if>
		<!--  c:otherwise>
	<!--  c:choose>
	<br/><br/>
	<!--  c:choose>
		<!--  c:when test="${not empty sessionScope.examVoteCounts}">
			Your exams have received ${sessionScope.examVoteCounts.positiveCount} positive votes, and ${sessionScope.examVoteCounts.negativeCount} negative votes. 
		<!--  c:when>
		<!--  c:otherwise>
			<!--  c:if test="${sessionScope.numberOfExamsUserContributed > 0}">
				Your exams have not received any votes.
			<!--  c:if>
		<!--  c:otherwise>
	<!--  c:choose -->
	<br/><br/>
	<c:choose>
		<c:when test="${not empty sessionScope.listOfNotificationsToBeDisplayed}">
					You have ${fn:length(listOfNotificationsToBeDisplayed)} notifications.<br/><br/>
			<div class="divWithBorder1" style="overflow:auto; height:150px; width:100%">
					<!--  TODO: put this in its own file, and include it, once the UI design settles.. -->
				<form action="/secured/ProfileSummaryServlet" method="post">
				<table  style="width:100%">
					<c:set var="rowNum" value="0"/>
					<c:forEach var="notification" items="${sessionScope.listOfNotificationsToBeDisplayed}">
						<c:set var="rowNum" value="${rowNum + 1}" />
						<c:choose><c:when test="${rowNum % 2 == 0}">
						<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
						</c:when>
						<c:otherwise>
						<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
						</c:otherwise></c:choose>
			
						<!-- <td style="width:10%"><input type="checkbox" name="n_chkbox_${notification.id}"/></td>  -->
						<td>${notification.prettyTime_stamp}</td>
						<td>${notification.text}</td>
						
						<jsp:text><![CDATA[</tr>]]></jsp:text>
					</c:forEach>
				</table>
				<br/><br/>
				<input type="submit" value="Clear" name="button"/>
				</form>
			</div>
		</c:when>
		<c:otherwise>
			You do not have any notifications waiting.
		</c:otherwise>
	</c:choose>
	<br/>
</div>
