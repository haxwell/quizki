
var CHOICE_IS_CORRECT_AND_CHOSEN = 1;
var CHOICE_IS_CORRECT_BUT_NOT_CHOSEN = 2;
var CHOICE_IS_INCORRECT_AND_CHOSEN = 3;
var CHOICE_IS_INCORRECT_AND_SEQUENCE = 4;
var CHOICE_IS_PHRASE_AND_WE_CANT_TELL_YET = 5;
var CHOICE_IS_FROM_QUESTION_TYPE_SET = 6;
var CHOICE_IS_INDETERMINEDLY_ANSWERED = -1;

var QUESTION_TYPE_SINGLE = 1;
var QUESTION_TYPE_MULTIPLE = 2;
var QUESTION_TYPE_PHRASE = 3;
var QUESTION_TYPE_SEQUENCE = 4;
var QUESTION_TYPE_SET = 5;

var QuestionTypes = (function() {
	var my = {};

	my.getString = function(intKey) {
		if (intKey == 1)
			return "Single";
		else if (intKey == 2)
			return "Multiple";
		else if (intKey == 3)
			return "Phrase";
		else if (intKey == 4)
			return "Sequence";
		else if (intKey == 5)
			return "Set";
		
		return "ERROR!!";
	};
	
	return my;
}());

// TODO: this should be moved to a common area.. question.js should not need to be included solely because, for instance, DifficultyChooserView is included.
var Difficulty = (function() {
	var my = {};
	
	var difficulty_id = 1,
		initialized = false;
	
	my.initialize = function() {
		if (!initialized) {
			difficulty_id = 1;
			_.extend(this, Backbone.Events);
			
			initialized = true;
		}
		
		return this;
	};

	my.getDifficultyId = function () {
		return difficulty_id;
	};
		
	my.setDifficultyId = function(val, throwEvent) {
		var _from = difficulty_id;
		var _to = val;
		
		difficulty_id = val;
		
		var changesObject = {difficulty_id:{from:_from,to:_to}};
		
		if (throwEvent !== false)
			this.trigger('difficultyChanged', changesObject);	
		
		return changesObject;
	};

	my.toJSON = function() {
		var rtn = '';

		rtn += JSONUtility.startJSONString(rtn);

		rtn += JSONUtility.getJSON('difficulty_id', difficulty_id);
		
		rtn = JSONUtility.endJSONString(rtn);
		
		return rtn;		
	};
	
	my.getDataObject = function () {
		return  {
			difficulty_id:difficulty_id,
		};
	};
	
	return my;
});

var ChosenChoicesQuestionChoiceItemViewModel = Backbone.Model.extend({
	defaults : {
		text:'',
		checked:'',
		sequence:'',
		id:'',
		millisecond_id:''
	}
});

var answerCorrectnessModel = Backbone.Model.extend({
	defaults : { 
		correctAndChosen:0, 
		correctButNotChosen:0, 
		incorrectAndChosen:0, 
		totalChoicesCount:0, 
		overallAnsweredCorrectly:undefined, 
		phraseAnswer:undefined,
		cssClass:''
	},
	isAnsweredCorrectly : function() {
		return (this.get('overallAnsweredCorrectly') == true);
	},
	incrementCorrectAndChosen : function() {
		this.set('correctAndChosen', this.get('correctAndChosen') + 1);
		this.set('totalChoicesCount', this.get('totalChoicesCount') + 1);
		this.set('cssClass', 'correctAndChosen');
    	
		if (this.get('overallAnsweredCorrectly') == undefined)
    		this.set('overallAnsweredCorrectly', true);
	},
	incrementCorrectButNotChosen : function () {
		this.set('correctButNotChosen', this.get('correctButNotChosen') + 1);
		this.set('totalChoiceCount', this.get('totalChoiceCount') + 1);
		this.set('cssClass', 'correctButNotChosen');
    	
   		this.set('overallAnsweredCorrectly', false);
	},
	incrementIncorrectAndChosen : function () {
		this.set('incorrectAndChosen', this.get('incorrectAndChosen') + 1);
		this.set('totalChoiceCount', this.get('totalChoiceCount') + 1);
		this.set('cssClass', 'incorrectAndChosen');
    	
   		this.set('overallAnsweredCorrectly', false);
	}
});

