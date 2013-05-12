<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<form id="profileExamForm" action="/secured/ProfileExamsServlet">
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
			<tr>
				<td></td>
				<td style="text-align:right"><input type="submit" name="button" value="Apply Filter -->"/> </td>
				<td><input type="text" name="containsFilter" value="${mruFilterText}" title="Only show exams with titles that contain this text..." style="width:100%;"/></td>
			</tr>
		<c:set var="rowNum" value="0"/>
		<c:set var="counter" value="0"/>
		<c:choose>
			<c:when test="${empty fa_listofexamstobedisplayed}">
				<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
				<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
				<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
				<jsp:text><![CDATA[<tr class="" style="width:100%"></tr>]]></jsp:text>
				<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
				<jsp:text><![CDATA[<td></td><td colspan="6">There are no exams to display! Either adjust the filter above, or add some exams of your own!]]></jsp:text>
				<jsp:text><![CDATA[</tr>]]></jsp:text>
			</c:when>
			<c:otherwise>
		<c:forEach var="exam" items="${fa_listofexamstobedisplayed}">
			<c:set var="rowNum" value="${rowNum + 1}" />
			<c:choose><c:when test="${rowNum % 2 == 0}">
			<jsp:text><![CDATA[<tr>]]></jsp:text>
			</c:when>
			<c:otherwise>
			<jsp:text><![CDATA[<tr class="rowHighlight">]]></jsp:text>
			</c:otherwise></c:choose>
			
				<c:set var="counter" value="${counter + 1}" />
				<td>${exam.id}</td>
					<c:choose><c:when test="${exam.user.id == currentUserEntity.id}">
						<td>
						<div class="examButtonDiv">
							<input type="submit" value="Edit Exam" id="exam_edit_button_${counter}" name="examButton_${exam.id}"/>
							<input type="submit" value="Delete Exam" id="exam_delete_button_${counter}" name="examButton_${exam.id}"/>
						</div>
						</td>
					</c:when>
					<c:otherwise>
						<td><input type="submit" value="Detail Exam" id="exam_detail_button_${counter}" name="examButton_${exam.id}"/></td>
					</c:otherwise>
					</c:choose>
				<td style="width:100%;">${exam.title}</td>
					<td><input type="submit" value="Take Exam" id="exam_take_button_${counter}" name="examButton_${exam.id}"/></td>

			<jsp:text><![CDATA[</tr>]]></jsp:text>
		</c:forEach>
		</c:otherwise>
		</c:choose>
		</tbody>
	</table>

	<input type="hidden" id="exam_valueOfLastPressedButton" name="exam_valueOfLastPressedButton">
	<input type="hidden" id="exam_nameOfLastPressedButton" name="exam_nameOfLastPressedButton">
	</form>
