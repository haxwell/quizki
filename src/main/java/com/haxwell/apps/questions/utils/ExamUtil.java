package com.haxwell.apps.questions.utils;

import java.util.HashSet;
import java.util.Set;

import com.haxwell.apps.questions.constants.DifficultyConstants;
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
	
	public String getExamDifficultyAsString(Exam exam)
	{
		int rtn = DifficultyConstants.JUNIOR-1;
		
		// get questions on the exam
		Set<Question> questions = exam.getQuestions();
		int totalQuestionCount = questions.size();
		
		// sum up the difficulties for them all
		int sumOfQuestionDifficulties = 0;
		
		for (Question q : questions)
			sumOfQuestionDifficulties += (q.getDifficulty().getId() - 1);

		////
		if (totalQuestionCount <= 3) {
			rtn = sumOfQuestionDifficulties / totalQuestionCount;
//			int mod = sumOfQuestionDifficulties % totalQuestionCount;
//			rtn += mod;
		}
		else {
			
			int rangeMax = (DifficultyConstants.GURU-1) * totalQuestionCount;
			int maxSegmentSize = rangeMax / 4;
			int segmentSizeRemainder = rangeMax % 4;
			int currSegmentSize = 0;
			int segmentIndex = 0;
			
			int[] arr = new int[rangeMax];
			
			for (int i = 0; i < rangeMax; i++) {
				
				if (currSegmentSize < maxSegmentSize)
					arr[i] = segmentIndex;
				else {
					if (segmentSizeRemainder > 0) {
						segmentSizeRemainder--;
					}
					
					segmentIndex++;
					arr[i] = segmentIndex;
					
					currSegmentSize = -1;
				}
				
				currSegmentSize++;
			}
			
			rtn = arr[sumOfQuestionDifficulties];
		}
		
		return DifficultyUtil.getDifficulty(Math.min(rtn + 1, 4)).getText();
	}
}
