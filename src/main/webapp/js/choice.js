
var Choice = Backbone.Model.extend({
	defaults: {
		id : '-1',
		text : '',
		iscorrect : 'false',
		isselected : 'false',
		sequence : '0',
		string : ''
	},
	initialize : function() {
		
	}
});
