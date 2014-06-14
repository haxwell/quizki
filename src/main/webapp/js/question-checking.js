var ChoiceCorrectnessModel = Backbone.Model.extend({
	defaults : {
		fieldId:undefined,
		correctnessStatus:-1,
		comment:'',
		cssClass:''
	}
});

var QuestionCorrectnessModel = Backbone.Model.extend({
	defaults : {
		answeredCorrectly:undefined,
		givenAnswer:undefined,
		correctAndChosen:0, 
		correctButNotChosen:0, 
		incorrectAndChosen:0, 
		totalChoicesCount:0, 
	},
	isAnsweredCorrectly : function() {
		return (this.get('answeredCorrectly') == true);
	},
	incrementCorrectAndChosen : function() {
		this.set('correctAndChosen', this.get('correctAndChosen') + 1);
		this.set('totalChoicesCount', this.get('totalChoicesCount') + 1);
		
		if (this.get('answeredCorrectly') == undefined)
			this.set('answeredCorrectly', true);
	},
	incrementCorrectButNotChosen : function () {
		this.set('correctButNotChosen', this.get('correctButNotChosen') + 1);
		this.set('totalChoiceCount', this.get('totalChoiceCount') + 1);
		
		this.set('answeredCorrectly', false);
	},
	incrementIncorrectAndChosen : function () {
		this.set('incorrectAndChosen', this.get('incorrectAndChosen') + 1);
		this.set('totalChoiceCount', this.get('totalChoiceCount') + 1);
		
		this.set('answeredCorrectly', false);
	}

});

var IsAnsweredCorrectlyThingy = (function() {
	var my = {};
	
	my.processQuestion = function() {
		// gets the current question, and sets in the model_factory a model which tells the client
		//  for each choice, whatever information is needed, answered correctly, cssClass, whatever.
		model_factory.put("questionCorrectnessModel", new QuestionCorrectnessModel());
		model_factory.put("correctnessResults", getChoiceCheckerFactory().check());
	};
	
	return my;
}());

var getChoiceCheckerFactory = function() {
	var currentQuestion = model_factory.get("currentQuestion");
	var qt = currentQuestion.getTypeId();
	
	if (qt == QUESTION_TYPE_SINGLE) {
		rtn = TypeSingleChoiceChecker;
	} else if (qt == QUESTION_TYPE_MULTIPLE) {
		rtn = TypeSingleChoiceChecker;
	} else if (qt == QUESTION_TYPE_SEQUENCE) {
		rtn = TypeSequenceChoiceChecker;
	} else if (qt == QUESTION_TYPE_SET) {
		rtn = TypeSetChoiceChecker;
	} else if (qt == QUESTION_TYPE_PHRASE) {
		rtn = TypePhraseChoiceChecker;
	}
	
	return rtn;
};

var TypeSingleChoiceChecker = (function() {

	var my = {};
	
	my.check = function() {
		
		var cq = model_factory.get("currentQuestion");
		var mostRecentExamAnswers = model_factory.get("answersToTheMostRecentExam");
		var qcm = model_factory.get("questionCorrectnessModel");
		var rtn = new Backbone.Collection([], {model : ChoiceCorrectnessModel});
		
		_.each(cq.getChoices().models, function(choice) {
			var answer = mostRecentExamAnswers.findWhere({fieldId:cq.getId() + ',' + choice.get('id')});
			var ccm = new ChoiceCorrectnessModel({fieldId:cq.getId() + ',' + choice.get('id')});
			var choiceTextStringified = stringifyApostrophesAndQuotes(choice.get('text'));
			var answerTextStringified = (answer === undefined) ? undefined : answer.get('value');
			
			// if an answer was supplied, and this choice says 'I am correct', and the supplied answer equals the text of this choice...
			if (answer != undefined && (choice.get('iscorrect') == 'true' || choice.get('iscorrect') === true) && answerTextStringified == choiceTextStringified ) {
				ccm.set('correctnessStatus', CHOICE_IS_CORRECT_AND_CHOSEN);
				ccm.set('cssClass', 'correctAndChosen');
				
				qcm.incrementCorrectAndChosen();
			}
			else if (answer == undefined && (choice.get('iscorrect') == 'true' || choice.get('iscorrect') === true)){
				ccm.set('correctnessStatus', CHOICE_IS_CORRECT_BUT_NOT_CHOSEN);
				ccm.set('cssClass', 'correctButNotChosen');
				
				qcm.incrementCorrectButNotChosen();
			}
			else if (answer != undefined && (choice.get('iscorrect') != 'true' || choice.get('iscorrect') !== true)){
				ccm.set('correctnessStatus', CHOICE_IS_INCORRECT_AND_CHOSEN);
				ccm.set('cssClass', 'incorrectAndChosen');
				
				qcm.incrementIncorrectAndChosen();
			}
			
			rtn.add(ccm);
		});
		
		return rtn;
	};
	
	return my;
}());

var TypeSequenceChoiceChecker = (function() {

	var my = {};
	
	my.check = function() {
		
		var cq = model_factory.get("currentQuestion");
		var mostRecentExamAnswers = model_factory.get("answersToTheMostRecentExam");
		var qcm = model_factory.get("questionCorrectnessModel");
		var rtn = new Backbone.Collection([], {model : ChoiceCorrectnessModel});

		_.each(cq.getChoices().models, function(choice) {
			var answer = mostRecentExamAnswers.findWhere({fieldId:cq.getId() + ',' + choice.get('id')});
			var ccm = new ChoiceCorrectnessModel({fieldId:cq.getId() + ',' + choice.get('id')});
			
			if (answer.get('value') == choice.get('sequence')) {
				ccm.set('correctnessStatus', CHOICE_IS_CORRECT_AND_CHOSEN);
				ccm.set('cssClass', 'correctAndChosen');
				
				qcm.incrementCorrectAndChosen();
			}
			else {
				ccm.set('correctnessStatus', CHOICE_IS_INCORRECT_AND_CHOSEN);
				ccm.set('cssClass', 'incorrectAndChosen');
				ccm.set('comment', ' (You typed: ' + answer.get('value') + ')');

				qcm.incrementIncorrectAndChosen();
			}
			
			rtn.add(ccm);
		});
		
		return rtn;
	};
	
	return my;
}());
		
