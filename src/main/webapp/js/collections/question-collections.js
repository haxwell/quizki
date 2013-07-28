
Quizki.Collection = Backbone.Collection.extend({
		initialize: function() {
			_.extend(this, Backbone.Events);
			
			if (arguments[0] !== undefined)	{
				this.modelKeysFunction = arguments[0].modelKeyFunction;
			}
		},
		put: function(model, throwEvent) {
			// Created this method put, because I couldn't find a way to override add(), so that I could
			//  trigger the 'somethingChanged' event when something was added.
			model.millisecond_id = new Date().getMilliseconds();
			
			var millis = new Date().getMilliseconds();
			
			if (this.modelKeysFunction !== undefined) {
				var nameOfComparisonKey = this.modelKeysFunction();
				
				if (nameOfComparisonKey.length > 0) {
					
					var v = _.filter(
								_.filter(this.models, 
										function(item) { 
											return item.attributes.val !== undefined && item.attributes.val[nameOfComparisonKey] !== undefined; 
										}
								)
								, function(item) { 
									return item.attributes.val[nameOfComparisonKey] == arr[i][nameOfComparisonKey];
								}
							);
					
					if (v.length == 0)
						this.add( [ {val:model, millisecond_id:millis} ] );
					}
				}
				else 
					this.add([{val:model, millisecond_id:millis}]);
			
			if (throwEvent !== false)
				this.trigger('somethingChanged');
			
			return model.millisecond_id;     
		},
		addArray: function(arr, isThrowEvent) {
			for (var i=0; i<arr.length; i++) {

				var millis = new Date().getMilliseconds();
				
				if (this.modelKeysFunction !== undefined) {
					var nameOfComparisonKey = this.modelKeysFunction();
					
					if (nameOfComparisonKey.length > 0) {
						
						var v = _.filter(
									_.filter(this.models, function(item) { 
										return item.attributes.val !== undefined && item.attributes.val[nameOfComparisonKey] !== undefined; 
									})
									, function(item) { 
								return item.attributes.val[nameOfComparisonKey] == arr[i][nameOfComparisonKey];
						});
						
						if (v.length == 0)
							this.add( [ {val:arr[i], millisecond_id:millis} ] );
						}
					}
					else 
						this.add([{val:arr[i], millisecond_id:millis}]);
			}
			
			if (isThrowEvent !== false)
				this.trigger('somethingChanged');	
		},
		getByMillisecondId: function(millis) {
			return _.filter(this, function (item) { return item.millisecond_id == millis; })[0];
		},
		update :function (millis, attr, value, throwEvent){
			var v = _.filter(this.models, function (item) {return item.attributes.millisecond_id == millis;	})[0];
			
			v.attributes.val[attr] = value;
			
//			var map = {};
//			map[attr] = value;
//			v.set(map);
			
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

