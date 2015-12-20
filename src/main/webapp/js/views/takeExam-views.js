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

	// REFACTOR: this is doing two things.. first it has the Quit button, and exits the exam. Second, it maintains
	//  a counter for the current question number and total number of questions.
	Quizki.QuitThisExamView = Backbone.View.extend({
		initialize:function() {
			this.listenTo(ExamEngine, 'examEngineSetNewCurrentQuestion', function(event) { this.render();});
			
			this.showEditBtn = arguments[0].showEditBtn;
			this.render();
		},
		events: {
			"click #btnQuit":"sendEmBackToTheIndexPage"
		},
		render:function() {
			var curr = ExamEngine.getCurrentQuestionIndex() + 1,
				total = ExamEngine.getTotalQuestionCount();
			
			this.$el.html(view_utility.executeTemplate('/templates/TakeExamWithQuitThisExamButton.html', {currQuestionIndex:curr, totalQuestionCount:total}));
			return this;
		},
		sendEmBackToTheIndexPage: function() {
			var url="/index.jsp";
			window.location.href = url;
		}
	});

	Quizki.ExamSingleQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.choiceModel = arguments[0]; // a choice model 
			this.id = this.choiceModel.get('id');
			
			this.eventHandlerMap = new KeyValueMap();
			
			this.render();
		},
		setEventHandler:function(key, func) {
			this.eventHandlerMap.put(key, func);
		},
		getEventHandler:function(key) {
			return this.eventHandlerMap.get(key);
		},
		render:function() {
			
			var selected = '';
			var v = this.choiceModel.get('isselected');
			
			// TODO: figure out if we're passing a string, or the boolean value here, and remove the other case. If we're doing both, fix it so there's only one
			if (v !== undefined && (v == "true" || v === true)) {
				selected = 'checked';
			}
			
			this.$el.html(view_utility.executeTemplate('/templates/ExamSingleQuestionChoiceItemView.html', {id:this.id,checked:selected,text:this.choiceModel.get('text'),disabled:''}));
			return this;
		},
		doThingsUniqueToTheView:function(el) {
			var $radioBtns = el.find('.radio1');
			$radioBtns.bootstrapSwitch('toggleRadioStateAllowUncheck');
		}
	});
	
	Quizki.ExamMultipleQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.choiceModel = arguments[0]; // a choice model 
			this.id = this.choiceModel.get('id');
			
			this.eventHandlerMap = new KeyValueMap();

			this.render();
		},
		setEventHandler:function(key, func) {
			this.eventHandlerMap.put(key, func);
		},
		getEventHandler:function(key) {
			return this.eventHandlerMap.get(key);
		},
		render:function() {
			var selected = '';
			var v = this.choiceModel.get('isselected');
			
			// TODO: figure out if we're passing a string, or the boolean value here, and remove the other case. If we're doing both, fix it so there's only one
			if (v !== undefined && (v == "true" || v === true)) {
				selected = 'checked';
			}

			this.$el.html(view_utility.executeTemplate('/templates/ExamMultipleQuestionChoiceItemView.html', {id:this.id,checked:selected,text:this.choiceModel.get('text'),disabled:''}));
			return this;
		}
	});
	
	Quizki.ExamPhraseQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.choiceModel = arguments[0]; // a choice model 
			this.id = this.choiceModel.get('id');
			
			this.eventHandlerMap = new KeyValueMap();

			this.render();
		},
		setEventHandler:function(key, func) {
			this.eventHandlerMap.put(key, func);
		},
		getEventHandler:function(key) {
			return this.eventHandlerMap.get(key);
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/ExamPhraseQuestionChoiceItemView.html', {id:this.id,answer:this.choiceModel.get('phrase')}));
			return this;
		}
	});
	
	Quizki.ExamSequenceQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.choiceModel = arguments[0]; // a choice model 
			this.id = this.choiceModel.get('id');
			
			this.eventHandlerMap = new KeyValueMap();

			this.render();
		},
		setEventHandler:function(key, func) {
			this.eventHandlerMap.put(key, func);
		},
		getEventHandler:function(key) {
			return this.eventHandlerMap.get(key);
		},
		render:function() {
			var _selectedSequence = this.choiceModel.get('selectedSequence') == -1 ? "" : this.choiceModel.get('selectedSequence');
			this.$el.html(view_utility.executeTemplate('/templates/ExamSequenceQuestionChoiceItemView.html', {id:this.id,sequence:_selectedSequence,text:this.choiceModel.get('text')}));
			return this;
		}
	});

	Quizki.ExamSetQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.choiceModel = arguments[0]; // a choice model 
			this.id = this.choiceModel.get('id');

			this.fieldNumber = arguments[1];
			this.eventHandlerMap = new KeyValueMap();

			this.render();
		},
		setEventHandler:function(key, func) {
			this.eventHandlerMap.put(key, func);
		},
		getEventHandler:function(key) {
			return this.eventHandlerMap.get(key);
		},
		render:function() {
			if (this.fieldNumber == undefined) {
				var _text = this.choiceModel.get('text');
				
				_text = removeAllOccurrences('[[', _text);
				_text = removeAllOccurrences(']]', _text);
				
				this.$el.html(view_utility.executeTemplate('/templates/ExamSetQuestionShowingChoiceItemView.html', {id:this.id,text:_text}));	
			}
			else {
				var _text = getBeginningAndEndingTextForSetQuestion(this.fieldNumber, this.choiceModel.get('text'));
				var _answer = this.choiceModel.get('phrase');
				
				if (_text == undefined) {
					_text = new Array(); _text.push(''); _text.push(''); // no beforeTheField and afterTheField, so put placeholders in.. the effect is that only the text input field shows.
				}
				else {
					for (var i=0; i < _text.length; i++) {
						_text[i] = removeAllOccurrences('[[', _text[i]);
						_text[i] = removeAllOccurrences(']]', _text[i]);
					}
				}
				
				this.$el.html(view_utility.executeTemplate('/templates/ExamSetQuestionShowingTextFieldItemView.html', {id:this.id,answer:_answer,textBegin:_text[0],textEnd:_text[1]}));
			}
			
			return this;
		}
	});
	
	// this view represents the list of choices
	Quizki.ExamChoiceListView = Backbone.View.extend({
		tagName:'ul',
		
		initialize: function() {
			this.$el = arguments[0].el;
			this.ChoiceItemViewCollection = new Array();

			this.listenTo(ExamEngine, 'examEngineSetNewCurrentQuestion', function(event) { this.render();});
			
			this.render();
		},
		events: {
			// do nothing
		},
		renderElement: function(childChoiceView) {
			var ul = this.$el.find("#listOfChoices");

			// this is a callback, which will get the appropriate model from questionChoiceCollection
			var isCorrectChangedCallbackFunc = function(event,data) {

				var id = event.target.id.replace('switch','');
				var currQuestion = model_factory.get("currentQuestion");
				var v = $(event.target).find("div.switch-animate").hasClass('switch-on') + '';
				var oldAnswer = currQuestion.getChoice(id).get('isselected');
				
				if (v != oldAnswer) {
					currQuestion.updateChoice(id, 'isselected', v);
					
					var key = currQuestion.getId() + "," + currQuestion.getChoice(id).id;
					var answers = model_factory.get("answersMap");

					answers.destroy(key);
					
					if (v == 'true')
						answers.put(key, currQuestion.getChoice(id).get('text'));
					
					event_intermediary.throwEvent('choicesChanged');
				}
			};

			var onSequenceTextFieldBlurFunc = function(event,data) {
				var id = event.target.id.replace('sequenceTextField','');
				var currQuestion = model_factory.get("currentQuestion");
				var oldAnswer = currQuestion.getChoice(id).get('selectedSequence');
				var newAnswer = $(event.target).val();
				
				if (newAnswer != oldAnswer) {
					currQuestion.updateChoice(id, 'selectedSequence', newAnswer);
					
					var key = currQuestion.getId() + "," + currQuestion.getChoice(id).id;
					var answers = model_factory.get("answersMap");
					
					answers.destroy(key);
					answers.put(key, newAnswer);
					
					event_intermediary.throwEvent('choicesChanged');
				}
			};

			var onPhraseTextFieldBlurFunc = function(event,data) {
				var id = event.target.id.replace('phraseTextField','');
				var currQuestion = model_factory.get("currentQuestion");
				var oldAnswer = currQuestion.getChoice(id).get('phrase');
				var newAnswer = $(event.target).val();
				
				if (newAnswer != oldAnswer) {
					var choices = currQuestion.getChoices();
					
					_.each(choices.models, function(model) { 
						var cId = model.get('id');
						currQuestion.updateChoice(cId, 'phrase', newAnswer);
					});
					
					var choice = currQuestion.getChoice(id);
					
					// TODO: Question should be able to use the get('id') format as well.. but its not.. look into that.
					var key = currQuestion.getId() + ',' + choice.get('id');
					
					// TODO: Rather than this hacky IF statement, need to create a AnswerMapKeyGeneratorFactory.
					//  we'd pass the current question, the event, and get back the key to be used in the answer map.
					if (currQuestion.getTypeId() == QUESTION_TYPE_SET) {
						var fieldId = undefined;
						_.each(currQuestion.getChoiceIdsToBeAnswered(), function(model) { 
							if (model.indexOf(choice.get('id') + ';') > -1) 
								fieldId = model.split(';')[1]; 
						});

						if (fieldId != undefined)
							key += "," + fieldId;
					}
					
					var answers = model_factory.get('answersMap');
					
					answers.destroy(key);
					answers.put(key, newAnswer);
					
					event_intermediary.throwEvent('choicesChanged');
				}
			};
			
			var onPhraseTextFieldKeypressFunc = function(event,data) {
				if (event.keyCode != 13) return;
				
				onPhraseTextFieldBlurFunc(event,data);
				
				nextBtnClicked();
			};

			ul.append( childChoiceView.render().$el.html() );
			
			childChoiceView.setEventHandler("iscorrectchanged", isCorrectChangedCallbackFunc);
			childChoiceView.setEventHandler("onsequencetextfieldblur", onSequenceTextFieldBlurFunc);
			childChoiceView.setEventHandler("onphrasetextfieldblur", onPhraseTextFieldBlurFunc);
			childChoiceView.setEventHandler("onphrasetextfieldkeypress", onPhraseTextFieldKeypressFunc);
			
			var obj = {id:childChoiceView.id, view:childChoiceView};
			
			this.ChoiceItemViewCollection.push(obj);
		},
		render:function() {
			this.ChoiceItemViewCollection = new Array();
			
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<ul class='choiceItemList span8' id='listOfChoices'></ul>" )() );
			
			var views = TakeExamChoiceItemFactory.getViewsForCurrentQuestion();
			
			_.each(views, function(view) { this.renderElement(view); }, this);
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();

//			TakeExamChoiceItemFactory.getFinalizeViewCollectionFunction()();
			
			// find the bootstrap switch div, add a change listener to it, when change happens, call the handler
			_.each(this.ChoiceItemViewCollection, function(model) {
				$("#switch" + model.view.id).on('switch-change', model.view.getEventHandler("iscorrectchanged"));
				$("#sequenceTextField" + model.view.id).on('blur', model.view.getEventHandler("onsequencetextfieldblur"));
				$("#phraseTextField" + model.view.id).on('blur', model.view.getEventHandler("onphrasetextfieldblur"));
				$("#phraseTextField" + model.view.id).on('keypress', model.view.getEventHandler("onphrasetextfieldkeypress"));
				$("#phraseTextField" + model.view.id).on('blur', model.view.getEventHandler("onphrasetextfieldblur_setquestion"));
			});

			return this;
		}
	});
	
	Quizki.ExamNavigationButtons = Backbone.View.extend({
		initialize: function() {
			this.$el = arguments[0].el;
			this.isOkayToMoveForward = false;

			this.listenTo(ExamEngine, 'examEngineSetNewCurrentQuestion', function(event) { this.render(); });
			this.listenTo(event_intermediary, 'nowInExamReviewMode', function(event) { this.render(); });
			
			this.render();
		},
		events: {
			"click #firstBtn": "firstBtnClicked",
			"click #prevBtn": "prevBtnClicked",
			"click #nextBtn": "nextBtnClicked",
			"click #lastBtn": "lastBtnClicked",
			"keypress #jumpToIndex_Input": "jumpToIndex"
		},
		firstBtnClicked: function() {
			ExamEngine.firstQuestion();
		},
		lastBtnClicked: function() {
			ExamEngine.lastQuestion();
		},		
		prevBtnClicked: function() {
			ExamEngine.prevQuestion();
		},
		jumpToIndex: function(event) {
			if (event.keyCode != 13) return;

			var $textField = $('#jumpToIndex_Input');
			var textFieldVal = $textField.val();

			// if the text field value is a number
			if (!isNaN(parseFloat(textFieldVal)) && isFinite(textFieldVal))
				ExamEngine.setQuestionByIndex(textFieldVal-1);
		},
		nextBtnClicked: function() {
			nextBtnClicked();
		},
		render:function () {
			var _hidden = ExamEngine.getAllQuestionsHaveBeenAnswered() ? "" : "hidden";
			
			var template = view_utility.executeTemplate('/templates/ExamInProgressNavigationButtonsView.html', {disabled:"", hidden:_hidden});
			this.$el.html( template );
		}
	});
