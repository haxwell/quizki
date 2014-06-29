package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;		

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.entities.Difficulty;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class DifficultyTest {

	@Test
	public void testObjectStringConstructor() {
		Difficulty sut = new Difficulty(DifficultyConstants.GURU_STR);
		assertTrue(sut.getText().equals(DifficultyConstants.GURU_STR));
	}
	
	@Test
	public void testObjectLongConstructor() {
		Difficulty sut = new Difficulty(DifficultyConstants.GURU);
		assertTrue(sut.getText() == null);
		assertTrue(sut.getId() == DifficultyConstants.GURU);
	}
	
	@Test
	public void testObjectStringLongConstructor() {
		Difficulty sut = new Difficulty(DifficultyConstants.GURU_STR, DifficultyConstants.GURU);
		assertTrue(sut.getText().equals(DifficultyConstants.GURU_STR));
		assertTrue(sut.getId() == DifficultyConstants.GURU);
	}

	@Test
	public void testSettersAndGetters() {
		Difficulty sut = new Difficulty(DifficultyConstants.GURU_STR, DifficultyConstants.GURU);
		
		sut.setText(DifficultyConstants.SENIOR_STR);
		sut.setId(DifficultyConstants.INTERMEDIATE);
		
		assertTrue(sut.getText().equals(DifficultyConstants.SENIOR_STR));
		assertTrue(sut.getId() == DifficultyConstants.INTERMEDIATE);
	}
}
