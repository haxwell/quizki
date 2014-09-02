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

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>About - Quizki</title>
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

<br/><br/><br/>

<b>Simply described--</b>
<br/><br/>
Quizki is an application that allows you to test yourself. 
<br/><br/>
Consider the following scenario: You have just become interested in a topic you've never studied before. You read articles online, you read every book you can find, and you even look into what you'd need to know to get a certification. In other words, you're serious about learning this. Now, of course, as you're reading, you understand the new information presented to you. But, how can you be sure a week later, when you go to take the test, that you still comprehend (and recall!) what you've read?
<br/><br/>
Enter Quizki.
<br/><br/>
Quizki is based around the concepts of <b>questions</b>, <b>answers</b>, and <b>exams</b>. You can enter questions and answers that describe the idea you are trying to remember. The point being, that, later, if you can answer the question correctly (or even better, a bunch of similar questions), you can be sure that you understand the concept at hand. The questions you enter can be grouped together and presented to you in the form of an exam. You can complete that exam whenever you are ready. Then, Quizki will grade it, and tell you which questions you have <div class="selectedAndCorrect" style="display:inline">answered correctly</div>, and which <div class="correctButNotSelected" style="display:inline">you missed</div>. This way, you'll know which parts of those books you need to focus on.
<br/><br/><br/><br/>
<div style="text-align:center"><a href="index.jsp">home</a></div>

<b>A Quick List of Contents</b><br/><br/>

<ol>
<li> <a href="#organization">How Quizki is organized</a></li>
<li> <a href="#questions">Questions</a></li>
<li> <a href="#exams">Exams</a></li>
<ol><li><a href="#takingAnExam">Taking an Exam</a></li></ol>
<!--  <li> <a href="#browsing">Browsing / Listing Questions &amp; Exams</a></li>  -->
<li> <a href="#contactInfo">Contact info</a></li>
</ol>
<br/><br/>
<div style="text-align:center"><a href="index.jsp">home</a></div>

<b id="organization">How Quizki is organized--</b>
<br/><br/>
The most important page in Quizki is the home page. You can get to it at anytime by clicking on the Quizki link in the upper left corner of any page. You can cancel anything you are doing in Quizki, by going to the home page.
<br/><br/>

Primarily, there a two types of actions you can do in Quizki...
<ul><li>You can take an Exam from Quizki.</li>
<li>You can add a Question or Exam to Quizki.</li>
</ul>
<br/><br/><br/>

<div style="text-align:center"><a href="javascript:history.go(-1)">back</a></div>
<b id="questions">Lets talk about Questions first.</b>
<br/><br/>	
From the home page, click on the <b>Question</b> link.
<br/><br/>	
You will be brought to the Create Question page. From this page, you can enter the information necessary to give Quizki a very useful question.
<br/><br/>	
Questions are made up of<br/>
<ul>
	<li><b>Text</b> - This is what Quizki will present to you to jog your memory during an exam. It can be text, complete with formatting like <b>bold</b>, <i>italics</i>, or <u>underlines</u>. You could even include a picture! 
	</li>
	<li><b>Description</b> - An optional description of the question. This is especially useful if your question has only pictures, and no text. In this case, the description will allow a user to search for (and find) this question.	
	</li>	
	<li><b>Difficulty</b> -	You can set an indication of how hard this question is. When generating exams later on, you will be able to filter out easy questions, or include particularly hard questions, based on this setting.
	</li>			
	<li><b>Type</b> - This refers to the format that the answers will come in. Your choices are:
		<ul>			
			<li><i>Single</i> - Given a list of possible answers, a SINGLE answer is correct.</li>
			<li><i>Multiple</i> - Given a list of possible answers, MULTIPLE answers must be selected in order to answer the question correctly.</li>
			<li><i>Phrase</i> - The question must be answered by typing a short phrase, for instance 'Denver' or 'The War of the Roses'.</li>
			<li><i>Sequence</i> - Given a list of possible answers, the user must indicate the correct SEQUENCE they should be in, in order to answer the question correctly.</li>
			<li><i>Set</i> - A list of items, one of which will be randomly blanked out when Quizki presents the question to you. You type the missing phrase.</li> 
		</ul>
	</li>
	<li><b>Answers</b> - These are the potential answers to the question.
	</li>
	<li><b>Topics</b> - These are short, generally one word, descriptions of what this question is about. Think of Topics as a way of grouping questions together.
	</li>	
	<li><b>References</b> - These are citations, etc. which a person could use to find confirmation that the answers to this question are correct.
	</li>
</ul>	
<br/><br/><br/>

<div style="text-align:center"><a href="javascript:history.go(-1)">back</a></div>
<b id="exams">Now lets talk about Exams..</b>
<br/><br/>
Once you have entered a few questions, you may want to arrange them in an exam.<br/><br/>

