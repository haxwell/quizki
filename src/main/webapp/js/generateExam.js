
var FilteredTopicListGetter = (function() {
	var my = {};
	
	my.get = function(filterByUserId, containsFilter) {
		var data_obj = { filterByUserId:false, containsFilter:'' };
		var data_url = '/ajax/topic-getJSONListOf.jsp';

		var rtn = undefined;
		
		makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
			var index = data.indexOf("<!DOCTYPE");
			var jsonExport = data;
			
			if (index != -1) {
				jsonExport = data.substring(0, index);
			}
			
			var parsedJSONObject = jQuery.parseJSON(jsonExport);

			rtn = new Quizki.Collection();
			rtn.addArray(parsedJSONObject.topic);
		});
		
		return rtn;
	};
	
	return my;

}());