var ChosenChoicesQuestionChoiceItemViewHelper = (function () {
	var my = {};

	my.processAnswerCorrectnessForThisChoice = function(_viewmodel) {

		var cq = model_factory.get('currentQuestion');
		var mostRecentExamAnswers = model_factory.get("answersToTheMostRecentExam");
		var answer = mostRecentExamAnswers.findWhere({fieldId:cq.getId()+','+_viewmodel.get('id')});
		var cccStatus = this.getChoiceCorrectlyChosenStatus(answer, cq, _viewmodel);
		var answerCorrectnessModel = model_factory.get("answerCorrectnessModel");
		
		if (cccStatus == CHOICE_IS_CORRECT_AND_CHOSEN) {
			answerCorrectnessModel.incrementCorrectAndChosen();
			
        } else if (cccStatus == CHOICE_IS_CORRECT_BUT_NOT_CHOSEN) {
			answerCorrectnessModel.incrementCorrectButNotChosen();

        } else if (cccStatus == CHOICE_IS_INCORRECT_AND_CHOSEN) {
			answerCorrectnessModel.incrementIncorrectAndChosen();
        	
        } else if (cccStatus == CHOICE_IS_INCORRECT_AND_SEQUENCE) {
			answerCorrectnessModel.incrementIncorrectAndChosen(); 
        	_viewmodel.comment = ' (You typed: ' + answer.get('value') + ')';

        } else if (cccStatus == CHOICE_IS_PHRASE_AND_WE_CANT_TELL_YET) {
        	if (answer != undefined) { // if an answer was supplied for this choice...
        		answerCorrectnessModel.set('phraseAnswer', answer.get('value'));

            	if (answerCorrectnessModel.isAnsweredCorrectly() == false) {
        			_.each(cq.getChoices().models, function(model) { 
        				if (model.get('text') == answer.get('value')) 
        					answerCorrectnessModel.set('overallAnsweredCorrectly', true); 
        			});
        		}
        	}
        } else if (cccStatus == CHOICE_IS_FROM_QUESTION_TYPE_SET) {
        	
			var fieldId = undefined;
			
			var choicesToBeAnsweredArray = cq.getChoiceIdsToBeAnswered();
			_.each(choicesToBeAnsweredArray.split(','), function(model) { 
				var v = model.split(';'); 
				if (v.length === 2) {
					fieldId = v[1];
				}
			});

        	if (fieldId != undefined) {
        		answer = mostRecentExamAnswers.findWhere({fieldId:cq.getId()+','+_viewmodel.get('id')+','+fieldId});
        	}
			
        	if (answer != undefined) { // if an answer was supplied for this choice...
    			
        		var selectedChoices = _.filter(cq.getChoices().models, function(model) { 
        				return choicesToBeAnsweredArray.indexOf(model.get('id')) > -1; 
        			});

        		_.each(selectedChoices, function(model) { 
					var fieldText = getTextOfGivenFieldForSetQuestion(fieldId, model.get('text'));
    				var answeredCorrectly = (fieldText == answer.get('value'));
					
					if (answeredCorrectly) {
						answerCorrectnessModel.incrementCorrectAndChosen();
						answerCorrectnessModel.set('phraseAnswer', answer.get('value'));
					}
					else {
						answerCorrectnessModel.incrementIncorrectAndChosen();
						_viewmodel.set('comment', ' (You typed: ' + answer.get('value') + ', instead of: ' + fieldText + ')');
					}
        		});
        	}
        	
			var v = removeAllOccurrences('[[', _viewmodel.get('text'));
			v = removeAllOccurrences(']]', v);

			_viewmodel.set('text', v);
        }
	};
	
	my.getChoiceCorrectlyChosenStatus = function(answer, cq, _viewmodel) {
		var cccStatus = CHOICE_IS_INDETERMINEDLY_ANSWERED;

		if (cq.getTypeId() == QUESTION_TYPE_PHRASE) 
			return CHOICE_IS_PHRASE_AND_WE_CANT_TELL_YET;
		
		if (cq.getTypeId() == QUESTION_TYPE_SET)
			return CHOICE_IS_FROM_QUESTION_TYPE_SET;
		
    	if (answer != undefined && _viewmodel.get('checked') == 'checked' && answer.get('value') == (cq.getTypeId() == QUESTION_TYPE_SEQUENCE ? _viewmodel.get('sequence') : _viewmodel.get('text') )) {
    		cccStatus = CHOICE_IS_CORRECT_AND_CHOSEN;
    	} else if (answer == undefined && _viewmodel.get('checked') == 'checked') {
    		cccStatus = CHOICE_IS_CORRECT_BUT_NOT_CHOSEN;
    	} else if (answer != undefined && _viewmodel.get('checked') !== 'checked') {
    		cccStatus = CHOICE_IS_INCORRECT_AND_CHOSEN;
        } else if (answer != undefined && cq.getTypeId() == QUESTION_TYPE_SEQUENCE &&  _viewmodel.get('checked') == 'checked' && answer.get('value') !== _viewmodel.get('sequence')) {
        	cccStatus = CHOICE_IS_INCORRECT_AND_SEQUENCE;
        } 

		return cccStatus;
	};
	
	return my;
});

