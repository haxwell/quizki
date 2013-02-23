<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<table class="displayExam">
		<thead>
		<tr>
			<th>ID</th>
			<th></th>
			<th>Exam Name</th>
			<th></th>
			<th></th>
		</tr>
		</thead>
		<tbody>
		<c:set var="rowNum" value="0"/>
		<c:forEach var="exam" items="${fa_listofexamstobedisplayed}">
			<c:set var="rowNum" value="${rowNum + 1}" />
			<c:choose><c:when test="${rowNum % 2 == 0}">
			<jsp:text><![CDATA[<tr>]]></jsp:text>
			</c:when>
			<c:otherwise>
			<jsp:text><![CDATA[<tr class="rowHighlight">]]></jsp:text>
			</c:otherwise></c:choose>
			
				<td>${exam.id}</td>
				<td></td>
				<td>${exam.title}</td>
					<td><input type="submit" value="Take Exam" name="examButton_${exam.id}"/></td>
					<c:choose><c:when test="${exam.user.id == currentUserEntity.id}">
						<td><input type="submit" value="Edit Exam" name="examButton_${exam.id}"/></td>
						<td><input type="submit" value="Delete Exam" name="examButton_${exam.id}"/></td>
					</c:when>
					<c:otherwise>
						<td><input type="submit" value="Detail Exam" name="examButton_${exam.id}"/></td>
					</c:otherwise>
					</c:choose>

			<jsp:text><![CDATA[</tr>]]></jsp:text>
		</c:forEach>
		</tbody>
	</table>
