package com.haxwell.apps.questions.utils;

import java.util.HashSet;
import java.util.Set;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;

public class ExamUtil {

	
	/**
	 * Returns a Set, containing a unique instance of the topic from each of the questions on the exam.
	 * 
	 * @param exam
	 * @return
	 */
	public Set<Topic> getExamTopics(Exam exam)
	{
		Set<Question> questions = exam.getQuestions();
		
		Set<Topic> rtn = new HashSet<Topic>();
		
		for (Question q : questions)
		{
			rtn.addAll(q.getTopics());
		}
		
		return rtn;
	}
}
