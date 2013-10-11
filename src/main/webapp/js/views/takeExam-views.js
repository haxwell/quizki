	Quizki.QuitThisExamView = Backbone.View.extend({
		initialize:function() {
			this.showEditBtn = arguments[0].showEditBtn;
			this.render();
		},
		events: {
			"click #btnQuit":"sendEmBackToTheIndexPage"
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/TakeExamWithQuitThisExamButton.html', {}));
			return this;
		},
		sendEmBackToTheIndexPage: function() {
			var url="/index.jsp";
			window.location.href = url;
		}
	});

	Quizki.ExamQuestionTextView = Backbone.View.extend({
		initialize:function() {
			this.listenTo(ExamEngine, 'currentQuestionUpdated', function(event) { this.render();});
			
			this.render();
		},
		render: function() {
			var currentQuestion = model_factory.get("currentQuestion");

			this.$el.html( view_utility.executeTemplate('/templates/ExamQuestionTextAndDescriptionView.html', {text:currentQuestion.getText(), description:currentQuestion.getDescription()}));
			
			tinyMCE.init({
		        theme : "advanced",
		        mode : "textareas",
		        plugins : "autoresize",
		        readonly : 1,
				content_css : "css/quizki_tinymce_custom_content.css"
			});
			
			return this;
		}
	});
	
	Quizki.ExamSingleQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes.val;
			
			this.render();
		},
		render:function() {
			
			var selected = '';
			
			if (this.model.isselected !== undefined && (this.model.isselected == "true" || this.model.isselected === true)) {
				selected = 'checked';
			}
			
			this.$el.html(view_utility.executeTemplate('/templates/ExamSingleQuestionChoiceItemView.html', {id:this.model.id,checked:selected,text:this.model.text,disabled:''}));
			return this;
		}
	});
	
	Quizki.ExamMultipleQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes.val;
			
			this.render();
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/ExamMultipleQuestionChoiceItemView.html', {id:this.model.id,text:this.model.text}));
			return this;
		}
	});
	
	Quizki.ExamStringQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes.val;
			
			this.render();
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/ExamStringQuestionChoiceItemView.html', {id:this.model.id}));
			return this;
		}
	});
	
	Quizki.ExamSequenceQuestionChoiceItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes.val;
			
			this.render();
		},
		render:function() {
			var _sequence = this.model.sequence == 0 ? "" : this.model.sequence;
			this.$el.html(view_utility.executeTemplate('/templates/ExamSequenceQuestionChoiceItemView.html', {id:this.model.id,sequence:_sequence,text:this.model.text}));
			return this;
		}
	});

	// this view represents an item in a list of choices
//	Quizki.ExamQuestionChoiceItemView = Backbone.View.extend({
//		tagName:'li',
//		
//		initialize: function() {
//			this.model = arguments[0].attributes;
//			
//			var text = this.model.val.text;
//			
//			// we have to check true in two different ways, because we have two different means of getting here.. the put from the button/enter press
//			//  of the ***View, or the array of the initial question's choices.. the server in its ajax response is sending iscorrect as a string, 
//			//  instead of a value. That should be cleaned up one day..
//			var checked = (this.model.val.iscorrect == 'true' || this.model.val.iscorrect === true) ? 'checked' : '';
//			var sequence = this.model.val.sequence || 0;
//			var id = this.model.millisecond_id || new Date().getMilliseconds();
//			
//			this.model = {text:text,checked:checked,sequence:sequence,id:id};
//		},
//		getHideSequence:function() {
//			var currQuestion = model_factory.get('currentQuestion');
//			
//			var hideSequence = "hidden";
//			
//			if (currQuestion.type_id == "4")
//				hideSequence = "";
//
//			return hideSequence;
//		},
//		render:function() {
//			var view = "QuestionChoiceItemView";
//            var _model = this.model;
//            var hideSequence = this.getHideSequence();
//
//			this.$el.html(view_utility.executeTemplate('/templates/TakeExamQuestionChoiceItemView.html', {}));
//
//            var template = Quizki[view].prototype.template;
//			this.$el.html( template );
//			
//			return this;
//		},
//		milliseconds: function() { return this.model.id; },
//		setText: function(newText) { this.model.text = newText; },
//		getText: function() { return this.model.text; },
//	});

	// this view represents the list of choices
	Quizki.ExamChoiceListView = Backbone.View.extend({
		tagName:'ul',
		
		initialize: function() {
			// the model for this view is the current question.. 
			//  renderElement needs to get the current question type, then call a factory which
			//  returns a function which acts as an iterator for (or returns a collection of)
			//  views representing the choices
			this.$el = arguments[0].el;
			this.ChoiceItemViewCollection = new Array();

			this.listenTo(ExamEngine, 'currentQuestionUpdated', function(event) { this.render();});
			
			this.render();
		},
		events: {
			// do nothing
		},
		renderElement: function(model) {
			var ul = this.$el.find("#listOfChoices");

			ul.append( model.val.render().$el.html() );
			
			var obj = {millisecondId:model.millisecond_id, view:model.val};
			
			this.ChoiceItemViewCollection.push(obj);
		},
		render:function() {
			this.ChoiceItemViewCollection = new Array();
			
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<ul class='choiceItemList span6' id='listOfChoices'></ul>" )() );
			
			var views = TakeExamChoiceItemFactory.getViewsForCurrentQuestion();
			
			_.each(views.models, function(model) { this.renderElement(model.attributes); }, this);
			
			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();
			
			return this;
		}
		
	});
	
	Quizki.ExamNavigationButtons = Backbone.View.extend({
		initialize: function() {
			this.$el = arguments[0].el;

			this.listenTo(ExamEngine, 'currentQuestionUpdated', function(event) { this.render();});
			
			this.render();
		},
		events: {
			"click #prevBtn": "prevBtnClicked",
			"click #nextBtn": "nextBtnClicked"
		},
		prevBtnClicked: function() {
			ExamEngine.prevQuestion();
		},
		nextBtnClicked: function() {
			ExamEngine.nextQuestion();
		},
		render:function () {
			// eventually, we'll need to check if the exam is completed, and display another set of buttons..
			//  but for now.. this one will do..
			var template = view_utility.executeTemplate('/templates/ExamInProgressNavigationButtonsView.html', {disabled:""});
			this.$el.html( template );
			
		}
	});
