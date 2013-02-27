<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	<br/><br/>

      <c:if test="${not empty requestScope.successes}">
      	<c:forEach var="str" items="${requestScope.successes}">
      		<span class="greenText">${str}</span><br/>	
      	</c:forEach>
      	<br/>
      </c:if>
      
      <c:if test="${not empty requestScope.validationErrors}">
      	<c:forEach var="str" items="${requestScope.validationErrors}">
      		<span class="redText">${str}</span><br/>
      	</c:forEach>
      	<br/>      	
      </c:if>

	<form  action="/secured/ProfileAccountServlet" method="post">
		New Password: <input type="password" name="newPassword"/>
		<br/><br/>
		Confirm Password: <input type="password" name="confirmPassword"/>
		<br/><br/>
		
		<input type="submit" value="Change Password" name="button" />
	</form>
	<br/><br/>

</div>
