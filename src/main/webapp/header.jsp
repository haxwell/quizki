
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

<div id="header" class="row header">
	<div class="span3 logo"></div>
	<div class="span6 form-heading">
		<h1>Create/Edit Exam</h1>
	</div>
	<div class="user span3">
		<c:choose>
			<c:when test="${empty sessionScope.currentUserEntity}">
				<c:if test="${empty sessionScope.shouldLoginLinkBeDisplayed}">
					<a class="homeLink" id="headerLoginLink" href="/login.jsp">register
						/ login</a>
				</c:if>
			</c:when>
			<c:otherwise>
	     	${sessionScope.currentUserEntity.username}
	     	<c:if
					test="${(empty sessionScope.shouldLoginLinkBeDisplayed) && (qfn:isAdministrator(sessionScope.currentUserEntity))}">
					<!--a class="darkerHomeLink" id="headerAdminProfileLink"
						href="/secured/admin-profile.jsp">admin page</a-->
				</c:if>
				<c:if test="${empty sessionScope.shouldLoginLinkBeDisplayed}">
					<!--a class="darkerHomeLink" id="headerProfileLink"
						href="/secured/profile.jsp">view profile</a>
					<a class="darkerHomeLink" id="logoutLink" href="/logout.jsp">logout</a-->
				</c:if>
			</c:otherwise>
		</c:choose>

	</div>
</div>