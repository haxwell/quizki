
Quizki.Collection = Backbone.Collection.extend({
	
		initialize: function() {
			_.extend(this, Backbone.Events);
			
			this.isSkipDuplicates = false;
			
			if (arguments[1] !== undefined)
				this.isSkipDuplicates = !arguments[1].duplicatesAllowed;
		},
		put: function(model, throwEvent) {
			// Created this method put, because I couldn't find a way to override add(), so that I could
			//  trigger the 'somethingChanged' event when something was added.
			model.millisecond_id = new Date().getMilliseconds();
			
			// TODO: use a filter, check for models with the same id, and create a new id if necessessary.
			
			this.add(model);
			
			if (throwEvent !== false)
				this.trigger('somethingChanged');
			
			return model.millisecond_id;
		},
		addArray: function(arr, isSkipDuplicates, isThrowEvent) {
			var x = {};
			this.isSkipDuplicates = isSkipDuplicates || false;
			
			for (var i=0; i<arr.length; i++) {
				
				if (this.isSkipDuplicates) {
					if (x[arr[i]] == undefined) {
						this.add([{val:arr[i]+""}]); 
						x[arr[i]] = "";
					}
				}
				else 
					this.add([{val:arr[i]+""}]);
			}
			
			if (isThrowEvent !== false)
				this.trigger('somethingChanged');	
		},
		getByMillisecondId: function(millis) {
			return _.filter(this, function (item) { return item.millisecond_id == millis; })[0];
		},
		update :function (millis, attr, value, throwEvent){
			var v = _.filter(this.models, function (item) {return item.attributes.millisecond_id == millis;	})[0];
			
			var map = {};
			map[attr] = value;
			v.set(map);
			
			if (throwEvent !== false)
				this.trigger('somethingChanged'); 
		},
		remove: function(millis, throwEvent) {
			// new list where only those that do not match millis remain, all except the one we've been asked to remove
			this.models = _.reject(this.models, function(item) { return item.attributes.millisecond_id == millis; });
			
			if (throwEvent !== false)
				this.trigger('somethingChanged'); 
		},
		releasePentUpEvents : function() {
			this.trigger('somethingChanged');
		}
		
	});

