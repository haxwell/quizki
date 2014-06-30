package com.haxwell.apps.questions.utils;

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
	// TODO: Rename this to getRandomListOfUniqueIntegers.. or something with the word unique in it.
	public static List<Integer> getRandomListOfNumbers(int listSize)
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
