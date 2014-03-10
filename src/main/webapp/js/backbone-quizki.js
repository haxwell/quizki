	
var Quizki = {
	views : {},
	models: {}
};


// TODO: see how much of this can be replaced by _.pluck(list, propertyName)
KeyValueMap = function() {
	var arr = {};
	var count = 0;
	var dirty = false;
	
	return {
			put: function(id, obj) {
				arr[id] = obj;
				dirty = true;
			},
			get: function(id) {
				return arr[id];
			},
			destroy: function(id) {
				// TODO: eradicate this method.. remove() is a better name, consistent with Quizki.Collection
				delete arr[id];
				dirty = true;
			},
			remove: function(id) {
				this.destroy(id);
			},
			reset: function() {
				arr = {};
				count = 0;
			},
			size: function() {
				if (!dirty)
					return count;
				
				var cnt = 0;
				
				for (var property in arr)
					if (arr[property] !== undefined) cnt++;

				dirty = false;
				count = cnt;
				
				return cnt;
			},
			keys: function() {
				var rtn = {};
				
				for (var property in arr)
					rtn[property] = property;
				
				return rtn;
			},
			toString: function () {
				var str = 'KeyValueMap --> ';
				
				for (var property in arr) {
					str += property + ": ";
					
					if (arr[property] === undefined)
						str += "undefined";
					else
						str += arr[property].toString();
				}
				
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

// REMINDER: call initialize() in your JSP to make use of this object.
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
			var mID = Math.floor(Math.random() * 9999) + 1;
			
			return {val:model, millisecond_id:mID};
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
	
	my.startJSONArray = function(str) {
		return '{"'+str+'": ';
	};
	
	my.startElementInJSONArray = function(str) {
		return str + '[{'; // next call getJSON()
	};
	
	my.endElementInJSONArray = function(str) {
		return str + ']';
	};
	
	my.endJSONArray = function(str) {
		return this.endJSONString(str);
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
	
	my.getJSONForBackboneCollection = function(fieldName, backboneCollection, appendComma) {
		var rtn = '\"' + fieldName + '\":' + JSON.stringify(backboneCollection.toJSON());

		if (appendComma !== false)
			rtn += ', ';
		
		return rtn;
	};
	
	my.getJSON_ExistingQuoteFriendly = function(fieldName, valueToAdd, appendComma) {
		if (valueToAdd == undefined || valueToAdd == null || valueToAdd.length == 0)
			valueToAdd = '\"\"';
		
		var rtn = '\"' + fieldName + '\":' + valueToAdd;

		if (appendComma !== false)
			rtn += ', ';
		
		return rtn;
	};
	
	// Inspired by ExamEngine.. it keeps a list of questions as json strings, in the order they appear.
	//  this is to put those in an overall json string.
	//
	// TODO: Remove this, it doesn't seem to be used at present..
	my.getJSONForQuizkiCollection = function(name, quizkiCollectionOfJSONStrings, indexFunction) {
		if (indexFunction === undefined) 
			indexFunction = function(index) { return index; };
		
		var rtn = this.startJSONArray(name);
		
		rtn += '[';
		
		for (var x=0; x<quizkiCollectionOfJSONStrings.size(); x++) {
			var jsonstr = quizkiCollectionOfJSONStrings.at(indexFunction(x));
			
			rtn += jsonstr;
			
			if (x+1 < quizkiCollectionOfJSONStrings.size())
				rtn += ', ';
		}
		
		rtn += ']';
		
		rtn = this.endJSONArray(rtn);
		
		return rtn;
	};
	
	// TODO: Remove this.. it doesn't seem to be used at present...
	my.getJSONForAnArray = function(name, arrayOfJSONStrings, indexFunction) {
		if (indexFunction === undefined) 
			indexFunction = function(index) { return index; };
		
		var rtn = this.startJSONArray(name);
		
		rtn += '[';
		
		for (var x=0; x<quizkiCollectionOfJSONStrings.length; x++) {
			var jsonstr = quizkiCollectionOfJSONStrings.at(indexFunction(x));
			
			rtn += jsonstr;
			
			if (x+1 < quizkiCollectionOfJSONStrings.length)
				rtn += ', ';
		}
		
		rtn += ']';
		
		rtn = this.endJSONArray(rtn);
		
		return rtn;
	};
	
	my.getJSONForKeyValueMap = function(map, overallName, keyFieldName, valueFieldName, customFunc) {
		var rtn = this.startJSONArray(overallName);
		
		rtn += '[';
		
		var keys = map.keys();
		
		var count = 0;
		for (var key in keys) {
			var value = map.get(key);
			
			value = value.replace("'", "&quot;");
			
			rtn = this.startJSONString(rtn);
			rtn += '"' + keyFieldName + '": "' + key + '", ';
			rtn += '"' + valueFieldName + '": "' + value + '"';
			
			// added this so that I could add another attribute to the resulting map JSON, if for instance
			//  I needed to parse the map's compound key field to get a single id. For instance, the answers
			//  map used by takeExam has a compound key of XX,YY representing the question id and the choice
			//  on that question. But display question needs to be able to get all the answers for a given
			//  question, and I couldn't search the Backbone.Collection like "give me all XX* elements", the
			//  only way to do it was "give me all XX elements", so I put this in to add that XX element field.
			if (customFunc !== undefined) {
				var fn = customFunc.getFieldName();
				var va = customFunc.processKeyValue(key,value);
				
				rtn += ',"' + fn + '": "' + va + '"';
			}
			
			rtn = this.endJSONString(rtn);
			
			if (count + 1 < map.size()) {
				count++;
				rtn += ', ';
			}
		}
		
		rtn += ']';
		
		rtn = this.endJSONArray(rtn);
		
		return rtn;
	};
	
	return my;
	
}());

