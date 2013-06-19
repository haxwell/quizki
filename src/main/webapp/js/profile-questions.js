
					function Questions_convertToHTMLString(obj, rowNum) {
						var topicsArr = obj.topics;
						var rtn = "";
						
						rtn += "<tr id=\"tableRow_" + rowNum + "\">";
						rtn += "<td>";
						
						if (obj.description.length > 0) {
							rtn += "<a href=\"/displayQuestion.jsp?questionId=" + obj.id + "\">" + obj.description + "</a>";
						}
						else {
							rtn += "<a href=\"/displayQuestion.jsp?questionId=" + obj.id + "\">" + obj.textWithoutHTML + "</a>";
						}
						
						rtn += "</td><td>";

						if (topicsArr.length > 0) {
							for (var i=0; i<topicsArr.length; i++) {
								rtn += topicsArr[i].text + "<br/>";
							}
						}
						
						rtn += "</td><td>";
						
						rtn += obj.type_text;
						rtn += "</td><td>";
						rtn += obj.difficulty_text;
						rtn += "</td><td>";
						
						// TODO: figure out a way of populating the Vote info.. Probably put it in a JSON str, like [{"objectId":"1","votesUp":"1","votesDown":"0"}]
						//  then create a map of some sort out of it..
						rtn += " -- ";
						rtn += "</td></tr>";
						
						return rtn;
					}
