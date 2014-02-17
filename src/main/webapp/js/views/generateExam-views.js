	Quizki.AllTopicsListView = Backbone.View.extend({
		
		initialize:function() {
			this.render();

			model_constructor_factory.put("stagedSelectedListOfTopics", function() { return new Backbone.Collection([], { model: Topic }); });
			
			this.listenTo(model_factory.get('currentListOfTopics'), 'add', function(event) { this.render(); });
			this.listenTo(model_factory.get('currentListOfTopics'), 'remove', function(event) { this.render(); });
			this.listenTo(model_factory.get('currentListOfTopics'), 'reset', function(event) { this.render(); });
		},
		renderElement: function(model) {
			var listOfTopics = this.$el.find("#listOfTopics");
			
			var topicItem = new Quizki.AllTopicsListItemView(model, 'topicItem');
			listOfTopics.append( topicItem.render().$el.html() );
		},
		render:function() {
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<select multiple class='span3 mainSelectBox' id='listOfTopics'></select>" )() );
			
			var topics = model_factory.get("currentListOfTopics");
			
			_.each(topics.models, function(model) { this.renderElement(model); }, this);
			
			return this;
		},
		events: {
			"dblclick .topicItem":"addItemToSelectedList",
			"click .topicItem":"stageSelectedItemForListOfTopics"
		},
		addItemToSelectedList: function(event) {
			var currentListOfTopics = model_factory.get("currentListOfTopics");
			
			var topicText = $(event.target).html().trim();
			
			var topicObject = currentListOfTopics.findWhere({ text:topicText }).attributes;
			
			currentListOfTopics.remove(topicObject);
			
			// add an object to the selected item list
			model_factory.get("selectedListOfTopics").add(topicObject);
			
			model_factory.get("stagedSelectedListOfTopics").reset();
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
			this.$el.html( _.template( "<select multiple class='span3 selectedSelectBox' id='selectedListOfTopics'></select>" )() );
			
			var topics = model_factory.get("selectedListOfTopics");
			
			_.each(topics.models, function(model) { this.renderElement(model.attributes); }, this);
			
			return this;
		},
		events: {
			"dblclick .selectedTopicItem":"removeItemFromSelectedList",
			"click .selectedTopicItem":"stageSelectedItemForListOfTopics"			
		},
		removeItemFromSelectedList: function(event) {
			var selectedListOfTopics = model_factory.get("selectedListOfTopics");
			
			var topicText = $(event.target).html().trim();
			
			// remove that item from currentListOfTopics
			var topicObject = selectedListOfTopics.findWhere({ text:topicText }).attributes;
			
			selectedListOfTopics.remove(topicObject);
			
			// add an object to the selected item list
			model_factory.get("currentListOfTopics").add(topicObject);
			
			model_factory.get("stagedUnselectedListOfTopics").reset();
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
		renderElement: function(model) {
			var listOfMatchingExams = this.$el.find("#matchingExamsView");
			
			var examItem = new Quizki.MatchingExamItemView(model);
			listOfMatchingExams.append( examItem.render().$el.html() );
		},
		render:function() {
			this.$el.html( _.template( "<select multiple class='span5 examsSelectBox' id='matchingExamsView'></select>" )() );			
			
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
	
	Quizki.TakeGeneratedOrSelectedExamButtonView = Backbone.View.extend({
		initialize:function() {
			this.showEditBtn = arguments[0].showEditBtn;
			this.render();
		},
		events: {
			"click #btnTakeGeneratedExam": "takeGeneratedExam",
			"click #btnTakeSelectedExam" : "takeSelectedExam"
		},
		render:function() {
			this.$el.html(view_utility.executeTemplate('/templates/TakeGeneratedOrSelectedExamBtnView.html', {}));
			return this;
		},
		takeGeneratedExam : function() {
		
		},
		takeSelectedExam : function() {
			
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
	