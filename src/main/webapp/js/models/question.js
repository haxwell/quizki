/**
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
 */


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

var SEQUENCE_0 = "0";

var UI_ID_Generator = (function() {
	var my = {};
	
	my.getNewId = function() {
		return (Math.floor(Math.random() * 9999) + 1) + '';
	};
	
	return my;
	
}());

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
		ui_id:''
	}
});

// TODO: change this name.. its used in more places than just after question text changes.
var PostQuestionTextChangedEventFactory = (function () {
	var my = {};
	
	my.getHandler = function(question) {
		if (question.getTypeId() == QUESTION_TYPE_PHRASE) {
			return function(question) {
				var dynamicFields = getTextOfAllDynamicFields(question.getText());

				// before processing the choices, check: are there dynamic fields in the text, 
				//		or were there dynamic fields our last time through?
				if (dynamicFields.length > 0 || question.getDynamicData('dynamicFieldsPresent') == true) {
					var choices = question.getChoices();
					choices.reset();
					
					question.setDynamicData("dynamicFieldsPresent", dynamicFields.length > 0);
					
					for (var i=0; i < dynamicFields.length; i++) {
						question.addChoice(dynamicFields[i], true, SEQUENCE_0, "dynamicChoice", false);
					}
					
					question.fireLastSuppressedEvent();
					
					ReadOnlyManager.throwEvent(question);
				}
				else {
					question.setDynamicData('dynamicFieldsPresent', false);
				}
			};
		}
		
		return undefined; // no handler defined for the given question type
	};
	
	return my;
}());

// it takes a list of functions per question type, and when given a question, runs through the functions for that type,
//  and returns true, saying yes be read only, only if all functions agree. If any say no, don't be, it returns false.