// TODO: change this name.. its used in more places than just after question text changes.
var PostQuestionTextChangedEventFactory = (function () {
	var my = {};
	
	my.getHandler = function(question) {
		if (question.getTypeId() == QUESTION_TYPE_PHRASE) {
			return function(question) {
				var choices = question.getChoices();
				choices.reset();
				
				var dynamicFields = getTextOfAllDynamicFields(question.getText());
				for (var i=0; i < dynamicFields.length; i++) {
					question.addChoice(dynamicFields[i], true, "0", "dynamicChoice", false);
				}
				
				question.fireLastSuppressedEvent();
				
				ReadOnlyManager.throwEvent(question);
			};
		}
		
		return undefined;
	};
	
	return my;
}());

// it takes a list of functions per question type, and when given a question, runs through the functions for that type,
//  and returns true, saying yes be read only, only if all functions agree. If any say no, don't be, it returns false.
var ReadOnlyManager = (function() {
	var my = {};
	
	var handlers = {};
	
	my.addHandler = function(questionType, func) {
		var coll = handlers[questionType];
		
		if (coll == undefined) {
			coll = new Array();
			handlers[questionType] = coll;
		}
		
		coll.push(func);
	};
	
	my.getReadOnlyRecommendation = function(question) {
		var recc = false;
		var index = 0;
		
		if (question != undefined) {
			var coll = handlers[question.getTypeId()];

			if (coll != undefined) {
				do {
					recc = coll[index++](question);
	
				} while (recc !== false && index < coll.length);
			}
		}
		
		return recc;
	};
	
	my.throwEvent = function(question) {
		if (this.getReadOnlyRecommendation(question)) {
			event_intermediary.throwEvent("readOnlyApplied");	
		}
		else {
			event_intermediary.throwEvent("readOnlyCleared");
		}
	};
	
	return my;
}());

