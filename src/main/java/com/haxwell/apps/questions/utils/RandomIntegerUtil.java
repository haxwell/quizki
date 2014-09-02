package com.haxwell.apps.questions.utils;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RandomIntegerUtil {
	
	public static Logger log = Logger.getLogger(RandomIntegerUtil.class.getName());
	
	/**
	 * Returns a list of unique integers, each one a value from 0 to listSize. 
	 * 
	 * @param listSize
	 * @return
	 */
	public static List<Integer> getRandomlyOrderedListOfUniqueIntegers(int listSize)
	{
		Random randomGenerator = new Random();
		List<Integer> rtn = new ArrayList<Integer>();
		
		List<Integer> seedList = new ArrayList<Integer>();
		
		for (int i = 0; i < listSize; i++) 
			seedList.add(i, i);
		
		int counter = seedList.size();
		while (counter > 0) {
			int rand = randomGenerator.nextInt(seedList.size());
			
			rtn.add(seedList.remove(rand));
			counter--;
		}
		
		log.log(Level.FINEST, "Just created random list of " + listSize + " numbers.. ");
		log.log(Level.FINEST, StringUtil.getToStringOfEach(rtn));
		
		return rtn;
	}

	public static int getRandomInteger(int max) {
		return new Random().nextInt(max); 
	}
}
