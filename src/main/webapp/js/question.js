var Question = (function() {
	var text = "";
	var description = "";
	var type_id = 1;
	var difficulty_id = 1;
	var topics = "";
	var references = "";
	var choices = {};
	
	return {
		initialize : function() {
			this.initializeFields();
			
			_.extend(rtn, Backbone.Events);
		},
		initializeFields : function () {
			text = ''; description = ''; type_id = 1; difficulty_id = 1;
			topics = ''; references = ''; choices = {};
		}, 
		initWithAJAXSource : function(source) {
			text = source.text;	description = source.description; type_id = source.type_id; 
			difficulty_id = source.difficulty_id; topics = source.topics;
			references = source.references; choices = source.choices;
			
			_.extend(rtn, Backbone.Events);
		},
		reset : function() {
			this.initializeFields();
			
			trigger('reset');
			trigger('changed');
		},
		getText : function() {
			return text;
		},
		setText : function(val) {
			var _from = text;
			var _to = val;
			
			text = val;
			
			trigger('changed', {text:{from:_from,to:_to}});			
		},
		getDescription: function() {
			return description;
		},
		setDescription: function(val) {
			var _from = description;
			var _to = val;
			
			description = val;
			
			trigger('changed', {description:{from:_from,to:_to}});			
		},
		getTypeId : function() {
			return type_id;
		},
		setTypeId : function(val) {
			var _from = type_id;
			var _to = val;
			
			type_id = val;
			
			trigger('changed', {type_id:{from:_from,to:_to}});			
		},
		getDifficultyId : function () {
			return difficulty_id;
		},
		setDifficultyId : function(val) {
			var _from = difficulty_id;
			var _to = val;
			
			difficulty_id = val;
			
			trigger('changed', {difficulty_id:{from:_from,to:_to}});			
		},
		getTopics : function() {
			return topics;
		},
		setTopics : function(val) {
			var _from = topics;
			var _to = val;
			
			topics = val;
			
			trigger('changed', {topics:{from:_from,to:_to}});			
		},
		getReferences : function() {
			return references;
		},
		setReferences : function(val) {
			var _from = references;
			var _to = val;
			
			references = val;
			
			trigger('changed', {references:{from:_from,to:_to}});			
		},
		getChoices : function() {
			return choices;
		},
		setChoices : function(val) {
			var _from = choices;
			var _to = val;
			
			choices = val;
			
			trigger('changed', {choices:{from:_from,to:_to}});			
		}
	};
});

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

	var data_url = undefined;
	var data_obj = undefined;
	
	if (entityId != undefined && entityId != "") {
    	data_url = "/ajax/getSingleQuestion.jsp";
    	data_obj = { entityIdFilter : entityId }; 
    	
	}
	else {
		data_url = "/ajax/getBlankQuestion.jsp";
		data_obj = { };
	}
	
	if (data_url !== undefined && data_obj != undefined) {
		makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
			
			var index = data.indexOf("<!DOCTYPE");
			var jsonExport = data;
			
			if (index != -1) {
				jsonExport = data.substring(0, index);
			}
			
			var parsedJSONObject = jQuery.parseJSON(jsonExport);
			
			rtn = parsedJSONObject.question[0];
		});

		_.extend(rtn, Backbone.Events);
	}

	return rtn;
};
