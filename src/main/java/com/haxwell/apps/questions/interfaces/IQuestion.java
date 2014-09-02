package com.haxwell.apps.questions.interfaces;

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

import java.util.Set;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.entities.QuestionType;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;

public interface IQuestion {

	long getId();
//	void setId();
	
//	int getOwnerId();
//	void setOwnerId(int i);
	
	Set<Choice> getChoices();
	void setChoices(Set<Choice> set);
	
	Set<Topic> getTopics();
	void setTopics(Set<Topic> set);
	
	Difficulty getDifficulty(); 
	void setDifficulty(Difficulty d);
	
	String getText();
	void setText(String str);

	public QuestionType getQuestionType();
	public void setQuestionType(QuestionType questionType);

	public User getUser();
	public void setUser(User user);
}
