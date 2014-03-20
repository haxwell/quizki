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
			this.model = arguments[0].attributes;
			this.millisecondId = arguments[0].attributes.id;
			
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
			
			if (this.model.isselected !== undefined && (this.model.isselected == "true" || this.model.isselected === true)) {
				selected = 'checked';
			}
			
			this.$el.html(view_utility.executeTemplate('/templates/ExamSingleQuestionChoiceItemView.html', {id:this.millisecondId,checked:selected,text:this.model.text,disabled:''}));
			return this;
		},
		doThingsUniqueToTheView:function(el) {
			var $radioBtns = el.find('.radio1');
			$radioBtns.bootstrapSwitch('toggleRadioStateAllowUncheck');
		}
	});
	
	Quizki.ExamMultipleQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes;
			this.millisecondId = arguments[0].attributes.id;
			
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
			
			if (this.model.isselected !== undefined && (this.model.isselected == "true" || this.model.isselected === true)) {
				selected = 'checked';
			}

			this.$el.html(view_utility.executeTemplate('/templates/ExamMultipleQuestionChoiceItemView.html', {id:this.millisecondId,checked:selected,text:this.model.text,disabled:''}));
			return this;
		}
	});
	
	Quizki.ExamPhraseQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes;
			this.millisecondId = arguments[0].attributes.id;
			
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
			this.$el.html(view_utility.executeTemplate('/templates/ExamPhraseQuestionChoiceItemView.html', {id:this.millisecondId,answer:this.model.phrase}));
			return this;
		}
	});
	
	Quizki.ExamSequenceQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes;
			this.millisecondId = arguments[0].attributes.id;
			
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
			var _selectedSequence = this.model.selectedSequence == -1 ? "" : this.model.selectedSequence;
			this.$el.html(view_utility.executeTemplate('/templates/ExamSequenceQuestionChoiceItemView.html', {id:this.millisecondId,sequence:_selectedSequence,text:this.model.text}));
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

				var millisecond_id = event.target.id.replace('switch','');
				var currQuestion = model_factory.get("currentQuestion");
				var v = $(event.target).find("div.switch-animate").hasClass('switch-on') + '';
				var oldAnswer = currQuestion.getChoice(millisecond_id).get('isselected');
				
				if (v != oldAnswer) {
					currQuestion.updateChoice(millisecond_id, 'isselected', v);
					
					var key = currQuestion.getId() + "," + currQuestion.getChoice(millisecond_id).id;
					var answers = model_factory.get("answersMap");

					answers.destroy(key);
					
					if (v == 'true')
						answers.put(key, currQuestion.getChoice(millisecond_id).get('text'));
					
					event_intermediary.throwEvent('choicesChanged');
				}
			};

			var onSequenceTextFieldBlurFunc = function(event,data) {
				var millisecond_id = event.target.id.replace('sequenceTextField','');
				var currQuestion = model_factory.get("currentQuestion");
				var oldAnswer = currQuestion.getChoice(millisecond_id).get('selectedSequence');
				var newAnswer = $(event.target).val();
				
				if (newAnswer != oldAnswer) {
					currQuestion.updateChoice(millisecond_id, 'selectedSequence', newAnswer);
					
					var key = currQuestion.getId() + "," + currQuestion.getChoice(millisecond_id).id;
					var answers = model_factory.get("answersMap");
					
					answers.destroy(key);
					answers.put(key, newAnswer);
					
					event_intermediary.throwEvent('choicesChanged');
				}
			};

			var onPhraseTextFieldBlurFunc = function(event,data) {
				var millisecond_id = event.target.id.replace('phraseTextField','');
				var currQuestion = model_factory.get("currentQuestion");
				var oldAnswer = currQuestion.getChoice(millisecond_id).get('phrase');
				var newAnswer = $(event.target).val();
				
				if (newAnswer != oldAnswer) {
					var choices = currQuestion.getChoices();
					
					_.each(choices.models, function(model) { 
						var cId = model.get('id');
						currQuestion.updateChoice(cId, 'phrase', newAnswer);
					});
					
					var key = currQuestion.getId() + "," + currQuestion.getChoice(millisecond_id).id;
					var answers = model_factory.get("answersMap");
					
					answers.destroy(key);
					answers.put(key, newAnswer);
					
					event_intermediary.throwEvent('choicesChanged');
				}
			};

			ul.append( childChoiceView.val.render().$el.html() );
			
			childChoiceView.val.setEventHandler("iscorrectchanged", isCorrectChangedCallbackFunc);
			childChoiceView.val.setEventHandler("onsequencetextfieldblur", onSequenceTextFieldBlurFunc);
			childChoiceView.val.setEventHandler("onphrasetextfieldblur", onPhraseTextFieldBlurFunc);
			
			var obj = {millisecondId:childChoiceView.val.millisecondId, view:childChoiceView.val};
			
			this.ChoiceItemViewCollection.push(obj);
		},
		render:function() {
			this.ChoiceItemViewCollection = new Array();
			
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<ul class='choiceItemList span6' id='listOfChoices'></ul>" )() );
			
			var views = TakeExamChoiceItemFactory.getViewsForCurrentQuestion();
			
			_.each(views.models, function(view) { this.renderElement(view.attributes); }, this);
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();

//			TakeExamChoiceItemFactory.getFinalizeViewCollectionFunction()();
			
			// find the bootstrap switch div, add a change listener to it, when change happens, call the handler
			_.each(this.ChoiceItemViewCollection, function(model) {
				$("#switch" + model.view.millisecondId).on('switch-change', model.view.getEventHandler("iscorrectchanged"));
				$("#sequenceTextField" + model.view.millisecondId).on('blur', model.view.getEventHandler("onsequencetextfieldblur"));
				$("#phraseTextField" + model.view.millisecondId).on('blur', model.view.getEventHandler("onphrasetextfieldblur"));
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
		},
		render:function () {
			var _hidden = ExamEngine.getAllQuestionsHaveBeenAnswered() ? "" : "hidden";
			
			var template = view_utility.executeTemplate('/templates/ExamInProgressNavigationButtonsView.html', {disabled:"", hidden:_hidden});
			this.$el.html( template );
		}
	});
