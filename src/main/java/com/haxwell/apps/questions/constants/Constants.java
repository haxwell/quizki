package com.haxwell.apps.questions.constants;

public class Constants {

	public static final String LOGIN_JSP_URL = "/login.jsp";
	
	public static final String CURRENT_EXAM = "currentExam";
	public static final String CURRENT_QUESTION = "currentQuestion";
	public static final String CURRENT_QUESTION_AS_JSON = "currentQuestionAsJson";
	public static final String CURRENT_QUESTION_KEY = "currentQuestionKey";
	public static final String DISPLAY_QUESTION = "displayQuestion";
//	public static final String CURRENT_EXAM_HISTORY = "currentExamHistory";
	public static final String CURRENT_EXAM_SELECTED_QUESTION_IDS = "exam_selectedQuestionIds";
	
	public static final String IN_EDITING_MODE = "inEditingMode";
	
	public static final String LIST_OF_FIELD_NAMES_OF_CHOICES = "listOfFieldNamesForTheCurrentQuestionsChoices";
	public static final String LIST_OF_VALUES_OF_CHOICES = "listOfCurrentQuestionsChoicesValues";
	public static final String LIST_OF_VALUES_OF_CHOICES_FOR_DISPLAY_QUESTION = "listOfCurrentQuestionsChoicesValuesForDisplayQuestion";
	
	public static final String EXAM_HISTORY_QUESTION_INDEX_LIST = "examHistoryQuestionIndexList";
	public static final String CURRENT_QUESTION_NUMBER = "currentQuestionNumber";
	public static final String TOTAL_POTENTIAL_QUESTIONS = "totalNumberOfQuestionsInCurrentExam";
	public static final String EXAM_IN_PROGRESS = "examInProgress";
	public static final String CURRENT_USER_ENTITY = "currentUserEntity";
	public static final String CURRENT_QUESTIONS_ANSWERS = "currentQuestionsAnswers";
	public static final String LIST_SAYING_WHICH_CHOICES_ARE_CORRECT = "listSayingWhichChoicesAreCorrect";
	public static final String VALIDATION_ERRORS = "validationErrors";
	
	public static final String QUESTION_TOPICS = "fa_listofexamtopics";
	public static final String ALLOW_GENERATED_EXAM_TO_BE_TAKEN = "allowGeneratedExamToBeTaken";
	public static final String ALLOW_GENERATED_EXAM_TO_BE_EDITED = "allowGeneratedExamToBeEdited";
	public static final String SUCCESS_MESSAGES = "successes";
	public static final String TEXT_TO_DISPLAY_FOR_PREV_PAGE = "textToDisplayForPrevPage";
	
	public static final String MRU_FILTER_TEXT = "mruFilterText";
	public static final String MRU_FILTER_DIFFICULTY = "mruFilterDifficulty";
	public static final String MRU_FILTER_MINE_OR_ALL = "mruMineOrAll";	
	public static final String MRU_FILTER_MINE_OR_ALL_OR_SELECTED = "mruFilterMyAllOrSelectedFilter";
	public static final String MRU_FILTER_TOPIC_TEXT = "mruFilterTopicText";
	public static final String MRU_FILTER_QUESTION_TYPE = "mruFilterQuestionType";	
	public static final String MRU_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM = "mruMaxNumberOfQuestionsOnGeneratedExam";
	public static final String MRU_FILTER_PAGINATION_QUANTITY = "mruFilterPaginationQuantity";
	
	public static final String LIST_OF_EXAMS_TO_BE_DISPLAYED = "fa_listofexamstobedisplayed";
	public static final String LIST_OF_QUESTIONS_TO_BE_DISPLAYED = "fa_listofquestionstobedisplayed";
	public static final String LIST_OF_QUESTIONS_TO_BE_DISPLAYED_HAS_BEEN_FILTERED = "listOfQuestionsToBeDisplayedHasBeenFiltered";	
	public static final String LIST_OF_TOPICS_TO_BE_DISPLAYED = "fa_listofalltopics";
	public static final String LIST_OF_MAJOR_TOPICS = "fa_listofmajortopics";
	public static final String LIST_OF_EXAM_TOPICS = "fa_listofexamtopics";
	public static final String LIST_OF_TOPICS_TO_BE_INCLUDED = "fa_listofincludedtopics";
	public static final String LIST_OF_TOPICS_TO_BE_EXCLUDED = "fa_listofexcludedtopics";
	public static final String LIST_OF_NOTIFICATIONS_TO_BE_DISPLAYED = "listOfNotificationsToBeDisplayed";

	public static final String EXAM_GENERATION_IS_IN_PROGRESS = "examGenerationIsInProgress";
	public static final String QUIZKI_PERSISTENCE_UNIT = "quizki-pu";

	public static final String NEXT_SEQUENCE_NUMBER = "nextSequenceNumber";