var TypeSetChoiceChecker = (function() {

	var my = {};
	
	my.check = function() {
		
		var cq = model_factory.get("currentQuestion");
		var mostRecentExamAnswers = model_factory.get("answersToTheMostRecentExam");
		var qcm = model_factory.get("questionCorrectnessModel");
		var rtn = new Backbone.Collection([], {model : ChoiceCorrectnessModel});
		
		var fieldId = undefined;
		
		var choicesToBeAnsweredArray = cq.getChoiceIdsToBeAnswered();
		_.each(choicesToBeAnsweredArray, function(model) { 
			var v = model.split(';'); 
			if (v.length === 2) {
				fieldId = v[1];
			}
		});

    	_.each(cq.getChoices().models, function(choice) {
    		var ccm = new ChoiceCorrectnessModel();
    		ccm.set('fieldId', cq.getId()+','+choice.get('id'));
    		var answer = undefined;
    		
    		if (fieldId != undefined) {
	    		answer = mostRecentExamAnswers.findWhere({fieldId:cq.getId()+','+choice.get('id')+','+fieldId});
	    	}
			
	    	if (answer != undefined) { // if an answer was supplied for this choice...
				
	    		var choicesToBeAnsweredArrayAsString = choicesToBeAnsweredArray.join(','); 
	    		
	    		if (choicesToBeAnsweredArrayAsString.indexOf(choice.get('id') > -1)) {
					var fieldText = getTextOfGivenFieldForSetQuestion(fieldId, choice.get('text'));
					var answeredCorrectly = (fieldText == answer.get('value'));
					
					if (answeredCorrectly) {
						ccm.set('correctnessStatus', CHOICE_IS_CORRECT_AND_CHOSEN);
						ccm.set('cssClass', 'correctAndChosen');
						
						qcm.incrementCorrectAndChosen();
						qcm.set('givenAnswer', answer.get('value'));
					}
					else {
						ccm.set('correctnessStatus', CHOICE_IS_INCORRECT_AND_CHOSEN);
						ccm.set('cssClass', 'incorrectAndChosen');
						ccm.set('comment', ' (You typed: ' + answer.get('value') + ', instead of: ' + fieldText + ')');
						
						qcm.incrementIncorrectAndChosen();
						qcm.set('givenAnswer', answer.get('value'));
					}
	    		}
	    	}
	    	
	    	rtn.add(ccm);
    	});
    	
    	return rtn;
	};
	
	return my;
}());

var TypePhraseChoiceChecker = (function() {

	var my = {};
	
	my.check = function() {
		
		var cq = model_factory.get("currentQuestion");
		var mostRecentExamAnswers = model_factory.get("answersToTheMostRecentExam");
		var qcm = model_factory.get("questionCorrectnessModel");
		var rtn = new Backbone.Collection([], {model : ChoiceCorrectnessModel});

		if (cq.get('dynamicData').length > 0) {
			var work = _.filter(cq.get('dynamicData').models, function(model) { 
				return model.get('key') == 'dynamicFieldToBeBlankedOut'; 
			});
			
			if (work.length > 0) {
				
				_.each(cq.getChoices().models, function(choice) {
					var fieldText = getTextOfGivenFieldForSetQuestion(work[0].get('value'), cq.get('text'));
					var answer = mostRecentExamAnswers.findWhere({fieldId:cq.getId() + ',' + choice.get('id')});
					var ccm = new ChoiceCorrectnessModel({fieldId:cq.getId() + ',' + choice.get('id')});
					
					if (answer != undefined) {
						if (answer.get('value') == fieldText) {
							ccm.set('correctnessStatus', CHOICE_IS_CORRECT_AND_CHOSEN);
							ccm.set('cssClass', 'correctAndChosen');
							
							qcm.incrementCorrectAndChosen();
							qcm.set('givenAnswer', answer.get('value'));
						}
						else {
							ccm.set('correctnessStatus', CHOICE_IS_INCORRECT_AND_CHOSEN);
							ccm.set('cssClass', 'incorrectAndChosen');
							ccm.set('comment', ' (You typed: ' + answer.get('value') + ', instead of: ' + fieldText + ')');
							
							qcm.incrementIncorrectAndChosen();
							qcm.set('givenAnswer', answer.get('value'));
						}
					}
					
					rtn.add(ccm);
				});
			}
		} else {
			// non dynamic question

			var answer = mostRecentExamAnswers.findWhere({question_id:cq.getId()});
			
			_.each(cq.getChoices().models, function(choice) {
				var ccm = new ChoiceCorrectnessModel({fieldId:cq.getId() + ',' + choice.get('id')});

				if (answer != undefined) {
					if (answer.get('value') == choice.get('text')) {
						ccm.set('correctnessStatus', CHOICE_IS_CORRECT_AND_CHOSEN);
						ccm.set('cssClass', 'correctAndChosen');
						qcm.incrementCorrectAndChosen();
					}

					qcm.set('givenAnswer', answer.get('value'));
				}
				
				rtn.add(ccm);
			});
		}
		
		return rtn;
    };
	
	return my;
}());