var TakeExamChoiceItemFactory = (function() {
	var my = {};
	
	function getArrayOfRandomNumbers(maxIndex) {
		var indexesArr = new Array();
		var rtn = new Array();
		var y = maxIndex;
		
		for (var i=0; i < maxIndex; i++)
			indexesArr[i] = i;
		
		for (var x=0; x < y; ) {
			var v = Math.floor((Math.random()*indexesArr.length)+1) - 1;
			
			rtn[x++] = indexesArr[v];
			indexesArr.splice(v, 1);
		}
		
		return rtn;
	}
	
	function getOrderingOfChoicesCollection() {
		var coll = model_factory.get("orderingOfChoicesCollection");
		
		if (coll == undefined) {
			coll = new Backbone.Collection([], { model: OrderingOfChoicesModel });
			model_factory.put("orderingOfChoicesCollection", coll);
		}
		
		return coll;
	}
	
	// there are views for each type of choice that could be displayed with a question.
	// this returns a Collection of views appropriate for the currentQuestion
	my.getViewsForCurrentQuestion = function() {
		var rtn = new Array();
		var currentQuestion = model_factory.get("currentQuestion");
		var type = currentQuestion.getTypeId();
		var choices = currentQuestion.getChoices(); // Backbone.Collection of choices
		
		var orderingOfChoicesColl = getOrderingOfChoicesCollection();
		
		var orderingOfChoicesModel = orderingOfChoicesColl.findWhere({questionId:currentQuestion.getId()});
		
		if (orderingOfChoicesModel == undefined) {
			orderingOfChoicesModel = new OrderingOfChoicesModel({
				questionId:currentQuestion.getId(), 
				ordering:getArrayOfRandomNumbers(currentQuestion.getChoices().size())
			});
			
			orderingOfChoicesColl.add(orderingOfChoicesModel);
		}
		
		var ordering = orderingOfChoicesModel.get('ordering');
		
		if (type == QUESTION_TYPE_SINGLE) {
			for (var x=0; x < ordering.length; x++) {
				rtn.push( new Quizki.ExamSingleQuestionChoiceItemView(choices.at(ordering[x])));
			}
		}
		else if (type == QUESTION_TYPE_MULTIPLE) {
			for (var x=0; x < ordering.length; x++) {
				rtn.push( new Quizki.ExamMultipleQuestionChoiceItemView(choices.at(ordering[x])));
			}
		}
		else if (type == QUESTION_TYPE_PHRASE) {
			rtn.push( new Quizki.ExamPhraseQuestionChoiceItemView( choices.at(0) )); 
		}
		else if (type == QUESTION_TYPE_SEQUENCE) {
			for (var x=0; x < ordering.length; x++) {
				rtn.push( new Quizki.ExamSequenceQuestionChoiceItemView(choices.at(ordering[x])));
			}
		}
		else if (type == QUESTION_TYPE_SET) {
			for (var x=0; x < ordering.length; x++) {
				var choice = choices.at(ordering[x]);
				var choiceIsToBeAnswered = currentQuestion.getChoiceIdsToBeAnswered().indexOf(choice.get('id')) > -1;
				
				rtn.push( new Quizki.ExamSetQuestionChoiceItemView(choice, choiceIsToBeAnswered) );
			}
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
		
		// take the CSV string from the session model,
		var csv = model_factory.get("currentExamQuestionIdsSortedByTheirID");
		
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

// this function abstracted out so that it can be called from multiple places,
//  independent of the ExamNavigationButtons view (which has the Next button)
var nextBtnClicked = function() {
	var q = ExamEngine.nextQuestion();
	
	if (q == null) {
		// there are no more questions, pop a dialog telling
		//  the user they are at the end.
		var dlg = $('#dialogText').dialog({ 
				autoOpen: false, resizable: false, modal: true,
			    dialogClass:'dialog_stylee', width:500,  
				buttons: [{
			        text : "< Wait!! Let me review!", 
			        click : function() {
			        	event_intermediary.throwEvent('nowInExamReviewMode');
			        	$( this ).dialog( "close" );
			        }},
			        {
			        text : "GRADE IT!",
			        click : function() {
			        	$( this ).dialog( "close" );
			        	
			        	// make ajax call
			        	var v = ExamEngine.getQuestionsAsJsonString();
			        	var answersAsJson = JSONUtility.getJSONForKeyValueMap(
			        			model_factory.get("answersMap"), 
			        			"answers", "fieldId", "value", 
			        			{
			        				getFieldName:function() {return "question_id";}, 
			        				processKeyValue:function(key,value) {return key.substring(0, key.indexOf(','));} 
			        			});
			        	
			        	var data_obj = { questions_json:v, answers_json:answersAsJson };
			        	
			        	makeAJAXCall_andWaitForTheResults('/ajax/exam-grade.jsp', data_obj, function(data, status) {
			        		
							var index = data.indexOf("<!DOCTYPE");
							var jsonExport = data;
							
							if (index != -1) {
								jsonExport = data.substring(0, index);
							}
							
							var parsedJSONObject = jQuery.parseJSON(jsonExport);
			        		
			        		window.location.href = '/examReportCard.jsp';					        		
			        	});
			        } 
			      }]
		});
		
		dlg.dialog('open');
	}
};


// 
var OrderingOfChoicesModel = Backbone.Model.extend({
	defaults: {
		questionId : -1,
		ordering : []
	},
	initialize : function() {
		
	}
});

