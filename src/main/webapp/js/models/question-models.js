	Quizki.QuestionChoice = Backbone.Model.extend({
		defaults: {
			text:'',
			iscorrect:false
		},
		initialize:function() {

		}
	});

	// supposed to maintain a reference to a list of choices
	//  it gets notified when the user clicks the add choice btn
	//  --
	Quizki.QuestionChoiceModel = Backbone.Model.extend({
		initialize:function() {
			this.choiceCollection = 
				model_factory.get(	"questionChoiceCollection", 
						function() { return new Quizki.QuestionChoiceCollection(); }
				);

		},
		add:function(choiceData) {
			// add the choice to this.choiceCollection
			this.choiceCollection.add(choiceData);
			
			this.choiceCollection.trigger('somethingChanged');			
		},
		addFromJSON:function(json) {
			_.forEach(json, function(model) {
				this.choiceCollcetion.add(model);
			});
		},
		models:function () {
			return this.choiceCollection.models(); // or however Backbone.Collection returns all its elements
		}
	});
