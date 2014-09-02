package com.haxwell.apps.questions.managers;

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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.haxwell.apps.questions.utils.StringUtil;

public class AutocompletionManager extends Manager {

	public static void write(long userId, long environmentId, String text) {
		EntityManager em = emf.createEntityManager();

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		
		String queryStr = "INSERT IGNORE INTO autocomplete_history (user_id, environment_id, text) VALUES (" + userId + ", " + environmentId + ", " + text + ")";
			
		Query query = em.createNativeQuery(queryStr);
			
		query.executeUpdate();
		
		transaction.commit();		
		
		em.close();
	}
	
	public static void write(long userId, long environmentId, StringUtil.FieldIterator fieldIterator) {
		EntityManager em = emf.createEntityManager();

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		
		while (fieldIterator.hasNext()) {
			String text = fieldIterator.next();
			String queryStr = "INSERT IGNORE INTO autocomplete_history (user_id, environment_id, text) VALUES (" + userId + ", " + environmentId + ", " + text + ")";
			
			Query query = em.createNativeQuery(queryStr);
			
			query.executeUpdate();
		}
		
		transaction.commit();		
		
		em.close();
	}
	
	public static void delete(long userId, long environmentId, String text) {
		EntityManager em = emf.createEntityManager();

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		
		String queryStr = "DELETE FROM autocomplete_history WHERE user_id = " + userId + " AND environment_id = " + environmentId + " AND text = " + text;
		
		Query query = em.createNativeQuery(queryStr);
		
		query.executeUpdate();
		
		transaction.commit();		
		
		em.close();
	}
	
	public static void delete(long userId, long environmentId, StringUtil.FieldIterator fieldIterator) {
		EntityManager em = emf.createEntityManager();

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		
		while (fieldIterator.hasNext()) {
			String text = fieldIterator.next();
			String queryStr = "DELETE FROM autocomplete_history WHERE user_id = " + userId + " AND environment_id = " + environmentId + " AND text = " + text;
			
			Query query = em.createNativeQuery(queryStr);
			
			query.executeUpdate();
		}
		
		transaction.commit();		
		
		em.close();
	}
	
	public static String get(long userId, long environmentId) {
		EntityManager em = emf.createEntityManager();

		String queryStr = "SELECT text FROM autocomplete_history WHERE user_id = " + userId + " AND environment_id = " + environmentId;
		
		Query query = em.createNativeQuery(queryStr);
		
		List list = query.getResultList();
		
		// take each item in the list, and for the ones with commas, break them up into tokens, and put each token in a Set
		//  and then put the overall string in the Set
		Iterator iterator = list.iterator();
		Set<String> set = new HashSet<>();
		final String delimiter = "|";
		
		
		while (iterator.hasNext()) {
			String str = (String)iterator.next();
			
			if (str.contains(delimiter)) {
				StringTokenizer tokenizer = new StringTokenizer(str, delimiter);
				
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					
					token = wrapInQuotes(token);
					
					set.add(token);
				}
			}

			str = wrapInQuotes(str);
			
			set.add(str);
		}
		
		// iterate through the set creating a javascript array of its contents
		Iterator<String> setIterator = set.iterator();
		final boolean DO_NOT_WRAP_IN_QUOTES = false;
		
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtil.startJavascriptArray());
		
		while (setIterator.hasNext()) {
			StringUtil.addToJavascriptArray(sb, setIterator.next(), DO_NOT_WRAP_IN_QUOTES);
		}
		
		StringUtil.closeJavascriptArray(sb);
		
		return sb.toString();
	}

	private static String wrapInQuotes(String token) {
		if (token.charAt(0) != '\"')
			token = "\"" + token;
		
		if (token.charAt(token.length()-1) != '\"')
			token += "\"";
		return token;
	}
}
