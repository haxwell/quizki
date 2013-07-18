	// this view represents an item in a list of choices
	Quizki.QuestionChoiceItemView = Backbone.View.extend({
		tagName:'li',
		
		className:'quizkiChoice',
		
		initialize: function() {
			this.model = arguments[0];
			
			this.model = {text:this.model.attributes.text,checked:(this.model.attributes.iscorrect ? 'checked' : '')};
		},
		render:function() {
			//this.$el.empty();
			
            var view = "QuestionChoiceItemView";
            var _model = this.model;
            
			var rtn = makeAJAXCall_andWaitForTheResults('/templates/' + view + '.html', { }, 
            		function(textTemplate) {
            			
        				// this is not returning a function???
        				Quizki[view].prototype.template = _.template(textTemplate, {text:_model.text,checked:_model.checked}, {});
        			}
            );

            var template = Quizki[view].prototype.template;
			this.$el.html( template );
			
			return this.$el.html();
		}
	});

	Quizki.ChoiceListView = Backbone.View.extend({
		initialize: function() {
			this.model = arguments[0].model;
			this.model.on('somethingAdded', this.render, this);
			
			this.$el = arguments[0].el;
		},
		renderElement:function (model) {
			var ul = this.$el.find("#listOfChoices");
			
			var questionChoiceItemView = new Quizki.QuestionChoiceItemView(model); //{checked:(model.iscorrect ? 'checked' : ''), text:model.text});
			ul.append( questionChoiceItemView.render() ); //.html()?
		},
		render:function() {
			//  this is returning a function??
			this.$el.html( _.template( "<ul class='choiceItemList' id='listOfChoices'></ul>" )() );
			
			_.each(this.model.models, function(model) { this.renderElement(model)}, this);
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();
			
			return this; //.html()?
		}
	});

	
	// This view, when clicked, alerts the QuestionChoice model,
	// and passes an object with which it can get choice text, and choice
	//  correctness. The collection adds that to itself. A view listens for
	//  that event, and draws each item in the collection.
	Quizki.EnterNewChoiceView = Backbone.View.extend({
		initialize: function() {
			this.choiceCollection = arguments[0].model;
			this.choiceModel = new Quizki.QuestionChoiceModel({ model:this.choiceCollection});
			
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
		btnClicked: function (event) { alert("submit button clicked!"); 
	
			    	// add a choice
			    	// tell the model, something got added.
					this.choiceModel.add({text:$('#enterAnswerTextField'),iscorrect:($('#id_enterNewChoiceDiv > div.switch > div > input').attr('checked') != undefined)});
			    	
			       	// the model then adds it to its list
			    	//  another view, displaying the list, listens to that list
			    	//  when the view gets notified it adds an item to the ui
			    	// dont do it here...
			    	
			    	// also each choice in the list is a model
			    	// and a view
			    	// so when the user clicks to remove a choice
			    	// the choice view tells its model, destroy yourself
			    	// list of choices is a model that listens to each choice in it
			    	// when listOfChoices notified one of its choices destroyed itself, 
			    	//   it removes that choice from teh list
			    	// the view which is watching the list updates itself
			    	
			    	// get the value from the text field
			    	//var textFieldVal = $("#enterAnswerTextField").val();
			    	
			    	//if (textFieldVal != undefined)
//			    		if (textFieldValue != "") {
			    	
				    	// get the slider, is it correct?
				    	//var isCorrect = getIsNewChoiceAnswerCheckboxSetToCorrect();
				    	
				    	//var choice = { text:textFieldVal,
				    	//				iscorrect:(isCorrect ? "true":"false") };
				    	
				    	// add the row to the ui
				    	//addChoiceToUI(getChoiceToAddToUI(choice));
				    	//$('#' + getMostRecentChoiceId())['bootstrapSwitch']();
		    		//}
		    	}
	});

	Quizki.QuestionChoiceCollectionView = Backbone.View.extend({
		defaults: { },
		initialize:function() {
			
		}
	});
	
	// this view represents a list of choices
	Quizki.ChoiceListView_OLD = Backbone.View.extend({
		tagName:'ul',
		
		className:'quizkiChoiceList',
		
		initialize: function() {
			// model is set as a parameter when doing a new on this view.
			// model is expected to be a QuestionChoiceCollection
			this.model = arguments[0].model;
			this.model.on('somethingAdded', this.render, this);
			
			this.$el = arguments[0].el;
		},
		render: function() {
			//this.$el.empty();
	        
//			this.$el.append("<ul>");
			
			_.each(this.model.models, function (choice) {
	            this.$el.append(new Quizki.QuestionChoiceItemView_OLD({model:choice}).render().el);
	        }, this);
			
//			this.$el.append("</ul>");
			
			var template = _.template( this.$el.html(), {model:this.model} );
			this.$el.html();
			
	        return this;
		}
	});
	