From the home page, click on the 'Exam' link, to the right of the 'Question' link mentioned above.<br/>
<br/>
You will be brought to the Create Exam page. From this page, you select from the questions that have been entered in Quizki, building up a list of questions to group together in a specific exam.<br/><br/>	


Each exam has a...<br/>
<ul>
	<li><b>Title</b> - some text to differentiate this exam from the others.</li>		
	<li><b>List of questions</b> - a listing of each of the questions that will be presented as part of this exam.</li>
</ul>	

By default, you can choose from all of the questions that you have entered. If you would like, you can choose from all of the questions that everybody else has entered, too! To change which questions you are able to choose from, select 'Mine' or 'All' in the first dropdown on the left. <br/><br/>	

Regardless whether you are showing only your questions, or 'all' questions, you can filter the list further in three ways:<br/>	

	<ul>
	<li><b>Search in Questions ...</b> - Quizki will only show questions who's text or description contains the given string. So, if you enter 'bears' here, only questions with the word 'bears' in the text or the description will appear in the list.</li>	
	<li><b>Search in Topics] ...</b> - Quizki will only show questions which have a topic that contains the string you entered here. For instance, if the only topics in Quizki are A, B, and C, if you enter B here, questions which belong to topic A and topic C will not appear, only questions who belong to Topic B.</li>
	<li><b>Include Question Type ...</b> - Quizki will only include questions that have the type that you select here. For instance, if you choose 'Set' no questions which have a type of 'Single', 'Multiple', 'Sequence' or 'Phrase' will appear.</li>
	<li><b>Include Difficulties ...</b> - Quizki will only include questions that have the difficulty value that you select here.</li>
	</ul>

This is the most detailed way of creating an exam. Once you select the questions you want, and hit Add Exam, the exam will be saved, and you can have Quizki present it to you at any time.<br/><br/>	

If you don't want to go through the detail of choosing each question that will appear on your exam, you can, instead, click <b>Generate Exam</b> from the home page, and create a list of topics. Quizki will then randomly choose questions that belong to the topics you chose. You can take the exam Quizki creates right away, based on those questions.<br/><br/>	

Finally, the fastest, but least customized way, of taking an exam, is to choose from the list of popular topics on the home page. Quizki will generate an exam right away based on that topic, and you can test yourself on it immediately.<br/><br/>	

<br/><br/>
<div style="text-align:center"><a href="javascript:history.go(-1)">back</a></div>

<b id="takingAnExam">Taking an exam</b><br/><br/>	

Before an exam starts, you will be presented with a screen which tells you <br/>
	<ul>
	<li>that you are about to begin taking an exam,</li> 
	<li>which topics the questions will cover</li>
	<li>and how many questions there are in the exam</li>
	</ul>	
<br/><br/>
You press BEGIN to start taking the exam.<br/><br/>	

Once you are in the actual exam, you are presented with a question to answer. This question can be text, or pictures, or both. How you answer the question will depend on the type of question it is.<br/>	

	<ul>
	<li>If you have boxes with red X's in them, these are Multiple choice questions. Clicking on an X changes it to a green check mark. To indicate that you've selected a choice to be the correct answer, click it, and change it to a green check mark.</li>
	<li>If you have a single text box, this is a PHRASE question. You must enter some text (perhaps a word or three) which correctly answers the question.</li>
	<li>If you have a list of choices, one of which has a single text box, this is a SET question. You must enter some text (perhaps a word or three) which correctly describes the missing element in the set.</li>
	<li>If each answer has a small text box to the left of it, this is a SEQUENCE question. You must put a number in the text box indicating its position in the sequence of answers. For instance, you would put a 1 by the answer which should come first, a 2 by the second answer, etc.</li>
	</ul>
<br/>
Once you have answered a question, hit NEXT to go to the next question. If you would like to go back to a previous question, you can hit the PREV button to move backwards. You can change your answers, and your changes will be remembered.<br/><br/>	

Once you have answered all of the questions, you will be presented with a final chance to go back and review your answers. If you are satisfied that everything has been answered correctly, you can click GRADE IT to find out how you did.<br/><br/>	

Quizki will tell you how many questions you answered correctly, and how many you answered incorrectly. Questions with a GREEN circle to the left of them are ones you answered correctly. If the question has a RED circle, you missed this question. You can click on the question to get more information on why a choice was marked correct or incorrect.<br/>

<br/><br/><br/>
<div style="text-align:center"><a href="javascript:history.go(-1)">back</a></div>
<br/>
<b id="contactInfo">Contact us!</b><br/><br/>	
Quizki is being actively developed, and changes are being made almost ever day. In addition, plans are in the works to make the actual Quizki code available freely to the world, soon.<br/><br/> If you would like to get in touch with questions, comments, suggestions or flames, please email me at HAXWE33LL at GMA33IL dot COM (take out the 33's).

<br/><br/><br/><br/>
<div style="text-align:center"><a href="javascript:history.go(-1)">back</a></div>
<br/><br/>

</body>
</html>
</jsp:root>
