<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="header" class="header">
<table style="width:100%">
<tr>
<td style="width:33%">
<a class="homeLink" href="/quizki/index.jsp">Quizki</a>
</td>
<td style="text-align:center; width:34%">
<a class="whatIsThis" href="/quizki/about.jsp">What is this?</a>
</td>
<td style="text-align:right; width:33%;">
    <c:choose>
     <c:when test="${empty sessionScope.currentUserEntity}">
	<a class="homeLink" href="login.jsp">register / login</a>
     </c:when>
     <c:otherwise>
     	Hello, ${sessionScope.currentUserEntity.username} <a class="darkerHomeLink" href="logout.jsp">logout</a><br/>
     </c:otherwise>
    </c:choose>
</td>
</tr>
</table>
</div>

