package com.haxwell.apps.questions.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {

	public static boolean isNullOrEmpty(String str)
	{
		return str == null ? true : (str.length() <= 0);
	}
	
	public static String startJavascriptArray()
	{
		return "[";
	}
	
	public static void addToJavascriptArray(StringBuffer sb, String val)
	{
		if (sb.length() > 0)
		{
			char lastChar = sb.charAt(sb.length() - 1); 
			
			if (lastChar != ',' && lastChar != '[')
				sb.append(',');
		}
		
		sb.append("\"" + val + "\""); 
	}

	public static void closeJavascriptArray(StringBuffer sb) {
		sb.append("]");
	}
	
	public static String getToStringOfEach(Collection<?> coll)
	{
		if (coll == null) return "";
		
		Iterator<?> iterator = coll.iterator();
		StringBuffer rtn = new StringBuffer();
		
		while (iterator.hasNext())
		{
			rtn.append(iterator.next().toString() + " -\\\n");
		}
		
		return rtn.toString();
	}

	public static String getCSVString(List<Long> list) {
		if (list == null) return "";
		
		StringBuffer sb = new StringBuffer();
		Iterator<Long> iterator = list.iterator();
		
		while (iterator.hasNext())
		{
			sb.append(iterator.next());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}

	public static List<Long> getListOfLongsFromCSV(String topicsToInclude) {
		LinkedList<Long> list = new LinkedList<Long>();
		
		StringTokenizer tokenizer = new StringTokenizer(topicsToInclude, ",");
		
		while (tokenizer.hasMoreTokens())
		{
			list.add(Long.parseLong(tokenizer.nextToken()));
		}
		
		return list;
	}
	
	/**
	 * Expects two CSVs of numbers. 
	 * 
	 * @param strCSVOne
	 * @param strCSVTwo
	 * @return
	 */
	public static boolean allNumericStringsInOneMatchCSVElementsInTwo(String strCSVOne, String strCSVTwo)
	{
		List<Long> verifyList = null;
		List<Long> controlList = null;
		
		try {
			verifyList = StringUtil.getListOfLongsFromCSV(strCSVOne);
		}
		catch (NumberFormatException nfe) {
			
		}
		
		try {
			controlList = StringUtil.getListOfLongsFromCSV(strCSVTwo);
		}
		catch (NumberFormatException nfe) {
			
		}
		
		boolean rtn = (verifyList != null && controlList != null);
		
		if (rtn)
		{
			Iterator<Long> iterator = verifyList.iterator();

			while (iterator.hasNext() && rtn)
			{
				Long l = iterator.next();
				rtn = controlList.contains(l);
			}
		}

		return rtn;
	}

	/**
	 * Expects two CSVs of numbers. 
	 * 
	 * @param strCSVOne
	 * @param strCSVTwo
	 * @return
	 */
	public static boolean anyNumericStringsInOneFoundInCSVElementsInTwo(String strCSVOne, String strCSVTwo)
	{
		List<Long> verifyList = null;
		List<Long> controlList = null;
		
		try {
			verifyList = StringUtil.getListOfLongsFromCSV(strCSVOne);
		}
		catch (NumberFormatException nfe) {
			
		}
		
		try {
			controlList = StringUtil.getListOfLongsFromCSV(strCSVTwo);
		}
		catch (NumberFormatException nfe) {
			
		}
		
		boolean integerListsCreatedOkay = (verifyList != null && controlList != null);
		boolean rtn = false;
		
		if (!rtn && integerListsCreatedOkay)
		{
			Iterator<Long> iterator = verifyList.iterator();

			while (iterator.hasNext() && !rtn)
			{
				Long i = iterator.next();
				rtn = controlList.contains(i);
			}
		}

		return rtn;
	}
	
}
