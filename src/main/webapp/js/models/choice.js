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


var Choice = Backbone.Model.extend({
	defaults: {
		id : '-1', // ID from the database, not set if this is Choice has not been persisted.
		ui_id : '-1', // an ID for the User Interface to use.. not sent to the server
		text : '',
		iscorrect : 'false',
		isselected : 'false',
		sequence : '0',
		selectedSequence : '-1',
		phrase : ''
	},
	initialize : function() {
		
	}
});
