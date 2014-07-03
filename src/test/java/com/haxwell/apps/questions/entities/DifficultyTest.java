package com.haxwell.apps.questions.entities;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class DifficultyTest {

	@Test
	public void testObjectDefaultConstructor() {
		Difficulty sut = new Difficulty();
		assertTrue(StringUtil.isNullOrEmpty(sut.getText()));
		assertTrue(sut.getId() == 0);
	}
	
	@Test
	public void testObjectStringConstructor() {
		Difficulty sut = new Difficulty(DifficultyConstants.GURU_STR);
		assertTrue(StringUtil.equals(sut.getText(), DifficultyConstants.GURU_STR));
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
		assertTrue(StringUtil.equals(sut.getText(), DifficultyConstants.GURU_STR));
		assertTrue(sut.getId() == DifficultyConstants.GURU);
	}

	@Test
	public void testSettersAndGetters() {
		Difficulty sut = new Difficulty(DifficultyConstants.GURU_STR, DifficultyConstants.GURU);
		
		sut.setText(DifficultyConstants.SENIOR_STR);
		sut.setId(DifficultyConstants.INTERMEDIATE);
		
		assertTrue(StringUtil.equals(sut.getText(), DifficultyConstants.SENIOR_STR));
		assertTrue(sut.getId() == DifficultyConstants.INTERMEDIATE);
	}
	
	@Test
	public void testEquals() {
		Difficulty sut1 = new Difficulty();
		Difficulty sut2 = new Difficulty();
		
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		
		sut1.setId(1);
		
		assertFalse(sut1.equals(sut2));

		sut2.setId(1);
		
		sut1.setText("text");
		
		assertFalse(sut1.equals(sut2));
		
		sut2.setText("text");
		
		assertTrue(sut1.equals(sut2));
	}
}
