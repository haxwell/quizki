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
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn"%>

<link href="/css/header.css" rel="stylesheet" type="text/css" />

<div id="header" class="row header-row">
	<div class="span3"><a id="quizkiHomePageLink" href="/index.jsp"><img class="quizki_logo" src="/images/logo-light.png"></a></div>
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