var Question = (function() {
	var my = {};
	
	var id = -1;
	var user_id = -1;
	var user_name = "";
	var text = "";
	var description = "";
	var type_id = 1;
	var topics = undefined; /* will be a Backbone.Collection */
	var references = undefined; /* will be a Backbone.Collection */
	var choices = undefined; /* will be a Backbone.Collection */
	var difficulty = undefined; /* will be a Difficulty object */
	
	var lastSuppressedEventName = undefined;
	var lastSuppressedEventObject = undefined;
	
	var choiceIdsToBeAnswered = undefined; /* will be an array, used when this is a Set question */
	
	// private method
	function initializeFields() {
		id = -1; user_id = -1; user_name = '';
		text = ''; description = ''; type_id = 1; difficulty = new Difficulty().initialize();
		topics = new Backbone.Collection([], {model: Topic}); 
		references = new Backbone.Collection([], {model: Reference}); 
		choices = new Backbone.Collection([], {model : Choice});
		dynamicData = new Backbone.Collection([], {model : KeyValuePair});
	};
	
	my.initialize = function() {
		initializeFields();
		_.extend(this, Backbone.Events);
	};
	
	my.initWithAJAXSource = function(source) {
		this.initialize();
		
		id = source.id; user_id = source.user_id; user_name = source.user_name;
		text = source.text;	description = source.description; type_id = source.type_id; 
		
		difficulty.setDifficultyId(source.difficulty_id);
		
		topics.add(source.topics);
		references.add(source.references);
		
		choices = new Backbone.Collection([], {model:Choice});
		choices.add(source.choices);
		
		_.forEach(source.dynamicDataFieldNames, function(model) { var obj = {key:model, value:source[model]}; dynamicData.add(obj); });
		
		_.extend(this, Backbone.Events);
	};
	
	my.initWithJSONSource = function(source) {
		var obj = JSON.parse(source);
		
		this.initWithAJAXSource(obj);
	};
	
	my.resetQuestion = function() {
		initializeFields();
		
		this.trigger('resetQuestion');
	};
	
	my.toJSON = function() {
		var rtn = '';

		rtn += JSONUtility.startJSONString(rtn);
		
		rtn += JSONUtility.getJSON('id', id+'');
		rtn += JSONUtility.getJSON('text', text);
		rtn += JSONUtility.getJSON('description', description);
		rtn += JSONUtility.getJSON('type_id', type_id+'');
		rtn += JSONUtility.getJSON('difficulty_id', difficulty.getDifficultyId()+'');
		rtn += JSONUtility.getJSON('user_id', user_id+'');
		rtn += JSONUtility.getJSON('user_name', user_name);
		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('topics', JSON.stringify(topics.toJSON()));
		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('references', JSON.stringify(references.toJSON()));
		
		if (choiceIdsToBeAnswered !== undefined) {
			rtn += JSONUtility.getJSON_ExistingQuoteFriendly('choiceIdsToBeAnswered', '"' + choiceIdsToBeAnswered.join(',') + '"');
		}
		
		var dynamicKeys = dynamicData.pluck('key');
		rtn += JSONUtility.getJSONForArray('dynamicDataFieldNames', dynamicKeys);
		
		_.each(dynamicKeys, function(model) { rtn += JSONUtility.getJSON(model, dynamicData.get(model).get('value')); });
		
		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('choices', JSON.stringify(choices.toJSON()), false);
		
		rtn = JSONUtility.endJSONString(rtn);
		
		return rtn;
	};
	
	my.getDataObject = function () {
		return  {
				id:id,
				text:text,
				description:description,
				type_id:type_id,
				difficulty_id:difficulty.getDifficultyId(),
				user_id:user_id,
				topics:JSON.stringify(topics.toJSON()), 
				references:JSON.stringify(references.toJSON()),
				choices:JSON.stringify(choices.toJSON())
		};
	};
	
	my.getId = function() {
		return id;
	};
	
	my.getUserId = function() {
		return user_id;
	};
	
	my.getUserName = function() {
		return user_name;
	};
	
	my.getText = function() {
		return text;
	};
	
	my.setText = function(val, throwEvent) {
		var _from = text;
		var _to = val;
		
		text = val;
		
		if (throwEvent !== false)
			this.trigger('questionTextChanged', {text:{from:_from,to:_to}});			
		else
			this.saveSuppressedEvent('questionTextChanged', {text:{from:_from,to:_to}});
	};
	
	my.getDescription = function() {
		return description;
	};
	
	my.setDescription = function(val, throwEvent) {
		var _from = description;
		var _to = val;
		
		description = val;
		
		if (throwEvent !== false)			
			this.trigger('questionTextChanged', {description:{from:_from,to:_to}});
		else
			this.saveSuppressedEvent('questionTextChanged', {description:{from:_from,to:_to}});
	};

	my.getTypeId = function() {
		return type_id;
	};
		
	my.setTypeId = function(val, throwEvent) {
		var _from = type_id;
		var _to = val;
		
		type_id = val;
		
		if (throwEvent !== false)
			this.trigger('questionTypeChanged', {type_id:{from:_from,to:_to}});
		else
			this.saveSuppressedEvent('questionTypeChanged', {type_id:{from:_from,to:_to}});
	};
		
	my.getDifficulty = function() {
		return difficulty;
	};
	
	my.getDifficultyId = function () {
		return difficulty.getDifficultyId();
	};

	my.setDifficultyId = function(val, throwEvent) {
		var changesObject = difficulty.setDifficultyId(val, false);
		
		if (throwEvent !== false)
			this.trigger('difficultyChanged', changesObject);
		else
			this.saveSuppressedEvent('difficultyChanged', changesObject);
	};
	
	my.getTopics = function() {
		return topics;
	};
	
	// deprecated! -- you should get the collection you want to update, and add the models yourself
	//  Backbone will take care of the rest. If you're interested in events, listen to the collection itself.
	my.setTopics = function(val, throwEvent) {
//		var arr = val.split(',');
//		
//		for (str in arr) {
//			if (topics.findWhere({text:str}) == undefined) {
//				var topic = new Topic();
//				topic.set('id', '-1');
//				topic.set('text', str);
//				topics.add(topic);
//			}
//		}
//		
//		if (throwEvent !== false)
//			this.trigger('topicsChanged', { });			
	};
	
	my.getReferences = function() {
		return references;
	};
		
	// deprecated! -- you should get the collection you want to update, and add the models yourself
	//  Backbone will take care of the rest. If you're interested in events, listen to the collection itself.
	my.setReferences = function(val, throwEvent) {
//		var arr = val.split(',');
//		
//		for (str in arr) {
//			var reference = new Reference();
//			reference.set('id', '-1');
//			reference.set('text', str);
//			references.add(reference);
//		}
//		
//		if (throwEvent !== false)
//			this.trigger('referencesChanged', { });			
	};
		
	my.getChoices = function() {
		return choices;
	};
		
	my.addChoice = function(_text, _iscorrect, _sequence, _metadata, throwEvent) {
		millisecond_id = new Date().getMilliseconds()+'';
		
		choices.add({id:millisecond_id,text:_text,iscorrect:_iscorrect+'',sequence:_sequence,isselected:'false',metadata:_metadata});

		console.log("added choice: " + _text);
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
		else
			this.saveSuppressedEvent('choicesChanged', {choices:{val:""}});
		
		return millisecond_id;
	};
	
	my.getChoice = function(_millisecondId) {
		return choices.where({id:_millisecondId})[0];
	};
		
	my.updateChoice = function(_millisecondId, _attrToUpdate, _val, throwEvent) {
		choices.where({id:_millisecondId})[0].set(_attrToUpdate, _val+'');
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
		else
			this.saveSuppressedEvent('choicesChanged', {choices:{val:""}});

	};
		
	my.removeChoice = function(_millisecondId, throwEvent) {
		choices.reset(_.reject(choices.models, function(choice) { return choice.get('id') == _millisecondId; }));
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
		else
			this.saveSuppressedEvent('choicesChanged', {choices:{val:""}});

	};
	
	my.getChoiceIdsToBeAnswered = function() {
		// TODO: This should be returned via Backbone, using a style like q.get('choiceIdsToBeAnswered').. need to get model
		//  definitions of a question correctly working in order to do that
		
		return dynamicData.findWhere({key:'choiceIdsToBeAnswered'}).get('value');
	};
	
	my.hasBeenAnswered = function() {
		var rtn = false;
		
		if (type_id == QUESTION_TYPE_SINGLE || type_id == QUESTION_TYPE_MULTIPLE) {
			rtn = _.some(choices.models, function(choice) {
				return choice.get('isselected') == "true";
			});
		} else if (type_id == QUESTION_TYPE_PHRASE) {
			rtn = _.some(choices.models, function(choice) {
				return choice.get('phrase') != '';
			});
		} else if (type_id == QUESTION_TYPE_SEQUENCE) {
			rtn = _.every(choices.models, function(choice) {
				return choice.get('sequence') != 0;
			});
		} else if (type_id == QUESTION_TYPE_SET) {
			rtn = _.every(choiceIdsToBeAnswered, function(choiceId) {
				var choice = choices.findWhere({id:choiceId});
				
				if (choice !== undefined)
					return choice.get('phrase') != '';
				else
					return false;
			});
		}
		
		return rtn;
	};
	
	my.saveSuppressedEvent = function(name, object) {
		lastSuppressedEventName = name;
		lastSuppressedEventObject = object;
	};
	
	my.fireLastSuppressedEvent = function() {
		this.trigger(lastSuppressedEventName, lastSuppressedEventObject);
	};
	
	my.clearSuppressedEvent = function() {
		lastSuppressedEventName = undefined;
		lastSuppressedEventObject = undefined;
	};
		
	return my;
});


