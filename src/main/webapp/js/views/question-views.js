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
	}}());

	Quizki.QuestionTypeView = Backbone.View.extend({
		initialize:function() {
			this.render();
			
			var currQuestion = model_factory.get("currentQuestion");
			
			this.listenTo(currQuestion, 'changed', function(event) { this.render(); });
		},
		events: {
			"change select":"changed"
		},
		changed:function(event) {
			// get the value from the html element
			var val = event.target.value;
			
			// set it in the model
			var currentQuestion = model_factory.get("currentQuestion");

			var _from = currentQuestion.type_id;
			var _to = val;
			
			if (_from != _to) {
				currentQuestion.type_id = _to;
			
				// either model should throw event, or we do it here.. methinks the model.. but don't see a way of doing that so..
				currentQuestion.trigger('changed', {type_id:{from:_from,to:_to}});
			}
		},
		render: function() {
			this.$el.html( view_utility.executeTemplate('/templates/QuestionTypeView.html', {}));
			
			var currentQuestion = model_factory.get("currentQuestion");
			
			var optionId = currentQuestion.type_id || -1;
			
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
			
			this.listenTo(currQuestion, 'changed', function(event) { this.render(); });
		},
		events: {
			"keypress #id_questionText":"updateText",
			"keypress #id_questionDescription":"updateDescription"
		},
		render: function() {
			var currentQuestion = model_factory.get("currentQuestion");
			
			this.$el.html( view_utility.executeTemplate('/templates/QuestionTextAndDescriptionView.html', {text:currentQuestion.text, description:currentQuestion.description}));
			
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
		        onchange_callback : "myCustomOnChangeHandler"						
			});
			
			return this;
		},
		updateText:function(event) {
			var currentQuestion = model_factory.get("currentQuestion");
			currentQuestion.text = $(event.target).val();
		},
		updateDescription:function(event) {
			var currentQuestion = model_factory.get("currentQuestion");
			currentQuestion.description = $(event.target).val();
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
			// TODO: I don't like this here.. the view shouldn't know how to create a question object.. it should just
			//  pass the data, and get a question object back
			var data_url = "/ajax/question-save.jsp";
			var currentQuestion = model_factory.get("currentQuestion");

			var data_obj = {
				id:currentQuestion.id,
				text:currentQuestion.text,
				description:currentQuestion.description,
				type_id:currentQuestion.type_id,
				difficulty_id:currentQuestion.difficulty_id,
				user_id:currentQuestion.user_id
			};
			
			for (var i=0;i<this.prependages.length;i++) {
				var coll = model_factory.get(this.prependages[i]+"AttrWellCollection");
				
				data_obj[this.prependages[i]] = method_utility.getCSVFromCollection(coll, "text");
			}

			var v = model_factory.get("questionChoiceCollection");
			
			// need to add choices to the data obj.
			var choicesAsJSONString = '{ "choice":[';
			
			for (var i=0;i<v.models.length;i++) {
				
				var attrs = "{";
				for (var property in v.models[i]["attributes"]["val"]) {
					attrs += '"' + property + '":' + '"' + v.models[i]["attributes"]["val"][property] + '",';
				}
				
				attrs += "}";
				
				if (i+1 < v.models.length)
					attrs += ",";
				
				choicesAsJSONString += attrs;
			}
			
			choicesAsJSONString += "]}";
			
			data_obj["choices"] = choicesAsJSONString;
			
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
			// TODO: There needs to be a Quizki.Question object, which has the necessary fields, and also has methods, specifically this method,
			//  to reset itself. Also Events, but not sure about that.. not sure there's anything really to do... that kind of works, now.. *shrug*
			var currQuestion = model_factory.get('currentQuestion');
			
			currQuestion.text = "";
			currQuestion.description = "";
			currQuestion.type_id = 1;
			currQuestion.difficulty_id = 1;
			currQuestion.topics = "";
			currQuestion.references = "";
			currQuestion.choices = {};
			
			currQuestion.trigger('reset');
			currQuestion.trigger('changed');
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
			
			this.listenTo(currQuestion, 'changed', function(event) { 
				var currQuestion = model_factory.get("currentQuestion");
				this.buttonId = currQuestion.difficulty_id; this.render(); 
				});
		},
		events : {
			"click button":"changed"
		},
		changed:function(event) {
			var currQuestion = model_factory.get("currentQuestion");
			
			var _from = currQuestion.difficulty_id;
			var _to = event.target.value;
			
			if (_from != _to) {
				currQuestion.difficulty_id = _to;
				
				currQuestion.trigger('changed', {difficulty_id:{from:_from,to:_to}});				
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
			
			if (arguments[0].modelToListenTo != undefined) {
				var modelToListenTo = model_factory.get(arguments[0].modelToListenTo);
				this.listenTo(modelToListenTo, arguments[0].modelEventToListenFor, function() { 
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
			this.model = model_factory.get("questionChoiceCollection");

			// compare this to .listenTo().. which is better?
			// this.model.on('somethingChanged', this.render, this);
			this.listenTo(this.model, 'somethingChanged', this.render);
			
			this.$el = arguments[0].el;
			
			this.ChoiceItemViewCollection = new Array();
			
			var currQuestion = model_factory.get('currentQuestion');
			this.listenTo(currQuestion, 'changed', function(event) { this.setStateOnQuestionChangedEvent(event); this.render(); });
			
			// TODO: perhaps for these views which need to reset their internal, unexposed models when the 
			//  current question resets, can be passed the model, its event name, and call to reset their
			//  internal model.. similar to how i did that with the attributeWellViews. its better than this..
			this.listenTo(currQuestion, 'reset', function(event) {
				var model = model_factory.get("questionChoiceCollection");
				model.reset();
			});
			
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
			var currQuestion = model_factory.get('currentQuestion');
			return (currQuestion.type_id == "3" || currQuestion.type_id == "4");
		},
		setStateOnInitialization: function () {
			var currQuestion = model_factory.get('currentQuestion');
			this.setSequenceFieldsAreVisible(currQuestion.type_id == "3" || currQuestion.type_id == "4");
		},
		setStateOnQuestionChangedEvent: function(event) {
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
			var currMillisecondId = $currentLineItem.attr("id");

			this.model = model_factory.get("questionChoiceCollection");

			this.model.update(currMillisecondId, 'text', $currentLineItem.find('.edit').val());
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
				
				this.model = model_factory.get("questionChoiceCollection");				
				this.model.update(millisecond_id, 'iscorrect', $(event.target).find('.switch-animate').hasClass('switch-on'), false);
			};

			var onSequenceTextFieldBlurFunc = function(event,data) {
				var millisecond_id = event.target.id.replace('sequenceTextField','');
				var choiceCollection = model_factory.get("questionChoiceCollection");
				choiceCollection.update(millisecond_id, 'sequence', $(event.target).val(), false);
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
			
			_.each(this.model.models, function(model) { this.renderElement(model); }, this);
			
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
			
			this.model = model_factory.get("questionChoiceCollection");
			this.model.remove(currMillisecondId);
		}
		
	});

	
	// This view, when clicked, alerts the QuestionChoice model,
	// and passes an object with which it can get choice text, and choice
	//  correctness. The collection adds that to itself. ChoiceListView listens for
	//  that event, and draws each item in the collection.
	Quizki.EnterNewChoiceView = Backbone.View.extend({
		initialize: function() {
			this.choiceModel = model_factory.get("questionChoiceCollection");
			
			this.$el = arguments[0].el;
			
			var currQuestion = model_factory.get('currentQuestion', false);
			this.listenTo(currQuestion, 'changed', function(event) { this.setStateOnQuestionChangedEvent(event); this.render(); });
			
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
		setStateOnQuestionChangedEvent: function(event) { 
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
				// tell the model, user requested a choice be added.
				var id = this.choiceModel.models.length + 1;
				
				this.millisecond_id = 
					this.choiceModel.put({id:-1,text:tokens[i],iscorrect:($('#id_enterNewChoiceDiv > div.switch > div.switch-on').length > 0),sequence:"0"});
			}

			$textField.val('');
		},
		updateOnEnter : function (event) {
			if (event.keyCode == 13) {
				this.btnClicked();
			}
		}
	});
	