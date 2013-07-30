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

		},
		events: {
			"keypress #id_questionText":"updateText",
			"keypress #id_questionDescription":"updateDescription"
		},
		render: function() {
			var currentQuestion = model_factory.get("currentQuestion");
			
			this.$el.html( view_utility.executeTemplate('/templates/QuestionTextAndDescriptionView.html', {text:currentQuestion.text, description:currentQuestion.description}));
			
			return this;
		},
		updateText:function(event) {
			var v = $(event.target).val();
			
			// store v as a model 
		},
		updateDescription:function(event) {
			var v = $(event.target).val();
		}
	});

	Quizki.SaveButtonView = Backbone.View.extend({
		initialize:function() {
			this.render();
		},
		events: {
			"click #btnSaveAndAddAnother":"saveQuestion",
			"click #btnSave":"saveQuestion"
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/QuestionHeaderWithSaveButtons.html', {}));			
			return this;
		},
		saveQuestion: function() {
			alert("Save Question!");
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
			
			// saveNewEntries()
			var $elements = $('#textFieldDiv'+this.id+' > .editing');
			
			$elements.addClass('hideForEditing');
			$elements.removeClass('editing');
			
			var viewKey = model_factory.get( this.id + "ViewKey" );
			
			this.model = model_factory.get(	viewKey + "AttrWellCollection");
			
			var arr = $('#textFieldDiv'+this.id+' > input.edit').val().split(',');
			arr = method_utility.giveAttributeNamesToElementsOfAnArray("text",arr);
			
			this.model.addArray(arr, true);

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
				return item.attributes.val && entryText === item.attributes.val; 
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
			
			this.onIsCorrectChangedHandler = arguments[1];
		}, 
		render:function() {

			// This method is due for a refactor.. it is handling two different cases, updating an LI element, and creating an LI element,
			//  depending on if said element exists..
			
			var view = "QuestionChoiceItemView";
            var _model = this.model;

            // if this view already exists in the html, get it!
            var li = $("li#"+_model.id);
            
            // TODO: rewrite this to use jquery's wrap.. or something.. instead of two htmls..
            if (li.length > 0) {
            	this.$el = li;

            	Quizki[view].prototype.template = view_utility.executeTemplate('/templates/' + view + '-minus-LI-tag.html', {id:_model.id,text:_model.text,checked:_model.checked,sequence:_model.sequence});
            }
            else {
            	Quizki[view].prototype.template = view_utility.executeTemplate('/templates/' + view + '.html', {id:_model.id,text:_model.text,checked:_model.checked,sequence:_model.sequence});
            }

            var template = Quizki[view].prototype.template;
			this.$el.html( template );
			
			return this;
		},
		milliseconds: function() { return this.model.id; },
		setText: function(newText) { this.model.text = newText; },
		getText: function() { return this.model.text; },
		setIsCorrectChangedHandler: function(func) { this.onIsCorrectChangedHandler = func;},
		getIsCorrectChangedHandler: function() { return this.onIsCorrectChangedHandler;}
		
	});

	// this view represents the list of choices
	Quizki.ChoiceListView = Backbone.View.extend({
		tagName:'ul',
		
		initialize: function() {
			this.model = model_factory.get("questionChoiceCollection");

			// compare this to .listenTo().. which is better?
			this.model.on('somethingChanged', this.render, this);
			
			this.$el = arguments[0].el;
			
			this.ChoiceItemViewCollection = new Array();
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
			
			// can I just call close() now? Please?
			
			var $currentLineItem = this.$el.find(".editing");
			var currMillisecondId = $currentLineItem.attr("id");

			this.model = model_factory.get("questionChoiceCollection");
			this.model.update(currMillisecondId, 'text', $currentLineItem.find('.edit').val());
		},
		renderElement:function (model) {
			var ul = this.$el.find("#listOfChoices");
			
			// need to set a callback, which will get the appropriate model from questionChoiceCollection
			//  set the isCorrect attr on it. Should not redraw the list, thats already been done
			var isCorrectChangedCallbackFunc = function(event,data) {

				var millisecond_id = event.target.id.replace('switch','');
				
				this.model = model_factory.get("questionChoiceCollection");				
				this.model.update(millisecond_id, 'iscorrect', $(event.target).find('.switch-animate').hasClass('switch-on'), false);
			};
			
			var questionChoiceItemView = new Quizki.QuestionChoiceItemView(model, isCorrectChangedCallbackFunc);
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
			
			this.setCheckBoxDisabled(currQuestion.type_id == "2" || currQuestion.type_id == "3");
		},
		setStateOnQuestionChangedEvent: function(event) { 
			
			this.setCheckBoxDisabled(event.type_id.to == "2" || event.type_id.to == "3"); 
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
			
			var tokens = $textField.val().split(',');
			
			for (var i=0; i<tokens.length; i++) {
				// tell the model, user requested a choice be added.
				this.millisecond_id = this.choiceModel.put({text:tokens[i],iscorrect:($('#id_enterNewChoiceDiv > div.switch > div.switch-on').length > 0)});
			}

			$textField.val('');
		},
		updateOnEnter : function (event) {
			if (event.keyCode == 13) {
				// TODO: sure wish I could just call the btnClicked method...
				var $textField = $('#enterAnswerTextField');
				
				var tokens = $textField.val().split(',');
				
				for (var i=0; i<tokens.length; i++) {
					// tell the model, user requested a choice be added.
					this.millisecond_id = this.choiceModel.put({text:tokens[i],iscorrect:($('#id_enterNewChoiceDiv > div.switch > div.switch-on').length > 0)});
				}

				$textField.val('');
			}
		}
	});
	