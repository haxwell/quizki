
var Exam = Backbone.Model.extend({
	defaults: { 
		id:'-1',
		title:'',
		message:'',
		owningUserId:'',
		topics:'',
		difficulty:'',
		entityStatus:''
	},
	initialize: function() {
		// do nothing
	}
});
