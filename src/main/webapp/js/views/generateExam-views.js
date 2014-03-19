	Quizki.AllTopicsListView = Backbone.View.extend({
		
		initialize:function() {
			this.render();

			console.log('allTopicsListView initialize() -- about to store a constructor function..');
			
			model_constructor_factory.put("stagedSelectedListOfTopics", function() { return new Backbone.Collection([], { model: Topic }); });
			
			console.log('allTopicsListView initialize() -- about to do the listenTos..');
			
			this.listenTo(model_factory.get('currentListOfTopics'), 'add', function(event) { this.render(); });
			this.listenTo(model_factory.get('currentListOfTopics'), 'remove', function(event) { this.render(); });
			this.listenTo(model_factory.get('currentListOfTopics'), 'reset', function(event) { this.render(); });
			
			console.log('allTopicsListView initialize() complete');
		},
		renderElement: function(model) {
			var listOfTopics = this.$el.find("#listOfTopics");
			
			var topicItem = new Quizki.AllTopicsListItemView(model, 'topicItem');
			listOfTopics.append( topicItem.render().$el.html() );
		},
		render:function() {
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<select multiple class='span3 mainSelectBox' id='listOfTopics' style='height:160px;'></select>" )() );
			
			var topics = model_factory.get("currentListOfTopics");
			
			_.each(topics.models, function(model) { this.renderElement(model); }, this);
			
			return this;
		},
		events: {
			"dblclick .topicItem":"addItemToSelectedList",
			"click .topicItem":"stageSelectedItemForListOfTopics"
		},
		addItemToSelectedList: function(event) {
			var stagedColl = model_factory.get("stagedSelectedListOfTopics");
			var selectedTopics = model_factory.get("selectedListOfTopics");
			var mainListOfTopics = model_factory.get("currentListOfTopics");

			var topicText = $(event.target).html().trim();
			
			var topicObject = mainListOfTopics.findWhere({ text:topicText }).attributes;
			
			// add an object to the selected item list
			selectedTopics.add(topicObject);
			event_intermediary.throwEvent('selectedListOfTopicsChanged');
			
			mainListOfTopics.remove(topicObject);			
			event_intermediary.throwEvent('mainListOfTopicsChanged');
			
			stagedColl.reset();
		},
		stageSelectedItemForListOfTopics: function(event) {
			var currentListOfTopics = model_factory.get("currentListOfTopics");
			var selectedTopics = $('#listOfTopics option:selected');
			
			var coll = model_factory.get("stagedSelectedListOfTopics");
			
			if (coll.length >= selectedTopics.length)
				coll.reset();
			
			selectedTopics.each(function(item) {
				var topicText = $(this).val(); 
				var topicObject = currentListOfTopics.findWhere({ text:topicText }).attributes;
				
				coll.add(topicObject);
			});
		}
	});

	Quizki.AllTopicsListItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes;
			this.fieldClass = arguments[1];
		},
		render: function(model) {
			var _model = this.model;
			var _fieldClass = this.fieldClass;
			
			this.$el.html( view_utility.executeTemplate('/templates/ItemInAllTopicsList.html', {text:_model.text,klass:_fieldClass}));
			
			return this;
		}
	});
	
	Quizki.AllTopicsListFilterView = Backbone.View.extend({
		initialize:function() {
			this.render();
			
			this.checked = false;
		},
		render: function(model) {
			// TODO: will need to remember state.. probably.
			this.$el.html(view_utility.executeTemplate('/templates/AllTopicsListFilterView.html', { }));

			//get the actual bootstrap slider ui component div
			var $slider = this.$el.find('.switch-square');
			$slider.bootstrapSwitch();
			
			return this;
		},
		events: {
			"blur #topicContainsFilter" : "applyTextFilter",
			"click #onlyMyTopicsFilter" : "applyOnlyMyTopicsFilter"
		},
		applyTextFilter: function(event) {
			FilteredTopicListGetter.get(this.checked, $(event.target).val(), model_factory.get("currentListOfTopics"), model_factory.get("selectedListOfTopics"));
		},
		applyOnlyMyTopicsFilter: function(event) {
			this.checked = !this.checked;
			var text = $("#topicContainsFilter").val();
			
			FilteredTopicListGetter.get(this.checked, text, model_factory.get("currentListOfTopics"), model_factory.get("selectedListOfTopics"));
		}
	});
	
	Quizki.SelectedTopicsListView = Backbone.View.extend({
		
		initialize:function() {
			this.render();
			
			model_constructor_factory.put("stagedUnselectedListOfTopics", function() { return new Backbone.Collection([], { model: Topic }); });
			
			this.listenTo(event_intermediary, 'selectedListOfTopicsChanged', function(event) { this.render(); });
		},
		renderElement: function(model) {
			var listOfTopics = this.$el.find("#selectedListOfTopics");
			
			var v = view_utility.executeTemplate('/templates/ItemInAllTopicsList.html', {text:model.text,klass:'selectedTopicItem'});
			
			listOfTopics.append( v );
		},
		render:function() {
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<select multiple class='span3 selectedSelectBox' id='selectedListOfTopics' style='height:160px;'></select>" )() );
			
			var topics = model_factory.get("selectedListOfTopics");
			
			_.each(topics.models, function(model) { this.renderElement(model.attributes); }, this);
			
			return this;
		},
		events: {
			"dblclick .selectedTopicItem":"removeItemFromSelectedList",
			"click .selectedTopicItem":"stageSelectedItemForListOfTopics"			
		},
		removeItemFromSelectedList: function(event) {
			var stagedColl = model_factory.get("stagedUnselectedListOfTopics");
			var selectedTopics = model_factory.get("selectedListOfTopics");
			var mainListOfTopics = model_factory.get("currentListOfTopics");
			
			var topicText = $(event.target).html().trim();
			
			var topicObject = selectedTopics.findWhere({ text:topicText }).attributes;
			
			selectedTopics.remove(topicObject);
			event_intermediary.throwEvent('selectedListOfTopicsChanged');

			mainListOfTopics.add(topicObject);
			event_intermediary.throwEvent('mainListOfTopicsChanged');			
			
			stagedColl.reset();
		},
		stageSelectedItemForListOfTopics: function(event) {
			var selectedListOfTopics = model_factory.get("selectedListOfTopics");
			var selectedTopics = $('#selectedListOfTopics option:selected');
			
			var coll = model_factory.get("stagedUnselectedListOfTopics");
			
			if (coll.length >= selectedTopics.length)
				coll.reset();
			
			selectedTopics.each(function(item) {
				var topicText = $(this).val(); 
				var topicObject = selectedListOfTopics.findWhere({ text:topicText }).attributes;
				
				coll.add(topicObject);
			});
		}
	});
	
	Quizki.ArrowView = Backbone.View.extend({
		initialize:function() {
			this.showEditBtn = arguments[0].showEditBtn;
			this.render();
		},
		events: {
			"click #btnAddTopic": "addTopic",
			"click #btnRemoveTopic" : "removeTopic"
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/GenerateExamArrowButtons.html', {}));
			return this;
		},
		addTopic : function() {
			var stagedColl = model_factory.get("stagedSelectedListOfTopics");
			var selectedTopics = model_factory.get("selectedListOfTopics");
			var mainListOfTopics = model_factory.get("currentListOfTopics");
			
			_.each(stagedColl.models, function(model) { selectedTopics.add(model); });
			event_intermediary.throwEvent('selectedListOfTopicsChanged');

			_.each(stagedColl.models, function(model) { mainListOfTopics.remove(model); });
			event_intermediary.throwEvent('mainListOfTopicsChanged');

			stagedColl.reset();
		},
		removeTopic : function() {
			var stagedColl = model_factory.get("stagedUnselectedListOfTopics");
			var selectedTopics = model_factory.get("selectedListOfTopics");
			var mainListOfTopics = model_factory.get("currentListOfTopics");

			_.each(stagedColl.models, function(model) { selectedTopics.remove(model); });
			event_intermediary.throwEvent('selectedListOfTopicsChanged');

			_.each(stagedColl.models, function(model) { mainListOfTopics.add(model); });
			event_intermediary.throwEvent('mainListOfTopicsChanged');

			stagedColl.reset();
		}
	});

	Quizki.MatchingExamsView = Backbone.View.extend({
		
		initialize:function() {
			this.listenTo(event_intermediary, 'matchingListOfExamsChanged', function(event) { this.render(); });
			
			this.render();
		},
		events : {
			"click .examItem":"setSelectedExam"
		},
		setSelectedExam : function(event) {
			var exams = model_factory.get("listOfMatchingExams");
			
			var examText = $(event.target).html().trim();
			var examObject = exams.findWhere({ title:examText }).attributes;
			
			model_factory.put("selectedExam", examObject);
		},
		renderElement: function(model) {
			var listOfMatchingExams = this.$el.find("#matchingExamsView");
			
			var examItem = new Quizki.MatchingExamItemView(model);
			listOfMatchingExams.append( examItem.render().$el.html() );
		},
		render:function() {
			this.$el.html( _.template( "<select multiple class='span5 examsSelectBox' id='matchingExamsView' style='height:160px;'></select>" )() );			
			
			// this model is set by the MatchingExamsListGetter object
			var exams = model_factory.get("listOfMatchingExams");
			
			_.each(exams.models, function(model) { this.renderElement(model); }, this);
			
			return this;
		}
	});
	
	Quizki.MatchingExamItemView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes;
		},
		render: function(model) {
			var _model = this.model;
			this.$el.html( view_utility.executeTemplate('/templates/ItemInMatchingExamsList.html', {text:_model.title}));
			
			return this;
		}
	});
	
	Quizki.MatchingExamsFilterView = Backbone.View.extend({
		initialize:function() {
			this.render();
		},
		render: function(model) {
			// TODO: will need to remember state.. probably.
			this.$el.html(view_utility.executeTemplate('/templates/MatchingExamsListFilterView.html', { }));
			return this;
		},
		events: {
			"click #mustContainAllTopicsChkbox" : "handleCheckboxClick"
		},
		handleCheckboxClick: function(event) {
			var model = model_factory.get("matchingExamsMustContainAllTopics");
			var v = !($(event.target).find("#mustContainAllTopicsChkbox").attr('checked') == 'checked');

			model.val = v;
		}
	});
	
	Quizki.OptionsForGenerateExamView = Backbone.View.extend({
		initialize:function() {
			this.model = arguments[0].attributes;
		},
		render: function(model) {
			this.$el.html(view_utility.executeTemplate('/templates/optionsForGenerateExamView.html', { }));
			return this;
		}
	});
	
	Quizki.MaxQuestionsView = Backbone.View.extend({
		initialize:function() {
			this.showEditBtn = arguments[0].showEditBtn;
			this.render();
		},
		events: {
			
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/MaxQuestionsView.html', {}));
			
			var spinner = $( "#spinner" ).spinner({min: 1, max:35}).val(5);
			
			$('.ui-spinner-button').click(function() { $(this).siblings('input').change(); });

			$('#spinner').spinner().change(function(){
			    model_factory.get("numberOfQuestions").val = $(this).spinner('value');         
	        });
			
			return this;
		}
	});

	Quizki.TakeSelectedExamButtonView = Backbone.View.extend({
		initialize:function() {
			this.showEditBtn = arguments[0].showEditBtn;
			this.render();
		},
		events: {
			"click #btnTakeSelectedExam" : "takeSelectedExam"
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/TakeSelectedExamBtnView.html', {}));
			return this;
		},
		takeSelectedExam : function() {
			window.location.href = '/beginExam.jsp?examId=' + model_factory.get("selectedExam").id;
		}
	});
	
	Quizki.TakeGeneratedExamButtonView = Backbone.View.extend({
		initialize:function() {
			this.showEditBtn = arguments[0].showEditBtn;
			this.render();
		},
		events: {
			"click #btnTakeGeneratedExam": "takeGeneratedExam"
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/TakeGeneratedExamBtnView.html', {}));
			return this;
		},
		takeGeneratedExam : function() {
			var selectedListOfTopics = model_factory.get("selectedListOfTopics");
			
			if (selectedListOfTopics != undefined && selectedListOfTopics.size() > 0 ) {
				// TODO: I'd like to have something else create this data object, though I don't know what else would need to other
				//   than this view.. still, it seems it should not be done here.
				var json = '';
	
				json += JSONUtility.startJSONString(json);
	
				json += JSONUtility.getJSON('difficulty_id', ''+model_factory.get("difficultyObj").getDifficultyId());
				json += JSONUtility.getJSON('numberOfQuestions', ''+model_factory.get("numberOfQuestions").val);
				json += JSONUtility.getJSONForBackboneCollection('selectedListOfTopics', selectedListOfTopics);
				json += JSONUtility.getJSONForBackboneCollection('excludedListOfTopics', new Backbone.Collection([], { model: Topic }), false);
				
				json = JSONUtility.endJSONString(json);
				
				var data_obj = { data : json };
	
	        	makeAJAXCall_andWaitForTheResults('/ajax/exam-generate.jsp', data_obj, function(data, status) {
	        		// TODO: Explicitly handle success and failure cases
	        		window.location.href = '/beginExam.jsp';					        		
	        	});
			}
		}
	});
	
	Quizki.HeaderTextForGenerateExam = Backbone.View.extend({
		initialize:function() {
			this.render();
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/HeaderTextForGenerateExam.html', { }));			
			return this;
		}
	});
	