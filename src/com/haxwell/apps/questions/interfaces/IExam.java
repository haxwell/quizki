package com.haxwell.apps.questions.interfaces;

import java.util.Set;

import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;

public interface IExam {

	public long getId();
	public void setId(long id);
	
	public String getTitle();
	public void setTitle(String title);
	
	public User getUser();
	public void setUser(User user);
	
	public Set<Question> getQuestions();
	public void setQuestions(Set<Question> questions);
}