
var Choice = Backbone.Model.extend({
	defaults: {
		id : '-1',
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
