	// this view represents an item in a list of choices
	Quizki.QuestionChoiceItemView = Backbone.View.extend({
		tagName:'li',
		
		className:'quizkiChoice',
		
		initialize: function() {
			this.model = arguments[0];
			
			var text = this.model.attributes.text;
			var checked = ((this.model.attributes.iscorrect == 'true' || this.model.attributes.iscorrect === true) ? 'checked' : '');
			var sequence = this.model.attributes.sequence || 0;
			
			this.model = {text:text,checked:checked,sequence:sequence};
		},
		render:function() {
            var view = "QuestionChoiceItemView";
            var _model = this.model;
            
			makeAJAXCall_andWaitForTheResults('/templates/' + view + '.html', { }, 
            		function(textTemplate) {
            			
        				// TO UNDERSTAND: why does this return text rather than a function to be executed?
        				Quizki[view].prototype.template = _.template(textTemplate, {text:_model.text,checked:_model.checked,sequence:_model.sequence}, {});
        			}
            );

            var template = Quizki[view].prototype.template;
			this.$el.html( template );
			
			return this.$el.html();
		}
	});

	// this view represents the list of choices
	Quizki.ChoiceListView = Backbone.View.extend({
		initialize: function() {
			this.model = 
				model_factory.get(	"questionChoiceCollection", 
						function() { return new Quizki.QuestionChoiceCollection(); }
				);

			this.model.on('somethingAdded', this.render, this);
			
			this.$el = arguments[0].el;
		},
		renderElement:function (model) {
			var ul = this.$el.find("#listOfChoices");
			
			var questionChoiceItemView = new Quizki.QuestionChoiceItemView(model);
			ul.append( questionChoiceItemView.render() );
		},
		render:function() {
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<table class='choiceItemList span5' id='listOfChoices'></table>" )() );
			
			_.each(this.model.models, function(model) { this.renderElement(model)}, this);
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();
			
			return this;
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
	