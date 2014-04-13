package com.haxwell.apps.questions.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.haxwell.apps.questions.entities.EntityWithAnIntegerIDBehavior;

public class StringUtil {

	public static String getField(int fieldNumber, String beginningFieldMarker, String endingFieldMarker, String str) {
		// 67
		// 67;9;2
		int beginIndex = -1;
		int endIndex = -1;
		int counter = (beginningFieldMarker.equals(endingFieldMarker) ? 1 : 0);
		
		for ( ; counter < fieldNumber; counter++) {
			beginIndex = str.indexOf(beginningFieldMarker, beginIndex+1);
		}
		
		if (beginIndex != -1) {
			endIndex = str.indexOf(endingFieldMarker, beginIndex+1);
		}
		
		String rtn = str;
		if (beginIndex > -1) {
			rtn = str.substring(beginIndex+beginningFieldMarker.length(), (endIndex == -1 ? str.length() : endIndex));
		}
		
		return rtn;
	}
	
	public static int getNumberOfOccurrances(String pattern, String str) {
		int index = str.indexOf(pattern);
		int count = 0;
		
		while (index > -1) {
			count++;
			index = str.indexOf(pattern, index+1);
		}
		
		return count;
	}
	
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

	public static String getCSVString(List list) {
		if (list == null) return "";
		
		StringBuffer sb = new StringBuffer();
		Iterator iterator = list.iterator();

		while (iterator.hasNext())
		{
			sb.append(iterator.next());
			
			if (iterator.hasNext())
				sb.append(",");
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns a CSV of the IDs from a collection of entities with Integer IDs.
	 * 
	 * @param coll
	 * @return
	 */
	public static String getCSVFromCollection(Collection<? extends EntityWithAnIntegerIDBehavior> coll)
	{
		if (coll == null) return "";
		
		Iterator<? extends EntityWithAnIntegerIDBehavior> iterator = coll.iterator();
		StringBuffer sb = new StringBuffer();
		
		while (iterator.hasNext())
		{
			EntityWithAnIntegerIDBehavior entity = iterator.next();

			sb.append(entity.getId());
			
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
	
	public static String getShortURL(String fullUrl) {
		int index = fullUrl.indexOf("/", "http://".length());
		return fullUrl.substring(index + 1);
	}
	
	public static boolean isEmptyJSON(String str) {
		String s = str.replaceAll(" ", "");
		
		return s.equals("{}");
	}
	
	public static String toJSON(String key, List<String> values) {

		// "errors":[{"val":"fdafdsafdsa,fdsafdsafdsa,3v3v"}]
		
		String rtn = " \"" + key + "\":[";

		String csv = getCSVString(values);
		String str = "";
		
		str = "{";
		
		str += "\"val\":";
		str += "\"" + csv + "\"";
		str += "}]";

		rtn += str;
		
		return rtn;
	}
	
	public static String toJSON(Map<String, List<String>> map) {
		Set<String> set = map.keySet();
		
		// { "errors":[{"val":"fdafdsafdsa,fdsafdsafdsa,3v3v"}],"successes":[{"val":"dffads"}] }
		
		String rtn = "{";
		Iterator<String> keyIterator = set.iterator();
		
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			
			rtn += toJSON(key, map.get(key));
			
			if (keyIterator.hasNext())
				rtn += ",";
		}
		
		rtn += " }";
		
		return rtn;
	}
	
//	/**
//	 * Expects two CSVs of numbers. 
//	 * 
//	 * @param strCSVOne
//	 * @param strCSVTwo
//	 * @return
//	 */
//	public static boolean allNumericStringsInOneMatchCSVElementsInTwo(String strCSVOne, String strCSVTwo)
//	{
//		List<Long> verifyList = null;
//		List<Long> controlList = null;
//		
//		try {
//			verifyList = StringUtil.getListOfLongsFromCSV(strCSVOne);
//		}
//		catch (NumberFormatException nfe) {
//			
//		}
//		
//		try {
//			controlList = StringUtil.getListOfLongsFromCSV(strCSVTwo);
//		}
//		catch (NumberFormatException nfe) {
//			
//		}
//		
//		boolean rtn = (verifyList != null && controlList != null);
//		
//		if (rtn)
//		{
//			Iterator<Long> iterator = verifyList.iterator();
//
//			while (iterator.hasNext() && rtn)
//			{
//				Long l = iterator.next();
//				rtn = controlList.contains(l);
//			}
//		}
//
//		return rtn;
//	}
//
//	/**
//	 * Expects two CSVs of numbers. 
//	 * 
//	 * @param strCSVOne
//	 * @param strCSVTwo
//	 * @return
//	 */
//	public static boolean anyNumericStringsInOneFoundInCSVElementsInTwo(String strCSVOne, String strCSVTwo)
//	{
//		List<Long> verifyList = null;
//		List<Long> controlList = null;
//		
//		try {
//			verifyList = StringUtil.getListOfLongsFromCSV(strCSVOne);
//		}
//		catch (NumberFormatException nfe) {
//			
//		}
//		
//		try {
//			controlList = StringUtil.getListOfLongsFromCSV(strCSVTwo);
//		}
//		catch (NumberFormatException nfe) {
//			
//		}
//		
//		boolean integerListsCreatedOkay = (verifyList != null && controlList != null);
//		boolean rtn = false;
//		
//		if (!rtn && integerListsCreatedOkay)
//		{
//			Iterator<Long> iterator = verifyList.iterator();
//
//			while (iterator.hasNext() && !rtn)
//			{
//				Long i = iterator.next();
//				rtn = controlList.contains(i);
//			}
//		}
//
//		return rtn;
//	}
	
	public static String removeQuotes(String str)
	{
		return str.replaceAll("\"", "");
	}
}