// TODO: move this to its own file, createQuestion.js
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
			// get all the recommending handlers for this question type
			var coll = handlers[question.getTypeId()];

			if (coll != undefined) {
				do {
					// call each of the handlers, and see what they say
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

var QuestionModelFactory = (function() {
	var my = {};
	
	my.getQuestionModel_JSON = function(jsonSource) {
		var obj = JSON.parse(jsonSource);
		return this.getQuestionModel_AJAX(obj);
	};
	
	my.getQuestionModel_AJAX = function(ajaxSource) {
		var model = undefined;
		
		if (ajaxSource.type_id == QUESTION_TYPE_SINGLE) {
			model = new SingleQuestionModel();
		} else if (ajaxSource.type_id == QUESTION_TYPE_MULTIPLE) {
			model = new MultipleQuestionModel();
		} else if (ajaxSource.type_id == QUESTION_TYPE_SEQUENCE) {
			model = new SequenceQuestionModel();
		} else if (ajaxSource.type_id == QUESTION_TYPE_PHRASE) {
			model = new PhraseQuestionModel();
		} else if (ajaxSource.type_id == QUESTION_TYPE_SET) {
			model = new SetQuestionModel();
		} 
		
		if (model != undefined)
			model.initWithAJAXSource(ajaxSource);
		
		return model;
	};
	
	my.getQuestionModel = function(questionType) {
		var model = undefined;
		
		if (questionType == QUESTION_TYPE_SINGLE || questionType === undefined) {
			model = new SingleQuestionModel();
		} else if (questionType == QUESTION_TYPE_MULTIPLE) {
			model = new MultipleQuestionModel();
		} else if (questionType == QUESTION_TYPE_SEQUENCE) {
			model = new SequenceQuestionModel();
		} else if (questionType == QUESTION_TYPE_PHRASE) {
			model = new PhraseQuestionModel();
		} else if (questionType == QUESTION_TYPE_SET) {
			model = new SetQuestionModel();
		} 
		
		if (model != undefined)
			model.initialize();
		
		return model;
	};
	
	return my;
}());

var QuestionModel = Backbone.Model.extend({
	defaults:{
		id:-1,
		user_id:-1,
		user_name:'',
		text:'',
		description:'',
		type_id:1,
		topics: undefined,
		references:undefined,
		choices:undefined,
		difficulty:undefined,
		lastSuppressedEventName:undefined,
		lastSuppressedEventObject:undefined
	},
	initialize:function() {
		_.extend(this, Backbone.Events);
		
		this.set('topics', new Backbone.Collection([], {model: Topic}));
		this.set('references', new Backbone.Collection([], {model: Reference}));
		this.set('choices', new Backbone.Collection([], {model: Choice}));
		this.set('difficulty', new Difficulty().initialize());
		
		this.typeSpecific_initialize();
	},
	initWithAJAXSource:function(source) {
		this.initialize();
		
		this.set('id', source.id); this.set('user_id', source.user_id); this.set('user_name', source.user_name);
		this.set('text', source.text); this.set('description', source.description); this.set('type_id', this.getTypeId()); 
		
		this.get('difficulty').setDifficultyId(source.difficulty_id);
		
		this.get('topics').reset(); this.get('topics').add(source.topics);
		this.get('references').reset(); this.get('references').add(source.references);
		
		this.get('choices').reset(); this.get('choices').add(source.choices);
		
		this.typeSpecific_initWithAJAXSource(source);
	},
	initWithJSONSource:function(source) {
		var obj = JSON.parse(source);
		this.initWithAJAXSource(obj);
	},
	resetQuestion:function() {
		this.set('id', -1); this.set('user_id', -1); this.set('user_name', ''); this.set('text', ''); this.set('description', ''); 
		this.set('type_id', this.getTypeId()); 
		this.set('topics', new Backbone.Collection([], {model: Topic}));
		this.set('references', new Backbone.Collection([], {model: Reference}));
		this.set('choices', new Backbone.Collection([], {model: Choice}));
		this.set('difficulty', new Difficulty().initialize());
		this.set('lastSuppressedEventName', undefined);
		this.set('lastSuppressedEventObject', undefined);

		this.trigger('resetQuestion');
	},
	toJSON:function() {
		var rtn = '';

		rtn += JSONUtility.startJSONString(rtn);
		
		rtn += JSONUtility.getJSON('id', this.get('id')+'');
		rtn += JSONUtility.getJSON('text', this.get('text'));
		rtn += JSONUtility.getJSON('description', this.get('description'));
		rtn += JSONUtility.getJSON('type_id', this.getTypeId()+'');
		rtn += JSONUtility.getJSON('difficulty_id', this.get('difficulty').getDifficultyId()+'');
		rtn += JSONUtility.getJSON('user_id', this.get('user_id')+'');
		rtn += JSONUtility.getJSON('user_name', this.get('user_name'));
		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('topics', JSON.stringify(this.get('topics').toJSON()));
		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('references', JSON.stringify(this.get('references').toJSON()));
		
		rtn += this.getTypeSpecificToJSON();
		
		var choices = this.get('choices');
		_.each(choices.models, function (model)
				{
					// we remove the id if it is set to -1, because if there are multiple models
					//  with the id of -1, only the first can be inserted in a collection.
					if (model.id === "-1")
					{
						model.unset('id', {silent:true});
					}
				});

		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('choices', JSON.stringify(choices.toJSON()), false);
		
		rtn = JSONUtility.endJSONString(rtn);
		
		return rtn;
	},
	getTypeSpecificToJSON: function() {
		return '';
	},
	typeSpecific_initWithAJAXSource: function(source) {
		return;
	},
	typeSpecific_initialize: function(source) {
		return;
	},
	getDataObject:function() {
		return  {
			id:this.get('id'),
			text:this.get('text'),
			description:this.get('description'),
			type_id:this.getTypeId(),
			difficulty_id:this.get('difficulty').getDifficultyId(),
			user_id:this.get('user_id'),
			topics:JSON.stringify(this.get('topics').toJSON()), 
			references:JSON.stringify(this.get('references').toJSON()),
			choices:JSON.stringify(this.get('choices').toJSON())
		};
	},
	getId:function() {
		return this.get('id');
	},
	setId:function(val, throwEvent) {
		var _from = this.get('id');
		var _to = val;
		
		this.set('id', val);
		
		if (throwEvent !== false)
			this.trigger('questionIDChanged', {text:{from:_from,to:_to}});			
		else
			this.saveSuppressedEvent('questionIDChanged', {text:{from:_from,to:_to}});
	},
	getUserId:function() {
		return this.get('user_id');
	},
	getUserName:function() {
		return this.get('user_name');
	},
	getText:function() {
		return this.get('text');
	},
	setText:function(val, throwEvent) {
		var _from = this.get('text');
		var _to = val;
		
		this.set('text', val);
		
		if (throwEvent !== false)
			this.trigger('questionTextChanged', {text:{from:_from,to:_to}});			
		else
			this.saveSuppressedEvent('questionTextChanged', {text:{from:_from,to:_to}});
	},
	getDescription:function() {
		return this.get('description');
	},
	setDescription:function(val, throwEvent) {
		var _from = this.get('description');
		var _to = val;
		
		this.set('description', val);
		
		if (throwEvent !== false)			
			this.trigger('questionTextChanged', {description:{from:_from,to:_to}});
		else
			this.saveSuppressedEvent('questionTextChanged', {description:{from:_from,to:_to}});
	},
	getTypeId:function() {
		return -1;
	},
//	setTypeId:function(val, throwEvent) {
//		var _from = this.get('type_id');
//		var _to = val;
//		
//		this.set('type_id', val);
//		
//		if (throwEvent !== false)
//			this.trigger('questionTypeChanged', {type_id:{from:_from,to:_to}});
//		else
//			this.saveSuppressedEvent('questionTypeChanged', {type_id:{from:_from,to:_to}});
//	},
	getDifficulty:function() {
		return this.get('difficulty');
	},
	getDifficultyId:function () {
		return this.get('difficulty').getDifficultyId();
	},
	setDifficultyId:function(val, throwEvent) {
		var changesObject = this.get('difficulty').setDifficultyId(val, false);
		
		if (throwEvent !== false)
			this.trigger('difficultyChanged', changesObject);
		else
			this.saveSuppressedEvent('difficultyChanged', changesObject);
	},
	getTopics:function() {
		return this.get('topics');
	},
	getReferences:function() {
		return this.get('references');
	},
	getChoices:function() {
		return this.get('choices');
	},
	addChoice:function(_text, _iscorrect, _sequence, _metadata, throwEvent) {
		var uiid = UI_ID_Generator.getNewId();
		
		this.get('choices').add({ui_id:uiid,text:_text,iscorrect:_iscorrect+'',sequence:_sequence,isselected:'false',metadata:_metadata});

		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
		else
			this.saveSuppressedEvent('choicesChanged', {choices:{val:""}});
		
		return uiid;
	},
	getChoice:function(_id) {
		var rtn;
		
		// try getting by the id
		rtn = this.get('choices').where({id:_id})[0];
		
		if (rtn === undefined)
			// try getting by the ui_id
			rtn = this.get('choices').where({ui_id:_id})[0];
		
		return rtn;
	},
	updateChoice:function(_id, _attrToUpdate, _val, throwEvent) {
		this.getChoice(_id).set(_attrToUpdate, _val+'');
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
		else
			this.saveSuppressedEvent('choicesChanged', {choices:{val:""}});
	},
	removeChoice:function(_id, throwEvent) {
		var choices = this.get('choices');
		choices.reset(_.reject(choices.models, function(choice) { return choice.get('ui_id') == _id || choice.get('id') == _id; }));
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
		else
			this.saveSuppressedEvent('choicesChanged', {choices:{val:""}});
	},
	hasBeenAnswered:function() {
		// child objects define this method
		return false;
	},
	saveSuppressedEvent:function(name, object) {
		lastSuppressedEventName = name;
		lastSuppressedEventObject = object;
	},
	fireLastSuppressedEvent:function() {
		this.trigger(lastSuppressedEventName, lastSuppressedEventObject);
	},
	clearSuppressedEvent:function() {
		lastSuppressedEventName = undefined;
		lastSuppressedEventObject = undefined;
	}
});

var SingleQuestionModel = QuestionModel.extend({
	getTypeId:function() {
		return QUESTION_TYPE_SINGLE;
	},
	hasBeenAnswered:function() {
		var rtn = _.some(this.get('choices').models, function(choice) {
			return choice.get('isselected') == "true";
		});
		
		return rtn;
	}
});

var MultipleQuestionModel = QuestionModel.extend({
	getTypeId:function() {
		return QUESTION_TYPE_MULTIPLE;
	},
	hasBeenAnswered:function() {
		var rtn = _.some(this.get('choices').models, function(choice) {
			return choice.get('isselected') == "true";
		});
		
		return rtn;
	}
});

var SequenceQuestionModel = QuestionModel.extend({
	getTypeId:function() {
		return QUESTION_TYPE_SEQUENCE;
	},
	hasBeenAnswered:function() {
		var rtn = _.every(this.get('choices').models, function(choice) {
			return choice.get('sequence') != 0;
		});
		
		return rtn;
	}
});

var DynamicDataQuestionModel = QuestionModel.extend({
	typeSpecific_initialize: function() {
		this.set('dynamicData', new Backbone.Collection([], {model : KeyValuePair}));
		
		_.extend(this, Backbone.Events);
	},
	typeSpecific_initWithAJAXSource:function(source) {
		var dyndata = this.get('dynamicData');
		
		_.forEach(source.dynamicDataFieldNames, function(model) { var obj = {key:model, value:source[model]}; dyndata.add(obj); });
	},
	initWithJSONSource:function(source) {
		var obj = JSON.parse(source);
		this.initWithAJAXSource(obj);
	},
	setDynamicData:function(key, value) {
		var coll = this.get('dynamicData');
		coll.reset(_.reject(coll.models, function(model) { return model.get('key') === key; }));
		coll.add({key:key, value:value});
	},
	getDynamicData:function(key) {
		var obj = this.get('dynamicData').findWhere({key:key});
		var rtn = undefined;
		
		if (obj !== undefined)
			rtn = obj.get('value');
		
		return rtn;
	}
});

var SetQuestionModel = DynamicDataQuestionModel.extend({
	getTypeId:function() {
		return QUESTION_TYPE_SET;
	},
	getChoiceIdsToBeAnswered:function() {
		var arr = new Array();
		var ddModels = this.get('dynamicData').where({key:'choiceIdsToBeAnswered'});
		
		_.forEach(ddModels, function(model) { arr.push(model.get('value')); });
		
		return arr;
	},
	hasBeenAnswered:function() { 
		var choicesColl = this.get('choices');
		var rtn = _.every(this.getChoiceIdsToBeAnswered(), function(choiceId) {
			var choice = choicesColl.findWhere({id:choiceId});
			return ((choice !== undefined) && (choice.get('phrase') != ''));
		});
		
		return rtn;
	},
	getTypeSpecificToJSON:function() {
		var rtn = '';
		
		var cIds = this.getChoiceIdsToBeAnswered();
		
		if (cIds) {
			rtn += JSONUtility.getJSON_ExistingQuoteFriendly('choiceIdsToBeAnswered', '"' + cIds.join(',') + '"');
		}
		
		var dynData = this.get('dynamicData');
		var dynamicKeys = dynData.pluck('key');
		rtn += JSONUtility.getJSONForArray('dynamicDataFieldNames', dynamicKeys);
		
		_.each(dynamicKeys, function(model) { rtn += JSONUtility.getJSON(model, dynData.get(model).get('value')); });
		
		return rtn;
	}
});

var PhraseQuestionModel = DynamicDataQuestionModel.extend({
	getTypeId:function() {
		return QUESTION_TYPE_PHRASE;
	},
	hasBeenAnswered:function() {
		var rtn = _.some(this.get('choices').models, function(choice) {
			return choice.get('phrase') != '';
		});
		
		return rtn;
	},
	getTypeSpecificToJSON:function() {
		var rtn = '';
		
		var dynData = this.get('dynamicData');
		var dynamicKeys = dynData.pluck('key');
		rtn += JSONUtility.getJSONForArray('dynamicDataFieldNames', dynamicKeys);
		
		_.each(dynamicKeys, function(model) { rtn += JSONUtility.getJSON(model, dynData.get(model).get('value')); });
		
		return rtn;
	},
	getChoiceByFieldNumber:function(fieldNumber) {
		var fieldText = getTextOfGivenFieldForSetQuestion(fieldNumber, this.get('text'));
		var choice = this.get('choices').findWhere({text:fieldText});
		
		return choice;
	}
	
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
 	var rtn = undefined; // = new Question(); 

	var currentQuestionAsJson = $("#idCurrentQuestionAsJson").val();

	// if somebody has already set a question in our special JSON field, lets use it!
	if (currentQuestionAsJson != undefined && currentQuestionAsJson != "") {
		rtn = QuestionModelFactory.getQuestionModel_JSON(currentQuestionAsJson);
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
	var rtn = undefined;
	
	makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
		
		var index = data.indexOf("<!DOCTYPE");
		var jsonExport = data;
		
		if (index != -1) {
			jsonExport = data.substring(0, index);
		}
		
		var parsedJSONObject = jQuery.parseJSON(jsonExport);
		
		rtn = QuestionModelFactory.getQuestionModel_AJAX(parsedJSONObject.question[0]);
		
		if (func !== undefined)
			func(rtn);
	});
	
	return rtn;
};
