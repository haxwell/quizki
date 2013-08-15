var Question = (function() {
	var my = {};
	
	var id = -1;
	var user_id = -1;
	var text = "";
	var description = "";
	var type_id = 1;
	var difficulty_id = 1;
	var topics = "";
	var references = "";
	var choices = undefined;
	
	function initializeFields() {
		id = -1; user_id = -1;
		text = ''; description = ''; type_id = 1; difficulty_id = 1;
		topics = ''; references = ''; choices = new Quizki.Collection();
	};
	
	my.initialize = function() {
		initializeFields();
		
		_.extend(this, Backbone.Events);
	};
	
	my.initWithAJAXSource = function(source) {
		id = source.id; user_id = source.user_id;
		text = source.text;	description = source.description; type_id = source.type_id; 
		difficulty_id = source.difficulty_id; 
		
		topics = method_utility.getCSVFromJSArray(source.topics, "text");
		references = method_utility.getCSVFromJSArray(source.references, "text"); 
		
		choices = new Quizki.Collection();
		choices.addArray(source.choices);
		
		_.extend(this, Backbone.Events);
	};
	
	my.reset = function() {
		initializeFields();
		
		this.trigger('reset');
	};
	
	my.setQuizkiCollection = function (key, quizkiCollection) {
		if (key == "topics")
			topics = method_utility.getCSVFromCollection(quizkiCollection, "text");
		
		if (key == "references")
			references = method_utility.getCSVFromCollection(quizkiCollection, "text");
	};
	
	my.getDataObject = function () {
		return  {
				id:id,
				text:text,
				description:description,
				type_id:type_id,
				difficulty_id:difficulty_id,
				user_id:user_id,
				topics:topics,
				references:references,
				choices:this.getChoicesAsJSONString()
		};
	};
	
	my.getText = function() {
		return text;
	};
	
	my.setText = function(val, throwEvent) {
		var _from = text;
		var _to = val;
		
		text = val;
		
		if (throwEvent !== false)
			this.trigger('questionTextChanged', {text:{from:_from,to:_to}});			
	};
	
	my.getDescription = function() {
		return description;
	};
	
	my.setDescription = function(val, throwEvent) {
		var _from = description;
		var _to = val;
		
		description = val;
		
		if (throwEvent !== false)			
			this.trigger('questionTextChanged', {description:{from:_from,to:_to}});			
	};

	my.getTypeId = function() {
		return type_id;
	};
		
	my.setTypeId = function(val, throwEvent) {
		var _from = type_id;
		var _to = val;
		
		type_id = val;
		
		if (throwEvent !== false)
			this.trigger('questionTypeChanged', {type_id:{from:_from,to:_to}});			
	};
		
	my.getDifficultyId = function () {
		return difficulty_id;
	};
		
	my.setDifficultyId = function(val, throwEvent) {
		var _from = difficulty_id;
		var _to = val;
		
		difficulty_id = val;
		
		if (throwEvent !== false)
			this.trigger('difficultyChanged', {difficulty_id:{from:_from,to:_to}});			
	};
	
	my.getTopics = function() {
		return topics;
	};
	
	my.setTopics = function(val, throwEvent) {
		var _from = topics;
		var _to = val;
		
		topics = val;
		
		if (throwEvent !== false)
			this.trigger('topicsChanged', {topics:{from:_from,to:_to}});			
	};
	
	my.getReferences = function() {
		return references;
	};
		
	my.setReferences = function(val, throwEvent) {
		var _from = references;
		var _to = val;
		
		references = val;
		
		if (throwEvent !== false)
			this.trigger('referencesChanged', {references:{from:_from,to:_to}});			
	};
		
	my.getChoices = function() {
		return choices;
	};
		
	my.addChoice = function(_text, _iscorrect, _sequence, throwEvent) {
		var choice = {id:-1,text:_text,iscorrect:_iscorrect,sequence:_sequence};
		var millisecond_id = choices.put(choice);

		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
		
		return millisecond_id;
	};
		
	my.updateChoice = function(_millisecondId, _attrToUpdate, _val, throwEvent) {
		choices.update(_millisecondId, _attrToUpdate, _val);
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
	};
		
	my.removeChoice = function(_millisecondId, throwEvent) {
		choices.remove(_millisecondId);
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
	};
		
	my.getChoicesAsJSONString = function() {
		var choicesAsJSONString = '{ "choice":[';
		
		for (var i=0; i < choices.models.length; i++) {
			
			var attrs = "{";
			for (var property in choices.models[i]["attributes"]["val"]) {
				attrs += '"' + property + '":' + '"' + choices.models[i]["attributes"]["val"][property] + '",';
			}
			
			attrs += "}";
			
			if (i+1 < choices.models.length)
				attrs += ",";
			
			choicesAsJSONString += attrs;
		}
		
		choicesAsJSONString += "]}";
		
		return choicesAsJSONString;
	};
	
	return my;
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
 	var rtn = new Question(); 
 	
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
			
			rtn.initWithAJAXSource(parsedJSONObject.question[0]);
		});
	}

	return rtn;
};
