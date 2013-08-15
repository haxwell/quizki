var view_utility = (function() {
	return {
		executeTemplate : function(templateURL, templateParams) {
	
			var _stringModel = model_factory.getStringModel();
			
			makeAJAXCall_andWaitForTheResults(templateURL, { }, 
	        		function(textFromTheURL) {
	    				// TO UNDERSTAND: why does this return text rather than a function to be executed?
						_stringModel.stringModel = _.template(textFromTheURL, templateParams, { });
	    			}
	        );
			
			var rtn = _stringModel.stringModel;
			
			model_factory.destroy(_stringModel.id);
			
			return rtn;
		}	
	}; 
}());

	Quizki.QuestionTypeView = Backbone.View.extend({
		initialize:function() {
			this.render();
			
			var currQuestion = model_factory.get("currentQuestion");
			
			this.listenTo(currQuestion, 'questionTypeChanged', function(event) { this.render(); });
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
			
			if (_from != _to) {
				currentQuestion.setTypeId(_to);
			}
		},
		render: function() {
			this.$el.html( view_utility.executeTemplate('/templates/QuestionTypeView.html', {}));
			
			var currentQuestion = model_factory.get("currentQuestion");
			
			var optionId = currentQuestion.getTypeId();// || -1;
			
			// iterate over each of the buttons, if it matches the model, set it as active
			// otherwise remove the active attribute
			_.each($("#questionTypeSelectBox").find("option"), function($item) { 
				$item = $($item);
				($item.val() == optionId) ? $item.attr('selected','selected') : $item.removeAttr('selected'); 
			});
			
			// HACK.
			var v = this.$el.html();
			this.$el.html(v);
			
			return this;
		}
	});

	Quizki.QuestionTextAndDescriptionView = Backbone.View.extend({
		initialize:function() {
			this.render();

			var currQuestion = model_factory.get("currentQuestion");
			
			this.listenTo(currQuestion, 'questionTextChanged', function(event) { this.render(); });
		},
		events: {
			"keypress #id_questionText":"updateText",
			"blur #id_questionDescription":"updateDescription"
		},
		render: function() {
			var currentQuestion = model_factory.get("currentQuestion");
			
			this.$el.html( view_utility.executeTemplate('/templates/QuestionTextAndDescriptionView.html', {text:currentQuestion.getText(), description:currentQuestion.getDescription()}));
			
    		tinyMCE.init({
		        theme : "advanced",
		        mode : "textareas",
		        plugins : "autoresize",
				content_css : "../css/quizki_tinymce_custom_content.css",
				theme_advanced_font_sizes: "10px,12px,13px,14px,16px,18px,20px",
				font_size_style_values : "10px,12px,13px,14px,16px,18px,20px",
		        theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyfull,|,formatselect",
		        theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,|,undo,redo,|,image,|,hr,removeformat,visualaid,|,sub,sup,|,charmap",
		        theme_advanced_buttons3 : "",
				theme_advanced_path : false,
				theme_advanced_statusbar_location : 0,
				help_shortcut : "",
		        onchange_callback : "questionTinyMCEChangeHandler"						
			});
			
			return this;
		},
		updateText:function(event) {
			var currentQuestion = model_factory.get("currentQuestion");
			currentQuestion.setText($(event.target).val(), false);
		},
		updateDescription:function(event) {
			var currentQuestion = model_factory.get("currentQuestion");
			currentQuestion.setDescription($(event.target).val(), false);
		}
	});

	Quizki.SaveButtonView = Backbone.View.extend({
		// A flaw in this view is that it must know the names of the models used by the 
		// other views, in order to build data sent to the server for persistence.
		
		// I thought about a way of separating this dependency, and thats basically that
		// each view, upon creation register itself, and the name of its model. This would
		// be done within a context, and then this view could ask that object, give me all
		// the model names for this context.
		
		// I suppose each model would then have to know how to persist itself to the string
		// that will go into the AJAX data object, because though we have the names, we have
		// no idea whats in each model, nor how to get it out in order to associate it with
		// an attribute name.
		
		// But thats a lot of work...
		initialize:function() {
			this.prependages = arguments[0].prependages;
			
			this.render();
		},
		events: {
			//"click #btnSaveAndAddAnother":"saveQuestion",
			"click #btnSave":"saveQuestion"
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/QuestionHeaderWithSaveButtons.html', {}));			
			return this;
		},
		saveQuestion: function() {
			var data_url = "/ajax/question-save.jsp";
			var data_obj = model_factory.get("currentQuestion").getDataObject();
			
			var resetCurrentQuestion = this.resetCurrentQuestion;
			
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
					arr = parsedJSONObject.errors[0].val.split(',');
					alertClass = 'alert-error';
				} else {
					arr = parsedJSONObject.successes[0].val.split(',');
					alertClass = 'alert-success';
					resetCurrentQuestion();
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
			});
		},
		resetCurrentQuestion:function() {
			model_factory.get('currentQuestion').reset();
		}
	});

	Quizki.DifficultyChooserView = Backbone.View.extend({
		initialize:function() {
			// the id of the button to select will have been passed in
			// that id, is basically the model for this view
			// store the model
			this.buttonId = arguments[0].id;
			
			// the ui manages the state of this button group. all 
			// this view has to do, when the time comes is return
			// the value.. or perhaps another class somewhere else can
			// as long as the iteration is done on the render, (which 
			// sets the 'active' attribute) that should do it..
			
			this.render();
			
			var currQuestion = model_factory.get("currentQuestion");
			
			this.listenTo(currQuestion, 'difficultyChanged', function(event) { 
				var currQuestion = model_factory.get("currentQuestion");
				this.buttonId = currQuestion.getDifficultyId(); this.render(); 
				});
		},
		events : {
			"click button":"changed"
		},
		changed:function(event) {
			var currQuestion = model_factory.get("currentQuestion");
			
			var _from = currQuestion.getDifficultyId();
			var _to = event.target.value;
			
			if (_from != _to) {
				currQuestion.setDifficultyId(_to);
			}
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/QuestionDifficultyChooserView.html', {}));
			var buttonId = this.buttonId || -1;
			
			// iterate over each of the buttons, if it matches the model, set it as active
			// otherwise remove the active attribute
			_.each($("#difficultyBtnGroup").find("button"), function($item) { 
				$item = $($item);
				($item.val() == buttonId) ? $item.addClass('active') : $item.removeClass('active'); 
			});
			
			return this;
		}
	});
	
	Quizki.QuestionAttributeWellView = Backbone.View.extend({
		initialize:function() {
			this.id = new Date().getMilliseconds();
			var viewKey = arguments[0].viewKey;
			
			// TODO: remove on destroy
			model_constructor_factory.put(viewKey + "AttrWellCollection", function() { return new Quizki.Collection({modelKeyFunction:function() {return "text"; }}); });
			model_constructor_factory.put(this.id + "ViewKey",function() {return ("" + viewKey); }); 
			
			this.model = model_factory.get(	viewKey + "AttrWellCollection");
			
			this.listenTo(this.model, 'somethingChanged', this.render);
			
			this.modelToListenTo = arguments[0].modelToListenTo;
			this.modelEventToListenFor = arguments[0].modelEventToListenFor;
			
			if (this.modelToListenTo != undefined) {
				var modelToListenTo = model_factory.get(this.modelToListenTo);
				this.listenTo(modelToListenTo, this.modelEventToListenFor, function() { 
					var model = model_factory.get( this.getModelKey());
					model.reset();
				});
			}
			
			this.template = undefined;
		},
		events: {
			"click .well_add_button":"toggleNewEntryField",
			"click button.entryField":"saveNewEntries",
			"keypress .edit.well":"pressEnterToSaveNewEntries",
			"dblclick span.label":"removeEntry",				
		},
		getModelKey: function() {
			var viewKey = model_factory.get( this.id + "ViewKey" );
			
			return (viewKey + "AttrWellCollection");
		},
		toggleNewEntryField:function(event){
			var $elements = $('#textFieldDiv'+this.id+' > .entryField'); 
			
			if ($elements.length > 0) {
				
				if ($elements.is(':visible')) {
					$elements.hide();
				}
				else {
					$elements.slideDown("slow");
					$('#textFieldDiv'+this.id+' > input.entryField').focus();
				}
			} 
			else {
				$elements = $('#textFieldDiv'+this.id+' > .editing');
				$elements.hide();
			}
		},
		pressEnterToSaveNewEntries : function(event) {
			if (event.keyCode != 13) return;
			
			this.saveNewEntries(event);
		},
		saveNewEntries:function(model) {
			var $elements = $('#textFieldDiv'+this.id+' > .editing');
			
			$elements.addClass('hideForEditing');
			$elements.removeClass('editing');
			
			var viewKey = model_factory.get( this.id + "ViewKey" );
			
			this.model = model_factory.get(	viewKey + "AttrWellCollection");
			
			var arr = $('#textFieldDiv'+this.id+' > input.edit').val().split(',');
			arr = method_utility.giveAttributeNamesToElementsOfAnArray("text",arr);
			
			this.model.addArray(arr, true);
			
			this.modelToListenTo.setQuizkiCollection(model_factory.get( this.id + "ViewKey" ), this.model);
		},
		removeEntry:function(event) {
			var viewKey = model_factory.get( this.id + "ViewKey" );
			
			this.model = model_factory.get(	viewKey + "AttrWellCollection");
			
			var entryText = $(event.target).html();
			
			this.model.models = _.reject(this.model.models, function(item) { 
				return item.attributes.val !== undefined 
						&& item.attributes.val.text 
						&& entryText === item.attributes.val.text; 
				});
			
			this.render();
		},
		renderElement:function(model) {
			if (model.attributes.val != undefined) {
				var ul = this.$el.find("#wellItemList"+this.id);
				
				ul.append( view_utility.executeTemplate('/templates/QuestionAttributeWellItemView.html', {text:model.attributes.val.text}));
			}
		},
		render:function() {
			var _id = this.id;
			
			this.$el.html(view_utility.executeTemplate('/templates/QuestionAttributeWellView.html', {id:_id}));
			
			this.viewKey = model_factory.get(_id + "ViewKey");
			this.model = model_factory.get(	this.viewKey + "AttrWellCollection");
			
			_.each(this.model.models, function(model) { this.renderElement(model); }, this);
			
			var currHtml = this.$el.html();
			currHtml += view_utility.executeTemplate('/templates/AttributeWellViewInputField.html',{id:_id});
			
			this.$el.html(currHtml);
			
			return this;
		}
	});

	// this view represents an item in a list of choices
	Quizki.QuestionChoiceItemView = Backbone.View.extend({
		tagName:'li',
		
		initialize: function() {
			this.model = arguments[0].attributes;
			
			var text = this.model.val.text;
			
			// we have to check true in two different ways, because we have two different means of getting here.. the put from the button/enter press
			//  of the ***View, or the array of the initial question's choices.. the server in its ajax response is sending iscorrect as a string, 
			//  instead of a value. That should be cleaned up one day..
			var checked = (this.model.val.iscorrect == 'true' || this.model.val.iscorrect === true) ? 'checked' : '';
			var sequence = this.model.val.sequence || 0;
			var id = this.model.millisecond_id || new Date().getMilliseconds();
			
			this.model = {text:text,checked:checked,sequence:sequence,id:id};
			
			this.disableCheckboxes = arguments[1];
			
			this.onIsCorrectChangedHandler = arguments[2];
			this.onSequenceTextFieldBlurHandler = arguments[3];
		},
		getHideSequence:function() {
			var currQuestion = model_factory.get('currentQuestion');
			
			var hideSequence = "hidden";
			
			if (currQuestion.type_id == "4")
				hideSequence = "";

			return hideSequence;
		},
		getDisabledText: function () {
			if (this.disableCheckboxes === true)
				return "disabled";
			
			return "";
		},
		render:function() {
			var view = "QuestionChoiceItemView";
            var _model = this.model;
            var hideSequence = this.getHideSequence();
            var disabled = this.getDisabledText();
            
           	Quizki[view].prototype.template = view_utility.executeTemplate('/templates/' + view + '.html', {id:_model.id,text:_model.text,checked:_model.checked,sequence:_model.sequence,hideSequence:hideSequence,disabled:disabled});

            var template = Quizki[view].prototype.template;
			this.$el.html( template );
			
			return this;
		},
		milliseconds: function() { return this.model.id; },
		setText: function(newText) { this.model.text = newText; },
		getText: function() { return this.model.text; },
		setIsCorrectChangedHandler: function(func) { this.onIsCorrectChangedHandler = func;},
		getIsCorrectChangedHandler: function() { return this.onIsCorrectChangedHandler;},
		setSequenceTextFieldBlurHandler: function(func) { this.onSequenceTextFieldBlurHandler = func;},
		getSequenceTextFieldBlurHandler: function() { return this.onSequenceTextFieldBlurHandler;}
		
	});

	// this view represents the list of choices
	Quizki.ChoiceListView = Backbone.View.extend({
		tagName:'ul',
		
		initialize: function() {
			this.$el = arguments[0].el;
			
			this.ChoiceItemViewCollection = new Array();
			
			var currQuestion = model_factory.get('currentQuestion');
			this.listenTo(currQuestion, 'choicesChanged', function(event) { this.setStateOnQuestionTypeChangedEvent(event); this.render(); });
			this.listenTo(currQuestion, 'questionTypeChanged', function(event) { this.setStateOnQuestionTypeChangedEvent(event); this.render(); });
			
			this.setStateOnInitialization();
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
			return (typeId == "3" || typeId == "4");
		},
		setStateOnInitialization: function () {
			var typeId = model_factory.get('currentQuestion').getTypeId();
			this.setSequenceFieldsAreVisible(typeId == "3" || typeId == "4");
		},
		setStateOnQuestionTypeChangedEvent: function(event) {
			var rtn = undefined;

			if (event !== undefined && event.type_id !== undefined) {
				if (event.type_id.to == "3" || event.type_id.to == "4") {
					rtn = true;
				}
				else if (rtn == undefined && (event.type_id.from == "3" || event.type_id.from == "4")) {
					rtn = false;
				}
			}
			
			this.setSequenceFieldsAreVisible(rtn || false);
		},
		events: {
			"dblclick":"edit",
			"keypress .edit":"closeOnEnter",
			"blur .edit":"close",
			"click .destroyBtn":"remove"
		},
		edit : function(event) {
			var _el = this.$el.find("li:hover");
			
			_el.addClass('editing');
			
			_el.find('.edit').removeClass('hideForEditing');
			_el.find(".edit").focus();			
		},
		close : function(event) {
			var $currentLineItem = this.$el.find(".editing");
			var millisecond_id = $currentLineItem.attr("id");

			var currQuestion = model_factory.get("currentQuestion");
			currQuestion.updateChoice(millisecond_id, 'text', $currentLineItem.find('.edit').val());
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

				var millisecond_id = event.target.id.replace('switch','');
				var currQuestion = model_factory.get("currentQuestion");
				var v = !($(event.target).find("input.checkbox").attr('checked') == 'checked');
				currQuestion.updateChoice(millisecond_id, 'iscorrect', v, false);
			};

			var onSequenceTextFieldBlurFunc = function(event,data) {
				var millisecond_id = event.target.id.replace('sequenceTextField','');
				var currQuestion = model_factory.get("currentQuestion");
				currQuestion.updateChoice(millisecond_id, 'sequence', $(event.target).val(), false);
			};
			
			var questionChoiceItemView = new Quizki.QuestionChoiceItemView(model, this.choiceItemSwitchesShouldBeDisabled(), isCorrectChangedCallbackFunc, onSequenceTextFieldBlurFunc);
			ul.append( questionChoiceItemView.render().$el.html() );
			
			var obj = {millisecondId:questionChoiceItemView.milliseconds(), view:questionChoiceItemView};
			
			this.ChoiceItemViewCollection.push(obj);
		},
		render:function() {
			this.ChoiceItemViewCollection = new Array();
			
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<ul class='choiceItemList span6' id='listOfChoices'></ul>" )() );
			
			var choices = model_factory.get("currentQuestion").getChoices();
			
			_.each(choices.models, function(model) { this.renderElement(model); }, this);
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();

			// find the bootstrap switch div, add a change listener to it, when change happens, call the handler
			_.each(this.ChoiceItemViewCollection, function(model) {
				$("#switch" + model.millisecondId).on('switch-change', model.view.getIsCorrectChangedHandler());
			});
			
			_.each(this.ChoiceItemViewCollection, function(model) {
				$("#sequenceTextField" + model.millisecondId).on('blur', model.view.getSequenceTextFieldBlurHandler());
			});
			
			return this;
		},
		remove:function(event) {
			var _el = this.$el.find("li:hover");
			var currMillisecondId = _el.attr("id");
			
			model_factory.get("currentQuestion").removeChoice(currMillisecondId);
		}
		
	});

	
	// This view, when clicked, alerts the QuestionChoice model,
	// and passes an object with which it can get choice text, and choice
	//  correctness. The collection adds that to itself. ChoiceListView listens for
	//  that event, and draws each item in the collection.
	Quizki.EnterNewChoiceView = Backbone.View.extend({
		initialize: function() {
			this.$el = arguments[0].el;
			
			var currQuestion = model_factory.get('currentQuestion', false);
			this.listenTo(currQuestion, 'questionTypeChanged', function(event) { this.setStateOnQuestionTypeChangedEvent(event); this.render(); });
			
			var state = method_utility.getQuizkiObject({});
			
			// TODO: destroy this..
			model_factory.put('EnterNewChoiceViewState', state);
			
			this.setStateOnInitialization();
			
			this.render();
		},
		setCheckBoxDisabled: function(bool) {
			var state = model_factory.get('EnterNewChoiceViewState');
			
			if (bool)
				state.val.checkBoxDisabled = "disabled";
			else
				state.val.checkBoxDisabled = "";
		},
		setStateOnInitialization: function () {
			var currQuestion = model_factory.get('currentQuestion');
			
			this.setCheckBoxDisabled(currQuestion.type_id == "3" || currQuestion.type_id == "4");
		},
		setStateOnQuestionTypeChangedEvent: function(event) { 
			if (event !== undefined && event.type_id !== undefined)
				this.setCheckBoxDisabled(event.type_id.to == "3" || event.type_id.to == "4"); 
		},
		render: function () {
			var state = model_factory.get('EnterNewChoiceViewState');
			
			var template = view_utility.executeTemplate('/templates/EnterNewChoiceView.html', {disabled:(state.val.checkBoxDisabled)});
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
		btnClicked: function (event) { //alert("submit button clicked!"); 
			var $textField = $('#enterAnswerTextField');
			var textFieldVal = $textField.val();
			
			if (textFieldVal == undefined || textFieldVal == '')
				return;
			
			var tokens = textFieldVal.split(',');
			
			for (var i=0; i<tokens.length; i++) {
				this.millisecond_id = model_factory.get("currentQuestion").addChoice(tokens[i], ($('#id_enterNewChoiceDiv > div.switch > div.switch-on').length > 0), "0");
			}

			$textField.val('');
		},
		updateOnEnter : function (event) {
			if (event.keyCode == 13) {
				this.btnClicked();
			}
		}
	});
	