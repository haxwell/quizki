package com.haxwell.apps.questions.constants;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

public enum DifficultyEnums {
	
 	UNDEFINED (-1, "undefined"),
 	ALL_DIFFICULTIES (0, "all"), 
 	JUNIOR (1, "Junior"),
	INTERMEDIATE (2, "intermediate"),
	SENIOR (3, "senior"),
	GURU (4, "guru");
	
	private final long rank;
	private final String valString;
	
	DifficultyEnums(long rank, String valString){
		this.rank = rank;
		this.valString = valString;
	}
	
	public long getRank() {
		return this.rank;
	}
	
	public String getValString() {
		return this.valString;
	}
	
		
	
	
	
}
