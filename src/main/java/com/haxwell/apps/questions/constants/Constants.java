package com.haxwell.apps.questions.constants;

public class Constants {

	public static final String LOGIN_JSP_URL = "/login.jsp";
	
	public static final String CURRENT_EXAM = "currentExam";
	public static final String CURRENT_QUESTION = "currentQuestion";
	public static final String CURRENT_QUESTION_KEY = "currentQuestionKey";
	public static final String DISPLAY_QUESTION = "displayQuestion";
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
	public static final String CURRENT_EXAM_HAS_BEEN_PERSISTED = "currentExamHasBeenPersisted";
	
	public static final String QUESTION_TOPICS = "fa_listofexamtopics";
	public static final String ALLOW_GENERATED_EXAM_TO_BE_TAKEN = "allowGeneratedExamToBeTaken";
	public static final String ALLOW_GENERATED_EXAM_TO_BE_EDITED = "allowGeneratedExamToBeEdited";
	public static final String SUCCESS_MESSAGES = "successes";
	public static final String TEXT_TO_DISPLAY_FOR_PREV_PAGE = "textToDisplayForPrevPage";
	
	public static final String MRU_FILTER_TEXT = "mruFilterText";
	public static final String MRU_FILTER_DIFFICULTY = "mruFilterDifficulty";
	public static final String MRU_FILTER_MINE_OR_ALL = "mruMineOrAll";	
	public static final String MRU_FILTER_TOPIC_TEXT = "mruFilterTopicText";
	public static final String MRU_FILTER_QUESTION_TYPE = "mruFilterQuestionType";	
	public static final String MRU_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM = "mruMaxNumberOfQuestionsOnGeneratedExam";	
	
	public static final String LIST_OF_EXAMS_TO_BE_DISPLAYED = "fa_listofexamstobedisplayed";
	public static final String LIST_OF_QUESTIONS_TO_BE_DISPLAYED = "fa_listoquestionstobedisplayed";	
	public static final String LIST_OF_TOPICS_TO_BE_DISPLAYED = "fa_listofalltopics";
	public static final String LIST_OF_MAJOR_TOPICS = "fa_listofmajortopics";
	public static final String LIST_OF_EXAM_TOPICS = "fa_listofexamtopics";
	public static final String LIST_OF_TOPICS_TO_BE_INCLUDED = "fa_listofincludedtopics";
	public static final String LIST_OF_TOPICS_TO_BE_EXCLUDED = "fa_listofexcludedtopics";

	public static final String EXAM_GENERATION_IS_IN_PROGRESS = "examGenerationIsInProgress";
	public static final String QUIZKI_PERSISTENCE_UNIT = "quizki-pu";

	public static final String NEXT_SEQUENCE_NUMBER = "nextSequenceNumber";

	public static final String LIST_OF_SEQUENCE_NUMBERS_FOR_CHOICES = "listOfSequenceNumbersForChoices";
	public static final String LIST_OF_SEQUENCE_NUMBERS_THE_USER_CHOSE = "listOfSequenceNumbersTheUserChose";
	public static final String LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS = "listOfPreviouslySuppliedAnswers";

	public static final String SHOULD_QUESTIONS_BE_DISPLAYED = "shouldQuestionsBeDisplayed";
	public static final String SHOULD_ALL_QUESTIONS_BE_DISPLAYED = "shouldAllQuestionsBeDisplayed";	
	public static final String SHOULD_ALL_EXAMS_BE_DISPLAYED = "shouldAllExamsBeDisplayed";
	public static final String SHOULD_ALLOW_QUESTION_EDITING = "shouldAllowQuestionEditing";
	public static final String SHOULD_GENERATE_NEW_RANDOM_CHOICE_INDEXES = "shouldGenerateNewRandomChoiceIndexes";
	public static final String SHOULD_LOGIN_LINK_BE_DISPLAYED = "shouldLoginLinkBeDisplayed";
	
	public static final String QUESTION_ID = "questionId";
	public static final String LIST_OF_RANDOM_CHOICE_INDEXES = "listOfRandomChoiceIndexes";
	public static final String STRING_QUESTION_TYPE_FIELDNAME = "stringAnswer";

	public static final String LIST_OF_INDEXES_TO_CHOICE_LIST_BY_SEQUENCE_NUMBER = "listOfIndexesToChoiceListBySequenceNumber";

	///
	public static final Integer MY_ITEMS = 1;
	public static final Integer ALL_ITEMS = 2;
	public static final String MY_ITEMS_STR = "mine";
	public static final String ALL_ITEMS_STR = "all";
	////

	public static final String SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS = "mineOrAll";

	public static final int MAX_QUESTION_TEXT_LENGTH = 9999;
	public static final int MAX_CHOICE_TEXT_LENGTH = 999;
	public static final int MAX_QUESTION_DESCRIPTION_LENGTH = 999;

	public static final String URL_TO_REDIRECT_TO_WHEN_BACK_BUTTON_PRESSED = "urlToGoToWhenUserPressesBackButton";

	public static final String DEFAULT_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM = "10";

	public static final String TOTAL_NUMBER_OF_TOPICS = "totalNumberOfTopics";
	
	public static final String NUMBER_OF_USER_CONTRIBUTED_QUESTIONS = "numberOfQuestionsUserContributed";
	public static final String NUMBER_OF_USER_CONTRIBUTED_EXAMS = "numberOfExamsUserContributed";

	public static final int PROFILE_SUMAMRY_TAB_INDEX = 0;
	public static final int PROFILE_QUESTION_TAB_INDEX = 1;
	public static final int PROFILE_EXAM_TAB_INDEX = 2;
	public static final int PROFILE_ACCOUNT_TAB_INDEX = 3;

	public static final String TAB_INDEX = "tabIndex";

	
}
