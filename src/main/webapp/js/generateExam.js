
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

			var cloneRtn = new Backbone.Collection(parsedJSONObject.topic, { model: Topic });
			
			var filterCollectionIds = {};

			if (filterCollection !== undefined)
				filterCollectionIds = filterCollection.pluck("id");

			var v = _.reject(cloneRtn.models, function(param) { return _.contains(filterCollectionIds, param.id); });
			
			rtn.reset(v);
		});
		
		return rtn;
	};
	
	return my;

}());
