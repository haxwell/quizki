
Quizki.QuestionChoiceCollection = Backbone.Collection.extend({
		model: Quizki.QuestionChoice,
		initialize: function() {
			_.extend(this, Backbone.Events);
		},
		put: function(model) {
			// Created this method put, because I couldn't find a way to override add(), so that I could
			//  trigger the 'somethingAdded' event when something was added.
			model.millisecond_id = new Date().getMilliseconds();
			
			this.add(model);
			
			this.trigger('somethingAdded');
			
			return model.millisecond_id;
		},
		addFromJSON: function(choices) {
			for (var i=0; i<choices.length; i++) {
				this.add(choices[i]);
			}
			
			this.trigger('somethingAdded');	
		},
		getByMillisecondId: function(millis) {
			return _.filter(this, function (item) { return item.millisecond_id == millis; })[0];
		},
		update :function (millis, attr, value){
			var v = _.filter(this.models, function (item) {return item.attributes.millisecond_id == millis;	})[0];
			
			var map = {};
			map[attr] = value;
			v.set(map);
			
			this.trigger('somethingAdded'); // should be renamed to somethingChanged.. or something
		}
	});
	
