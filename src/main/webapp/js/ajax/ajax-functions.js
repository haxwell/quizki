/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */


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
	}).done(returnFunction || new function() { });
}

function makeAJAXCall_andWaitForTheResults(data_url, data_obj, returnFunction) {
	$.ajax({
		type: "POST",
		url: data_url,
		data: data_obj,
		dataType: "text",
		async: false
	}).done(returnFunction || new function() { });
}

