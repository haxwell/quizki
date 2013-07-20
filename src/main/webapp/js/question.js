// TODO: rather than using the selector for entityId, it would be good to be able to pass in a function. But
//  since this is a declaration of a function, how do you say this function needs a function passed in, when 
//  all you are doing is passing the variable (representing the function's) name? for instance if I'm passing
//.  this functioin to another function, how does 
//
//		foo(getFunctionToRetrieveCurrentQuestion)
//
//	indicate to foo, it should pass a function as the parameter?
// 
// I don't know, but I know it will come along eventually.. so when it does.. address this note..
//
// UPDATE: perhaps don't use a pointer to a function like this, but an actual function declaration? But still,
//  when passing the declaration itself, how do you say function required.. selector required. Do you? or does
//  the calling method just need to know?

//
// Assumptions: Assumes there is a field defined matching $("#idEntityIdField").. It should contain the id of
//  the entity, likely passed in as a URL parameter.
//
var getFunctionToRetrieveCurrentQuestion = function() {
 	var rtn = { }; 
 	
 	// this method executes if the entity id hidden field is set
	var entityId = $("#idEntityIdField").val();
	
	if (entityId != undefined && entityId != "") {
    	var data_url = "/ajax/getSingleQuestion.jsp";
    	var data_obj = { entityIdFilter : entityId }; 
    	
    	makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
		
			var index = data.indexOf("<!DOCTYPE");
			var jsonExport = data;
			
			if (index != -1) {
				jsonExport = data.substring(0, index);
			}
			
			var parsedJSONObject = jQuery.parseJSON(jsonExport);
			
			rtn = parsedJSONObject.question[0];
		});
	}

	_.extend(rtn, Backbone.Events);
	return rtn;
	}
