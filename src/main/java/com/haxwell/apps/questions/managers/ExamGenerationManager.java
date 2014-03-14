package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.filters.DifficultyFilter;
import com.haxwell.apps.questions.filters.QuestionTopicFilter;
import com.haxwell.apps.questions.utils.ListFilterer;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.StringUtil;

public class ExamGenerationManager {

	public static Logger log = Logger.getLogger(ExamGenerationManager.class.getName());
	
	public static Exam generateExam(long numberOfQuestions, List<Long> topicIDsToInclude, List<Long> topicIDsToExclude, long filterDifficultyId, boolean requireMatchingDifficultyId)
	{
		log.log(Level.FINER, "GenerateExam() called");
		log.log(Level.FINER, numberOfQuestions + " questions, topics = [" + StringUtil.getToStringOfEach(topicIDsToInclude) + "],[" + StringUtil.getToStringOfEach(topicIDsToExclude) + "], " + (requireMatchingDifficultyId ? "The Only " : "Matching ") + "Difficulty ID = " + filterDifficultyId);
		
		Exam exam = new Exam();
		ArrayList<Question> mainColl = new ArrayList<>();
		
		Collection<Question> coll = null; 
		
		if (topicIDsToInclude == null || topicIDsToInclude.size() == 0)
		{
			throw new IllegalArgumentException("No topics found. Topics are required in order to generate an exam.");
		}
		else
		{
			List<ShouldRemoveAnObjectCommand> filters = new ArrayList<>();
			
			filters.add(new DifficultyFilter(filterDifficultyId, (requireMatchingDifficultyId ? DifficultyFilter.DIFFICULTY_IS_EQUAL : DifficultyFilter.DIFFICULTY_IS_GREATER_THAN)));
			filters.add(new QuestionTopicFilter(topicIDsToExclude));
			
			ListFilterer listFilterer = new ListFilterer<>();

			for (Long i : topicIDsToInclude) {
				coll = QuestionManager.getQuestionsByTopic(i, null);
				
				coll = listFilterer.process(coll, filters);

				for (Question q : coll)
					mainColl.add(q);
			}
		}

		List<Integer> list = RandomIntegerUtil.getRandomListOfNumbers(mainColl.size());

		numberOfQuestions = Math.min(numberOfQuestions, mainColl.size());
		int indexToRandomListOfIndexes = Math.max(list.size(), 1);
		
		// Until we get enough questions on the exam....
		while (exam.getQuestions().size() < numberOfQuestions && indexToRandomListOfIndexes > 0)
		{
			// get a random question from the list
			Question question = mainColl.get(list.get(--indexToRandomListOfIndexes));
			exam.addQuestion(question);
		}
		
		exam.setTitle("Generated Exam #403b");
		
		return exam;
	}
}
