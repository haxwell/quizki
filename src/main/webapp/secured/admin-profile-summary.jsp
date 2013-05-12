<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div>
	<h2>Admin Page</h2>
	<br/>
	<h1>${sessionScope.currentUserEntity.username}</h1>
	<br/>
	<c:choose>
		<c:when test="${sessionScope.totalNumberOfQuestions == 0}">
			No questions have yet been created in Quizki.
		</c:when>
		<c:otherwise>
			Quizki has ${sessionScope.totalNumberOfQuestions} questions.
		</c:otherwise>
	</c:choose>

	<br/><br/>
	<c:choose>
		<c:when test="${sessionScope.totalNumberOfExams == 0}">
			No exams have yet been created in Quizki.
		</c:when>
		<c:otherwise>
			Quizki has ${sessionScope.totalNumberOfExams} exams.
		</c:otherwise>
	</c:choose>
	<br/><br/>
</div>
