//var Exam = (function() {
//	var my = {};
//	
//	var questions = undefined;
//	
//	function initializeFields() {
//		questions = Quizki.Collection();
//	};
//	
//	my.initialize = function () {
//		initializeFields();
//		
//		_.extend(this, Backbone.Events);
//	};
//});

// TODO: this should be moved to a common area.. question.js should not need to be included solely because, for instance, DifficultyChooserView is included.
var Difficulty = (function() {
	var my = {};
	
	var difficulty_id = 1;
	
	my.initialize = function() {
		difficulty_id = 1;
		
		_.extend(this, Backbone.Events);
		
		return this;
	};

	my.getDifficultyId = function () {
		return difficulty_id;
	};
		
	my.setDifficultyId = function(val, throwEvent) {
		var _from = difficulty_id;
		var _to = val;
		
		difficulty_id = val;
		
		var changesObject = {difficulty_id:{from:_from,to:_to}};
		
		if (throwEvent !== false)
			this.trigger('difficultyChanged', changesObject);	
		
		return changesObject;
	};

	my.toJSON = function() {
		var rtn = '';

		rtn += JSONUtility.startJSONString(rtn);

		rtn += JSONUtility.getJSON('difficulty_id', difficulty_id);
		
		rtn = JSONUtility.endJSONString(rtn);
		
		return rtn;		
	};
	
	my.getDataObject = function () {
		return  {
			difficulty_id:difficulty_id,
		};
	};
	
	return my;
});

