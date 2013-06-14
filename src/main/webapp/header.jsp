
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://quizki.com/tld/qfn" prefix="qfn" %>

<div id="header" class="header">
<table style="width:100%">
<tr>
<td style="width:33%">
<a class="homeLink" href="/index.jsp">Quizki</a>
</td>
<td style="text-align:center; width:34%">
<a class="whatIsThis" href="/about.jsp">What is this?</a>
</td>
<td style="text-align:right; width:33%;">
	    <c:choose>
	     <c:when test="${empty sessionScope.currentUserEntity}">
			<c:if test="${empty sessionScope.shouldLoginLinkBeDisplayed}"> 
				<a class="homeLink" id="headerLoginLink" href="/login.jsp">register / login</a>
			</c:if>
	     </c:when>
	     <c:otherwise>
	     	Hello, ${sessionScope.currentUserEntity.username} <c:if test="${(empty sessionScope.shouldLoginLinkBeDisplayed) && (qfn:isAdministrator(sessionScope.currentUserEntity))}"><a class="darkerHomeLink" id="headerAdminProfileLink" href="/secured/admin-profile.jsp">admin page</a></c:if> <c:if test="${empty sessionScope.shouldLoginLinkBeDisplayed}"><a class="darkerHomeLink" id="headerProfileLink" href="/secured/profile.jsp">view profile</a>  <a class="darkerHomeLink" id="logoutLink" href="/logout.jsp">logout</a></c:if>
	     </c:otherwise>
	    </c:choose>
</td>
</tr>
</table>
</div>

