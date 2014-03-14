
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

<link href="/css/header.css" rel="stylesheet" type="text/css" />

<div id="header" class="row header-row">
	<div class="span3"><a href="/index.jsp"><img class="quizki_logo" src="/images/logo-light.png"></a></div>
	<div class="span6 page-description">
		<h1>${sessionScope.pageTitle}</h1>
	</div>
	<div class="headerUsernameInfo span3">
		<c:choose>
			<c:when test="${empty sessionScope.currentUserEntity}">
				<c:if test="${empty sessionScope.shouldLoginLinkBeDisplayed}">
					<a class="homeLink" id="headerLoginLink" href="/login.jsp">register / login</a>
				</c:if>
			</c:when>
			<c:otherwise>
	     	${sessionScope.currentUserEntity.username}
	     	<c:if test="${(empty sessionScope.shouldLoginLinkBeDisplayed) && (qfn:isAdministrator(sessionScope.currentUserEntity))}">
					<a class="darkerHomeLink" id="headerAdminProfileLink" href="/secured/admin-profile.jsp">admin page</a>
				</c:if>
				<c:if test="${empty sessionScope.shouldLoginLinkBeDisplayed}">
					<a class="darkerHomeLink" id="headerProfileLink" href="/secured/profile.jsp">view profile</a>
					<a class="darkerHomeLink" id="headerLogoutLink" href="/logout.jsp">logout</a>
				</c:if>
			</c:otherwise>
		</c:choose>
	</div>
</div>
