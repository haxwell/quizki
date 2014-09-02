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

	<form  action="/secured/AdminProfileAccountServlet" method="post">
		New Password: <input type="password" name="newPassword"/>
		<br/><br/>
		Confirm Password: <input type="password" name="confirmPassword"/>
		<br/><br/>
		
		<input type="submit" value="Change Password" name="button" />
	</form>
	<br/><br/>

</div>
