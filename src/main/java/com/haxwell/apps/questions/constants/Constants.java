package com.haxwell.apps.questions.constants;

public class Constants {

	public static final String LOGIN_JSP_URL = "/login.jsp";
	
	public static final String CURRENT_EXAM = "currentExam";
	public static final String CURRENT_QUESTION = "currentQuestion";
	public static final String CURRENT_EXAM_HISTORY = "currentExamHistory";
	
	public static final String IN_EDITING_MODE = "inEditingMode";
	
	public static final String LIST_OF_FIELD_NAMES_OF_CHOICES = "listOfFieldNamesForTheCurrentQuestionsChoices";
	public static final String LIST_OF_VALUES_OF_CHOICES = "listOfCurrentQuestionsChoicesValues";
	public static final String LIST_OF_VALUES_OF_CHOICES_FOR_DISPLAY_QUESTION = "listOfCurrentQuestionsChoicesValuesForDisplayQuestion";
	
	public static final String CURRENT_QUESTION_NUMBER = "currentQuestionNumber";
	public static final String TOTAL_POTENTIAL_QUESTIONS = "totalNumberOfQuestionsInCurrentExam";
	public static final String EXAM_IN_PROGRESS = "examInProgress";
	public static final String CURRENT_USER_ENTITY = "currentUserEntity";
	public static final String CURRENT_QUESTIONS_ANSWERS = "currentQuestionsAnswers";
	public static final String LIST_SAYING_WHICH_CHOICES_ARE_CORRECT = "listSayingWhichChoicesAreCorrect";
	public static final String VALIDATION_ERRORS = "validationErrors";
	
	// TODO: can the following two be merged? Are we ever in a situation where we're editing one, but the other has not been persisted?
	public static final String CURRENT_QUESTION_HAS_BEEN_PERSISTED = "currentQuestionHasBeenPersisted";
	public static final String CURRENT_EXAM_HAS_BEEN_PERSISTED = "currentQuestionHasBeenPersisted";
	
	public static final String QUESTION_TOPICS = "fa_listofexamtopics";
	public static final String ALLOW_GENERATED_EXAM_TO_BE_TAKEN = "allowGeneratedExamToBeTaken";
	public static final String SUCCESS_MESSAGES = "successes";
	public static final String TEXT_TO_DISPLAY_FOR_PREV_PAGE = "textToDisplayForPrevPage";
	public static final String MRU_FILTER_TEXT = "mruFilterText";
	public static final String MRU_FILTER_DIFFICULTY = "mruFilterDifficulty";
	public static final String LIST_OF_QUESTIONS_TO_BE_DISPLAYED = "fa_listoquestionstobedisplayed";
	public static final String MRU_FILTER_TOPIC_TEXT = "mruFilterTopicText";
	public static final String LIST_OF_TOPICS_TO_BE_DISPLAYED = "fa_listofalltopics";
	public static final String LIST_OF_MAJOR_TOPICS = "fa_listofmajortopics";
	public static final String LIST_OF_TOPICS_TO_BE_INCLUDED = "fa_listofincludedtopics";
	public static final String LIST_OF_TOPICS_TO_BE_EXCLUDED = "fa_listofexcludedtopics";
	public static final String EXAM_GENERATION_IS_IN_PROGRESS = "examGenerationIsInProgress";
	public static final String QUIZKI_PERSISTENCE_UNIT = "quizki-pu";

	public static final String NEXT_SEQUENCE_NUMBER = "nextSequenceNumber";

	public static final String LIST_OF_SEQUENCE_NUMBERS_FOR_CHOICES = "listOfSequenceNumbersForChoices";

	public static final String LIST_OF_SEQUENCE_NUMBERS_THE_USER_CHOSE = "listOfSequenceNumbersTheUserChose";

	public static final String SHOULD_QUESTIONS_BE_DISPLAYED = "shouldQuestionsBeDisplayed";
	public static final String SHOULD_ALLOW_QUESTION_EDITING = "shouldAllowQuestionEditing";
}
