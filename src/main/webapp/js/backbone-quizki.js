	
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



// thinking about a function which returns a singleton, unique for a given key,
//  and that singleton is used to get instances of models, etc.

var model_factory = (function(){
	var i = 0;
	var arr = {};
	
	return {
		get: function(id, constructionFunction) {
			if (arr[id] == undefined && constructionFunction != undefined) {
				console.log("the model_factory entry for " + id + " was undefined. Executing its constructor, and then saving the reference for next time.");
				arr[id] = constructionFunction();
			}
			
			return arr[id];
		},
		destroy: function(id) {
			arr[id] = undefined;
			console.log("the model_factory entry for " + id + " was eradicated,destroyed,vaquished. It no longer exists.");			
		}
		};
	}());

var method_utility = (function(){
	
	return {
		wrap:function(template, elementName) {
			return '<' + elementName + '>' + template + '</' + elementName + '>';
		},
		getNumericPortionOfString:function (str) {
			return str.match(/\d+/);
		}
	};
	}());

