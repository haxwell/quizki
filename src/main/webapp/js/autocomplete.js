var getAutocompleteHistoryForEnvironment = function(environment) {
	var rtn = undefined;
	
	var data_url = "/ajax/" + environment + "-getAutocompleteHistory.jsp";
	var data_obj = { };
	
	makeAJAXCall_andWaitForTheResults(data_url, data_obj, function(data,status) {
		
		var index = data.indexOf("<!DOCTYPE");
		var jsonExport = data;
		
		if (index != -1) {
			jsonExport = data.substring(0, index);
		}
		
		rtn = jQuery.parseJSON(jsonExport);
	});
	
	return rtn;
};