//  since this is a declaration of a function, how do you say this function needs a function passed in, when 
//  all you are doing is passing the variable (representing the function's) name? for instance if I'm passing
//.  this function to another function, how does 
//
//		foo(getFunctionToRetrieveCurrentQuestion)
//
//	indicate to foo, it should pass a function as the parameter?
// 
// I don't know, but I know it will come along eventually.. so when it does.. address this note..
//
// UPDATE: perhaps don't use a pointer to a function like this, but an actual function declaration? But still,
//  when passing the declaration itself, how do you say function required.. selector required. Do you? or does
//  the calling method just need to know?

//
// Assumptions: Assumes there is a field defined matching $("#idEntityIdField").. It should contain the id of
//  the entity, likely passed in as a URL parameter.
//
var getFunctionToRetrieveCurrentQuestion = function() {
 	var rtn = new Question(); 

	var currentQuestionAsJson = $("#idCurrentQuestionAsJson").val();

	// if somebody has already set a question in our special JSON field, lets use it!
	if (currentQuestionAsJson != undefined && currentQuestionAsJson != "") {
		rtn.initWithJSONSource(currentQuestionAsJson);
		$("#idCurrentQuestionAsJson").val('');
	}
	else {
		// otherwise, we need to get the question ourselves.. check the special Entity ID field..
		var entityId = $("#idEntityIdField").val();
		
		if (entityId != undefined && entityId != "") {
	    	rtn = getSingleQuestionByEntityId(entityId);
		}
		else {
			rtn = getBlankQuestionFromServer();
		}
	}

	return rtn;
};

var getSingleQuestionByEntityId = function(entityId, func) {
	var data_url = "/ajax/getSingleQuestion.jsp";
	var data_obj = { entityIdFilter : entityId }; 

	var rtn = getQuestionByAJAXCall(data_url, data_obj, func);
	
	return rtn;
};

var getBlankQuestionFromServer = function() {
	var data_url = "/ajax/getBlankQuestion.jsp";
	var data_obj = { }; 

	var rtn = getQuestionByAJAXCall(data_url, data_obj); 
	
	return rtn;
};

var getQuestionByAJAXCall = function(data_url, data_obj, func) {
	var rtn = new Question();
	
	makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
		
		var index = data.indexOf("<!DOCTYPE");
		var jsonExport = data;
		
		if (index != -1) {
			jsonExport = data.substring(0, index);
		}
		
		var parsedJSONObject = jQuery.parseJSON(jsonExport);
		
		rtn.initWithAJAXSource(parsedJSONObject.question[0]);
		
		if (func !== undefined)
			func(rtn);
	});
	
	return rtn;
};
