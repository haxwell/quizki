	// this view represents an item in a list of choices
	Quizki.QuestionChoiceItemView = Backbone.View.extend({
		tagName:'li',
		
		//template: _.template(makeAJAXCall_andWaitForTheResults('/templates/QuestionChoiceItemView.html', { }, undefined)),
		
		initialize: function() {
			this.model = arguments[0];
			
			var text = this.model.attributes.text;
			var checked = ((this.model.attributes.iscorrect == 'true' || this.model.attributes.iscorrect === true) ? 'checked' : '');
			var sequence = this.model.attributes.sequence || 0;
			var id = this.model.attributes.millisecond_id || new Date().getMilliseconds();
			
			this.model = {text:text,checked:checked,sequence:sequence,id:id};
			
			this.onIsCorrectChangedHandler = arguments[1];
		}, 
		render:function() {

			// This method is due for a refactor.. it is handling two different cases, updating an LI element, and creating an LI element,
			//  depending on if said element exists..
			
			var view = "QuestionChoiceItemView";
            var _model = this.model;

            // if this view already exists in the html, get it!
            var foo = $("li#"+_model.id);
            
            if (foo.length > 0) {
            	this.$el = foo;

    			makeAJAXCall_andWaitForTheResults('/templates/' + view + '-minus-LI-tag.html', { }, 
                		function(textTemplate) {
                			
            				// TO UNDERSTAND: why does this return text rather than a function to be executed?
            				Quizki[view].prototype.template = _.template(textTemplate, {id:_model.id,text:_model.text,checked:_model.checked,sequence:_model.sequence}, {});
            			}
                );
            }
            else {
				makeAJAXCall_andWaitForTheResults('/templates/' + view + '.html', { }, 
	            		function(textTemplate) {
	            			
	        				// TO UNDERSTAND: why does this return text rather than a function to be executed?
	        				Quizki[view].prototype.template = _.template(textTemplate, {id:_model.id,text:_model.text,checked:_model.checked,sequence:_model.sequence}, {});
	        			}
	            );
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
			this.model = 
				model_factory.get(	"questionChoiceCollection", 
						function() { return new Quizki.QuestionChoiceCollection(); }
				);

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
			_el.find(".edit").focus();			
		},
		close : function(event) {
			var $currentLineItem = this.$el.find(".editing");
			var currMillisecondId = $currentLineItem.attr("id");

			this.model = 
				model_factory.get(	"questionChoiceCollection", 
						function() { return new Quizki.QuestionChoiceCollection(); }
				);

			this.model.update(currMillisecondId, 'text', $currentLineItem.find('.edit').val());
		},
		closeOnEnter : function(event) {
			if (event.keyCode != 13) return;
			
			var $currentLineItem = this.$el.find(".editing");
			var currMillisecondId = $currentLineItem.attr("id");

			this.model = 
				model_factory.get(	"questionChoiceCollection", 
						function() { return new Quizki.QuestionChoiceCollection(); }
				);

			this.model.update(currMillisecondId, 'text', $currentLineItem.find('.edit').val());
		},
		renderElement:function (model) {
			var ul = this.$el.find("#listOfChoices");
			
			// need to set a callback, which will get the appropriate model from questionChoiceCollection
			//  set the isCorrect attr on it. Should not redraw the list, thats already been done
			var isCorrectChangedCallbackFunc = function(event,data) {

				var millisecond_id = event.target.id.replace('switch','');
				
				this.model = 
					model_factory.get(	"questionChoiceCollection", 
							function() { return new Quizki.QuestionChoiceCollection(); }
					);
				
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
			
			_.each(this.model.models, function(model) { this.renderElement(model)}, this);
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();

			// find the bootstrap switch div, add a change listener to it, when change happens, call the handler
			_.each(this.ChoiceItemViewCollection, function(model) {
				$("#switch" + model.millisecondId).on('switch-change', model.view.getIsCorrectChangedHandler());
			})
			
			return this;
		},
		remove:function(event) {
			var _el = this.$el.find("li:hover");
			var currMillisecondId = _el.attr("id");
			
			this.model = 
				model_factory.get(	"questionChoiceCollection", 
						function() { return new Quizki.QuestionChoiceCollection(); }
				);

			this.model.remove(currMillisecondId);
		}
		
	});

	
	// This view, when clicked, alerts the QuestionChoice model,
	// and passes an object with which it can get choice text, and choice
	//  correctness. The collection adds that to itself. ChoiceListView listens for
	//  that event, and draws each item in the collection.
	Quizki.EnterNewChoiceView = Backbone.View.extend({
		initialize: function() {
			this.choiceModel = 
				model_factory.get(	"questionChoiceCollection", 
						function() { return new Quizki.QuestionChoiceCollection(); }
				);

			
			this.$el = arguments[0].el;
			
			this.render();
		},
		render: function () {
			var variables = { label: 'LABEL!!' };
			var template = _.template( Quizki.EnterNewChoiceView.prototype.template, variables );
			this.$el.html( template );
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('#idNewChoiceCorrectnessSlider');
			$slider.bootstrapSwitch();
		},
		events: {
			"click button": "btnClicked",
			"keypress #enterAnswerTextField" : "updateOnEnter"
		},
		btnClicked: function (event) { //alert("submit button clicked!"); 
			// tell the model, user requested a choice be added.
			this.choiceModel.put({text:$('#enterAnswerTextField').val(),iscorrect:($('#id_enterNewChoiceDiv > div.switch > div.switch-on').length > 0)});
		}
	});
	