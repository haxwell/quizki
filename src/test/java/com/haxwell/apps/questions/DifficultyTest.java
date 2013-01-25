package com.haxwell.apps.questions;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.haxwell.apps.questions.entities.Difficulty;

public class DifficultyTest {

	@Test
	public void test() {
		String str = "test";
		
		Difficulty sut = new Difficulty(str);
		
		assertTrue(sut.getText().equals(str));
	}

}
