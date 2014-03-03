	//
	// Assumptions:
	//	This code assumes you have defined:
	//
	//	A text field with the id #offset
	//	A text field with the id #maxEntityCountFilter
	//  A text field with the id #*-entity-table-id, where * is the title of a tab which will use smooth scrolling.
	//		Its value should be the id of the table in which to insert more table rows.
	//  A text field with the id #*-data-object-definition, where * is the title of a tab which will use smooth scrolling.
	//		Its value will be set in the function setDataObjectDefinitions(), which is assumed to be defined.
	//  A text field with the id #prefix-to-current-view-hidden-fields, used to indicate the string which should be prepended
	//		to the ID of the hidden field located in the current view that is trying to be accessed. should be empty by
	// 		default.

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
						//setClonedHeaderInTheGlobalVariables();						
					});
					
					//
					// Calls displayMoreRows() when the user scrolls to the bottom of the page
					//
					$(window).scroll(function(){
				        if  ($(window).scrollTop() == $(document).height() - $(window).height()) {
					        if (isNoMoreItemsToDisplayFlagSet() == false && smoothScrollingEnabledOnCurrentTab()) {
					           //alert("Hit the bottom!");
					        	displayMoreRows(getFunctionCalledForEachRowByDisplayMoreRows(getPrefix()));
					        	
					        	var entity_level_function = window[getPrefix()+"_postUserHasScrolledAndRowsHaveBeenDisplayed"];
					        	
					        	if (entity_level_function != undefined)
					        		entity_level_function();
					        }
				        }
					});
					
					//
					// Show the hidden and fixed header when the original header scrolls offscreen
					//
					$(window).scroll(function() {
					    clearAlertDiv();
					    
					    var offset = $(this).scrollTop();
					    var headerOffset = getHeaderOffset();
					    var $headDOMElementInClonedHeader = getHeadDOMElementInClonedHeader();
					
					    if (offset >= headerOffset && $headDOMElementInClonedHeader.is(":hidden")) {
					    	var $fixedHeader = attachClonedHeaderToItsDOMElement();
					    	$fixedHeader.show();
					    	disableHeaderFilterFields();
					        // save the id of the now visible header...
					        $("#" + getPrefix() + "-header-div-prefix").val(getClonedHeaderId());
					    }
					    else if (offset < headerOffset) {
					    	$headDOMElementInClonedHeader.hide();
					        
					        // save the id of the now visible header...
					        $("#" + getPrefix() + "-header-div-prefix").val(getOrigHeaderId());
					    }
					});
					
					function attachClonedHeaderToItsDOMElement() {
						var $header = getHeadDOMElementInOriginalHeader().clone();
						
						$clonedHeader = getHeadDOMElementInClonedHeader();
						$clonedHeader.empty();
						
						return $clonedHeader.append($header);
					}
					
					function getHeadDOMElementInOriginalHeader() {
						// see is there a function defined with the name getPrefix()+_getHeadDOMElementInOriginalHeader()
						//  if so, call it, if not, return $(getOrigHeaderId()) 
						var entity_level_function = window[getPrefix()+"_getHeadDOMElementInOriginalHeader"];
						
						if (entity_level_function != undefined)
							return entity_level_function();
						else
							return $(getOrigHeaderId());
					}
					
					function getHeadDOMElementInClonedHeader() {
						// see is there a function defined with the name getPrefix()+_getHeadDOMElementInOriginalHeader()
						//  if so, call it, if not, return $(getOrigHeaderId()) 
						var entity_level_function = window[getPrefix()+"_getHeadDOMElementInClonedHeader"];
						
						if (entity_level_function != undefined)
							return entity_level_function();
						else
							return $(getClonedHeaderId());

					}

					// Think of this as a pointer to a pointer.. this dynamically constructed method call, think calling a runtime discovered
					//  method, a la reflection. This method call is to a method discovered at runtime. It is a pointer to that method.
					//  That method is implemented in the scope of the entity that this common code is dealing with, so for instance, the JS
					//  for question or exam, would implement this method. Its implementation would call the method that should be called for 
					//  each row displayed in the context of whatever entity this common code is running in..
					function internalFunction(row) {
						window[getPrefix()+"_thisFunctionCalledForEachRowByDisplayMoreRows"](row);
					}
					
					// This code is called by common, not entity specific, code. It is called immediately before that code needs to call displayMoreRows();
					// 
					// This code returns an object, with an attribute that is: the (1)function that is calling the (2)method that is calling the (3)method 
					// that is executed for each row shown by DisplayMoreRows().
					
					//  This calling code was likely an anonymous method, probably in response to an event
					//  .. this anon code doesn't have any knowledge of the method that should be called for each row when DisplayMoreRows executes.
					//  The JS code for the current scope DOES have that knowledge though, so it implements a uniquely named method which then 
					// executes that 'each row' method, one time.
					function getFunctionCalledForEachRowByDisplayMoreRows(prefix) {
						var return_obj = {
							func: undefined,
							params: undefined
						};
						
						var rtn = Object.create(return_obj);
						
						rtn.func = internalFunction;

						return internalFunction;
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
						
						setRowsOffsetToZero();
					});
					
					//
					// Populate the table on this tab, if necessary
					//
					$('a[data-toggle="tab"]').on('shown', function(e) {
						populateTheTable();
					});
					
					//
					// 
					// 
					function populateTheTable() {
						if (currentPageHasAnAJAXDataObjectDefinition()) {
							displayMoreRows(getFunctionCalledForEachRowByDisplayMoreRows(getPrefix()));
						}
					}
					
					//
					// Returns true if there is an AJAX data object definition for the current page
					//
					function currentPageHasAnAJAXDataObjectDefinition() {
						var prefix = getPrefix();
						
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
					// Returns the url which provides table data for the currently selected view
					//
					function getURLThatProvidesTableData() {
						var prefix = getPrefix();
						
						return $("#"+prefix+"-view-data-url").attr("value");
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
						
						var parsedJSONObject = jQuery.parseJSON(jsonExport);
						
						var prefix = getPrefix();
						var qArr = window[prefix+"_getJSONFromServerSuppliedData"](parsedJSONObject);
						var str = "";
						var entityTableId = $("#"+prefix+"-entity-table-id").attr("value");

						// if the function for each row needs to call methods which need some one-time setup,
						//  this call is that one-time...
						oneTimeSetupForMethodsCalledByTheFunctionCalledForEachRow(parsedJSONObject);
						
						if (qArr != undefined) {
							clearTableStatusRow();
							
							var numRows = $(entityTableId + " > tbody > tr").length
							
							for (var i=0; i<qArr.length; i++) {
								rowNum = i + numRows;
								str = window[prefix+"_convertToHTMLString"](qArr[i], rowNum);
								
								$(entityTableId + " > tbody:last").append(str);
								
								if (functionForEachRow != undefined)
									functionForEachRow($(entityTableId + " > tbody > tr:last"));
							}
							
							var msg = "";
							if (parsedJSONObject.addlItemCount == 0) {
								msg = "That's all folks!";
								setNoMoreItemsToDisplayFlag();
							} else if (parsedJSONObject.addlItemCount > 0) {
								msg = (parsedJSONObject.addlItemCount + " more items!");
							}
							
							appendTableStatusRow(msg, entityTableId, prefix);
						}
						else {
							clearTableStatusRow();
							
							var msg = "";
							
							if (parsedJSONObject.addlInfoCode == 0) {
								msg = "You haven't created anything to list here!"; 
							} else if (parsedJSONObject.addlInfoCode == 1) {
								msg = "Nothing matches that filter...";								
							} else if (parsedJSONObject.addlInfoCode == 2) {
								msg = "There are no selected items.";
							}
							
							appendTableStatusRow(msg, entityTableId, prefix);
							setNoMoreItemsToDisplayFlag();
						}
					}
					
					function getMoreRows_DataObjectForAJAX() {
						var prefix = getPrefix();
						return getDataObjectForAJAX(prefix+"-data-object-definition");
					}
					
					//
					// Makes an AJAX call to get addition table data for the current tab (page)
					//
					function getMoreRows() {
						var rtn = "";
						
						if (isNoMoreItemsToDisplayFlagSet() == false) {
							var prefix = getPrefix();
							var os = $("#" + prefix + "-offset").attr("value");
							
							if (os == undefined || os.length == 0) {
								os = 0;
								$("#" + prefix + "-offset").attr("value", os);
							}
	
							var mecf = $("#maxEntityCountFilter").attr("value");
							
							if (mecf == undefined || mecf.length == 0) {
								mecf = 10;
								$("#maxEntityCountFilter").attr("value", mecf);
							}
							
							var data_url = getURLThatProvidesTableData();
							var data_obj = getMoreRows_DataObjectForAJAX();
	
							makeAJAXCall_andWaitForTheResults(data_url, data_obj, 
								function(data,status){
									//alert("Data: " + data + "\nStatus: " + status);
									
									if (status == 'success') {
										var prefix = getPrefix();
										
										os = (os*1)+(mecf*1); // force numerical addition
										$("#" + prefix + "-offset").attr("value", os);
										$("#maxEntityCountFilter").attr("value", mecf);
										
										rtn = data;
									}
								});
						}

						return rtn;						
					}
					
					function setRowsOffsetToZero() {
						var prefix = getPrefix(); 
						$("#" + prefix + "-offset").attr("value", "0");
					}
					
					function appendTableStatusRow(msg, entityTableId, prefix) {
						var html = window[prefix+"_getNoItemsFoundHTMLString"](msg);
						$(entityTableId + " > tbody:last").append(html);
					}
					
					function clearTableStatusRow() {
						var prefix = getPrefix();
						var entityTableId = $("#"+prefix+"-entity-table-id").attr("value");
						
						$(entityTableId + " tbody tr:last(.table-status-row)").remove();
						$(entityTableId + " tbody tr:last(.table-status-row)").remove();
					}
					
					function setNoMoreItemsToDisplayFlag() {
						$("#" + getPrefix() + "-NoMoreItemsToDisplayFlag").attr("value", "true");
					}
					
					function isNoMoreItemsToDisplayFlagSet() {
						return $("#" + getPrefix() + "-NoMoreItemsToDisplayFlag").attr("value") == "true";
					}

					function clearNoMoreItemsToDisplayFlag() {
						$("#" + getPrefix() + "-NoMoreItemsToDisplayFlag").attr("value", "");
					}
					
					function populateAlertDiv(msgsArr, alertClassName) {
						var msgs = "";
						
						for (var i=0; i<msgsArr.length; i++) {
							msgs += msgsArr[i] + '<br/>';
						}
						
						var $idAlertDiv = $(getVisibleHeaderID()).find('#idAlertDiv'); 
												
						$idAlertDiv.html('');
						$idAlertDiv.html(msgs);
						$idAlertDiv.addClass(alertClassName);
						$idAlertDiv.removeClass('hidden');
					}
					
					function clearAlertDiv() {
						$(getVisibleHeaderID()).find('#idAlertDiv').addClass('hidden');
					}
					
					function getHiddenHeaderID() {
						var val = $("#" + getPrefix() + "-header-div-prefix").attr("value");
						
						var origHeaderId = getOrigHeaderId();
						var clonedHeaderId = getClonedHeaderId();
						
						if (val == origHeaderId) 
							return clonedHeaderId
						else
							return origHeaderID;
					}
					
					function getVisibleHeaderID() {
						var rtn = $("#" + getPrefix() + "-header-div-prefix").attr("value"); 
						return rtn;
					}
					
					function getPrefix() {
						var rtn = $("#prefix-to-current-view-hidden-fields").attr("value"); 
						return rtn;
					}
