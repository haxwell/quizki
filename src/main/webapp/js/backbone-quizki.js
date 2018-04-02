/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

	
var Quizki = {
	views : {},
	models: {}
};


// TODO: see how much of this can be replaced by _.pluck(list, propertyName) and the KeyValuePair model
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
			remove: function(id) {
				delete arr[id];
				dirty = true;
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
			put: function(id, model, throwEvent) {
				arr[id] = model;
				
				if (throwEvent !== false)
					event_intermediary.throwEvent(id+'::put::model_factory', model);
			},
			remove: function(id) {
				arr[id] = undefined;
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
		
		model_factory.put('event_intermediary', this, false);
	};
	
	my.throwEvent = function(event, data) {
			this.trigger(event, data);
	};
	
	return my;
	
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
	
	
	my.getJSONForArray = function(fieldName, array, appendComma) {
		var rtn = '"' + fieldName + '" : [';
		
		for (var v=0; v < array.length; v++) {
			rtn += '"' + array[v] + '"';
			
			if (v+1 < array.length)
				rtn += ',';
		}
		
		rtn += ']';
		
		if (appendComma !== false)
			rtn += ', ';
		
		return rtn;
	};
	
	my.getJSON = function(fieldName, valueToAdd, appendComma) {
		if (valueToAdd == undefined || valueToAdd == null)
			valueToAdd = '';
		else
			if ($.type(valueToAdd) === "string") {
				valueToAdd = valueToAdd.replace(/"/g, '\\\"');
				valueToAdd = valueToAdd.replace(/\n/g, "\\n");
			}
		
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
	my.getJSONForKeyValueMap = function(map, overallName, keyFieldName, valueFieldName, customFunc) {
		var rtn = this.startJSONArray(overallName);
		
		rtn += '[';
		
		var keys = map.keys();
		
		var count = 0;
		for (var key in keys) {
			var value = map.get(key);

			value = stringifyApostrophesAndQuotes(value);
			
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

