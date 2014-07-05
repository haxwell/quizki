package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.filters.DifficultyFilter;
import com.haxwell.apps.questions.filters.QuestionTopicFilter;
import com.haxwell.apps.questions.utils.ListFilterer;
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.ShouldRemoveAnObjectCommand;
import com.haxwell.apps.questions.utils.StringUtil;

public class ExamGenerationManager {

	private static Logger log = LogManager.getLogger();
	
	public static Exam generateExam(long numberOfQuestions, List<Long> topicIDsToInclude, List<Long> topicIDsToExclude, long filterDifficultyId, boolean requireMatchingDifficultyId)
	{
		log.entry();
		log.debug(numberOfQuestions + " questions, topics = [" + StringUtil.getToStringOfEach(topicIDsToInclude) + "],[" + StringUtil.getToStringOfEach(topicIDsToExclude) + "], " + (requireMatchingDifficultyId ? "The Only " : "Matching ") + "Difficulty ID = " + filterDifficultyId);
		
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
				coll = QuestionManager.getQuestionsByTopic(i);
				
				coll = listFilterer.process(coll, filters);

				for (Question q : coll)
					mainColl.add(q);
			}
		}

		List<Integer> list = RandomIntegerUtil.getRandomlyOrderedListOfUniqueIntegers(mainColl.size());

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
		
		return log.exit(exam);
	}
}
