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
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
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


<b>How Quizki is organized--</b>
<br/><br/>
The most important page in Quizki is the home page. You can get to it at anytime by clicking on the Quizki link in the upper left corner of any page. You can cancel anything you are doing in Quizki, by going to the home page.
<br/><br/>

The home page is centered around three general groups of actions that you can do in Quizki. 
<br/>
<ul><li>You can take from Quizki. These actions relate to exams.</li>
<li>You can give to Quizki. These actions relate to questions.</li>
<li>You can browse Quizki, looking through the full lists of either the questions or exams stored in Quizki.</li>
</ul>
<br/><br/><br/>

<div style="text-align:center"><a href="index.jsp">home</a></div>
<b>Lets talk about Questions first.</b>
<br/><br/>	
From the home page, click on the 'a question of your own' link.
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
			<li><i>Multiple</i> - Given a list of posisble answers, MULTIPLE answers must be selected in order to answer the question correctly.</li>
			<li><i>String</i> - The question must be answered by typing a short STRING of characters, for instance 'Denver' or 'The War of the Roses'.</li>
			<li><i>Sequence</i> - Given a list of possible answers, the user must indicate the correct SEQUENCE they should be in, in order to answer the question correctly.</li>
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

<div style="text-align:center"><a href="index.jsp">home</a></div>
<b>Now lets talk about Exams..</b>
<br/><br/>
Once you have entered a few questions, you may want to arrange them in an exam.<br/><br/>

From the home page, click on the link to 'Create your own unique exam.'<br/>
<br/>
You will be brought to the Create Exam page. From this page, you select from the questions that have been entered in Quizki, building up a list of questions to group together in a specific exam.<br/><br/>	


Each exam has a...<br/>
<ul>
	<li><b>Title</b> - some text to differentiate this exam from the others.</li>		
	<li><b>List of questions</b> - a listing of each of the questions that will be presented as part of this exam.</li>
</ul>	

By default, you can choose from all of the questions that you have entered. If you would like, you can choose from all of the questions that everybody else has entered, too! To change which questions you are able to choose from, select 'my' or 'all' in the Show .. Questions option on the right. <br/><br/>	

Regardless whether you are showing only your questions, or 'all' questions, you can filter the list further in three ways:<br/>	

	<ul>
	<li><b>Topic contains ...</b> - Quizki will only show questions which have a topic that contains the string you entered here. For instance, if the only topics in Quizki are A, B, and C, if you enter B here, questions which belong to topic A and topic C will not appear, only questions who belong to Topic B.</li>	
	<li><b>Question contains ...</b> - Quizki will only show questions who's text or description contains the given string. So, if you enter 'bears' here, only questions with the word 'bears' in the text or the description will appear in the list.</li>	
	<li><b>Include difficulties up to ...</b> - Quizki will only include questions that are LESS difficult than the value you select here. For instance, if you choose 'Intermediate' no questions which have a difficulty of 'Well Versed' or 'Guru' will appear.</li>
	</ul>

This is the most detailed way of creating an exam. Once you select the questions you want, and hit Add Exam, the exam will be saved, and you can have Quizki present it to you at any time.<br/><br/>	

If you don't want to go through the detail of choosing each question that will appear on your exam, you can, instead, select from a list of topics. Quizki will randomly choose questions that belong to the topics you chose. You can then take an exam right away, based on those questions.<br/><br/>	

Finally, the fastest, but least customized way, of taking an exam, is to choose from the list of popular topics on the home page. Quizki will generate an exam right away based on that topic, and you can test yourself on it immediately.<br/><br/>	

<br/><br/>
<div style="text-align:center"><a href="index.jsp">home</a></div>	

<b>Taking an exam</b><br/><br/>	

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
	<li>If you have radio buttons to select from, this is a SINGLE question. You must choose the single correct answer.</li>
	<li>If you have check boxes, this is a MULTIPLE question. There is more than one correct answer, and you must choose them all to answer the question correctly.</li>
	<li>If you have a single text box, this is a STRING question. You must enter a short string of characters (perhaps a word or three) which correctly answers the question.</li>
	<li>If each answer has a small text box to the left of it, this is a SEQUENCE question. You must put a number in the text box indicating its position in the sequence of answers. For instance, you would put a 1 by the answer which should come first, a 2 by the second answer, etc.</li>
	</ul>
<br/>
Once you have answered a question, hit NEXT to go to the next question. If you would like to go back to a previous question, you can hit the PREV button to move backwards. You can change your answers, and your changes will be remembered.<br/><br/>	

Once you have answered all of the questions, you will be presented with a final chance to go back and review your answers. If you are satisfied that everything has been answered correctly, you can click GRADE IT to find out how you did.<br/><br/>	

Quizki will tell you how many questions you answered correctly, and how many you answered incorrectly. Questions in GREEN are ones you answered correctly. If the question is listed in RED, you missed this question. You can click on the question to view everything about it. The answers section is colored to describe how you answered the question.<br/>

	<ul>
	<li>If the text is <div class="selectedButNotCorrect" style="display:inline">red</div>, this is an answer that you selected INCORRECTLY.</li>
	<li>If the text is <div class="correctButNotSelected" style="display:inline">red and bold</div>, this is an answer that you did NOT select, but that you should have.</li>
	<li>If the text is <div class="selectedAndCorrect" style="display:inline">green</div>, you selected this answer correctly.</li>
	</ul>

<br/><br/><br/>
<div style="text-align:center"><a href="index.jsp">home</a></div>
<br/>
Finally, back at the home page, the last area to explore in Quizki is the list of questions and exams. This will allow you to peruse all of the questions and exams that have been entered in Quizki. In both cases, you can choose to list only the things you have created, or to list everything.<br/><br/>	 

You can filter the list of exams by title. <br/><br/>	

The list of questions, you can filter by topic, question, and difficulty.<br/><br/>	 

<br/><br/><br/><br/>
<div style="text-align:center"><a href="javascript:history.go(-1)">back</a></div>
<br/><br/>

</body>
</html>
</jsp:root>