	public static final String LIST_OF_SEQUENCE_NUMBERS_FOR_CHOICES = "listOfSequenceNumbersForChoices";
//	public static final String LIST_OF_SEQUENCE_NUMBERS_THE_USER_CHOSE = "listOfSequenceNumbersTheUserChose";
	public static final String LIST_OF_PREVIOUSLY_SUPPLIED_ANSWERS = "listOfPreviouslySuppliedAnswers";

	public static final String SHOULD_QUESTIONS_BE_DISPLAYED = "shouldQuestionsBeDisplayed";
	public static final String SHOULD_ALLOW_QUESTION_EDITING = "shouldAllowQuestionEditing";
	public static final String SHOULD_GENERATE_NEW_RANDOM_CHOICE_INDEXES = "shouldGenerateNewRandomChoiceIndexes";
	public static final String SHOULD_LOGIN_LINK_BE_DISPLAYED = "shouldLoginLinkBeDisplayed";
	public static final String ONLY_SELECTED_QUESTIONS_SHOULD_BE_SHOWN = "onlySelectedQuestionsShouldBeShown";
	
	public static final String DO_NOT_INITIALIZE_PROFILE_MRU_SETTINGS = "doNotInitializeProfileMRUSettings";
	
	public static final String QUESTION_ID = "questionId";
	public static final String LIST_OF_RANDOM_CHOICE_INDEXES = "listOfRandomChoiceIndexes";
	public static final String STRING_QUESTION_TYPE_FIELDNAME = "stringAnswer";

//	public static final String LIST_OF_INDEXES_TO_CHOICE_LIST_BY_SEQUENCE_NUMBER = "listOfIndexesToChoiceListBySequenceNumber";

	///
	public static final Integer MY_ITEMS = 1;
	public static final Integer ALL_ITEMS = 0;
	public static final Integer SELECTED_ITEMS = 2;
	public static final String MY_ITEMS_STR = "mine";
	public static final String ALL_ITEMS_STR = "all";
	public static final String SELECTED_ITEMS_STR = "selected";
	////

	public static final String SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS = "mineOrAll";
	public static final String SHOW_ONLY_MY_ITEMS_OR_ALL_ITEMS_OR_SELECTED_ITEMS = "mineOrAllOrSelected";

	public static final int MAX_QUESTION_TEXT_LENGTH = 9999;
	public static final int MAX_CHOICE_TEXT_LENGTH = 999;
	public static final int MAX_QUESTION_DESCRIPTION_LENGTH = 999;

//	public static final String URL_TO_REDIRECT_TO_WHEN_BACK_BUTTON_PRESSED = "urlToGoToWhenUserPressesBackButton";

	public static final String DEFAULT_MAX_NUMBER_OF_QUESTIONS_ON_GENERATED_EXAM = "10";

	public static final String TOTAL_NUMBER_OF_TOPICS = "totalNumberOfTopics";
	public static final String TOTAL_NUMBER_OF_QUESTIONS = "totalNumberOfQuestions";
	public static final String TOTAL_NUMBER_OF_EXAMS = "totalNumberOfExams";
	
	public static final String NUMBER_OF_USER_CONTRIBUTED_QUESTIONS = "numberOfQuestionsUserContributed";
	public static final String NUMBER_OF_USER_CONTRIBUTED_EXAMS = "numberOfExamsUserContributed";

	public static final int PROFILE_SUMMARY_TAB_INDEX = 0;
	public static final int PROFILE_QUESTION_TAB_INDEX = 1;
	public static final int PROFILE_EXAM_TAB_INDEX = 2;
	public static final int PROFILE_ACCOUNT_TAB_INDEX = 3;

	public static final String TAB_INDEX = "tabIndex";

	public static final long NOTIFICATION_ID_EXAM_DELETED = 1;
	public static final long NOTIFICATION_ID_QUESTION_DELETED = 2;
	public static final long NOTIFICATION_ID_FEEDBACK_LEFT_FOR_EXAM = 3;	
	public static final long NOTIFICATION_ID_VOTED_ON_EXAM = 4;
	public static final long NOTIFICATION_ID_VOTED_ON_QUESTION = 5;

	public static final String EXAM_PAGINATION_DATA = "examPaginationData";
	public static final String QUESTION_PAGINATION_DATA = "questionPaginationData";

	public static final int DEFAULT_PAGINATION_PAGE_SIZE = 10;

	public static final String FEEDBACK_FOR_CURRENT_USER_AND_EXAM = "feedbackForCurrentUserAndExam";

	public static final String VOTE_DATA_FOR_LIST_OF_QUESTIONS_TO_BE_DISPLAYED = "voteDataForListOfQuestionsToBeDisplayed";

	public static final String PAGE_TITLE = "pageTitle";

	public static final String APPLICATION_PAGE_TITLES_CONTEXT = "applicationPageTitlesContext";

	public static final String SELECTED_ENTITY_IDS_AS_CSV = "selectedEntityIDs_AsCSV";

	public static final String IN_PRODUCTION_MODE = "ThisShitsLiveMan!";
	
	public static final String ANSWERS_TO_THE_MOST_RECENT_EXAM = "answersToTheMostRecentExam";
}
