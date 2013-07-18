
Quizki.QuestionChoiceCollection = Backbone.Collection.extend({
		model: Quizki.QuestionChoice,
		initialize: function() {
			_.extend(this, Backbone.Events);
			this.on('somethingAdded', function() { 
				//alert("Something was added to the choice collection"); 
				});
		},
		addFromJSON: function(choices) {
			for (var i=0; i<choices.length; i++) {
				this.add(choices[i]);
			}
		}
	});
	
