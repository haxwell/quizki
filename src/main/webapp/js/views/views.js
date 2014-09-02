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

// This view is shared between Create Question, and Display Question, and Take Exam.
Quizki.QuestionTextAndDescriptionView = Backbone.View.extend({
	initialize:function() {
		this.readOnly = arguments[0].readOnly;
		
		this.questionTextFormatter = arguments[0].questionTextFormatter;
		
		this.render();

		this.listenTo(event_intermediary, 'currentQuestion::put::model_factory', function(newQuestion) { this.render(); });
		
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
		var currentQuestion = model_factory.get("currentQuestion");

		var showDescriptionField = (currentQuestion.getDescription().length > 0) || currentQuestion.getId() == '-1';
		var hidden = ((showDescriptionField) ? "" : "hidden");
		var rows = (showDescriptionField ? "8" : "11");
		
		var disabledText = this.readOnly == undefined ? "" : "disabled";
		
		var qText = '';
		if (this.questionTextFormatter != undefined)
			qText = this.questionTextFormatter(currentQuestion);
		else
			qText = currentQuestion.getText();
		
		this.$el.html( view_utility.executeTemplate('/templates/QuestionTextAndDescriptionView.html', {text:qText, description:currentQuestion.getDescription(), disabled:disabledText, hidden:hidden, rows:rows}));
		
		if (this.readOnly != undefined) {
			tinyMCE.init({
		        theme : "modern",
		        menubar : false,
		        statusbar : false,
		        toolbar : false,
		        mode : "textareas",
		        plugins : "autoresize image charmap",
		        readonly : 1,
				content_css : "css/quizki_tinymce_custom_content.css"
			});
		} else {
			tinyMCE.init({
		        theme : "modern",
		        menubar : false,
		        statusbar : false,
		        resize : "both",
		        mode : "textareas",
		        plugins : "autoresize image charmap",
				content_css : "../css/quizki_tinymce_custom_content.css",
		        toolbar : "undo redo | bold italic underline strikethrough | alignleft aligncenter alignjustify | formatselect" + 
		                    " bullist numlist | outdent indent | image | hr removeformat | subscript superscript | charmap",
				help_shortcut : "",
			    setup: function(editor) {
			        editor.on('blur', function(e) {
			            questionTinyMCEBlurHandler(tinymce.activeEditor.getContent());
			        });
			    }				
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

		this.listenTo(event_intermediary, 'currentQuestion::put::model_factory', function(event) { 
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
