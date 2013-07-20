
Quizki.QuestionChoiceCollection = Backbone.Collection.extend({
		model: Quizki.QuestionChoice,
		initialize: function() {
			_.extend(this, Backbone.Events);
		},
		put: function(model) {
			// Created this method put, because I couldn't find a way to override add(), so that I could
			//  trigger the 'somethingAdded' event when something was added.
			this.add(model);
			
			this.trigger('somethingAdded');
		},
		addFromJSON: function(choices) {
			for (var i=0; i<choices.length; i++) {
				this.add(choices[i]);
			}
			
			this.trigger('somethingAdded');	
		}
	});
	
