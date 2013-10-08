var view_utility = (function() {
	return {
		executeTemplate : function(templateURL, templateParams) {
	
			var _stringModel = model_factory.getStringModel();
			
			makeAJAXCall_andWaitForTheResults(templateURL, { }, 
	        		function(textFromTheURL) {
	    				// TO UNDERSTAND: why does this return text rather than a function to be executed?
						_stringModel.stringModel = _.template(textFromTheURL, templateParams, { });
	    			}
	        );
			
			var rtn = _stringModel.stringModel;
			
			model_factory.destroy(_stringModel.id);
			
			return rtn;
		}	
	}; 
}());

Quizki.QuestionTextAndDescriptionView = Backbone.View.extend({
	initialize:function() {
		this.readOnly = arguments[0].readOnly;
		
		this.render();

		var currQuestion = model_factory.get("currentQuestion");
		
		this.listenTo(currQuestion, 'questionTextChanged', function(event) { this.render(); });
		this.listenTo(currQuestion, 'reset', function(event) { this.render(); });			
	},
	events: {
		"keypress #id_questionText":"updateText",
		"blur #id_questionDescription":"updateDescription"
	},
	render: function() {
		var currentQuestion = model_factory.get("currentQuestion");

		var disabledText = this.readOnly == undefined ? "" : "disabled";
		
		this.$el.html( view_utility.executeTemplate('/templates/QuestionTextAndDescriptionView.html', {text:currentQuestion.getText(), description:currentQuestion.getDescription(), disabled:disabledText}));
		
		if (this.readOnly != undefined) {
			tinyMCE.init({
		        theme : "advanced",
		        mode : "textareas",
		        plugins : "autoresize",
		        readonly : 1,
				content_css : "css/quizki_tinymce_custom_content.css"
			});
		} else {
			tinyMCE.init({
		        theme : "advanced",
		        mode : "textareas",
		        plugins : "autoresize",
				content_css : "../css/quizki_tinymce_custom_content.css",
				theme_advanced_font_sizes: "10px,12px,13px,14px,16px,18px,20px",
				font_size_style_values : "10px,12px,13px,14px,16px,18px,20px",
		        theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyfull,|,formatselect",
		        theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,|,undo,redo,|,image,|,hr,removeformat,visualaid,|,sub,sup,|,charmap",
		        theme_advanced_buttons3 : "",
				theme_advanced_path : false,
				theme_advanced_statusbar_location : 0,
				help_shortcut : "",
		        onchange_callback : "questionTinyMCEChangeHandler"						
			});
		}
		
		return this;
	},
	updateText:function(event) {
		var currentQuestion = model_factory.get("currentQuestion");
		currentQuestion.setText($(event.target).val(), false);
	},
	updateDescription:function(event) {
		var currentQuestion = model_factory.get("currentQuestion");
		currentQuestion.setDescription($(event.target).val(), false);
	}
});


