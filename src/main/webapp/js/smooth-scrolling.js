	//
	// Assumptions:
	//	This code assumes you have defined:
	//
	//	A text field with the id #offset
	//	A text field with the id #maxEntityCountFilter
	//  A text field with the id #*-tab-data-url, where * is the title of a tab which will use smooth scrolling. 
	//		Its value should be the url of the AJAX call to get more table rows.
	//  A text field with the id #*-entity-table-id, where * is the title of a tab which will use smooth scrolling.
	//		Its value should be the id of the table in which to insert more table rows.
	//  A text field with the id #prefix-to-current-view-hidden-fields, which will have an empty value by default.
	//  A text field with the id #*-data-object-definition, where * is the title of a tab which will use smooth scrolling.
	//		Its value will be set in the function setDataObjectDefinitions(), which is assumed to be defined.

	//	function setDataObjectDefinitions() - A method which defines a JSON string which describes the filters to be passed
	//		to the AJAX url, and sets that string in the appropriate #*-data-object-definition field. The JSON string should
	// 		look like:
	//			var str = "{\"fields\": [{\"name\":\"containsFilter\",\"id\":\"#containsFilter\"}]}";
	//
	//		with a 'name' and 'id' pair for each field
	//


					var functionCalledForEachRowByDisplayMoreRows = undefined;

					//
					// Called when browser document loads. It then calls setDataObjectDefinitions() defined
					//  in the current page.
					//
					$(document).ready(function() {
						setDataObjectDefinitions();
						setClonedHeaderInTheGlobalVariables();						
					});
					
					//
					// Calls displayMoreRows() when the user scrolls to the bottom of the page
					//
					$(window).scroll(function(){
				        if  ($(window).scrollTop() == $(document).height() - $(window).height()) {
					        if (smoothScrollingEnabledOnCurrentTab()) {
					           //alert("Hit the bottom!");
					           displayMoreRows(functionCalledForEachRowByDisplayMoreRows);
					        }
				        }
					});
					
					function setFunctionCalledForEachRowByDisplayMoreRows(func) {
						functionCalledForEachRowByDisplayMoreRows = func;
					}

					//
					//
					//
					function setFocusOnTheContainer() {
						document.activeElement.blur();
					}

					//
					// Before the just-clicked-on tab is shown, get its prefix, and store it in the hidden field.
					//
					$('a[data-toggle="tab"]').on('show', function(e) {
						var tab = e.target;
						
						// identify the tab, get its text, which serves as a prefix to the hidden fields for the current tab
						var tabText = tab.innerText;
						
						// write that prefix in the hidden prefix field
						$("#prefix-to-current-view-hidden-fields").attr("value", tabText);
					});
					
					//
					// Populate the table on this tab, if necessary
					//
					$('a[data-toggle="tab"]').on('shown', function(e) {
						if (currentPageHasAnAJAXDataObjectDefinition()) {
							displayMoreRows(functionCalledForEachRowByDisplayMoreRows);
						}
					});
					
					//
					// Returns true if there is an AJAX data object definition for the current page
					//
					function currentPageHasAnAJAXDataObjectDefinition() {
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						
						// a list of the name of the field in the data object, and the name of the field with its value
						var dataObjDefinition_json = $("#"+prefix+"-data-object-definition").attr("value");

						return dataObjDefinition_json != undefined;					
					}
					
					//
					// Returns true if smooth scrolling is enabled on the current tab (page)
					//
					function smoothScrollingEnabledOnCurrentTab() {
						return currentPageHasAnAJAXDataObjectDefinition();
					}
					
					//
					// Returns the url which provides table data for the currently selected tab
					//
					function getURLThatProvidesTableData() {
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						
						return $("#"+prefix+"-view-data-url").attr("value");
					}
					
					//
					// Returns the data object that this tab uses in its calls to get AJAX table data
					//
					function getDataObjectForAJAX() {
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						
						// a list of the name of the field in the data object, and the name of the field with its value
						var dataObjDefinition_json = $("#"+prefix+"-data-object-definition").attr("value");
						
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
					
					//
					// Gets the table data for this tab, and then adds rows to the table
					//
					function displayMoreRows(functionForEachRow) {
						var data = getMoreRows();
						
						var index = data.indexOf("<!DOCTYPE");
						var jsonExport = data;
						
						if (index != -1) {
							jsonExport = data.substring(0, index);
						}
						
						var obj = jQuery.parseJSON(jsonExport);
						
						var qArr = obj.question;
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						var str = "";
						var entityTableId = $("#"+prefix+"-entity-table-id").attr("value");

						if (qArr != undefined) {
							clearTableStatusRow();
							
							var numRows = $(entityTableId + " > tbody > tr").length
							
							for (var i=0; i<qArr.length; i++) {
								rowNum = i + numRows;
								str = window[prefix+"_convertToHTMLString"](qArr[i], rowNum);
								
								$(entityTableId + " > tbody:last").append(str);
								
								if (functionForEachRow != undefined)
									functionForEachRow($(entityTableId + " > tbody:last"));
							}
							
							var msg = "";
							if (obj.addlItemCount == 0) {
								msg = "That's all folks!";
								setNoMoreItemsToDisplayFlag();
							} else if (obj.addlItemCount > 0) {
								msg = (obj.addlItemCount + " more items!");
							}
							
							appendTableStatusRow(msg, entityTableId, prefix);
						}
						else {
							clearTableStatusRow();
							
							var msg = "";
							
							if (obj.addlInfoCode == 0) {
								msg = "You haven't created any questions!"; 
							} else if (obj.addlInfoCode == 1) {
								msg = "Nothing matches that filter...";								
							}
							
							appendTableStatusRow(msg, entityTableId, prefix);
							setNoMoreItemsToDisplayFlag();
						}
					}
					
					//
					// Makes an AJAX call to get addition table data for the current tab (page)
					//
					function getMoreRows() {
						var os = $("#offset").attr("value");
						
						if (os == undefined || os.length == 0) {
							os = 0;
							$("#offset").attr("value", os);
						}

						var mecf = $("#maxEntityCountFilter").attr("value");
						
						if (mecf == undefined || mecf.length == 0) {
							mecf = 10;
							$("#maxEntityCountFilter").attr("value", mecf);
						}
						
						var rtn = "";
						var data_url = getURLThatProvidesTableData();
						var data_obj = getDataObjectForAJAX();

						$.ajax({
							type: "POST",
							url: data_url,
							data: data_obj,
							dataType: "text",
							async: false
						}).done(function(data,status){
								//alert("Data: " + data + "\nStatus: " + status);
								
								if (status == 'success') {
									os = (os*1)+(mecf*1); // force numerical addition
									$("#offset").attr("value", os);
									$("#maxEntityCountFilter").attr("value", mecf);
									
									rtn = data;
								}
							});
						
						return rtn;
					}
					
					function setRowsOffsetToZero() {
						$("#offset").attr("value", "0");
					}
					
					function clearNoMoreItemsToDisplayFlag() {
						$("#idNoMoreItemsToDisplayFlag").attr("value", "");
					}

					function appendTableStatusRow(msg, entityTableId, prefix) {
						var html = window[prefix+"_getNoItemsFoundHTMLString"](msg);
						$(entityTableId + " > tbody:last").append(html);
					}
					
					function clearTableStatusRow() {
						var prefix = $("#prefix-to-current-view-hidden-fields").attr("value");
						var entityTableId = $("#"+prefix+"-entity-table-id").attr("value");
						
						$(entityTableId + " tbody tr:last(.table-status-row)").remove();
						$(entityTableId + " tbody tr:last(.table-status-row)").remove();
					}
					
					function setNoMoreItemsToDisplayFlag() {
						$("#idNoMoreItemsToDisplayFlag").attr("value", "true");
					}
