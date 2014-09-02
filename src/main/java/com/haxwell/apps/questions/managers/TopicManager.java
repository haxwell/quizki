package com.haxwell.apps.questions.managers;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.haxwell.apps.questions.entities.EntityWithIDAndTextValuePairBehavior;
import com.haxwell.apps.questions.entities.Topic;

public class TopicManager extends EntityWithIDAndTextValuePairManager {

	static TopicManager instance = null;
	
	private TopicManager() {
		
	}
	
	public static EntityWithIDAndTextValuePairManager getInstance() {
		if (instance == null)
			instance = new TopicManager();
		
		return instance;
	}
	
	public String getDBTableName() {
		return "Topic";
	}
	
	public Class getEntityClass() {
		return Topic.class;
	}
	
	public List<? extends EntityWithIDAndTextValuePairBehavior> getNewList() {
		return new ArrayList<Topic>();
	}
	
	public String getEntityName() {
		return "topic";
	}
}