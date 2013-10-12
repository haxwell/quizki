	
var Quizki = {
	views : {},
	models: {}
};


KeyValueMap = function() {
	var arr = {};
	var count = 0;
	
	return {
			put: function(id, obj) {
				arr[id] = obj;
				count++;
			},
			get: function(id) {
				return arr[id];
			},
			destroy: function(id) {
				arr[id] = undefined;
				count--;
			},
			size: function() {
				return count;
			},
			toString: function () {
				var str = 'KeyValueMap --> ';
				
				for (var property in arr)
					str += property + ": " + arr[property].toString();
				
				return str;
			}
		};
	};
	
var model_constructor_factory = new KeyValueMap();

var model_factory = (function(){
	var arr = {};

	return {
			get: function(id, attemptToCreateIfNonexistant) {
				if (arr[id] == undefined && attemptToCreateIfNonexistant !== false) {
					var func = model_constructor_factory.get(id);
					
					if (func != undefined)
						arr[id] = func();
				}
				
				return arr[id];
			},
			contains: function(id) {
				return arr[id] != undefined;
			},
			put: function(id, model) {
				// todo: perhaps an event when this happens?
				arr[id] = model;
			},
			getStringModel:function() {
				var id = new Date().getMilliseconds();
				
				arr["stringModel"+id] = "";
				
				return {id:id,stringModel:arr["stringModel"+id]};
			},
			destroy: function(id) {
				arr[id] = undefined;
				arr["stringModel"+id] = undefined;
			}
		};
	}());

// This was created because I needed an event thrown. The object which should have
// rightly been doing it, currentQuestion, cannot be depended on to not be replaced
// with another instance. (Ref the #idCurrentQuestionAsJson field to understand why)
// so, the next logically responsible object, the handler which caused the change in
// state in currentQuestion, was created in a renderElement() method; the objects 
// that need to hear my event thrown, won't know when this handler is created, and
// won't be able to attach to it. They don't know about it, as rightly they should not
// in a good OO design. So, this object is a third party. The handler can tell it,
// throw this event, and the 'won't know objects' can listen to this event_thrower
// for the third-party event.
var event_intermediary = (function(){
	var my = {};
	
	my.initialize = function() {
		_.extend(this, Backbone.Events);
	};
	
	my.throwEvent = function(event) {
			this.trigger(event);
	};
	
	return my;
	
}());

var method_utility = (function(){
	
	return {
		getCSVFromJSArray:function(arr, elementFieldName) {
			var rtn = "";
			
			for (var i=0;i<arr.length; i++) {
				var v = arr[i];
				
				if (v != undefined) {
					rtn += v[elementFieldName];
					
					if (i+1 < arr.length)
						rtn += ",";
				}
			}
			
			return rtn;
		},
		getCSVFromCollection:function(coll, elementFieldName) {
			var rtn = "";
			
			for (var i=0;i<coll.models.length; i++) {
				var v = coll.models[i].attributes.val;
				
				if (v != undefined) {
					rtn += v[elementFieldName];
					
					if (i+1 < coll.models.length)
						rtn += ",";
				}
			}
			
			return rtn;
		},
		giveAttributeNamesToElementsOfAnArray:function (attrName, arr) {
			// takes an array of values (think: csv), and an attribute name
			// that applies to each element of the array. it then creates
			// an object for each element, assigning the element to an
			// attribute of the object, and then the object to an array
			// which is returned.
			
			// this function is necessary, because some quizki collections
			// need to contain only unique items. in order to determine
			// what is unique, we need a way of comparing them. See 
			//  collection_utility.addUniqueModelToCollection(). It takes
			// a collection, an entity to be added to the collection, and
			// a function which returns the name of the attribute of the
			// entity by which it is to be compared, so as to determine
			// uniqueness.
			
			if (!arr) return null;
			
			var rtn = new Array();
			
			for (var i=0;i<arr.length;i++) {
				var obj = { };
				
				obj[attrName] = arr[i];
				rtn[i] = obj;
			}
			
			return rtn;
		},
		getQuizkiObject:function(model) {

			// a 'Quizki Object' is an object with a random id and a javascript 
			// object of any definition.
			var val = Math.floor(Math.random() * 9999) + 1;
			
			return {val:model, millisecond_id:val};
		}
	};
}());

var JSONUtility = (function() {
	var my = {};
	
	my.startJSONString = function(str) {
		return (str += '{ ');
	};
	
	my.endJSONString = function(str) {
		return (str += ' }');
	};
	
	my.getJSON = function(fieldName, valueToAdd, appendComma) {
		if (valueToAdd == undefined || valueToAdd == null)
			valueToAdd = '';
		else
			valueToAdd = valueToAdd.replace(/"/g, '\\\"');
		
		var rtn = '\"' + fieldName + '\":\"' + valueToAdd + '\"';
		
		if (appendComma !== false)
			rtn += ', ';
		
		return rtn;
	};
	
	my.getJSON_ExistingQuoteFriendly = function(fieldName, valueToAdd, appendComma) {
		if (valueToAdd == undefined || valueToAdd == null)
			valueToAdd = '';

		var rtn = '\"' + fieldName + '\":' + valueToAdd;

		if (appendComma !== false)
			rtn += ', ';
		
		return rtn;
	};
	
	return my;
	
}());

