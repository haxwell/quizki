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

	Quizki.QuestionTypeView = Backbone.View.extend({
		initialize:function() {
			this.readOnly = arguments[0].readOnly;
			
			this.render();
			
			var currQuestion = model_factory.get("currentQuestion");
			
			this.listenTo(event_intermediary, 'currentQuestion::put::model_factory', function(event) { 
				this.render(); 

				var currentQuestion = model_factory.get("currentQuestion");
				var func = PostQuestionTextChangedEventFactory.getHandler(currentQuestion);
		        if (func != undefined)
		        	func(currentQuestion);
			});
		},
		events: {
			"change select":"changed"
		},
		changed:function(event) {
			// get the value from the html element
			var val = event.target.value;
			
			// set it in the model
			var currentQuestion = model_factory.get("currentQuestion");
			var _from = currentQuestion.getTypeId();
			var _to = val;

			var obj = JSON.parse(currentQuestion.toJSON());
			obj.type_id = _to;
			
			var newCurrentQuestion = QuestionModelFactory.getQuestionModel_AJAX(obj);

			model_factory.put("currentQuestion", newCurrentQuestion);
			
			event_intermediary.throwEvent("questionTypeChanged", {from:_from, to:_to});
		},
		render: function() {
			var currentQuestion = model_factory.get("currentQuestion");
			var typeId = currentQuestion.getTypeId();

			if (this.readOnly) {
				var str = QuestionTypes.getString(typeId);
				this.$el.html( view_utility.executeTemplate('/templates/QuestionTypeView-readOnly.html', {type:str}));
			}
			else {
				this.$el.html( view_utility.executeTemplate('/templates/QuestionTypeView.html', {}));
				
				// iterate over each of the buttons, if it matches the model, set it as active
				// otherwise remove the active attribute
				_.each($("#questionTypeSelectBox").find("option"), function($item) { 
					$item = $($item);
					($item.val() == typeId+'') ? $item.attr('selected','selected') : $item.removeAttr('selected'); 
				});
			}
			
			// HACK.
			var v = this.$el.html();
			this.$el.html(v);
			
			return this;
		}
	});

	Quizki.SaveButtonView = Backbone.View.extend({
		initialize:function() {
			this.readOnly = arguments[0].readOnly;
			this.getDataObjectFunc = arguments[0].getDataObjectFunc;
			
			this.render();
		},
		events: {
			"click #btnSave":"saveQuestion"
		},
		render:function() {
			var classAttributeHidden = (this.readOnly == true) ? "hidden" : "";
			
			this.$el.html(view_utility.executeTemplate('/templates/QuestionHeaderWithSaveButtons.html', {hidden:classAttributeHidden}));			
			return this;
		},
		saveQuestion: function() {
			var data_url = "/ajax/question-save.jsp";
			var data_obj = this.getDataObjectFunc();
			
			// do the ajax call
			makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) { 
				var index = data.indexOf("<!DOCTYPE");
				var jsonExport = data;
				
				if (index != -1) {
					jsonExport = data.substring(0, index);
				}
				
				var parsedJSONObject = jQuery.parseJSON(jsonExport);
				var arr = undefined;
				var alertClass = undefined;
				
				if (parsedJSONObject.errors != undefined) {
					arr = parsedJSONObject.errors;
					alertClass = 'alert-error';
				} else {
					arr = parsedJSONObject.successes;
					alertClass = 'alert-success';

					var newCurrentQuestion = QuestionModelFactory.getQuestionModel();
					
					model_factory.put("currentQuestion", newCurrentQuestion);
					
					event_intermediary.throwEvent("QuestionSuccessfullySaved");
				}
				
				var msgs = "";
				
				for (var i=0; i<arr.length; i++) {
					msgs += arr[i] + '<br/>';
				}

				var $alertDiv = $("#idAlertDiv"); 
				
				$alertDiv.html('');
				$alertDiv.html(msgs);
				$alertDiv.removeClass('alert-success');
				$alertDiv.removeClass('alert-error');
				$alertDiv.addClass(alertClass);
				$alertDiv.removeClass('hidden');
				
			    $("#createDupeQuestionLink").click(function(e) {
			    	var q = getSingleQuestionByEntityId(parsedJSONObject.newQuestionId);
			    	
			    	q.setId(-1);
			    	
			    	_.each(q.getChoices().models, function(model) { 
			    		model.set('ui_id', UI_ID_Generator.getNewId());
			    		model.set('id', '-1');
			    	});
			    	
			    	model_factory.put("currentQuestion", q);
			    	
			    	$alertDiv.addClass('hidden');
			    	
			    	e.preventDefault(); 
			    });
			});
		}
	});

	Quizki.CreatedByView = Backbone.View.extend({
		initialize:function() {
			this.render();
		},
		render:function() {
			var currQuestion = model_factory.get("currentQuestion");
			var name = currQuestion.getUserName();
			var userId = model_factory.get("currentUserId");
			
			if (userId == currQuestion.getUserId())
				name = "You";
			
			this.$el.html(view_utility.executeTemplate('/templates/CreatedBy.html', {creator:name}));
			return this;
		}
	});
	
	Quizki.QuestionHeaderButtonView = Backbone.View.extend({
		initialize:function() {
			this.showEditBtn = arguments[0].showEditBtn;
			this.showBackBtn = arguments[0].showBackBtn;
			this.render();
			
			if (arguments[0].afterDisplayFunction != undefined)
				arguments[0].afterDisplayFunction();
		},
		events: {
			"click #btnEdit":"editQuestion",
			"click #btnBack":"goBack"
		},
		render:function() {
			var editBtnVisibility = (this.showEditBtn == false ? "hidden" : "");
			var backBtnVisibility = (this.showBackBtn == false ? "hidden" : "");
			
			this.$el.html(view_utility.executeTemplate('/templates/QuestionHeaderWithButtons.html', {hideEditBtn:editBtnVisibility, hideBackBtn:backBtnVisibility}));
			return this;
		},
		editQuestion: function() {
			var url="/secured/question.jsp?questionId=" + model_factory.get("currentQuestion").getId();
			window.location.href = url;
		},
		goBack: function() {
			window.history.go(-1);
		}
	});

	Quizki.QuestionAttributeWellView = Backbone.View.extend({
		initialize:function() {
			this.template = undefined;
			this.readOnly = arguments[0].readOnly;
			
			this.id = Math.floor(Math.random() * 9999) + 1;
			var viewKey = arguments[0].viewKey;
			
			model_constructor_factory.put(this.id + "ViewKey",function() {return ("" + viewKey); });
			
			// the key to use for storing the appropriate collection from the Question in the model
			var backboneModelKey = this.getBackboneModelKey();
			
			// function to use getting the appropriate collection from the Question
			model_constructor_factory.put(backboneModelKey, arguments[0].backboneFunc);
			
			// removing our reference to that appropriate collection when the current question changes.
			this.listenTo(event_intermediary, "currentQuestion::put::model_factory", function () {
				model_factory.remove(backboneModelKey);
				this.render();
			});
			
			this.KeyTo_modelToListenTo = arguments[0].KeyTo_modelToListenTo;
			this.modelEventToListenFor = arguments[0].modelEventToListenFor;
			
			if (this.KeyTo_modelToListenTo != undefined) {
				var modelToListenTo = model_factory.get(this.KeyTo_modelToListenTo);
				this.listenTo(modelToListenTo, this.modelEventToListenFor, function() { 

					// TODO: rename the keys and variable names here so that it is clear this model is being called
					//  when the view should be cleared, (entries and autocomplete values and everything)
					
					var viewKey = model_factory.get(this.id + "ViewKey");
					
					model_factory.remove( this.getBackboneModelKey() );
					model_factory.remove(viewKey + "AutocompleteHistory");
					model_factory.remove(viewKey + "AutocompleteField");
					
					model_factory.remove(viewKey + "AutocompleteEntries");
					model_factory.remove(viewKey + "DeletedAutocompleteEntries");
					model_factory.put(viewKey + "AutocompleteEntries", new Backbone.Collection([], {model: KeyValuePair}));
					model_factory.put(viewKey + "DeletedAutocompleteEntries", new Backbone.Collection([], {model: KeyValuePair}));
					
					this.render();
				});
			}

			// this is what the user typed during this session, to be sent to the server
			model_factory.put(viewKey + "AutocompleteEntries", new Backbone.Collection([], {model: KeyValuePair}));
			
			// these are items the user pressed delete on, in the autocomplete dropdown box
			model_factory.put(viewKey + "DeletedAutocompleteEntries", new Backbone.Collection([], {model: KeyValuePair}));
			
			this.listenTo(event_intermediary, 'escapeKeyPressedInAutocompleteField', function(event) { this.renderAutocompleteField(false); });
		},
		events: {
			"click .well_add_button":"toggleNewEntryField",
			"keypress .completelyField":"pressEnterToSaveNewEntries",
			"dblclick span.label":"removeEntry",				
		},
		getBackboneModelKey: function() {
			var viewKey = model_factory.get( this.id + "ViewKey" );
			
			return (viewKey + "AttrWellBackboneCollection");
		},
		toggleNewEntryField:function(event){
			var viewKey = model_factory.get( this.id + "ViewKey" );
			var field = $("#completelyField_" + viewKey);
			
			var makeVisible = undefined;
			
			if (field.children().length == 0)
				makeVisible = true;
			else 
				makeVisible = !field.is(':visible'); 
			
			this.renderAutocompleteField( makeVisible );
		},
		pressEnterToSaveNewEntries : function(event) {
			if (event.keyCode != 13) return;
			
			this.saveNewEntries(event);
		},
		saveNewEntries:function(model) {
			var viewKey = model_factory.get( this.id + "ViewKey" );
			var autocompleteField = model_factory.get(viewKey + "AutocompleteField");
			
			var text = autocompleteField.getText();
			var arr = text.split('|');
			
			autocompleteField.setText('');
			
			this.toggleNewEntryField();
			
			var backboneModel = model_factory.get( this.getBackboneModelKey() );
			var modelLength = backboneModel.length;
			for (var x=0; x < arr.length; x++) {
				if (arr[x].length > 0) backboneModel.add({text:arr[x].trim()});
			}

			// if any items were added to the backbone model 
			if (modelLength < backboneModel.length) {
				var viewKey = model_factory.get( this.id + "ViewKey" );
				model_factory.get(viewKey + "AutocompleteEntries").add({key:text, value:text});
			}
			
			this.render();
		},
		removeEntry:function(event) {
			if (this.readOnly != true) {
				var backboneModel = model_factory.get( this.getBackboneModelKey() );
				
				var entryText = $(event.target).html();
				
				var newColl = _.reject(backboneModel.models, function(item) { return entryText === item.get('text'); });
				backboneModel.reset(newColl);
				model_factory.put(this.getBackboneModelKey(), backboneModel);

				var viewKey = model_factory.get( this.id + "ViewKey" );
//				model_factory.get(viewKey + "DeletedAutocompleteEntries").add({key:entryText, value:entryText});
				var ace = model_factory.get(viewKey + "AutocompleteEntries");
				newColl = _.reject(ace.models, function(item) { return entryText === item.get('value'); });
				ace.reset(newColl);
				
				this.render();
			}
		},
		renderElement:function(model) {
			var modelText = model.get('text');
			if (modelText != undefined) {
				var ul = this.$el.find("#wellItemList"+this.id);
					
				ul.append( view_utility.executeTemplate('/templates/QuestionAttributeWellItemView.html', { text:modelText }) );
			}
		},
		renderAutocompleteField: function(makeVisible) {
			var viewKey = model_factory.get( this.id + "ViewKey" );
			var autocompleteField = model_factory.get(viewKey + "AutocompleteField");
			
			if (makeVisible) {
				if (autocompleteField == undefined) {
					autocompleteField = completely(document.getElementById('completelyField_' + viewKey), { 
						onKeyPress:function(e) { 
							if (e.keyCode == 27) { event_intermediary.throwEvent("escapeKeyPressedInAutocompleteField"); e.keyCode = undefined; } 
						},
						onDeleteKeyPress:function(text, index) {
							var coll = model_factory.get(viewKey + "DeletedAutocompleteEntries");
							coll.add({key:text, value:text});
						}
					});
					
					autocompleteField.options = model_factory.get(viewKey + 'AutocompleteHistory');
					autocompleteField.repaint(); 
					autocompleteField.input.focus();
					
					model_factory.put(viewKey + "AutocompleteField", autocompleteField);
				}
				else {
					$("#completelyField_" + viewKey).show();
					autocompleteField.repaint();
					autocompleteField.input.focus();
				}
			}
			else {
				$("#completelyField_" + viewKey).hide();
			}
		},
		render:function() {
			var readOnlyAttr = (this.readOnly == true) ? "readOnly" : "";
			var _id = this.id;
			
			this.$el.html(view_utility.executeTemplate('/templates/QuestionAttributeWellView.html', {id:_id, readOnly:readOnlyAttr}));
			
			var backboneModel = model_factory.get( this.getBackboneModelKey() );
			
			_.each(backboneModel.models, function(model) { this.renderElement(model); }, this);
			
			var _viewKey = model_factory.get( this.id + "ViewKey" );
			
			model_factory.remove(_viewKey + "AutocompleteField");
			
			var currHtml = this.$el.html();
			currHtml += view_utility.executeTemplate('/templates/AttributeWellViewInputField.html',{id:_id, viewKey:_viewKey});
			
			this.$el.html(currHtml);
			
			return this;
		}
	});

	Quizki.ChosenChoicesQuestionChoiceItemView = Backbone.View.extend({
		tagName:'li',
		
		initialize:function() {
			this.choiceModel = arguments[0];
			
			var text = this.choiceModel.get('text');
			
			text = removeAllOccurrences('[[', text);
			text = removeAllOccurrences(']]', text);
			
			var checked = this.choiceModel.get('iscorrect') === 'true' ? 'checked' : '';
			var sequence = this.choiceModel.get('sequence') || 0;
			
			var ui_id = this.choiceModel.get('ui_id');// || UI_ID_Generator.getNewId();
			var id = this.choiceModel.get('id');
			
			this.viewmodel = new ChosenChoicesQuestionChoiceItemViewModel({text:text,checked:checked,sequence:sequence,id:id,ui_id:ui_id});
		},
		setHideSwitchAndSequence:function() {
			var currQuestion = model_factory.get('currentQuestion');
			
			this.hideSequence = "hidden";
			this.hideSwitch = "";
			
			if (currQuestion.getTypeId() == QUESTION_TYPE_SEQUENCE) {
				this.hideSequence = "";
				this.hideSwitch = "hidden";
			}
		},
		render:function() {
			var _viewmodel = this.viewmodel;
			
			this.setHideSwitchAndSequence();

			var cr = model_factory.get("correctnessResults");
			var currQuestion = model_factory.get('currentQuestion');
			var contextBasedId = (_viewmodel.get('ui_id') == '-1') ? _viewmodel.get('id') : _viewmodel.get('ui_id');
			var ccm = cr.findWhere({fieldId:currQuestion.getId() + ',' + contextBasedId});
			
			var template = view_utility.executeTemplate('/templates/ChosenChoicesQuestionChoiceItemView.html', 
					{_id:contextBasedId,
							text:_viewmodel.get('text'),
							comment:ccm.get('comment'),
							checked:_viewmodel.get('checked'),
							sequence:_viewmodel.get('sequence'),
							hideSequence:this.hideSequence, hideSwitch:this.hideSwitch, choiceCorrectStatusClass:ccm.get('cssClass')
					});
            
			this.$el.html( template );
			
            return this;
		},
		getUIId: function() { return (this.viewmodel.get('ui_id') == '-1') ? this.viewmodel.get('id') : this.viewmodel.get('ui_id'); },
		setText: function(newText) { ; }, // TODO: is this necessary??
		getText: function() { return this.viewmodel.get('text'); }, // TODO: or this?
	});
	
	// this view represents an item in a list of choices
	Quizki.QuestionChoiceItemView = Backbone.View.extend({
		tagName:'li',
		
		initialize: function() {
			this.choiceModel = arguments[0];
			
			var text = this.choiceModel.get('text');
			
			var checked = this.choiceModel.get('iscorrect') === 'true' ? 'checked' : '';
			var sequence = this.choiceModel.get('sequence') || 0;
			var ui_id = this.choiceModel.get('ui_id');// || UI_ID_Generator.getNewId();
			var id = this.choiceModel.get('id');
			
			this.viewmodel = {text:text,checked:checked,sequence:sequence,id:id,ui_id:ui_id};
			
			this.disableCheckboxes = arguments[1];
			
			this.onIsCorrectChangedHandler = arguments[2];
			this.onSequenceTextFieldBlurHandler = arguments[3];
			
			this.readOnly = arguments[4];
		},
		getHideSequence:function() {
			var currQuestion = model_factory.get('currentQuestion');
			
			var hideSequence = "hidden";
			
			if (currQuestion.getTypeId() == "4")
				hideSequence = "";

			return hideSequence;
		},
		getDisabledText: function () {
			if (this.disableCheckboxes === true)
				return "disabled";
			
			return "";
		},
		render:function() {
            var _viewmodel = this.viewmodel;
            var hideSequence = this.getHideSequence();
            var disabled = this.getDisabledText();
            var readOnlyAttr = (this.readOnly == true) ? "readOnly" : "";
            var contextBasedId = (_viewmodel.ui_id == '-1') ? _viewmodel.id : _viewmodel.ui_id;
            
            var template = view_utility.executeTemplate('/templates/QuestionChoiceItemView.html', 
            		{_id: contextBasedId,
            				text:_viewmodel.text,checked:_viewmodel.checked,sequence:_viewmodel.sequence,
            				hideSequence:hideSequence,disabled:disabled,readOnly:readOnlyAttr
            		});
			this.$el.html( template );
			
			return this;
		},
		getUIId: function() { return (this.viewmodel.ui_id == '-1') ? this.viewmodel.id : this.viewmodel.ui_id;	},
		setText: function(newText) { this.viewmodel.text = newText; },
		getText: function() { return this.viewmodel.text; },
		setIsCorrectChangedHandler: function(func) { this.onIsCorrectChangedHandler = func;},
		getIsCorrectChangedHandler: function() { return this.onIsCorrectChangedHandler;},
		setSequenceTextFieldBlurHandler: function(func) { this.onSequenceTextFieldBlurHandler = func;},
		getSequenceTextFieldBlurHandler: function() { return this.onSequenceTextFieldBlurHandler;}
	});

	// this view represents the list of choices
	Quizki.ChoiceListView = Backbone.View.extend({
		tagName:'ul',
		
		initialize: function() {
			this.readOnly = arguments[0].readOnly;
			this.inExamContext = arguments[0].inExamContext;
			
			this.$el = arguments[0].el;
			
			this.ChoiceItemViewCollection = new Array();
			
			var currQuestion = model_factory.get('currentQuestion');
			this.setCurrentQuestionListenTo(currQuestion);
			
			this.listenTo(event_intermediary, 'readOnlyApplied', function(event) { this.setReadOnly(true); });
			this.listenTo(event_intermediary, 'readOnlyCleared', function(event) { this.setReadOnly(false); });
			
			this.listenTo(event_intermediary, 'currentQuestion::put::model_factory', function(newQuestion) { this.setCurrentQuestionListenTo(newQuestion); this.render(); });
			
			this.setStateOnInitialization();
			
			this.render();
		},
		setCurrentQuestionListenTo: function(currQuestion) {
			this.listenTo(currQuestion, 'choicesChanged', function(event) { this.render(); });
		},
		setSequenceFieldsAreVisible: function (bool) {
			var _el = this.$el.find(".sequenceDiv");
			
			if (bool) {
				_el.removeClass("hidden");
			}
			else {
				_el.addClass("hidden");
			}
		},
		choiceItemSwitchesShouldBeDisabled : function() {
			var typeId = model_factory.get('currentQuestion').getTypeId();
			return (typeId == QUESTION_TYPE_PHRASE || typeId == QUESTION_TYPE_SEQUENCE || typeId == QUESTION_TYPE_SET) || (this.readOnly == true);
		},
		setStateOnInitialization: function () {
			var typeId = model_factory.get('currentQuestion').getTypeId();
			this.setSequenceFieldsAreVisible(typeId == QUESTION_TYPE_PHRASE || typeId == QUESTION_TYPE_SEQUENCE || typeId == QUESTION_TYPE_SET);
		},
		setStateOnQuestionTypeChangedEvent: function(event) {
			var rtn = undefined;

			if (event !== undefined && event.type_id !== undefined) {
				// if the event was to change TO 'phrase' or 'sequence'....
				if (event.type_id.to == QUESTION_TYPE_PHRASE || event.type_id.to == QUESTION_TYPE_SEQUENCE || event.type_id.to == QUESTION_TYPE_SET) {
					rtn = true;
				}
				else if (rtn == undefined && (event.type_id.to == QUESTION_TYPE_PHRASE || event.type_id.to == QUESTION_TYPE_SEQUENCE || event.type_id.to == QUESTION_TYPE_SET)) {
					rtn = false;
				}
			}
			
			this.setSequenceFieldsAreVisible(rtn || false);
			this.setReadOnly(ReadOnlyManager.getReadOnlyRecommendation(model_factory.get('currentQuestion')));
		},
		setReadOnly: function(bool) {
			this.readOnly = bool;
			this.render();
		},
		events: {
			"dblclick":"edit",
			"keypress .edit":"closeOnEnter",
			"blur .edit":"close",
			"click .destroyBtn":"remove"
		},
		edit : function(event) {
			if (this.readOnly != true) {
				var _el = this.$el.find("li:hover");
				
				_el.addClass('editing');
				
				_el.find('.edit').removeClass('hideForEditing');
				_el.find(".edit").focus();
			}
		},
		close : function(event) {
			var $currentLineItem = this.$el.find(".editing");
			var ui_id = $currentLineItem.attr("id");

			var currQuestion = model_factory.get("currentQuestion");
			currQuestion.updateChoice(ui_id, 'text', $currentLineItem.find('.edit').val());
		},
		closeOnEnter : function(event) {
			if (event.keyCode != 13) return;

			this.close(event);
		},
		renderElement:function (model) {
			var ul = this.$el.find("#listOfChoices");
			
			// this is a callback, which will get the appropriate model from questionChoiceCollection
			//  set the isCorrect attr on it. Does not redraw the list, thats already been done
			var isCorrectChangedCallbackFunc = function(event,data) {
				var ui_id = event.target.id.replace('switch','');
				var currQuestion = model_factory.get("currentQuestion");
				var v = $(event.target).find("div.switch-animate").hasClass('switch-on');
				
				currQuestion.updateChoice(ui_id, 'iscorrect', v+'', false);
			};

			var onSequenceTextFieldBlurFunc = function(event,data) {
				var ui_id = event.target.id.replace('sequenceTextField','');
				var currQuestion = model_factory.get("currentQuestion");
				currQuestion.updateChoice(ui_id, 'sequence', $(event.target).val()+'', false);
			};
			
			var questionChoiceItemView = undefined;
			
			if (this.inExamContext) {
				questionChoiceItemView = new Quizki.ChosenChoicesQuestionChoiceItemView(model);
			}
			else {
				questionChoiceItemView = new Quizki.QuestionChoiceItemView(model, this.choiceItemSwitchesShouldBeDisabled(), isCorrectChangedCallbackFunc, onSequenceTextFieldBlurFunc, this.readOnly);
			}
			
			ul.append( questionChoiceItemView.render().$el.html() );
			
			var obj = {ui_id:questionChoiceItemView.getUIId(), view:questionChoiceItemView};
			
			this.ChoiceItemViewCollection.push(obj);
		},
		render:function() {
			this.ChoiceItemViewCollection = new Array();
			
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<ul class='choiceItemList span6' id='listOfChoices'></ul>" )() );
			
			var cq = model_factory.get("currentQuestion");
			var choices = cq.getChoices();
			
			_.each(choices.models, function(model) { this.renderElement(model); }, this);

			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();

			if (!this.inExamContext) {
				// find the bootstrap switch div, add a change listener to it, when change happens, call the handler
				_.each(this.ChoiceItemViewCollection, function(model) {
					$("#switch" + model.ui_id).on('switch-change', model.view.getIsCorrectChangedHandler());
				});
				
				_.each(this.ChoiceItemViewCollection, function(model) {
					$("#sequenceTextField" + model.ui_id).on('blur', model.view.getSequenceTextFieldBlurHandler());
				});
			}
			
			return this;
		},
		remove:function(event) {
			var _el = this.$el.find("li:hover");
			var currUIId = _el.attr("id");
			
			model_factory.get("currentQuestion").removeChoice(currUIId);
		}
		
	});

	
	// This view, when clicked, alerts the QuestionChoice model,
	// and passes an object with which it can get choice text, and choice
	//  correctness. The collection adds that to itself. ChoiceListView listens for
	//  that event, and draws each item in the collection.
	Quizki.EnterNewChoiceView = Backbone.View.extend({
		initialize: function() {
			this.$el = arguments[0].el;

			this.readOnly = false;
			
			var currQuestion = model_factory.get('currentQuestion', false);
			this.listenTo(event_intermediary, 'readOnlyApplied', function(event) { this.setReadOnly(true); });
			this.listenTo(event_intermediary, 'readOnlyCleared', function(event) { this.setReadOnly(false); });

			this.listenTo(event_intermediary, 'currentQuestion::put::model_factory', function(newQuestion) { this.setStateOnQuestionTypeChangedEvent(event); this.render(); /*this.setCurrentQuestionListenTo(newQuestion);*/ });
			
			this.setCurrentQuestionListenTo(currQuestion);
			
			// TODO: remove this..
			model_factory.put('EnterNewChoiceViewState', new KeyValuePair);
			
			this.setStateOnInitialization();
			
			this.initializeBtnClickedTextProcessors();
			
			this.render();
		},
		setCurrentQuestionListenTo:function(currQuestion) {
			this.listenTo(currQuestion, 'resetQuestion', function(event) { this.setStateOnQuestionTypeChangedEvent(event); this.render(); });			
		},
		setReadOnly: function(bool) {
			this.readOnly = bool;
			this.render();
		},
		setCheckBoxDisabled: function(bool) {
			var state = model_factory.get('EnterNewChoiceViewState');
			
			if (bool)
//				state.val.checkBoxDisabled = "disabled";
				state.set('value', 'disabled');
			else
//				state.val.checkBoxDisabled = "";
				state.set('value', '');
		},
		setStateOnInitialization: function () {
			var currentQuestion = model_factory.get('currentQuestion');
			var type_id = currentQuestion.getTypeId();
			
			this.setCheckBoxDisabled(type_id == QUESTION_TYPE_PHRASE || type_id == QUESTION_TYPE_SEQUENCE || type_id == QUESTION_TYPE_SET);
			
			ReadOnlyManager.throwEvent(currentQuestion);
		},
		setStateOnQuestionTypeChangedEvent: function(event) { 
			if (event !== undefined && event.type_id !== undefined)
				this.setCheckBoxDisabled(event.type_id.to == QUESTION_TYPE_PHRASE || event.type_id.to == QUESTION_TYPE_SEQUENCE || event.type_id.to == QUESTION_TYPE_SET);
			else {
				var type_id = model_factory.get('currentQuestion').getTypeId();
				this.setCheckBoxDisabled(type_id == QUESTION_TYPE_PHRASE || type_id == QUESTION_TYPE_SEQUENCE || type_id == QUESTION_TYPE_SET);
			}
			
			var currentQuestion = model_factory.get("currentQuestion");
			ReadOnlyManager.throwEvent(currentQuestion);
		},
		render: function () {
			var state = model_factory.get('EnterNewChoiceViewState');
			var readOnlyAttr = this.readOnly == true ? "disabled" : "";
			
			var template = view_utility.executeTemplate('/templates/EnterNewChoiceView.html', {readOnly:readOnlyAttr ,disabled:(state.get('value'))});
			this.$el.html( template );
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('#idNewChoiceCorrectnessSlider');
			$slider.bootstrapSwitch();
			
			return this;
		},
		events: {
			"click button": "btnClicked",
			"keypress #enterAnswerTextField" : "updateOnEnter"
		},
		btnClicked: function (event) { 
			// take the text from the enter new choice item text field, and create choice(s) out of it
			var $textField = $('#enterAnswerTextField');
			var textFieldVal = $textField.val();
			
			if (textFieldVal == undefined || textFieldVal == '')
				return;
			
			var collSize = this.processors.size();
			var count = 0;
			var textProcessed = false;
			
			while (count < collSize && !textProcessed) {
				var func = this.processors.at(count).get('value');
				
				textProcessed = func(textFieldVal);
				
				count++;
			}
			
			$textField.val('');
		},
		updateOnEnter : function (event) {
			if (event.keyCode == 13) {
				this.btnClicked();
			}
		},
		initializeBtnClickedTextProcessors : function () {
			var coll = new Backbone.Collection([], {model: KeyValuePair});
			
			coll.add({key:"TrueSingleQuestionWithMultipleTokens", value:function(textToProcess) {
				// if the question is of type Single, and there are pipe dividers in the textToProcess, and
				//  the isCorrect box is set to true, then divide the text up into tokens, and set the first
				//  one to true, and the rest to false.
				
				var rtn = false;
				
				var cq = model_factory.get("currentQuestion");
				var isCorrectBoxValue = ($('#id_enterNewChoiceDiv > div.switch > div.switch-on').length > 0);
				var tokens = textToProcess.split('|');
				
				if (cq.getTypeId() === QUESTION_TYPE_SINGLE && isCorrectBoxValue === true && tokens.length > 1) {

					model_factory.get("currentQuestion").addChoice(tokens[0], IS_CORRECT, SEQUENCE_0);
					
					for (var i=1; i<tokens.length; i++) {
						this.ui_id = model_factory.get("currentQuestion").addChoice(tokens[i], NOT_CORRECT, SEQUENCE_0);
					}
					
					rtn = true;
				}
				
				return rtn;
			}});
			
			coll.add({key:"Sequence", value:function(textToProcess) {
				// if the question is of type Sequence and textToProcess is not null string then divide the text up into 1..n tokens
				// sequence numbers start at 1
				// set the next sequence number to the count of existing choices +1
				// add the new choices and their sequence number to the collection as correct
				// ignore the isCorrectBoxValue for Sequence questions
				
				var rtn = false;
				
				var cq = model_factory.get("currentQuestion");
				var tokens = textToProcess.split('|');
				var nextSequenceNumber = cq.get('choices').length + 1;
				var sequenceNumber;
				var token;
				
				if (cq.getTypeId() === QUESTION_TYPE_SEQUENCE && textToProcess != "" && tokens.length >= 1) {
					
					for (sequenceNumber=nextSequenceNumber, token=0; sequenceNumber < (nextSequenceNumber + tokens.length); sequenceNumber++, token++) {		
						this.ui_id = cq.addChoice(tokens[token], true, sequenceNumber.toString());
					}
					
					rtn = true;
				}
				
				return rtn;
			}});
			
			
			

			coll.add({key:"trueFalse", value:function(textToProcess) {
				
				// if the question is of type Single, and the isCorrect box is set to True.. then if the text
				//  is tf Create a True choice set to correct and a False choice set to not correct.
				//  if the text is ft,
				//  Create a False choice that is correct, and a True choice that is incorrect.
				
				var rtn = false;
				var cq = model_factory.get("currentQuestion");
				var isCorrectBoxValue = ($('#id_enterNewChoiceDiv > div.switch > div.switch-on').length > 0);
				
				if (cq.getTypeId() === QUESTION_TYPE_SINGLE && isCorrectBoxValue === true) {
					var textToProcess = textToProcess.toLowerCase();
	
					if (textToProcess === "tf") {
						cq.addChoice("True", IS_CORRECT, SEQUENCE_0);
						cq.addChoice("False", NOT_CORRECT, SEQUENCE_0);
						
						rtn = true;
					}
					else if (textToProcess === "ft") {
						cq.addChoice("True", NOT_CORRECT, SEQUENCE_0);
						cq.addChoice("False", IS_CORRECT, SEQUENCE_0);
						
						rtn = true;
					}
				}
				
				return rtn;
			}});
			
			coll.add({key:"default", value:function(textToProcess) {
				// split the text into tokens, and create choices for each, with the correctness set to the value in
				//  the isCorrect box.
				
				var tokens = textToProcess.split('|');

				for (var i=0; i<tokens.length; i++) {
					this.ui_id = model_factory.get("currentQuestion").addChoice(tokens[i], ($('#id_enterNewChoiceDiv > div.switch > div.switch-on').length > 0), SEQUENCE_0);
				}
				
				return true;
			}});
			
			this.processors = coll;
		}
	});
	