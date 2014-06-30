package com.haxwell.apps.questions.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.QuestionType;
import com.haxwell.apps.questions.entities.Reference;
import com.haxwell.apps.questions.entities.Topic;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.entities.UserRole;

public class TestQuestionUtil {

	public static Question getQuestion() {
		Question q = new Question();
		
		long id = RandomIntegerUtil.getRandomInteger(1000);
		String description = "description"+id;
		String text = "<p>Some [[dynamic]] question text for question number " + id + "</p>";
		Difficulty difficulty = new Difficulty(DifficultyConstants.GURU_STR, DifficultyConstants.GURU);
		QuestionType qType = new QuestionType(TypeConstants.PHRASE, TypeConstants.PHRASE_STR);
		long entityStatus = EntityStatusConstants.ACTIVATED;
		User user = new User(); user.setId(1); user.setUsername("username");
		Set<Choice> choices = getSetOfChoices();
		Set<Topic> topics = getSetOfTopics();
		Set<Reference> refs = getSetOfReferences();
		
		q.setId(id);
		q.setDescription(description);
		q.setText(text);
		q.setDifficulty(difficulty);
		q.setQuestionType(qType);
		q.setEntityStatus(entityStatus);
		q.setUser(user);
		q.setChoices(choices);
		q.setTopics(topics);
		q.setReferences(refs);
		
		String dynamicDataKey1 = "dd1";
		String dynamicDataValue1 = "v1";
		String dynamicDataKey2 = "dd2";
		String dynamicDataValue2 = "v2";

		q.setDynamicData(dynamicDataKey1, dynamicDataValue1);
		q.setDynamicData(dynamicDataKey2, dynamicDataValue2);

		return q;
	}
	
	public static Set<Question> getSetOfQuestions(int count) {
		Set<Question> set = new HashSet<>();
		
		List<Integer> ids = RandomIntegerUtil.getRandomListOfNumbers(count);
		
		for (int i = 0; i < count; i++) {
			Question q = getQuestion();
			
			q.setId(ids.get(i));
			
			set.add(q);
		}
		
		return set;
	}
	
	public static Set<Choice> getSetOfChoices() {
		Set<Choice> set = new HashSet<>();
		
		set.add(new Choice(1L, "choice1", Choice.CORRECT));
		set.add(new Choice(2L, "choice1", Choice.NOT_CORRECT));
		set.add(new Choice(3L, "choice1", Choice.NOT_CORRECT));
		
		return set;
	}
	
	public static Set<Topic> getSetOfTopics() {
		Set<Topic> set = new HashSet<>();
		
		set.add(new Topic("topic1"));
		set.add(new Topic("topic2"));
		
		return set;
	}

	public static Set<Reference> getSetOfReferences() {
		Set<Reference> set = new HashSet<>();
		
		set.add(new Reference("reference1"));
		set.add(new Reference("reference2"));
		
		return set;
	}
	
	public static Exam getExam() {
		Exam exam = new Exam();
		
		long id = RandomIntegerUtil.getRandomInteger(1000);
		String title = "examTitle"+id;
		String message = "examMessage"+id;
		User user = new User(); user.setId(1); user.setUsername("username");
		long entityStatus = EntityStatusConstants.ACTIVATED;
		Difficulty difficulty = new Difficulty(DifficultyConstants.JUNIOR_STR, DifficultyConstants.JUNIOR);
		Set<Question> qset = TestQuestionUtil.getSetOfQuestions(3);
		Set<Topic> topics = TestQuestionUtil.getSetOfTopics();
		
		exam.setId(id);
		exam.setTitle(title);
		exam.setMessage(message);
		exam.setUser(user);
		exam.setDifficulty(difficulty);
		exam.setEntityStatus(entityStatus);
		exam.setQuestions(qset);
		exam.setTopics(topics);

		return exam;
	}
	
	public static Set<Exam> getSetOfExams(int count) {
		Set<Exam> set = new HashSet<>();
		
		List<Integer> ids = RandomIntegerUtil.getRandomListOfNumbers(count);
		
		for (int i = 0; i<count; i++) {
			Exam e = getExam();
			
			e.setId(ids.get(i));
			
			set.add(e);
		}
		
		return set;
	}
	
	public static Set<UserRole> getSetOfUserRoles(int count) {
		Set<UserRole> set = new HashSet<>();
		
		List<Integer> ids = RandomIntegerUtil.getRandomListOfNumbers(count);
		
		for (int i=0; i<count; i++) {
			UserRole ur = new UserRole();
			
			Integer id = ids.get(i);
			
			ur.setId(id);
			ur.setText("userRole" + id);
			
			set.add(ur);
		}
		
		return set;
	}
}
