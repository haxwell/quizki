	Quizki.QuestionTypeView = Backbone.View.extend({
		initialize:function() {
			this.readOnly = arguments[0].readOnly;
			
			this.render();
			
			var currQuestion = model_factory.get("currentQuestion");
			
			this.listenTo(currQuestion, 'questionTypeChanged', function(event) { this.render(); });
			this.listenTo(currQuestion, 'resetQuestion', function(event) { this.render(); });			
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
			var currentQuestion = model_factory.get("currentQuestion");
			var optionId = currentQuestion.getTypeId();// || -1;
			
			if (this.readOnly == undefined) {
				this.$el.html( view_utility.executeTemplate('/templates/QuestionTypeView.html', {}));
				
				// iterate over each of the buttons, if it matches the model, set it as active
				// otherwise remove the active attribute
				_.each($("#questionTypeSelectBox").find("option"), function($item) { 
					$item = $($item);
					($item.val() == optionId) ? $item.attr('selected','selected') : $item.removeAttr('selected'); 
				});
			}
			else {
				var str = QuestionTypes.getString(optionId);
				this.$el.html( view_utility.executeTemplate('/templates/QuestionTypeView-readOnly.html', {type:str}));
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
			
			this.render();
		},
		events: {
			"click #btnSave":"saveQuestion"
		},
		render:function() {
			var classAttributeHidden = this.readOnly == undefined ? "" : "hidden";
			
			this.$el.html(view_utility.executeTemplate('/templates/QuestionHeaderWithSaveButtons.html', {hidden:classAttributeHidden}));			
			return this;
		},
		saveQuestion: function() {
			var data_url = "/ajax/question-save.jsp";
			var data_obj = model_factory.get("currentQuestion").getDataObject();
			
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
					
					model_factory.get('currentQuestion').resetQuestion();
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
			this.readOnly = arguments[0].readOnly;
			
			this.id = Math.floor(Math.random() * 9999) + 1;
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
			
			var modelToListenTo = model_factory.get(this.modelToListenTo);
			modelToListenTo.setQuizkiCollection(model_factory.get( this.id + "ViewKey" ), this.model);
		},
		removeEntry:function(event) {
			if (this.readOnly == undefined) {
				var viewKey = model_factory.get( this.id + "ViewKey" );
				
				this.model = model_factory.get(	viewKey + "AttrWellCollection");
				
				var entryText = $(event.target).html();
				
				this.model.models = _.reject(this.model.models, function(item) { 
					return item.attributes.val !== undefined 
							&& item.attributes.val.text 
							&& entryText === item.attributes.val.text; 
					});
				
				this.render();
			}
		},
		renderElement:function(model) {
			if (model.attributes.val != undefined) {
				var ul = this.$el.find("#wellItemList"+this.id);
				
				ul.append( view_utility.executeTemplate('/templates/QuestionAttributeWellItemView.html', {text:model.attributes.val.text}));
			}
		},
		render:function() {
			var readOnlyAttr = this.readOnly == undefined ? "" : "readOnly";
			var _id = this.id;
			
			this.$el.html(view_utility.executeTemplate('/templates/QuestionAttributeWellView.html', {id:_id, readOnly:readOnlyAttr}));
			
			this.viewKey = model_factory.get(_id + "ViewKey");
			this.model = model_factory.get(	this.viewKey + "AttrWellCollection");
			
			_.each(this.model.models, function(model) { this.renderElement(model); }, this);
			
			var currHtml = this.$el.html();
			currHtml += view_utility.executeTemplate('/templates/AttributeWellViewInputField.html',{id:_id});
			
			this.$el.html(currHtml);
			
			return this;
		}
	});

	Quizki.ChosenChoicesQuestionChoiceItemView = Backbone.View.extend({
		tagName:'li',
		
		initialize:function() {
			this.model = arguments[0];
			
			var text = this.model.get('text');
			
			// we have to check true in two different ways, because we have two different means of getting here.. the put from the button/enter press
			//  of the ***View, or the array of the initial question's choices.. the server in its ajax response is sending iscorrect as a string, 
			//  instead of a value. That should be cleaned up one day..
			var checked = (this.model.get('iscorrect') == 'true' || this.model.get('iscorrect') === true) ? 'checked' : '';
			var sequence = this.model.get('sequence') || 0;
			
			// TODO: now that Choice is a backbone model, and we're using backbone as we should.. is millisecond_id still necessary?
			var millisecond_id = this.model.get('id') || new Date().getMilliseconds();
			var id = this.model.get('id');
			
			this.viewmodel = {text:text,checked:checked,sequence:sequence,id:id,millisecond_id:millisecond_id};
		},
		getChoiceCorrectlyChosenStatus : function(o, cq) {
			var rtn = -1;

			var _viewmodel = this.viewmodel;
			
        	if (o != undefined && _viewmodel.checked == 'checked' && o.get('value') == (cq.getTypeId() == "4" ? _viewmodel.sequence : _viewmodel.text)) {
        		// this choice was correct, and you chose it.
        		rtn = 1;
        	} else if (o == undefined && _viewmodel.checked == 'checked') {
        		// this choice was correct, and you did not choose it.
        		rtn = 2;
        	} else if (o != undefined && _viewmodel.checked !== 'checked') {
        		// this choice was chosen, but is incorrect.
        		rtn = 3;
            } else if (cq.getTypeId() == "4" && o != undefined && _viewmodel.checked == 'checked' && o.get('value') !== _viewmodel.sequence) {
            	rtn = 4;
            } else if (cq.getTypeId() == "3" && o != undefined && _viewmodel.checked == 'checked' && o.get('value') != _viewmodel.text) {
            	rtn = 5;
            }

			return rtn;
		},
		setHideSwitchAndSequence:function() {
			var currQuestion = model_factory.get('currentQuestion');
			
			this.hideSequence = "hidden";
			this.hideSwitch = "";
			
			if (currQuestion.getTypeId() == "4") {
				this.hideSequence = "";
				this.hideSwitch = "hidden";
			}
		},
		render:function() {
			var _model = this.model,
    			cq = model_factory.get('currentQuestion'),
				mostRecentExamAnswers = model_factory.get("answersToTheMostRecentExam"),
        		o = mostRecentExamAnswers.findWhere({fieldId:cq.getId()+','+_model.id}),
				status = this.getChoiceCorrectlyChosenStatus(o, cq),
            	choiceCorrectStatusClass = undefined,
            	answerCorrectnessModel = model_factory.get("answerCorrectnessModel");

			this.setHideSwitchAndSequence();

			_model.comment = '';
			
			// TODO: make answerCorrectnessModel an object with methods to handle setting the state, rather than setting
			//  the individual elements here..
			
            if (status == 1) {
            	choiceCorrectStatusClass = 'correctAndChosen';
            	answerCorrectnessModel.correctAndChosen++;
            } else if (status == 2) {
            	choiceCorrectStatusClass = 'correctButNotChosen';
            	answerCorrectnessModel.correctButNotChosen++;
            	answerCorrectnessModel.overallAnsweredCorrectly = false;
            } else if (status == 3) {
            	choiceCorrectStatusClass = 'incorrectAndChosen';
            	answerCorrectnessModel.incorrectAndChosen++;
            	answerCorrectnessModel.overallAnsweredCorrectly = false;
            } else if (status == 4 || status == 5) {
            	choiceCorrectStatusClass = 'incorrectAndChosen';
            	_model.comment = ' (You typed: ' + o.get('value') + ')';
            	answerCorrectnessModel.incorrectAndChosen++;
            	answerCorrectnessModel.overallAnsweredCorrectly = false;
            }// else if (status == 5) {
            	// string
            //}
            
            answerCorrectnessModel.totalChoicesCount++;
			
            var template = view_utility.executeTemplate('/templates/ChosenChoicesQuestionChoiceItemView.html', {milli_id:_model.get('id'),text:_model.get('text'),comment:_model.comment,checked:_model.get('checked'),sequence:_model.get('sequence'),hideSequence:this.hideSequence,hideSwitch:this.hideSwitch,choiceCorrectStatusClass:choiceCorrectStatusClass});
            
			this.$el.html( template );
			
            return this;
		},
		milliseconds: function() { return this.viewmodel.id; },
		setText: function(newText) { ; },
		getText: function() { return this.viewmodel.text; },
	});
	
	// this view represents an item in a list of choices
	Quizki.QuestionChoiceItemView = Backbone.View.extend({
		tagName:'li',
		
		initialize: function() {
			this.model = arguments[0];
			
			var text = this.model.get('text');
			
			// we have to check true in two different ways, because we have two different means of getting here.. the put from the button/enter press
			//  of the ***View, or the array of the initial question's choices.. the server in its ajax response is sending iscorrect as a string, 
			//  instead of a value. That should be cleaned up one day..
			var checked = (this.model.get('iscorrect') == 'true' || this.model.get('iscorrect') === true) ? 'checked' : '';
			var sequence = this.model.get('sequence') || 0;
			var millisecond_id = this.model.get('id') || new Date().getMilliseconds();
			var id = this.model.get('id');
			
			this.viewmodel = {text:text,checked:checked,sequence:sequence,id:id,millisecond_id:millisecond_id};
			
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
            var _viewmodel = this.viewmodel,
            	hideSequence = this.getHideSequence(),
            	disabled = this.getDisabledText(),
            	readOnlyAttr = this.readOnly == undefined ? "" : "readOnly";
            
            var template = view_utility.executeTemplate('/templates/QuestionChoiceItemView.html', {milli_id:_viewmodel.id,text:_viewmodel.text,checked:_viewmodel.checked,sequence:_viewmodel.sequence,hideSequence:hideSequence,disabled:disabled,readOnly:readOnlyAttr});
			this.$el.html( template );
			
			return this;
		},
		milliseconds: function() { return this.viewmodel.id; },
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
			this.listenTo(currQuestion, 'resetQuestion', function(event) { this.setStateOnQuestionTypeChangedEvent(event); this.render(); });
			this.listenTo(currQuestion, 'choicesChanged', function(event) { /*this.setStateOnQuestionTypeChangedEvent(event);*/ this.render(); });
			this.listenTo(currQuestion, 'questionTypeChanged', function(event) { this.setStateOnQuestionTypeChangedEvent(event); this.render(); });
			
			this.setStateOnInitialization();
			
			this.render();
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
			return (typeId == "3" || typeId == "4") || this.readOnly !== undefined;
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
			if (this.readOnly == undefined) {
				var _el = this.$el.find("li:hover");
				
				_el.addClass('editing');
				
				_el.find('.edit').removeClass('hideForEditing');
				_el.find(".edit").focus();
			}
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
				var v = $(event.target).find("div.switch-animate").hasClass('switch-on');
				
				currQuestion.updateChoice(millisecond_id, 'iscorrect', v+'', false);
			};

			var onSequenceTextFieldBlurFunc = function(event,data) {
				var millisecond_id = event.target.id.replace('sequenceTextField','');
				var currQuestion = model_factory.get("currentQuestion");
				currQuestion.updateChoice(millisecond_id, 'sequence', $(event.target).val()+'', false);
			};
			
			var questionChoiceItemView = undefined;
			
			if (this.inExamContext) {
				questionChoiceItemView = new Quizki.ChosenChoicesQuestionChoiceItemView(model);
			}
			else {
				questionChoiceItemView = new Quizki.QuestionChoiceItemView(model, this.choiceItemSwitchesShouldBeDisabled(), isCorrectChangedCallbackFunc, onSequenceTextFieldBlurFunc, this.readOnly);
			}
			
			ul.append( questionChoiceItemView.render().$el.html() );
			
			var obj = {millisecondId:questionChoiceItemView.milliseconds(), view:questionChoiceItemView};
			
			this.ChoiceItemViewCollection.push(obj);
		},
		render:function() {
			this.ChoiceItemViewCollection = new Array();
			model_factory.destroy("answerCorrectnessModel");
			
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<ul class='choiceItemList span6' id='listOfChoices'></ul>" )() );
			
			var choices = model_factory.get("currentQuestion").getChoices();
			
			_.each(choices.models, function(model) { this.renderElement(model); }, this);
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();

			if (!this.inExamContext) {
				// find the bootstrap switch div, add a change listener to it, when change happens, call the handler
				_.each(this.ChoiceItemViewCollection, function(model) {
					$("#switch" + model.millisecondId).on('switch-change', model.view.getIsCorrectChangedHandler());
				});
				
				_.each(this.ChoiceItemViewCollection, function(model) {
					$("#sequenceTextField" + model.millisecondId).on('blur', model.view.getSequenceTextFieldBlurHandler());
				});
			}
			
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
			this.listenTo(currQuestion, 'resetQuestion', function(event) { this.setStateOnQuestionTypeChangedEvent(event); this.render(); });
			
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
			var type_id = model_factory.get('currentQuestion').getTypeId();
			
			this.setCheckBoxDisabled(type_id == "3" || type_id == "4");
		},
		setStateOnQuestionTypeChangedEvent: function(event) { 
			if (event !== undefined && event.type_id !== undefined)
				this.setCheckBoxDisabled(event.type_id.to == "3" || event.type_id.to == "4");
			else {
				var typeId = model_factory.get('currentQuestion').getTypeId();
				this.setCheckBoxDisabled(typeId == "3" || typeId == "4");
			}
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
	