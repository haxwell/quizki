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
	
	// to be used once I get bootstrap-switch upgraded... which I hope is soon its waaay behind as of now..
	my.getFinalizeViewCollectionFunction = function() {
		var currentQuestion = model_factory.get("currentQuestion");
		var type = currentQuestion.getTypeId();
		var rtn = undefined;

		if (type == 1) {
			rtn = function() {
				$('.radio').on('switch-change', function() {
					$('.radio').bootstrapSwitch('toggleRadioStateAllowUncheck');					
				});
			};
		}
		else {
			rtn = function () {
				;
			};
		}

		return rtn;
	};

	return my;

}());

var ExamEngine = (function() {
	var my = {},
		listQuestionIds = new Array(),
		questionToJSON_map = new KeyValueMap(),
		index = -1,
		totalNumberOfQuestions = 0,
		lastReturnedQuestion = null,
		isOkayToMoveForward = false,
		allQuestionsHaveBeenAnswered = false;
	
	function getQuestionByItsId(id) {
		var str = questionToJSON_map.get(id);

		var rtn = undefined;
		
		// if not cached, ajax call for it
		if (str == undefined || str == '') {
			rtn = getSingleQuestionByEntityId(id);
			
			questionToJSON_map.put(rtn.getId(), rtn.toJSON());
		}
		else {
			// otherwise, use our cached version
			rtn = new Question();
			rtn.initWithJSONSource(str);
		}
		
		lastReturnedQuestion = rtn;
		
		return rtn;
	};
	
	function cacheCurrentQuestionAsJSON() {
		if (model_factory.contains("currentQuestion")) {
			var currQ = model_factory.get("currentQuestion");
			questionToJSON_map.put(currQ.getId(), currQ.toJSON());
		}
	}
	
	function getQuestionByItsIndex(idx) {
		return getQuestionByItsId(listQuestionIds[idx]);
	}
	
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
		
		this.listenTo(event_intermediary, 'choicesChanged', function(event) { 
			isOkayToMoveForward = true; 
		});

		var rtn = this.firstQuestion(); 
		$("#idCurrentQuestionAsJson").val(rtn.toJSON());
		this.trigger('examEngineSetNewCurrentQuestion');
	};
	
	my.getCurrentQuestionIndex = function() {
		return index;
	};
	
	my.getTotalQuestionCount = function() {
		return totalNumberOfQuestions;
	};
	
	my.setAllQuestionsHaveBeenAnswered = function(bool) {
		allQuestionsHaveBeenAnswered = bool;
	};
	
	my.getAllQuestionsHaveBeenAnswered = function() {
		return allQuestionsHaveBeenAnswered;
	};
	
	my.nextQuestion = function() {
		if (isOkayToMoveForward == false) {
			return lastReturnedQuestion;
		}

		if (index + 1 < totalNumberOfQuestions) {
			// get question by that index from listQuestionsAsJsonStrings
			isOkayToMoveForward = false;

			return this.setQuestionByIndex(++index);
		}
		else {
			this.setAllQuestionsHaveBeenAnswered(true);
			return null; 	// there is no next question
		}
	};
	
	my.prevQuestion = function() {
		var rtn = undefined;
		
		if (index > 0) {
			rtn = this.setQuestionByIndex(--index);
		}
		else {
			rtn = lastReturnedQuestion;
		}

		return rtn;
	};
	
	my.setQuestionByIndex = function(idx) {
		rtn = undefined;
		
		if (idx >= 0 && idx < totalNumberOfQuestions) {
			index = idx;
	
			cacheCurrentQuestionAsJSON();
			rtn = getQuestionByItsIndex(index);
			
			$("#idCurrentQuestionAsJson").val(rtn.toJSON());
			model_factory.put("currentQuestion", rtn);
			this.trigger('examEngineSetNewCurrentQuestion');
			
			lastReturnedQuestion = rtn;
			isOkayToMoveForward = lastReturnedQuestion.hasBeenAnswered();
		}
		
		return rtn;
	};

	my.firstQuestion = function() {
		return this.setQuestionByIndex(0);
	};

	my.lastQuestion = function() {
		return this.setQuestionByIndex(totalNumberOfQuestions - 1);
	};
	
	my.getQuestionsAsJsonString = function() {
		var rtn = JSONUtility.startJSONArray('questions');
		
		rtn += '[';
		
		for (var x=0; x<listQuestionIds.length; x++) {
			var qAsJSON = questionToJSON_map.get(listQuestionIds[x]);
			
			rtn += qAsJSON;
			
			if (x+1<listQuestionIds.length)
				rtn += ', ';
		}
		
		rtn += ']';
		
		rtn = JSONUtility.endJSONArray(rtn);
		
		return rtn;
	};
	
	return my;
	
}());

