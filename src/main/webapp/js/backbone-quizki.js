	
var Quizki = {
		views : {},
		models: {},
		
		loadTemplates: function(views, dataForTheViews, callback) {
	        var deferreds = [];

	        $.each(views, function(index, view) {
	            if (Quizki[view]) {
	                //var rtn = $.get(
	            	//		'/templates/' + view + '.html',
	            	//   	function(template) {
	            	//            				console.debug(">>> " + template);
	            	//          			}
	            	//		);
	                
	                var rtn = makeAJAXCall_andWaitForTheResults('/templates/' + view + '.html', { }, 
	                		function(template) {
	                			// how to pass in variables for variable-type-fields in the HTML?
	                			// perhaps here is not the best place to do this template thing..
	                			// variables would be nice.. (to test, try putting LABEL as a var in
	                			// the template HTML.. you can't pass a model value for it to _.template.. we're
	                			//  too early in the process.. none created!)
	                			
		        				// console.debug(">>> " + template);
		        				
		        				// I think it means that this is too early in the process, and the _.template() call
		        				//  needs to be made closer to the time the template is to be displayed.. specifically,
		        				//  when we have that information.
		        				Quizki[view].prototype.template = _.template(template, {}, {});
		        			}
	                );
	            	
	            	//deferreds.push(rtn);
	            } else {
	                console.debug(view + " not found");
	            }
	        });

	        //$.when.apply(null, deferreds).done(callback);
		}
	};


KeyValueMap = function() {
	var arr = {};
	
	return {
			put: function(id, func) {
				arr[id] = func;
			},
			get: function(id) {
				return arr[id];
			},
			destroy: function(id) {
				arr[id] = undefined;
			}
		};
	};

var model_constructor_factory = new KeyValueMap();

// thinking about a function which returns a singleton, unique for a given key,
//  and that singleton is used to get instances of models, etc.

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
			}
		};
	}());

var method_utility = (function(){
	
	return {
		wrap:function(template, elementName) {
			return '<' + elementName + '>' + template + '</' + elementName + '>';
		},
		getCSVFromCollection:function(coll, elementFieldName) {
			var rtn = "";
			
			for (var i=0;i<coll.models.length; i++) {
				var v = coll.models[i].attributes.val;
				
				if (v != undefined) {
					rtn += v[elementFieldName]+",";	
				}
			}
			
			return rtn;
		},
		giveAttributeNamesToElementsOfAnArray:function (attrName, arr) {
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
			return {val:model, millisecond_id:new Date().getMilliseconds()};
		}
	};
	}());

