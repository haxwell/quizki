	Quizki.AllTopicsListView = Backbone.View.extend({
		tagName:'ul',
		
		initialize:function() {
			this.render();
			
			this.listenTo(model_factory.get('currentListOfTopics'), 'somethingChanged', function(event) { this.render(); });
		},
		renderElement: function(model) {
			var ul = this.$el.find("#listOfTopics");
			
			var topicItem = new Quizki.AllTopicsListItemView(model, 'topicItem');
			ul.append( topicItem.render().$el.html() );
		},
		render:function() {
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<ul class='span3' id='listOfTopics'></ul>" )() );
			
			var topics = model_factory.get("currentListOfTopics");
			
			_.each(topics.models, function(model) { 
				var keyMap = model_factory.get("currentListOfTopicsKeyMap");
				keyMap.put(model.attributes.val.text, model.attributes.millisecond_id);
				
				this.renderElement(model); 
			}, this);
			
			return this;
		},
		events: {
			"dblclick .topicItem":"addItemToSelectedList"
		},
		addItemToSelectedList: function(event) {
			var currentListOfTopics = model_factory.get("currentListOfTopics");
			
			var topicText = $(event.target).html().trim();
			
			// use a key map with the millisecond ID mapped to the text
			// get the millisecond id,
			var keyMap = model_factory.get("currentListOfTopicsKeyMap");
			var id = keyMap.get(topicText);
			
			// remove that item from currentListOfTopics
			var topicObject = currentListOfTopics.getByMillisecondId(id);
			currentListOfTopics.remove(id);
			
			// remove it from the key map
			keyMap.remove(topicText);
			
			// add an object to the selected item list
			var selectedListOfTopics = model_factory.get("selectedListOfTopics");
			var mID = selectedListOfTopics.put(topicObject.attributes.val);
			
			// store the millisecond id in a key map
			var keyMap2 = model_factory.get("selectedListOfTopicsKeyMap");
			keyMap2.put(topicText, mID);
		}
	});
	
	Quizki.AllTopicsListItemView = Backbone.View.extend({
		tagName:'li',
		
		initialize:function() {
			this.model = arguments[0].attributes;
			this.fieldClass = arguments[1];
		},
		render: function(model) {
			var _model = this.model;
			var _fieldClass = this.fieldClass;
			
			this.$el.html( view_utility.executeTemplate('/templates/ItemInAllTopicsList.html', {text:_model.val.text,klass:_fieldClass}));
			
			return this;

		}
	});
	
	Quizki.AllTopicsListFilterView = Backbone.View.extend({
		initialize:function() {
			this.render();
		},
		render: function(model) {
			// TODO: will need to remember state.. probably.
			this.$el.html(view_utility.executeTemplate('/templates/AllTopicsListFilterView.html', { }));
			return this;
		},
		events: {
			"keypress #topicContainsFilter" : "handleKeypress"
		},
		handleKeypress: function(model) {
			
		}
	});
	
	Quizki.SelectedTopicsListView = Backbone.View.extend({
		tagName:'ul',
		
		initialize:function() {
			this.render();
			
			this.listenTo(model_factory.get('selectedListOfTopics'), 'somethingChanged', function(event) { this.render(); });
		},
		renderElement: function(model) {
			var ul = this.$el.find("#selectedListOfTopics");
			
			var topicItem = new Quizki.AllTopicsListItemView(model, 'selectedTopicItem');
			ul.append( topicItem.render().$el.html() );
		},
		render:function() {
			//  TO UNDERSTAND: why does this return a function to be executed, rather than a string?
			this.$el.html( _.template( "<ul class='span3' id='selectedListOfTopics'></ul>" )() );
			
			var topics = model_factory.get("selectedListOfTopics");
			
			_.each(topics.models, function(model) { this.renderElement(model); }, this);
			
			return this;
		},
		events: {
			"dblclick .selectedTopicItem":"removeItemFromSelectedList"
		},
		removeItemFromSelectedList: function(event) {
			var selectedListOfTopics = model_factory.get("selectedListOfTopics");
			
			var topicText = $(event.target).html().trim();
			
			// use a key map with the millisecond ID mapped to the text
			// get the millisecond id,
			var keyMap = model_factory.get("selectedListOfTopicsKeyMap");
			var id = keyMap.get(topicText);
			
			// remove that item from currentListOfTopics
			var topicObject = selectedListOfTopics.getByMillisecondId(id);
			selectedListOfTopics.remove(id);
			
			// remove it from the key map
			keyMap.remove(topicText);
			
			// add an object to the selected item list
			var currentListOfTopics = model_factory.get("currentListOfTopics");
			var mID = currentListOfTopics.put(topicObject.attributes.val);
			
			// store the millisecond id in a key map
			var keyMap2 = model_factory.get("currentListOfTopicsKeyMap");
			keyMap2.put(topicText, mID);
		}
		
	});
	
	Quizki.ArrowView = Backbone.View.extend({
		
	});

	Quizki.MatchingExamsView = Backbone.View.extend({
		tagName:'ul',
		
		initialize:function() {
			this.render();
		},
		renderElement: function(model) {
			var ul = this.$el.find("#matchingExamsView");
			
			var examItem = new Quizki.MatchingExamItemView(model);
			ul.append( examItem.render().$el.html() );
		},
		render:function() {
			this.$el.html( _.template( "<ul class='span3' id='matchingExamsView'></ul>" )() );
			
			var exams = model_factory.get("listOfMatchingExams");
			
			_.each(exams.models, function(model) { this.renderElement(model); }, this);
			
			return this;
		}
	});
	
	Quizki.MatchingExamItemView = Backbone.View.extend({
		tagName:'li',
		
		initialize:function() {
			this.model = arguments[0].attributes;
		},
		render: function(model) {
			var _model = this.model;
			this.$el.html( view_utility.executeTemplate('/templates/ItemInMatchingExamList.html', {text:_model.val.text}));
			
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
			// TODO: handle checkbox click
		},
		handleCheckboxClick: function(model) {
			
		}
	});
	
	Quizki.OptionsForGenerateExamView = Backbone.View.extend({
		
	});
	
	Quizki.TakeGeneratedOrSelectedExamButtonView = Backbone.View.extend({
		
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
	