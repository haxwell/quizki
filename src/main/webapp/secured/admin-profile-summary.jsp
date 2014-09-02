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
