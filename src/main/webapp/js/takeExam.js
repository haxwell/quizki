var TakeExamChoiceItemFactory = (function() {
	var my = {};
	
	// there are views for each type of choice that could be displayed with a question.
	// this returns a Collection of views appropriate for the currentQuestion
	my.getViewsForCurrentQuestion = function() {
		var rtn = new Quizki.Collection();
		var currentQuestion = model_factory.get("currentQuestion");
		var type = currentQuestion.getTypeId();
		var choices = currentQuestion.getChoices(); // Quizki.Collection of choices
		
		if (type == 1) {
			_.each(choices.models, function(model) { rtn.put( new Quizki.ExamSingleQuestionChoiceItemView(model)); }, this);
		}
		else if (type == 2) {
			_.each(choices.models, function(model) { rtn.put( new Quizki.ExamMultipleQuestionChoiceItemView(model)); }, this);
		}
		else if (type == 3) {
			rtn.put( new Quizki.ExamStringQuestionChoiceItemView( choices.at(0) )); 
		}
		else if (type == 4) {
			_.each(choices.models, function(model) { rtn.put( new Quizki.ExamSequenceQuestionChoiceItemView(model)); }, this);
		}
		
		return rtn;
	};
	
	return my;

}());

var ExamEngine = (function() {
	var my = {};
	var listQuestionIds = new Array();
	var listQuestionsAsJsonStrings = new KeyValueMap();
	var index = -1;
	var totalNumberOfQuestions = 0;
	
	
	// would like this to be a private method.......................
	my.getQuestionByItsId = function(id) {
		var str = listQuestionsAsJsonStrings.get(id);

		var rtn = undefined;
		
		// if not cached, ajax call for it
		if (str == undefined || str == '') {
			rtn = getSingleQuestionByEntityId(id);
			
			listQuestionsAsJsonStrings.put(rtn.getId(), rtn.toJSON());
		}
		else {
			// otherwise, use our cached version
			rtn = new Question();
			rtn.initWithJSONSource(str);
		}
		
		return rtn;
	};
	
	my.initialize = function() {
		_.extend(this, Backbone.Events);
		
		// take the CSV string from the examHistoryquestionIndex model,
		var csv = model_factory.get("examHistoryQuestionIndexList");
		
		//  initialize the array
		listQuestionIds = csv.split(',');
		
		//  set totalNumberOfQuestions
		totalNumberOfQuestions = listQuestionIds.length;
		
		index = 0;
		
		model_factory.put('ExamEngine', this);

		var rtn = this.getFirstQuestion(); 
		$("#idCurrentQuestionAsJson").val(rtn.toJSON());
		this.trigger('currentQuestionUpdated');
	};
	
	my.nextQuestion = function() {
		var indexOfTheNextQuestionInTheExam = index + 1;
		
		if (indexOfTheNextQuestionInTheExam < 0) {
			indexOfTheNextQuestionInTheExam = 0;
		}
		
		if (indexOfTheNextQuestionInTheExam >= totalNumberOfQuestions) {
			return null; 	// there is no next question
		}
		
		// get question by that index from listQuestionsAsJsonStrings
		return this.setQuestionByIndex(indexOfTheNextQuestionInTheExam);
	};
	
	my.prevQuestion = function() {
		var indexOfTheNextQuestionInTheExam = index - 1;
		
		if (indexOfTheNextQuestionInTheExam < 0) {
			indexOfTheNextQuestionInTheExam = 0;
		}
		
		// get question by that index from listQuestionsAsJsonStrings
		return this.setQuestionByIndex(indexOfTheNextQuestionInTheExam);
	};
	
	my.setQuestionByIndex = function(idx) {
		index = idx;
		
		var rtn = this.getQuestionByItsId(listQuestionIds[index]);
		
		$("#idCurrentQuestionAsJson").val(rtn.toJSON());
		this.trigger('currentQuestionUpdated');
		
		return rtn;
	};
	
	my.getFirstQuestion = function() {
		return this.getQuestionByItsId(listQuestionIds[0]);
	};
	
	my.getLastQuestion = function() {
		return this.getQuestionByItsId(listQuestionIds[listQuestionIds.length - 1]);
	};
	
	return my;
	
}());

