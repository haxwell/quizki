	
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

			// a 'Quizki Object' is an object with an id based on the millisecond it was created,
			//  and a javascript object of any definition.
			
			return {val:model, millisecond_id:new Date().getMilliseconds()};
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

