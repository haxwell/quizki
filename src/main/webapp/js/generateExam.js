// Gets a list of topics from the server, returned and filtered by 
//	user ID (set _filterByUserId to true) 
//	a text string and (_containsFilter)
//	a collection of topics (filterCollection)
//
// Sets the Backbone.Collection referenced by collection to the resulting list
var FilteredTopicListGetter = (function() {
	var my = {};
	
	my.get = function(_filterByUserId, _containsFilter, collection, filterCollection) {
		var data_obj = { filterByUserId:_filterByUserId, containsFilter:_containsFilter };
		var data_url = '/ajax/topic-getJSONListOf.jsp';

		var rtn = collection;
		
		makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
			var index = data.indexOf("<!DOCTYPE");
			var jsonExport = data;
			
			if (index != -1) {
				jsonExport = data.substring(0, index);
			}
			
			var parsedJSONObject = jQuery.parseJSON(jsonExport);

			var topicsRtndFromSvr = new Backbone.Collection(parsedJSONObject.topic, { model: Topic });
			
			var filterCollectionIds = {};

			if (filterCollection !== undefined)
				filterCollectionIds = filterCollection.pluck("id");

			// for each model returned from the server, reject it if it has the same id as an entry in the list of topics to be filtered out 
			var v = _.reject(topicsRtndFromSvr.models, function(param) { return _.contains(filterCollectionIds, param.id); });
			
			// do a mass update of the rtn collection, so it only contains the values not filtered out above
			rtn.reset(v);
		});
		
		return rtn;
	};
	
	return my;

}());

// Gets a collection of exam objects which represent exams which have at least one question belonging to at least one of the given topics.
//
// They are sorted by their relevance. An exam is considered more relevant if it has:
//
// 		a question which belongs to a single given topic
//		a question which belongs to multiple given topics
//		multiple questions which fit the above criteria
//		no questions which do not match the given topics
//
// Sets the Backbone.Collection referenced by collection to the resulting list
var MatchingExamsListGetter = (function() {
	var my = {};

	my.initialize = function() {
		_.extend(this, Backbone.Events);
	};
	
	my.listenFor = function(eventIntermediaryEventName, getReturnCollectionFunc, getTopicsFilterCollectionFunc, filterByUserIdFunc, mustContainAllTopicsFunc) {

		this.listenTo(event_intermediary, eventIntermediaryEventName, 
				function(event) { 
					this.get(
							(filterByUserIdFunc == undefined ? false : filterByUserIdFunc()), 
							(mustContainAllTopicsFunc == undefined ? false : mustContainAllTopicsFunc()), 
							getReturnCollectionFunc(),
							getTopicsFilterCollectionFunc()
					);
				});
	};
	
	my.get = function(_filterByUserId, _mustContainAllTopicsFilter, collection, _topicsFilterCollection) {
		var data_obj = { filterByUserId:_filterByUserId, mustContainAllTopicsFilter:_mustContainAllTopicsFilter, topics_json:JSON.stringify(_topicsFilterCollection.toJSON()) };
		var data_url = '/ajax/exam-getByTopics.jsp';

		var rtn = collection;
		
		makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
			var index = data.indexOf("<!DOCTYPE");
			var jsonExport = data;
			
			if (index != -1) {
				jsonExport = data.substring(0, index);
			}
			
			var parsedJSONObject = jQuery.parseJSON(jsonExport);
			var examsRtndFromSrvr = null;
			
			if (parsedJSONObject == null) {
				examsRtndFromSrvr = new Backbone.Collection();
			}
			else {
				examsRtndFromSrvr = new Backbone.Collection(parsedJSONObject.exam, { model: Exam });
			}
			
			rtn.reset(examsRtndFromSrvr.models);
		});
		
		event_intermediary.throwEvent("matchingListOfExamsChanged");
		
		return rtn;
	};
	
	return my;
}());
