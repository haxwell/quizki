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

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" xmlns:qfn="http://quizki.com/tld/qfn" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Home Page - Quizki</title>
		
		<jsp:text>
			
			<![CDATA[ <script src="pkgs/jquery/jquery-1.11.1.min.js" type="text/javascript"></script> ]]>
		
			<![CDATA[
				<script type="text/javascript">

					$(document).ready(function(){
						$("#getQuestionsBtn").click(function(){
							//alert("btn pushed!");

							$.post("/ajax/question-getFilteredList.jsp",
							{
								containsFilter: $("#idContainsFilter").val(),
								topicContainsFilter: $("#idTopicContainsFilter").val(),
								questionTypeFilter: $("#idQuestionTypeFilter").val(),
								difficultyFilter: $("#idDifficultyFilter").val(),
								authorFilter: $("#idAuthorFilter").val(),
								rangeOfEntitiesFilter: $("idRangeOfEntitiesFilter").val(),
								maxEntityCountFilter: $("#idMaxQuestionCountFilter").val(),
								offsetFilter: $("#idOffsetFilter").val()
							},
							function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									$('#resultsTextarea').val('');
									$('#resultsTextarea').val(data);									
								}
							});
						});

						$("#getSingleQuestionBtn").click(function(){
							//alert("btn pushed!");
							
							$.post("/ajax/getSingleQuestion.jsp",
							{
								entityIdFilter : $("#idQuestionIdFilter").val()
							},
							function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									$('#resultsTextarea').val('');
									$('#resultsTextarea').val(data);									
								}
							});
						});
					});					

				</script>
			]]>
		</jsp:text>
		
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>	
	
	<br/><br/>
	Question ID: <input id="idQuestionIdFilter" type="text" name="questionIdFilter" value=""/>
	<input id="getSingleQuestionBtn" type="submit" name="button" value="Get Single Question"/>
	<br/><br/>
	Question Contains: <input id="idContainsFilter" type="text" name="containsFilter" value="animal"/>
	Topic Contains: <input type="text" name="topicContainsFilter" id="idTopicContainsFilter" />
	Type text: <input type="text" name="questionTypeFilter" id="idQuestionTypeFilter" value="0" />
	Difficulty text: <input type="text" name="difficultyFilter" id="idDifficultyFilter" value="4" />
	Author text: <input type="text" name="authorFilter" id="idAuthorFilter" />	
	rangeOfEntities: <input id="idRangeOfEntitiesFilter" type="text" name="rangeOfEntitiesFilter" />
	maxQuestionCount: <input id="idMaxQuestionCountFilter" type="text" name="maxQuestionCountFilter" value="10"/>
	offset: <input id="idOffsetFilter" type="text" name="offsetFilter" value="0"/>

	<input id="getQuestionsBtn" type="submit" name="button" value="Get Questions"/>
	
	<br/><br/>
	<textarea id="resultsTextarea" rows="7" cols="90"></textarea>

</body>
</html>
</jsp:root>
