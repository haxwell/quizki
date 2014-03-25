var view_utility = (function() {
	return {
		executeTemplate : function(templateURL, templateParams) {
	
			var _stringModel = undefined;
			
			makeAJAXCall_andWaitForTheResults(templateURL, { }, 
	        		function(textFromTheURL) {
	    				// TO UNDERSTAND: why does this return text rather than a function to be executed?
						_stringModel = _.template(textFromTheURL, templateParams, { });
	    			}
	        );
			
			var rtn = _stringModel;
			
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
		this.listenTo(currQuestion, 'resetQuestion', function(event) { this.render(); });
		
		// Set up custom listenTo's, for views that need something custom to happen when something else happens, but that doesn't apply
		//  apply to all views which may use This view.
		if (arguments[0].modelToListenTo !== undefined) {
			this.modelToListenTo = arguments[0].modelToListenTo;
			this.modelEventToListenFor = arguments[0].modelEventToListenFor;
			
			var modelToListenTo = model_factory.get(this.modelToListenTo);
			this.listenTo(modelToListenTo, this.modelEventToListenFor, function() { 
				this.render();
			});
		}
	},
	events: {
		"keypress #id_questionText":"updateText",
		"blur #id_questionDescription":"updateDescription"
	},
	render: function() {
		// TODO: Research why this is necessary.. at least in displayQuestion, its causing a second, seemingly unnecessary call to the
		//  server for a question. It seems this is for a refresh of the current question, but what about it needs to be refreshed? can
		//  we check if that thing that needs to be refreshed is even dirty, before making the call to get the question over again?
//		model_factory.destroy("currentQuestion");
		
		var currentQuestion = model_factory.get("currentQuestion");

		var showDescriptionField = (currentQuestion.getDescription().length > 0) || currentQuestion.getId() == '-1';
		var hidden = ((showDescriptionField) ? "" : "hidden");
		var rows = (showDescriptionField ? "8" : "11");
		
		var disabledText = this.readOnly == undefined ? "" : "disabled";
		
		this.$el.html( view_utility.executeTemplate('/templates/QuestionTextAndDescriptionView.html', {text:currentQuestion.getText(), description:currentQuestion.getDescription(), disabled:disabledText, hidden:hidden, rows:rows}));
		
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


Quizki.DifficultyChooserView = Backbone.View.extend({
	initialize:function() {
		this.readOnly = arguments[0].readOnly;
		
		// the id of the button to select will have been passed in.
		// that id, is basically the model for this view
		// store the model
		this.buttonId = arguments[0].id;
		
		this.getModelNameKey = (arguments[0].getModelNameKey || "currentQuestion");
		
		// the ui manages the state of this button group. all 
		// this view has to do, when the time comes is return
		// the value.. or perhaps another class somewhere else can
		// as long as the iteration is done on the render, (which 
		// sets the 'active' attribute) that should do it..
		
		this.render();
		
		var currQuestion = model_factory.get(this.getModelNameKey);
		
		this.listenTo(currQuestion, 'difficultyChanged', function(event) { 
			var currQuestion = model_factory.get(this.getModelNameKey);
			this.buttonId = currQuestion.getDifficultyId(); this.render(); 
			});

		this.listenTo(currQuestion, 'resetQuestion', function(event) { 
			var currQuestion = model_factory.get(this.getModelNameKey);
			this.buttonId = currQuestion.getDifficultyId(); this.render(); 
			});
	},
	events : {
		"click button":"changed"
	},
	changed:function(event) {
		var currQuestion = model_factory.get(this.getModelNameKey);
		
		var _from = currQuestion.getDifficultyId();
		var _to = event.target.value;
		
		if (_from != _to) {
			currQuestion.setDifficultyId(_to);
		}
	},
	render:function() {
		var disabledText = this.readOnly == undefined ? "" : "disabled";
		
		this.$el.html(view_utility.executeTemplate('/templates/QuestionDifficultyChooserView.html', {disabled:disabledText}));
		var buttonId = this.buttonId || -1;
		
		// iterate over each of the buttons, if it matches the model, set it as active
		// otherwise remove the active attribute
		_.each($("#difficultyBtnGroup").find("button"), function($item) { 
			$item = $($item);
			($item.val() == buttonId) ? $item.addClass('active') : $item.removeClass('active'); 
		});
		
		return this;
	}
});