var Question = (function() {
	var my = {};
	
	var id = -1;
	var user_id = -1;
	var user_name = "";
	var text = "";
	var description = "";
	var type_id = 1;
	var topics = "";
	var references = "";
	var choices = undefined; /* will be a Quizki.Collection */
	var hasBeenAnswered = false;
	
	var difficulty = new Difficulty();
	
	// TODO: verify that this is a private method.. if it is then the other places we need private methods (search for it) maybe can use this style
	function initializeFields() {
		id = -1; user_id = -1; user_name = '';
		text = ''; description = ''; type_id = 1; difficulty.initialize();
		topics = ''; references = ''; choices = new Quizki.Collection();
	};
	
	my.initialize = function() {
		initializeFields();
		
		_.extend(this, Backbone.Events);
	};
	
	my.initWithAJAXSource = function(source) {
		id = source.id; user_id = source.user_id; user_name = source.user_name;
		text = source.text;	description = source.description; type_id = source.type_id; 
		difficulty_id = source.difficulty_id; 
		
		topics = JSON.stringify(source.topics);
		references = JSON.stringify(source.references);
		
		choices = new Quizki.Collection();
		choices.addArray(source.choices);
		
		this.hasBeenAnswered = _.filter(choices.models, function(item) { return item !== undefined && item.attributes.val !== undefined && item.attributes.val.isselected !== undefined; }).length > 0;
		
		_.extend(this, Backbone.Events);
	};
	
	my.initWithJSONSource = function(source) {
		var obj = JSON.parse(source);
		
		this.initWithAJAXSource(obj);
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
	
	my.toJSON = function() {
		var rtn = '';

		rtn += JSONUtility.startJSONString(rtn);
		
		rtn += JSONUtility.getJSON('id', id+'');
		rtn += JSONUtility.getJSON('text', text);
		rtn += JSONUtility.getJSON('description', description);
		rtn += JSONUtility.getJSON('type_id', type_id+'');
		rtn += JSONUtility.getJSON('difficulty_id', difficulty.getDifficultyId()+'');
		rtn += JSONUtility.getJSON('user_id', user_id+'');
		rtn += JSONUtility.getJSON('user_name', user_name);
		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('topics', topics);
		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('references', references);
		rtn += JSONUtility.getJSON_ExistingQuoteFriendly('choices', this.getChoicesAsJSONString(), false);
		
		rtn = JSONUtility.endJSONString(rtn);
		
		return rtn;
	};
	
	my.getDataObject = function () {
		return  {
				id:id,
				text:text,
				description:description,
				type_id:type_id,
				difficulty_id:difficulty.getDifficultyId(),
				user_id:user_id,
				topics:method_utility.getCSVFromJSArray(topics, "text"),
				references:method_utility.getCSVFromJSArray(references, "text"),
				choices:this.getChoicesAsJSONString()
		};
	};
	
	my.getId = function() {
		return id;
	};
	
	my.getUserId = function() {
		return user_id;
	};
	
	my.getUserName = function() {
		return user_name;
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
		return difficulty.getDifficultyId();
	};

	my.setDifficultyId = function(val, throwEvent) {
		var changesObject = difficulty.setDifficultyId(val, false);
		
		if (throwEvent !== false)
			this.trigger('difficultyChanged', changesObject);			
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
		var choice = {id:-1,text:_text,iscorrect:_iscorrect,sequence:_sequence,isselected:false};
		var millisecond_id = choices.put(choice);

		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
		
		return millisecond_id;
	};
	
	my.getChoice = function(_millisecondId) {
		return choices.getByMillisecondId(_millisecondId).attributes.val;
	};
		
	my.updateChoice = function(_millisecondId, _attrToUpdate, _val, throwEvent) {
		choices.update(_millisecondId, _attrToUpdate, _val);
		
		if (_attrToUpdate == 'isselected')
			this.hasBeenAnswered = true;
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
	};
		
	my.removeChoice = function(_millisecondId, throwEvent) {
		choices.remove(_millisecondId);
		
		if (throwEvent !== false)
			this.trigger('choicesChanged', {choices:{val:""}});
	};
	
	my.hasBeenAnswered = function() {
		return this.hasBeenAnswered;
	};
		
	my.getChoicesAsJSONString = function() {
		var choicesAsJSONString = '[';
		
		for (var i=0; i < choices.models.length; i++) {
			
			// in order to check whether a comma should be appended in a loop below,
			//  we have to know is there another element.. so, since in doing a 
			//    for (var property in ... )
			//  there is no way to see of there is a next element, you can't do a plus one,
			//  then we create two maps to store the two pieces of info we care about,
			//  in a way that we can use to determine if a next element exists, solving
			//  our comma problem. We save those two values by int index, and use a normal
			//  for loop based on an int to get the info out the maps, and be the basis
			//  of the comma check. #dareISayElegant?
			
			var propertyNameMap = new KeyValueMap();
			var propertyValueMap = new KeyValueMap();
			var index = 0;
			
			for (var property in choices.models[i]["attributes"]["val"]) {
				propertyNameMap.put(index, property);
				propertyValueMap.put(index, choices.models[i]["attributes"]["val"][property]);
				
				index++;
			}
			
			var attrs = "{";
			var propertyValueMapSize = propertyValueMap.size();
			for (var ii = 0; ii < propertyValueMapSize; ii++) {
				attrs += '"' + propertyNameMap.get(ii) + '":' + '"' + propertyValueMap.get(ii) + '"';
				
				if (ii+1 < propertyValueMapSize)
					attrs += ',';
			}
			
			attrs += "}";
			
			if (i+1 < choices.models.length)
				attrs += ",";
			
			choicesAsJSONString += attrs;
		}
		
		choicesAsJSONString += "]";
		
		return choicesAsJSONString;
	};
	
	return my;
});

var QuestionTypes = (function() {
	var my = {};

	my.getString = function(intKey) {
		if (intKey == 1)
			return "Single";
		else if (intKey == 2)
			return "Multiple";
		else if (intKey == 3)
			return "String";
		else if (intKey == 4)
			return "Sequence";
		
		return "ERROR!!";
	};
	
	return my;
}());

//  since this is a declaration of a function, how do you say this function needs a function passed in, when 
//  all you are doing is passing the variable (representing the function's) name? for instance if I'm passing
//.  this function to another function, how does 
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

	var currentQuestionAsJson = $("#idCurrentQuestionAsJson").val();

	// if somebody has already set a question in our special JSON field, lets use it!
	if (currentQuestionAsJson != undefined && currentQuestionAsJson != "") {
		rtn.initWithJSONSource(currentQuestionAsJson);
		$("#idCurrentQuestionAsJson").val('');
	}
	else {
		// otherwise, we need to get the question ourselves.. check the special Entity ID field..
		var entityId = $("#idEntityIdField").val();
		
		if (entityId != undefined && entityId != "") {
	    	rtn = getSingleQuestionByEntityId(entityId);
		}
		else {
			rtn = getBlankQuestionFromServer();
		}
	}

	return rtn;
};

var getSingleQuestionByEntityId = function(entityId) {
	var data_url = "/ajax/getSingleQuestion.jsp";
	var data_obj = { entityIdFilter : entityId }; 

	var rtn = getQuestionByAJAXCall(data_url, data_obj);
	
	return rtn;
};

var getBlankQuestionFromServer = function() {
	var data_url = "/ajax/getBlankQuestion.jsp";
	var data_obj = { }; 

	var rtn = getQuestionByAJAXCall(data_url, data_obj); 
	
	return rtn;
};

var getQuestionByAJAXCall = function(data_url, data_obj) {
	var rtn = new Question();
	
	makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
		
		var index = data.indexOf("<!DOCTYPE");
		var jsonExport = data;
		
		if (index != -1) {
			jsonExport = data.substring(0, index);
		}
		
		var parsedJSONObject = jQuery.parseJSON(jsonExport);
		
		rtn.initWithAJAXSource(parsedJSONObject.question[0]);
	});
	
	return rtn;
};