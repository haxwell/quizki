<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
	<c:choose>
		<c:when test="${not empty sessionScope.questionVoteCounts}">
			Your questions have received ${sessionScope.questionVoteCounts.positiveCount} positive votes, and ${sessionScope.questionVoteCounts.negativeCount} negative votes. 
		</c:when>
		<c:otherwise>
			<c:if test="${sessionScope.numberOfQuestionsUserContributed > 0}">
				Your questions have not received any votes.
			</c:if>
		</c:otherwise>
	</c:choose>
	<br/><br/>
	<c:choose>
		<c:when test="${not empty sessionScope.examVoteCounts}">
			Your exams have received ${sessionScope.examVoteCounts.positiveCount} positive votes, and ${sessionScope.examVoteCounts.negativeCount} negative votes. 
		</c:when>
		<c:otherwise>
			<c:if test="${sessionScope.numberOfQuestionsUserContributed > 0}">
				Your exams have not received any votes.
			</c:if>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${not empty sessionScope.userNotifications}">
			You do not have any notifications waiting. 
		</c:when>
	</c:choose>
	<br/><br/>
</div>
