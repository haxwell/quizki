
//
// Returns the data object that this tab uses in its calls to get AJAX table data
//
function getDataObjectForAJAX(dataObjectDefinitionFieldID) {
	
	// a list of the names of the fields in the data object, mapped to the ID of the hidden field with the value for each..
	var dataObjDefinition_json = $("#"+dataObjectDefinitionFieldID).attr("value");
	
	var obj = jQuery.parseJSON(dataObjDefinition_json);
	var arr = obj.fields;
	
	var rtn = { };
	
	for (var i=0; i<arr.length; i++) {
		
		try {
			var tmp = $(arr[i].id).attr("value");
			
			if (tmp == undefined) tmp = "";
			
			rtn[arr[i].name] = tmp;
		}
		catch (err) {
			// skip this field... TODO, handle this better.. an error means the dataObjDefinition is bad..
		}
	}
	
	return rtn;
}

function makeAJAXCall_andDoNotWaitForTheResults(data_url, data_obj, returnFunction) {
	$.ajax({
		type: "POST",
		url: data_url,
		data: data_obj,
		dataType: "text",
		async: true
	}).done(returnFunction);
}

function makeAJAXCall_andWaitForTheResults(data_url, data_obj, returnFunction) {
	$.ajax({
		type: "POST",
		url: data_url,
		data: data_obj,
		dataType: "text",
		async: false
	}).done(returnFunction);
}

