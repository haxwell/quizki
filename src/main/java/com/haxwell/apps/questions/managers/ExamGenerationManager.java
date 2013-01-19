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
import com.haxwell.apps.questions.utils.RandomIntegerUtil;
import com.haxwell.apps.questions.utils.StringUtil;

public class ExamGenerationManager {

	public static Logger log = Logger.getLogger(ExamGenerationManager.class.getName());
	
	public static Exam generateExam(long numberOfQuestions, List<Long> topicIDsToInclude, List<Long> topicIDsToExclude, long filterDifficultyId, boolean requireMatchingDifficultyId)
	{
		log.log(Level.INFO, "GenerateExam() called");
		log.log(Level.INFO, numberOfQuestions + " questions, topics = [" + StringUtil.getToStringOfEach(topicIDsToInclude) + "],[" + StringUtil.getToStringOfEach(topicIDsToExclude) + "], " + (requireMatchingDifficultyId ? "The Only " : "Matching ") + "Difficulty ID = " + filterDifficultyId);
		
		Exam exam = new Exam();
		ArrayList<Question> mainColl = new ArrayList<Question>();
		
		Collection<Question> coll = null; 
		
		if (topicIDsToInclude == null || topicIDsToInclude.size() == 0)
		{
			coll = QuestionManager.getAllQuestions();

			for (Question q : coll)
				mainColl.add(q);
		}
		else
		{
			for (Long i : topicIDsToInclude) {
				coll = QuestionManager.getQuestionsByTopic(i);

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
			boolean questionPassesTheFilters = true;
			
			// get a random question from the list
			Question question = mainColl.get(list.get(--indexToRandomListOfIndexes));
			
			// get the Topics from that question
			Set<Topic> questionTopics = question.getTopics();
			
			// for each topic on this random question
			if (topicIDsToExclude.size() > 0)
				for (Topic topic : questionTopics) {
					// if a topic ID on this question matches the list of those topic IDs to exclude,
					if (topicIDsToExclude.contains(topic.getId()))
					{
						questionPassesTheFilters = false;
						log.log(Level.INFO, "Excluded a question " + question.getId() + " because of TOPIC: " + topic.getId() + " " + topic.getText());
					}
				}
			
			if (questionPassesTheFilters)
			{
				Difficulty difficulty = question.getDifficulty();
				
				// if the difficulty is higher than requested..
				if (difficulty.getId() > filterDifficultyId)
				{
					questionPassesTheFilters = false;
					log.log(Level.INFO, "Excluded a question " + question.getId() + " because of DIFFICULTY: " + difficulty.getId() + " " + difficulty.getText());
				}
				
				// if the difficulty is not the exact level requested..
				if (requireMatchingDifficultyId && difficulty.getId() != filterDifficultyId) {
					questionPassesTheFilters = false;
					log.log(Level.INFO, "Excluded a question " + question.getId() + " because of DIFFICULTY (not a match): " + difficulty.getId() + " " + difficulty.getText());
				}
					
			}
			
			if (questionPassesTheFilters)
			{
				log.log(Level.INFO, "GenerateExam: Adding question " + indexToRandomListOfIndexes + " " + question);
				exam.addQuestion(question);
			}
				
		}
		
		exam.setTitle("Generated Exam #403b");
		
		return exam;
	}
}
