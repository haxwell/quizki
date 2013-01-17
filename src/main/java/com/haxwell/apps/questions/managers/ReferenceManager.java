package com.haxwell.apps.questions.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.haxwell.apps.questions.entities.Reference;

public class ReferenceManager extends Manager {

	public static Collection<Reference> getReferencesById(String csvString) {
		EntityManager em = emf.createEntityManager();
		
		String strQuery = "SELECT * FROM reference WHERE id=";
		StringTokenizer tokenizer = new StringTokenizer(csvString, ",");
		
		while (tokenizer.hasMoreTokens())
		{
			strQuery += tokenizer.nextToken();
			
			if (tokenizer.hasMoreTokens())
				strQuery += " OR id=";
		}
				
		Query query = em.createNativeQuery(strQuery, Reference.class);
		
		Collection<Reference> coll = query.getResultList();
		
		em.close();
		
		return coll;
	}
	
	public static Reference getReferenceById(int i)
	{
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createNativeQuery("SELECT * FROM reference WHERE id = ?1", Reference.class);
		
		query.setParameter(1, i);
		
		List<Reference> list = (List<Reference>)query.getResultList();
		
		em.close();

		return (list.size() > 0 ? list.get(0) : null);
	}
	
	public static Reference getReference(String text)
	{
		EntityManager em = emf.createEntityManager();
		
		// Look around the database, is there an existing Reference?
		Query query = em.createNativeQuery("SELECT * FROM reference WHERE text = ?1", Reference.class);
		
		query.setParameter(1, text);
		
		List<Reference> list = (List<Reference>)query.getResultList();
		
		em.close();

		return (list.size() > 0 ? list.get(0) : null);
	}
	
	// TODO: Create a standard getAll() method for Managers in general
	public static Collection<Reference> getAllReferences() {
		EntityManager em = emf.createEntityManager();
		
		Query query = em.createQuery("SELECT r FROM Reference r");
		
		Collection<Reference> rtn = (Collection<Reference>)query.getResultList(); 
		
		em.close();
		
		return rtn;
	}
	
	public static void delete(Set<Reference> references, Reference r)
	{
		references.remove(r);
	}
}
