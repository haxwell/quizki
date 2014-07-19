
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
