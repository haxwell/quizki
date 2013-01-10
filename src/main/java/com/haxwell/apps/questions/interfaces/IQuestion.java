package com.haxwell.apps.questions.interfaces;

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
